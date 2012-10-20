package net.mystia.mystiacensor.event;

import java.util.Set;

import net.mystia.mystiacensor.functions.MystiaCensorAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is called whenever we send a message
 * 
 * @author ron975
 * 
 */
public class MystiaCensorChatEvent extends Event implements Cancellable
{
	private Boolean isUsingFactions = false;
	private Player player;
	private Boolean cancel = false;
	private Set<Player> recipients;
	private String formatTags = MystiaCensorAPI.getFormat();
	private String originalMessage;
	private String censoredMessage;
	private static final HandlerList handlers = new HandlerList();
	
	public MystiaCensorChatEvent(Set<Player> messageRecipients, String originalMessage, String censoredMessage,String formatTags, Player fromPlayer)
	{
		this.player = fromPlayer;
		this.recipients = messageRecipients;
		this.originalMessage = originalMessage;
		this.censoredMessage = censoredMessage;
		this.formatTags = formatTags;
	}


	public void setFactionsStatus(Boolean status){
		this.isUsingFactions = status;
	}
	public Player getPlayer()
	{
		return player;
	}

	public String getCensoredMessage()
	{
		return censoredMessage;
	}

	public String getOriginalMessage()
	{
		return originalMessage;
	}

	/**
	 * This gets the format for the event.
	 * <br/>
	 * <b>Warning:</b> This does not return a valid string to be formatted with String.format
	 * <br />To return a full message, you must use MystiaCensorAPI.parseMessages()
	 * <br />For example, {@code MystiaCensorAPI.parseMessage(message, getFormat());}
	 * @return Format string with "+message" where message is to be replaced
	 * @see net.mystia.mystiacensor.functions.MystiaCensorAPI#parseMessage(String, String)
	 */
	public String getFormat()
	{
		return formatTags;
	}

	public Set<Player> getRecipients()
	{
		return recipients;
	}

	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	public boolean isCancelled()
	{
		return cancel;
	}

	public void setCancelled(boolean isCancelled)
	{
		cancel = isCancelled;
	}

}
