/*
 * DiscordSRV-Bungee: Bungee platform support plugin or the DiscordSRV project
 * Copyright (C) 2018 DiscordSRV
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.discordsrv.bungee;

import com.discordsrv.bungee.listener.ChatMessageListener;
import com.discordsrv.core.api.dsrv.platform.Platform;
import com.discordsrv.core.channel.LocalChatChannelLinker;
import com.discordsrv.core.conf.Configuration;
import com.google.common.util.concurrent.FutureCallback;
import lombok.Getter;
import net.dv8tion.jda.core.entities.TextChannel;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.naming.ConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Main plugin class for DiscordSRV-Bungee.
 */
@ParametersAreNonnullByDefault
public class DSRVBungee extends Plugin implements Platform<BungeeContext> {

    @Getter private BungeeContext context;

    /**
     * Enable listener.
     */
    @Override
    public void onEnable() {
        try {
            // config
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }
            URL defaultConfigUrl = getClass().getClassLoader().getResource("defaultConfig.yml");
            URL protectedConfigUrl = getClass().getClassLoader().getResource("protectedConfig.yml");
            URL configUrl = getClass().getClassLoader().getResource("config.yml");
            File userConfig = new File(getDataFolder(), "config.yml");
            if (!userConfig.exists()) {
                userConfig.createNewFile();
                InputStream inputStream = defaultConfigUrl.openStream();
                FileOutputStream outputStream = new FileOutputStream(userConfig);
                int bit;
                while ((bit = inputStream.read()) != -1) {
                    outputStream.write(bit);
                }
                inputStream.close();
                outputStream.close();
            }
            Configuration configuration = Configuration
                .getStandardConfiguration(new Yaml(), protectedConfigUrl, userConfig.toURI().toURL(), configUrl);
            // config mappings
            Map<String, String> mappings = new HashMap<>();
            mappings.put("plugin", BungeeContext.class.getName());
            mappings.put("channels", LocalChatChannelLinker.class.getName());
            configuration.applyRemapping(mappings);
            context = configuration.create(BungeeContext.class, configuration, getProxy(), this);
            // listeners
            getProxy().getPluginManager().registerListener(this, new ChatMessageListener(this));
        } catch (IOException | ConfigurationException | IllegalAccessException | InvocationTargetException | InstantiationException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * TODO send message with formatting & stuff.
     *
     * @param message
     *         the message
     * @param player
     *         the player that sent the message or null
     */
    public void sendMessage(final String message, final @Nullable ProxiedPlayer player) {
        context.getChatChannelLinker().translate(null, new FutureCallback<TextChannel>() { // TODO
            @Override
            public void onSuccess(@Nullable final TextChannel result) {
                if (result == null) {
                    return;
                }
                if (player != null) {
                    result.sendMessage(message + " (" + player.getName() + ")").queue();
                } else {
                    result.sendMessage(message).queue();
                }
            }

            @Override
            public void onFailure(final Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
