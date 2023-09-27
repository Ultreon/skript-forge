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
// import ch.njol.skript.aliases.ItemType;
// import ch.njol.skript.conditions.base.PropertyCondition;
// import ch.njol.skript.doc.Description;
// import ch.njol.skript.doc.Examples;
// import ch.njol.skript.doc.Name;
// import ch.njol.skript.doc.Since;
// 
// @Name("Is Occluding")
// @Description("Checks whether an item is a block and completely blocks vision.")
// @Examples("player's tool is occluding")
// @Since("2.5.1")
// public class CondIsOccluding extends PropertyCondition<ItemType> {
// 	
// 	static {
// 		register(CondIsOccluding.class, "occluding", "itemtypes");
// 	}
// 	
// 	@Override
// 	public boolean check(ItemType item) {
// 		return item.getMaterial().isOccluding();
// 	}
// 	
// 	@Override
// 	protected String getPropertyName() {
// 		return "occluding";
// 	}
// 	
// }
