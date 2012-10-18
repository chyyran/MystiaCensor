package net.mystia.mystiacensor.functions.integrate.chat;

import org.bukkit.Bukkit;

public enum ChatPlugin
{
	I_CHAT,M_CHAT,ESSENTIALS_CHAT,MYSTIA;
	
	public ChatPlugin getExternalChatPlugin(){
		if (Bukkit.getServer().getPluginManager().getPlugin("iChat") != null)
		{
			return I_CHAT;
		}else{
			if (Bukkit.getServer().getPluginManager().getPlugin("mChatSuite") != null)
			{
				return M_CHAT;
			}else{
				if (Bukkit.getServer().getPluginManager().getPlugin("EssentialsChat") != null){
					return ESSENTIALS_CHAT;
				}else{
					return MYSTIA;
				}
			}
			
			
		}
		
		
		
	}

}
