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

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		String originalMessage = event.getMessage();
		String censoredMessage = MystiaCensorAPI.getCensoredMessage(originalMessage);

		/*
		 * As how Factions does it, we cancel the existing event and call a new
		 * one on Monitor priority
		 */
		event.setCancelled(true);
		AsyncPlayerChatEvent monitorOnlyEvent = new AsyncPlayerChatEvent(false, player, originalMessage, new HashSet<Player>(Arrays.asList(Bukkit
			.getOnlinePlayers())));
		// Here we set the format
		monitorOnlyEvent.setFormat(MystiaCensorAPI.parseMessage(originalMessage, MystiaCensorAPI.parseChatTags(player)));
		MystiaCensorExternalFunctions.callEventAtMonitorOnly(monitorOnlyEvent);
		/*
		 *  Again, as how factions implements this, we log in console manually.
		 *  Since we're no longer using the String.format method, and getFormat() will return the whole
		 *  chat string to be sent, we have no need to send anything more than that.
		 */
		Bukkit.getConsoleSender().sendMessage(monitorOnlyEvent.getFormat());
			

		
		/*
		 * If we do not need to integrate with anything, send the message as
		 * formatted, while considering players that have censored.
		 * 
		 * If we need to integrate with mcMMO, we will check if the player is in a party or admin chat
		 * and will not send the message
		 * 
		 * If we need to integrate with Factions, we will parse the factions tags with parseFactionsChatString()
		 * 
		 * To prevent code duplication, we pass on the formatted chat string on method call
		 * rather than format the chat string in the method itself
		 * 
		 * Because we need to fire the Chat event properly, we must pass on to the method our Bukkit event parameters
		 */
		
		
		MystiaCensorAPI.sendMessage(event.getRecipients(),originalMessage, censoredMessage, MystiaCensorAPI.parseChatTags(player) ,player);
	}
}
