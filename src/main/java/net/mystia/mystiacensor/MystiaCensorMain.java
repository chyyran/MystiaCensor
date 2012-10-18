package net.mystia.mystiacensor;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.mystia.mystiacensor.functions.MystiaCensorAPI;
import net.mystia.mystiacensor.functions.MystiaCensorExternalFunctions;

public class MystiaCensorMain extends JavaPlugin
{
	private MystiaCensorExternalFunctions functions;
	private MystiaCensorAPI api;
	public void onEnable()
	{

		if (!functions.setupVault())
		{
			Bukkit.getServer().getLogger().info("Vault not found, disabling plugin");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}
		if (!functions.setupEconomy())
		{
			Bukkit.getServer().getLogger().info("Economy plugin not found. Economy chat features will not work");
		}
		if (!functions.setupPermissions())
		{
			Bukkit.getServer().getLogger().info("Permissions plugin not found. Permissions chat features will not work");
		}
		if (!functions.setupChat())
		{
			Bukkit.getServer().getLogger().info("Vault compatible chat plugin not found. Group prefix/suffix chat features will not work");
		}

	}

	
	
	public void onDisable()
	{

	}

}
