package net.mystia.mystiacensor.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.mystia.mystiacensor.MystiaCensorMain;
import net.mystia.mystiacensor.event.MystiaCensorChatEvent;
import net.mystia.mystiacensor.functions.MystiaCensorExternalFunctions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * MystiaCensor API
 * 
 * @author ron975
 * 
 */
public class MystiaCensorAPI
{

	private static MystiaCensorMain plugin;

	/**
	 * Get profanities from config
	 * 
	 * @return List of profanities as defined in config
	 */
	public static List<String> getProfanities()
	{
		Set<String> profanitySet = plugin.getConfig().getConfigurationSection("censors").getKeys(false);
		List<String> profanityList = new ArrayList<String>(profanitySet);
		return profanityList;
	}

	/**
	 * Censor the message
	 * 
	 * @param originalMessage
	 *            Original, uncensored message
	 * @return Censored message
	 */
	public static String getCensoredMessage(String originalMessage)
	{
		String censorMessage = null;
		for (String s : getProfanities())
		{
			censorMessage = censorMessage.replaceAll(s, getCensoredWord(s));
		}

		return censorMessage;

	}

	/**
	 * Get the replacement for a profanity as defined in config
	 * 
	 * @param originalWord
	 *            Original, uncensored word
	 * @return Censored word
	 */
	public static String getCensoredWord(String originalWord)
	{
		String censoredWord = plugin.getConfig().getString("censor." + originalWord);
		return censoredWord;
	}

	/**
	 * Get the user defined format from config
	 * 
	 * @return Format String
	 */
	public static String getFormat()
	{
		String getFormat = plugin.getConfig().getString("formatting.format");
		return getFormat;
	}

	/**
	 * Parse the chat string and replace with appropriate tags
	 * 
	 * @param message
	 *            Message
	 * @param player
	 *            Sender
	 * @return Formatted chat message
	 */
	public static String parseChatTags(String formatTags, Player fromPlayer)
	{
		formatTags = ChatColor.translateAlternateColorCodes('&', formatTags);
		/*
		 * Rather than insert the message here, do that with parseMessage
		 * formatTags = formatTags.replace("+message", ChatColor.translateAlternateColorCodes('&', message));
		 */
		formatTags = formatTags.replace("+displayname", fromPlayer.getDisplayName());
		formatTags = formatTags.replace("+name", fromPlayer.getName());
		formatTags = formatTags.replace("+world", fromPlayer.getWorld().getName());
		formatTags = formatTags.replace("+level", Integer.toString(fromPlayer.getExpToLevel()));
		formatTags = formatTags.replace("+health", Integer.toString(fromPlayer.getHealth()));
		if (MystiaCensorExternalFunctions.permission.isEnabled())
		{

			formatTags = formatTags.replace("+group", MystiaCensorExternalFunctions.permission.getPrimaryGroup(fromPlayer));
			if (MystiaCensorExternalFunctions.chat.isEnabled())
			{
				formatTags = formatTags.replace(
					"+groupprefix",
					MystiaCensorExternalFunctions.chat.getGroupPrefix(fromPlayer.getWorld(),
						MystiaCensorExternalFunctions.permission.getPrimaryGroup(fromPlayer)));
				formatTags = formatTags.replace(
					"+groupsuffix",
					MystiaCensorExternalFunctions.chat.getGroupSuffix(fromPlayer.getWorld(),
						MystiaCensorExternalFunctions.permission.getPrimaryGroup(fromPlayer)));
			}
		}
		if (MystiaCensorExternalFunctions.economy.isEnabled())
		{
			formatTags = formatTags.replace("+moneyintrounddown",
				Integer.toString((int) MystiaCensorExternalFunctions.economy.getBalance(fromPlayer.getName())));
			formatTags = formatTags.replace("+moneyintroundup",
				Integer.toString((int) Math.round(MystiaCensorExternalFunctions.economy.getBalance(fromPlayer.getName()))));
			formatTags = formatTags.replace("+money", Double.toString((MystiaCensorExternalFunctions.economy.getBalance(fromPlayer.getName()))));
		}
		
		
		return formatTags;

	}
	
	public static String parseChatTags(Player fromPlayer){
		return parseChatTags(getFormat(), fromPlayer);
	}

	
	/**
	 * Used to insert a message into formatted chat tags
	 * @param message Message to be inserted
	 * @param format Formatted chat tags
	 * @return Formatted chat tags with message inserted
	 */
	public static String parseMessage(String message, String format){
		message = message.replace("+message",message);
		return message;
	}
	

	/**
	 * Sends a censored message
	 * 
	 * While it is possible to send completely different formatted messages, this is strongly discouraged.
	 * @param messageRecipients
	 *            Players who will receive the message
	 * @param originalMessage
	 *            Original, uncensored unformatted message for API purposes
	 * @param censoredMessage
	 *            Censored unformatted message for API purposes
	 * @param originalFormattedMessage
	 *            Original, uncensored formatted message. This one will be sent to the player
	 * @param censoredFormattedMessage
	 *            Censored formatted message. This one will be sent to the player
	 *        
	 * @see net.mystia.mystiacensor.functions.MystiaCensorAPI#parseChatString(String
	 *      message, Player fromPlayer)
	 * @see net.mystia.mystiacensor.functions.integrate#parseFactionsChatString(String
	 *      message, Player fromPlayer)
	 */

	public static void sendMessage(Set<Player> messageRecipients, String originalMessage,
		String censoredMessage, String formatTags ,Player fromPlayer)
	{

		/*
		 * Before we do anything, we fire the event appropriately so others may
		 * be able to modify it
		 */
		MystiaCensorChatEvent event = new MystiaCensorChatEvent(messageRecipients,
			originalMessage, censoredMessage,formatTags, fromPlayer);
		Bukkit.getServer().getPluginManager().callEvent(event);

		/*
		 * Now, instead of using the supplied variables, we use the ones from the event.
		 */
		if (!event.isCancelled())
		{
			for (Player toPlayer : event.getRecipients())
			{
				if (toPlayer.hasPermission("mystia.censor"))
				{

					toPlayer.sendMessage(parseMessage(formatTags,censoredMessage));
				}
				else
				{
					toPlayer.sendMessage(parseMessage(formatTags,originalMessage));
					//toPlayer.sendMessage(parseMessage(MystiaCensorIntegrateFactions.parseFactionsChatString(formatTags, fromPlayer, toPlayer),originalMessage));
				}
			}
		}else{
			return;
		}
		
		
	
	}
	
	
	
	
}
