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
import com.discordsrv.core.api.channel.Chat;
import com.discordsrv.core.channel.MalleableChatChannelLookup;
import com.google.common.util.concurrent.FutureCallback;
import net.dv8tion.jda.core.entities.Channel;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.stream.Stream;

/**
 * MalleableChatChannelLookup implementation, for DiscordSRV-Bungee.
 */
@ParametersAreNonnullByDefault
public class BungeeChatChannelLookup extends MalleableChatChannelLookup<BungeeContext> {

    /**
     * Main constructor for the MalleableChatChannelLookup type.
     *
     * @param context
     *         The context in which this lookup is performing.
     */
    public BungeeChatChannelLookup(final BungeeContext context) {
        super(context);
    }

    /**
     * Fetches all the chats which this lookup can find.
     *
     * @param callback
     *         The callback to invoke when collected.
     */
    @Override
    public void getKnownChats(final FutureCallback<Stream<Chat>> callback) {
        // TODO
    }

    /**
     * Fetches all the channels which this lookup can find.
     *
     * @param callback
     *         The callback to invoke when collected.
     */
    @Override
    public void getKnownChannels(final FutureCallback<Stream<Channel>> callback) {
        // TODO
    }
}
