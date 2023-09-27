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
// import ch.njol.skript.conditions.base.PropertyCondition;
// import ch.njol.skript.doc.Description;
// import ch.njol.skript.doc.Examples;
// import ch.njol.skript.doc.Name;
// import ch.njol.skript.doc.Since;
// 
// @Name("Can Fly")
// @Description("Whether a player is allowed to fly.")
// @Examples("player can fly")
// @Since("2.3")
// 
// public class CondCanFly extends PropertyCondition<Player> {
// 	
// 	static {
// 		register(CondCanFly.class, PropertyType.CAN, "fly", "players");
// 	}
// 	
// 	@Override
// 	public boolean check(Player player) {
// 		return player.getAllowFlight();
// 	}
// 	
// 	@Override
// 	protected PropertyType getPropertyType() {
// 		return PropertyType.CAN;
// 	}
// 	
// 	@Override
// 	protected String getPropertyName() {
// 		return "fly";
// 	}
// }
