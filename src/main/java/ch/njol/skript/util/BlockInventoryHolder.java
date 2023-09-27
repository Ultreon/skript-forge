///**
// *   This file is part of Skript.
// * <p>
// *  Skript is free software: you can redistribute it and/or modify
// *  it under the terms of the GNU General Public License as published by
// *  the Free Software Foundation, either version 3 of the License, or
// *  (at your option) any later version.
// * <p>
// *  Skript is distributed in the hope that it will be useful,
// *  but WITHOUT ANY WARRANTY; without even the implied warranty of
// *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// *  GNU General Public License for more details.
// * <p>
// *  You should have received a copy of the GNU General Public License
// *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
// * <p>
// * Copyright Peter Güttinger, SkriptLang team and contributors
// */
//package ch.njol.skript.util;
//
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import org.bukkit.inventory.InventoryHolder;
//
///**
// * Main usage is {@link ch.njol.skript.expressions.ExprInventoryInfo}
// * This class allows Skript to return a block while being able to recognize it as {@link InventoryHolder},
// * You may only use this class if a expression's return type is an {@link InventoryHolder}.
// */
//public class BlockInventoryHolder extends BlockStateBlock implements InventoryHolder {
//
//	public BlockInventoryHolder(BlockState state) {
//		super(state, false);
//	}
//
//	@Override
//	public AbstractContainerMenu getInventory() {
//		return ((InventoryHolder) state).getInventory();
//	}
//}
