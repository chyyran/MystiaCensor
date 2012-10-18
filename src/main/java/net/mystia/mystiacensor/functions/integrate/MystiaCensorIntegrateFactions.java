package net.mystia.mystiacensor.functions.integrate;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;


public class MystiaCensorIntegrateFactions
{


	/**
	 * Gets name of faction player is in
	 * @param player
	 * @return Faction Name
	 */
	public String getFactionName(Player player)
	{
		return getFactionName(player, false);
	}
	/**
	 * Gets name of faction player is in
	 * @param player Player to get faction name
	 * @param isForced Are we forcing Factions to return a name?
	 * @return Faction Name
	 */
	public String getFactionName(Player player, Boolean isForced)
	{
		FPlayer fplayer = FPlayers.i.get(player);
		if (isForced)
		{
			return fplayer.getFaction().getTag();
		}
		else
		{
			if (fplayer.hasFaction())
			{
				return fplayer.getFaction().getTag();
			}
			else
			{
				return "";
			}
		}

	}
	/**
	 * Get relation color of one player, to another player
	 * @param fromPlayer Player we want to get the relation from
	 * @param toPlayer Relation colour of fromPlayer
	 * @return Relation color of fromPlayer's faction to toPlayer's faction
	 */
	public ChatColor getFactionRelationToColor(Player fromPlayer, Player toPlayer)
	{
		FPlayer fromFPlayer = FPlayers.i.get(fromPlayer);
		FPlayer toFPlayer = FPlayers.i.get(toPlayer);
		return fromFPlayer.getRelationTo(toFPlayer).getColor();

	}
	/**
	 * Gets the Faction title of the player
	 * @param player Player we want to get the title from
	 * @return Faction title
	 */
	public String getFactionTitle(Player player)
	{
		FPlayer fplayer = FPlayers.i.get(player);
		return fplayer.getTitle();
	}
	
	/**
	 * Gets the role prefix of the player
	 * @param player Player we want to get the roleprefix from
	 * @return Role prefix
	 */
	public String getFactionRolePrefix(Player player)
	{
		FPlayer fplayer = FPlayers.i.get(player);
		return fplayer.getRole().getPrefix();

	}


	public String parseFactionsChatString(String parsedChatString, Player fromPlayer, Player toPlayer)
	{
		// Left padding
		parsedChatString = parsedChatString.replace("[factions_title_pl]", " " + getFactionTitle(fromPlayer));
		parsedChatString = parsedChatString.replace("[factions_relcolor_pl]", " " + getFactionRelationToColor(fromPlayer, toPlayer).toString());
		parsedChatString = parsedChatString.replace("[factions_tag_pl]", " " + getFactionName(fromPlayer));
		parsedChatString = parsedChatString.replace("[factions_tagforce_pl]", " " + getFactionName(fromPlayer, true));
		parsedChatString = parsedChatString.replace("[factions_roleprefix_pl]", " " + getFactionRolePrefix(fromPlayer));
		// Right padding
		parsedChatString = parsedChatString.replace("[factions_title_pr]", getFactionTitle(fromPlayer) + " ");
		parsedChatString = parsedChatString.replace("[factions_relcolor_pr]", getFactionRelationToColor(fromPlayer, toPlayer).toString() + " ");
		parsedChatString = parsedChatString.replace("[factions_tag_pr]", getFactionName(fromPlayer) + " ");
		parsedChatString = parsedChatString.replace("[factions_tagforce_pr]", getFactionName(fromPlayer, true) + " ");
		parsedChatString = parsedChatString.replace("[factions_roleprefix_pr]", getFactionRolePrefix(fromPlayer) + " ");
		// No padding
		parsedChatString = parsedChatString.replace("[factions_title]", getFactionTitle(fromPlayer));
		parsedChatString = parsedChatString.replace("[factions_relcolor]", getFactionRelationToColor(fromPlayer, toPlayer).toString());
		parsedChatString = parsedChatString.replace("[factions_tag]", getFactionName(fromPlayer));
		parsedChatString = parsedChatString.replace("[factions_tagforce]", getFactionName(fromPlayer, true));
		parsedChatString = parsedChatString.replace("[factions_roleprefix]", getFactionRolePrefix(fromPlayer));

		return parsedChatString;
	}

}
