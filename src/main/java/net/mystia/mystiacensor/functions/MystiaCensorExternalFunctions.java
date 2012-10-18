package net.mystia.mystiacensor.functions;

import java.lang.reflect.Field;
import java.util.logging.Level;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.RegisteredServiceProvider;

public class MystiaCensorExternalFunctions
{
	public static Field fieldRegisteredListenerDotPriority;
	static
	{
		try
		{
			fieldRegisteredListenerDotPriority = RegisteredListener.class.getDeclaredField("priority");
			fieldRegisteredListenerDotPriority.setAccessible(true);
		}
		catch (Exception e)
		{
			Bukkit.getServer().getLogger().severe("A reflection trick is broken!");
		}

	}

	public static void callEventAtMonitorOnly(Event event)
	{
		synchronized (Bukkit.getPluginManager())
		{
			HandlerList handlers = event.getHandlers();
			RegisteredListener[] listeners = handlers.getRegisteredListeners();

			for (RegisteredListener registration : listeners)
			{
				try
				{
					EventPriority priority = (EventPriority) fieldRegisteredListenerDotPriority.get(registration);
					if (priority != EventPriority.MONITOR)
						continue;
				}
				catch (Exception e)
				{
					e.printStackTrace();
					continue;
				}

				// This rest is almost copy pasted from SimplePluginManager in
				// Bukkit:

				if (!registration.getPlugin().isEnabled())
				{
					continue;
				}

				try
				{
					registration.callEvent(event);
				}
				catch (AuthorNagException ex)
				{
					Plugin plugin = registration.getPlugin();

					if (plugin.isNaggable())
					{
						plugin.setNaggable(false);

						String author = "<NoAuthorGiven>";

						if (plugin.getDescription().getAuthors().size() > 0)
						{
							author = plugin.getDescription().getAuthors().get(0);
						}
						Bukkit
							.getServer()
							.getLogger()
							.log(
								Level.SEVERE,
								String.format("Nag author: '%s' of '%s' about the following: %s", author, plugin.getDescription().getName(),
									ex.getMessage()));
					}
				}
				catch (Throwable ex)
				{
					Bukkit
						.getServer()
						.getLogger()
						.log(Level.SEVERE,
							"Could not pass event " + event.getEventName() + " to " + registration.getPlugin().getDescription().getName(), ex);
				}
			}
		}
	}

	/**
	 * Setting up Vault here
	 */
	public static Permission permission = null;
	public static Economy economy = null;
	public static Chat chat = null;

	public boolean setupVault()
	{
		if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager()
			.getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null)
		{
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	public boolean setupChat()
	{
		RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null)
		{
			chat = chatProvider.getProvider();
		}

		return (chat != null);
	}

	public boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager()
			.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null)
		{
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}

}
