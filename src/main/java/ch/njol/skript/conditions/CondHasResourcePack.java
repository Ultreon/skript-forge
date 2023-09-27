// /**
//  *   This file is part of Skript.
//  *
//  *  Skript is free software: you can redistribute it and/or modify
//  *  it under the terms of the GNU General Public License as published by
//  *  the Free Software Foundation, either version 3 of the License, or
//  *  (at your option) any later version.
//  *
//  *  Skript is distributed in the hope that it will be useful,
//  *  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  *  GNU General Public License for more details.
//  *
//  *  You should have received a copy of the GNU General Public License
//  *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
//  *
//  * Copyright Peter Güttinger, SkriptLang team and contributors
//  */
// package ch.njol.skript.conditions;
// 
// import org.bukkit.entity.Player;
// 
// import ch.njol.skript.Skript;
// import ch.njol.skript.conditions.base.PropertyCondition;
// import ch.njol.skript.doc.Description;
// import ch.njol.skript.doc.Examples;
// import ch.njol.skript.doc.Name;
// import ch.njol.skript.doc.RequiredPlugins;
// import ch.njol.skript.doc.Since;
// 
// @Name("Has Resource Pack")
// @Description("Checks whether the given players have a server resource pack loaded. Please note that this can't detect " +
// 		"player's own resource pack, only the resource pack that sent by the server.")
// @Examples("if the player has a resource pack loaded:")
// @Since("2.4")
// @RequiredPlugins("Paper 1.9 or newer")
// public class CondHasResourcePack extends PropertyCondition<Player> {
// 
// 	static {
// 		if (Skript.methodExists(Player.class, "hasResourcePack"))
// 			register(CondHasResourcePack.class, PropertyType.HAVE, "[a] resource pack [(loaded|installed)]", "players");
// 	}
// 
// 	@Override
// 	public boolean check(Player player) {
// 		return player.hasResourcePack();
// 	}
// 
// 	@Override
// 	protected PropertyType getPropertyType() {
// 		return PropertyType.HAVE;
// 	}
// 
// 	@Override
// 	protected String getPropertyName() {
// 		return "resource pack loaded";
// 	}
// 
// }
