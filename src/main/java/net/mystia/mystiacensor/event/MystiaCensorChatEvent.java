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

	private Player player;
	private Boolean cancel = false;
	private Set<Player> recipients;
	private String formattedCensoredMessage;
	private String formattedOriginalMessage;
	private String originalMessage;
	private String censoredMessage;
	private static final HandlerList handlers = new HandlerList();
	private MystiaCensorAPI api;
	
	
	public MystiaCensorChatEvent(Set<Player> messageRecipients, String originalMessage, String censoredMessage, Player fromPlayer)
	{
		this.player = fromPlayer;
		this.recipients = messageRecipients;
		this.formattedOriginalMessage = api.parseChatString(originalMessage, fromPlayer);
		this.formattedCensoredMessage = api.parseChatString(censoredMessage,fromPlayer);
		this.originalMessage = originalMessage;
		this.censoredMessage = censoredMessage;
	}

	public MystiaCensorChatEvent(Set<Player> messageRecipients, String originalMessageFormatted, String censoredMessageFormatted,
		String originalMessage, String censoredMessage, Player fromPlayer)
	{
		this.player = fromPlayer;
		this.recipients = messageRecipients;
		this.formattedOriginalMessage = originalMessageFormatted;
		this.formattedCensoredMessage = censoredMessageFormatted;
		this.originalMessage = originalMessage;
		this.censoredMessage = censoredMessage;
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

	public String getFormattedCensoredMessage()
	{
		return formattedCensoredMessage;
	}

	public String getFormattedOriginalMessage()
	{
		return formattedOriginalMessage;
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
