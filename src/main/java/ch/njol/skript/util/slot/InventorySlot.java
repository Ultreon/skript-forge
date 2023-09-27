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
//package ch.njol.skript.util.slot;
//
//import com.github.ultreon.portutils.Material;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraftforge.eventbus.api.Event;
//import org.bukkit.inventory.CraftingInventory;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import org.bukkit.inventory.InventoryHolder;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.entity.player.Inventory;
//import org.eclipse.jdt.annotation.Nullable;
//
//import ch.njol.skript.bukkitutil.PlayerUtils;
//import ch.njol.skript.registrations.Classes;
//import ch.njol.skript.util.BlockInventoryHolder;
//
///**
// * Represents a slot in some inventory.
// */
//public class InventorySlot extends SlotWithIndex {
//
//	private final AbstractContainerMenu invi;
//	private final int index;
//
//	public InventorySlot(final AbstractContainerMenu invi, final int index) {
//		assert invi != null;
//		assert index >= 0;
//		this.invi = invi;
//		this.index = index;
//	}
//
//	public AbstractContainerMenu getInventory() {
//		return invi;
//	}
//
//	@Override
//	public int getIndex() {
//		return index;
//	}
//
//	@Override
//	@Nullable
//	public ItemStack getItem() {
//		if (index == -999) //Non-existent slot, e.g. Outside GUI
//			return null;
//		ItemStack item = invi.getItem(index);
//		return item == null  ? new ItemStack(Material.AIR, 1) : item.clone();
//	}
//
//	@Override
//	public void setItem(final @Nullable ItemStack item) {
//		invi.setItem(index, item != null && item.getType() != Material.AIR ? item : null);
//		if (invi instanceof Inventory)
//			PlayerUtils.updateInventory((ServerPlayer) invi.getHolder());
//	}
//
//	@Override
//	public int getAmount() {
//		ItemStack item = invi.getItem(index);
//		return item != null ? item.getAmount() : 0;
//	}
//
//	@Override
//	public void setAmount(int amount) {
//		ItemStack item = invi.getItem(index);
//		if (item != null)
//			item.setAmount(amount);
//		if (invi instanceof Inventory)
//			PlayerUtils.updateInventory((ServerPlayer) invi.getHolder());
//	}
//
//	@Override
//	public String toString(@Nullable Event e, boolean debug) {
//		InventoryHolder holder = invi != null ? invi.getHolder() : null;
//
//		if (holder instanceof BlockState)
//			holder = new BlockInventoryHolder((BlockState) holder);
//
//		if (holder != null) {
//			if (invi instanceof CraftingInventory) // 4x4 crafting grid is contained in player too!
//				return "crafting slot " + index + " of " + Classes.toString(holder);
//
//			return "inventory slot " + index + " of " + Classes.toString(holder);
//		}
//		return "inventory slot " + index + " of " + Classes.toString(invi);
//	}
//
//}
