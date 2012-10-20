package net.mystia.mystiacensor.functions.integrate;

import org.bukkit.Bukkit;

public class MystiaCensorIntegrateFunctions
{

	public static boolean isUsingMcMMO()
	{
		if (Bukkit.getServer().getPluginManager().getPlugin("mcMMO") != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean isUsingFactions()
	{
		if (Bukkit.getServer().getPluginManager().getPlugin("Factions") != null)
		{
			return true;
		}
		else
		{
			return false;
		}

	}
	

}
