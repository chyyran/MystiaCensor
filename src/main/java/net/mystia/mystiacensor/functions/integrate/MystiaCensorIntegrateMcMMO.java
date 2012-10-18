package net.mystia.mystiacensor.functions.integrate;

import org.bukkit.entity.Player;

import com.gmail.nossr50.datatypes.PlayerProfile;
import com.gmail.nossr50.util.Users;

public class MystiaCensorIntegrateMcMMO
{
	public boolean isInPartyOrAdminChat(Player chattingPlayer)
	{
		PlayerProfile profile = Users.getProfile(chattingPlayer);
		if (profile.getPartyChatMode() || profile.getAdminChatMode())
		{

			return true;
		}
		else
		{
			return false;
		}

	}
}
