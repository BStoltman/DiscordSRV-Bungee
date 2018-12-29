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

package com.discordsrv.bungee.lookup;

import com.discordsrv.bungee.BungeeContext;
import com.discordsrv.bungee.unit.BungeeMinecraftPlayer;
import com.discordsrv.core.api.user.MinecraftPlayer;
import com.discordsrv.core.user.MalleablePlayerUserLookup;
import com.google.common.util.concurrent.FutureCallback;
import net.dv8tion.jda.core.entities.User;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.stream.Stream;

/**
 * MalleablePlayerUserLookup implementation, for DiscordSRV-Bungee.
 */
@ParametersAreNonnullByDefault
public class BungeePlayerUserLookup extends MalleablePlayerUserLookup<BungeeContext> {

    /**
     * Main constructor for the MalleablePlayerUserLookup type.
     *
     * @param context
     *         The context in which this lookup is performing.
     */
    public BungeePlayerUserLookup(final BungeeContext context) {
        super(context);
    }

    /**
     * Fetches all online Minecraft players.
     *
     * @param callback
     *         The callback to invoke when users have been collected.
     */
    @Override
    public void getOnlinePlayers(final FutureCallback<Stream<MinecraftPlayer>> callback) {
        try {
            callback.onSuccess(getContext().getProxyServer().getPlayers().stream().map(BungeeMinecraftPlayer::new));
        } catch (Throwable throwable) {
            callback.onFailure(throwable);
        }
    }

    /**
     * Fetches all online Discord users from the known guilds.
     *
     * @param callback
     *         The callback to invoke when users have been collected.
     */
    @Override
    public void getOnlineUsers(final FutureCallback<Stream<User>> callback) {
        try {
            callback.onSuccess(getContext().getJda().getUsers().stream());
        } catch (Throwable throwable) {
            callback.onFailure(throwable);
        }
    }
}
