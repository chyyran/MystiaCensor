package net.mystia.mystiacensor.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.mystia.mystiacensor.MystiaCensorMain;
import net.mystia.mystiacensor.functions.MystiaCensorExternalFunctions;

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
	 * Here we parse the format string
	 * 
	 * @param message
	 *            Message
	 * @param player
	 *            Sender
	 * @return Formatted chat message
	 */
	public String getParsedChatString(String message, Player fromPlayer)
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
 * @param messageRecipients Players who will receive the message
 * @param originalMessage Original, uncensored formatted message 
 * @param censoredMessage Censored formatted message 
 * @param fromPlayer Player that originally send the message
 * @see net.mystia.mystiacensor.functions.MystiaCensorAPI#getParsedChatString(String message, Player fromPlayer)
 */

	public void sendFormattedMessage(Set<Player> messageRecipients, String originalMessage, String censoredMessage, Player fromPlayer)
	{
		for (Player toPlayer : messageRecipients)
		{
			if (toPlayer.hasPermission("mystia.censor"))
			{

				toPlayer.sendMessage(getParsedChatString(censoredMessage, fromPlayer));
			}
			else
			{
				toPlayer.sendMessage(getParsedChatString(originalMessage, fromPlayer));
			}
		}
	}

}
