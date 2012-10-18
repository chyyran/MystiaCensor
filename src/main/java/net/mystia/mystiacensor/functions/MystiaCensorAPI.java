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

	private MystiaCensorMain plugin;

	/**
	 * Get profanities from config
	 * 
	 * @return List of profanities as defined in config
	 */
	public List<String> getProfanities()
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
	public String getCensoredMessage(String originalMessage)
	{
		String censorMessage = null;
		for (String s : this.getProfanities())
		{
			censorMessage = censorMessage.replaceAll(s, this.getCensoredWord(s));
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
	public String getCensoredWord(String originalWord)
	{
		String censoredWord = plugin.getConfig().getString("censor." + originalWord);
		return censoredWord;
	}

	/**
	 * Get the user defined format from config
	 * 
	 * @return Format String
	 */
	public String getFormat()
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
	public String parseChatString(String message, Player fromPlayer)
	{
		String parsedChat = this.getFormat();
		parsedChat = parsedChat.replace("+message", message);
		parsedChat = parsedChat.replace("+displayname", fromPlayer.getDisplayName());
		parsedChat = parsedChat.replace("+name", fromPlayer.getName());
		parsedChat = parsedChat.replace("+world", fromPlayer.getWorld().getName());
		parsedChat = parsedChat.replace("+level", Integer.toString(fromPlayer.getExpToLevel()));
		parsedChat = parsedChat.replace("+health", Integer.toString(fromPlayer.getHealth()));
		if (MystiaCensorExternalFunctions.permission.isEnabled())
		{

			parsedChat = parsedChat.replace("+group", MystiaCensorExternalFunctions.permission.getPrimaryGroup(fromPlayer));
			if (MystiaCensorExternalFunctions.chat.isEnabled())
			{
				parsedChat = parsedChat.replace(
					"+groupprefix",
					MystiaCensorExternalFunctions.chat.getGroupPrefix(fromPlayer.getWorld(),
						MystiaCensorExternalFunctions.permission.getPrimaryGroup(fromPlayer)));
				parsedChat = parsedChat.replace(
					"+groupsuffix",
					MystiaCensorExternalFunctions.chat.getGroupSuffix(fromPlayer.getWorld(),
						MystiaCensorExternalFunctions.permission.getPrimaryGroup(fromPlayer)));
			}
		}
		if (MystiaCensorExternalFunctions.economy.isEnabled())
		{
			parsedChat = parsedChat.replace("+moneyintrounddown",
				Integer.toString((int) MystiaCensorExternalFunctions.economy.getBalance(fromPlayer.getName())));
			parsedChat = parsedChat.replace("+moneyintroundup",
				Integer.toString((int) Math.round(MystiaCensorExternalFunctions.economy.getBalance(fromPlayer.getName()))));
			parsedChat = parsedChat.replace("+money", Double.toString((MystiaCensorExternalFunctions.economy.getBalance(fromPlayer.getName()))));
		}

		parsedChat = ChatColor.translateAlternateColorCodes('&', parsedChat);
		return parsedChat;

	}

	/**
	 * Sends a censored message
	 * 
	 * @param messageRecipients
	 *            Players who will receive the message
	 * @param originalMessage
	 *            Original, uncensored formatted message
	 * @param censoredMessage
	 *            Censored formatted message
	 * @see net.mystia.mystiacensor.functions.MystiaCensorAPI#parseChatString(String
	 *      message, Player fromPlayer)
	 * @see net.mystia.mystiacensor.functions.integrate#parseFactionsChatString(String
	 *      message, Player fromPlayer)
	 */

	public void sendMessage(Set<Player> messageRecipients, String originalMessageFormatted, String censoredMessageFormatted, String originalMessage,
		String censoredMessage, Player fromPlayer)
	{

		/*
		 * Before we do anything, we fire the event appropriately so others may
		 * be able to modify it
		 */
		MystiaCensorChatEvent event = new MystiaCensorChatEvent(messageRecipients, originalMessageFormatted, censoredMessageFormatted,
			originalMessage, censoredMessage, fromPlayer);
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

					toPlayer.sendMessage(event.getFormattedCensoredMessage());
				}
				else
				{
					toPlayer.sendMessage(event.getFormattedOriginalMessage());
				}
			}
		}else{
			return;
		}
	}

}
