package net.mystia.mystiacensor.functions.integrate;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

import net.mystia.mystiacensor.functions.MystiaCensorAPI;

public class MystiaCensorIntegrateFactions
{

	private MystiaCensorAPI api;
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


	public String getFactionsParsedChatString(String parsedChatString, Player from, Player to)
	{
		// Left padding
		parsedChatString = parsedChatString.replace("[factions_title_pl]", " " + getFactionTitle(from));
		parsedChatString = parsedChatString.replace("[factions_relcolor_pl]", " " + getFactionRelationToColor(from, to).toString());
		parsedChatString = parsedChatString.replace("[factions_tag_pl]", " " + getFactionName(from));
		parsedChatString = parsedChatString.replace("[factions_tagforce_pl]", " " + getFactionName(from, true));
		parsedChatString = parsedChatString.replace("[factions_roleprefix_pl]", " " + getFactionRolePrefix(from));
		// Right padding
		parsedChatString = parsedChatString.replace("[factions_title_pr]", getFactionTitle(from) + " ");
		parsedChatString = parsedChatString.replace("[factions_relcolor_pr]", getFactionRelationToColor(from, to).toString() + " ");
		parsedChatString = parsedChatString.replace("[factions_tag_pr]", getFactionName(from) + " ");
		parsedChatString = parsedChatString.replace("[factions_tagforce_pr]", getFactionName(from, true) + " ");
		parsedChatString = parsedChatString.replace("[factions_roleprefix_pr]", getFactionRolePrefix(from) + " ");
		// No padding
		parsedChatString = parsedChatString.replace("[factions_title]", getFactionTitle(from));
		parsedChatString = parsedChatString.replace("[factions_relcolor]", getFactionRelationToColor(from, to).toString());
		parsedChatString = parsedChatString.replace("[factions_tag]", getFactionName(from));
		parsedChatString = parsedChatString.replace("[factions_tagforce]", getFactionName(from, true));
		parsedChatString = parsedChatString.replace("[factions_roleprefix]", getFactionRolePrefix(from));

		return parsedChatString;
	}

}
