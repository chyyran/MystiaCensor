package net.mystia.mystiacensor;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.mystia.mystiacensor.functions.MystiaCensorAPI;
import net.mystia.mystiacensor.functions.MystiaCensorExternalFunctions;

public class MystiaCensorListener implements Listener
{

	private MystiaCensorAPI api;

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		String originalMessage = event.getMessage();
		String censoredMessage = event.getMessage();
		censoredMessage = api.getCensoredMessage(originalMessage);

		/*
		 * As how Factions does it, we cancel the existing event and call a new
		 * one on Monitor priority
		 */
		event.setCancelled(true);
		AsyncPlayerChatEvent monitorOnlyEvent = new AsyncPlayerChatEvent(false, player, originalMessage, new HashSet<Player>(Arrays.asList(Bukkit
			.getOnlinePlayers())));
		// Here we set the format
		monitorOnlyEvent.setFormat(api.getParsedChatString(originalMessage, player));
		MystiaCensorExternalFunctions.callEventAtMonitorOnly(monitorOnlyEvent);
		// Again, as how factions implements this, we log in console manually
		Bukkit.getConsoleSender().sendMessage(
			String.format(monitorOnlyEvent.getFormat(), monitorOnlyEvent.getPlayer().getDisplayName(), monitorOnlyEvent.getMessage()));

		/*
		 * If we do not need to integrate with anything, send the message as
		 * formatted, while considering players that have censored
		 */
		api.sendFormattedMessage(event.getRecipients(), originalMessage, censoredMessage, player);
	}
}
