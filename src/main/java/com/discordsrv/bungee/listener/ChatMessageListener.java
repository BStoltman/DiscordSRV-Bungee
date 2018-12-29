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

package com.discordsrv.bungee.listener;

import com.discordsrv.bungee.DSRVBungee;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Chat message listener.
 */
@AllArgsConstructor
public class ChatMessageListener implements Listener {

    private DSRVBungee plugin;

    /**
     * ChatEvent listener.
     *
     * @param event ChatEvent
     */
    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer player = null;
        if (event.getSender() instanceof ProxiedPlayer) {
            player = (ProxiedPlayer) event.getSender();
        }
        plugin.sendMessage(event.getMessage(), player);
    }

}
