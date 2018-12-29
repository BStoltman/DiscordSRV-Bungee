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

import com.discordsrv.bungee.lookup.BungeeChatChannelLookup;
import com.discordsrv.bungee.lookup.BungeePlayerUserLookup;
import com.discordsrv.bungee.lookup.BungeeTeamRoleLookup;
import com.discordsrv.bungee.unit.BungeeConsole;
import com.discordsrv.core.api.channel.ChatChannelLinker;
import com.discordsrv.core.api.dsrv.Context;
import com.discordsrv.core.api.role.TeamRoleLinker;
import com.discordsrv.core.api.user.PlayerUserLinker;
import com.discordsrv.core.auth.PlayerUserAuthenticator;
import com.discordsrv.core.channel.LocalChatChannelLinker;
import com.discordsrv.core.conf.Configuration;
import com.discordsrv.core.conf.annotation.Configured;
import com.discordsrv.core.conf.annotation.Val;
import com.discordsrv.core.discord.DSRVJDABuilder;
import com.discordsrv.core.role.LocalTeamRoleLinker;
import com.discordsrv.core.user.LocalPlayerUserLinker;
import com.discordsrv.core.user.UplinkedPlayerUserLinker;
import lombok.Getter;
import net.dv8tion.jda.core.JDA;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.apache.commons.collections4.bidimap.DualLinkedHashBidiMap;

import javax.naming.ConfigurationException;
import javax.security.auth.login.LoginException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

/**
 * BungeeContext type, to provide the context for extensions.
 */
@Getter
public class BungeeContext implements Context {

    private final Configuration configuration;
    private final PlayerUserAuthenticator userAuthenticator;
    private final PlayerUserLinker playerUserLinker;
    private final BungeePlayerUserLookup playerUserLookup;
    private final TeamRoleLinker teamRoleLinker;
    private final BungeeTeamRoleLookup teamRoleLookup;
    private final ChatChannelLinker chatChannelLinker;
    private final BungeeChatChannelLookup chatChannelLookup;
    private final JDA jda;
    // Bungee specific
    private final ProxyServer proxyServer;
    private final Plugin plugin;

    /**
     * Main constructor for BungeeContext.
     *
     * @param configuration
     *         Configuration used to initiate objects
     * @param proxyServer
     *         the proxy servers
     * @param plugin
     *         the plugin object
     * @param remoteLinker
     *         user_remote_linking config option
     *
     * @throws ConfigurationException
     *         If there are no constructors with the {@link Configured} annotation.
     * @throws IllegalAccessException
     *         If the constructor attempted to be used is not accessible.
     * @throws InvocationTargetException
     *         Shouldn't happen, but inherited from {@link Constructor#newInstance(Object...)}.
     * @throws InstantiationException
     *         If instantiation of the type fails.
     * @throws LoginException
     *         If the provided token is invalid.
     */
    @Configured
    public BungeeContext(final @Val("configuration") Configuration configuration,
                         final @Val("proxyServer") ProxyServer proxyServer, final @Val("plugin") Plugin plugin,
                         final @Val("use_remote_linking") boolean remoteLinker)
        throws ConfigurationException, IllegalAccessException, InvocationTargetException, InstantiationException,
               LoginException {
        this.playerUserLookup = new BungeePlayerUserLookup(this);
        this.playerUserLinker = remoteLinker ? configuration.create(UplinkedPlayerUserLinker.class, playerUserLookup)
            : configuration.create(LocalPlayerUserLinker.class, playerUserLookup);
        this.teamRoleLookup = new BungeeTeamRoleLookup(this);
        this.chatChannelLookup = new BungeeChatChannelLookup(this);
        this.configuration = configuration;
        this.userAuthenticator = configuration.create(PlayerUserAuthenticator.class, playerUserLinker,
            proxyServer.getScheduler().unsafe().getExecutorService(plugin));
        this.teamRoleLinker = configuration.create(LocalTeamRoleLinker.class, teamRoleLookup);
        this.chatChannelLinker = configuration
            .create(LocalChatChannelLinker.class, new DualLinkedHashBidiMap<>(), chatChannelLookup,
                new BungeeConsole(this));
        this.proxyServer = proxyServer;
        this.plugin = plugin;
        this.jda = configuration.create(DSRVJDABuilder.class).build();
    }

    /**
     * Fetches the event handler of this DSRVContext.
     *
     * @return eventHandler The event handler.
     */
    @Override
    public Consumer<Runnable> getEventHandler() {
        return runnable -> proxyServer.getScheduler().runAsync(plugin, runnable);
    }
}
