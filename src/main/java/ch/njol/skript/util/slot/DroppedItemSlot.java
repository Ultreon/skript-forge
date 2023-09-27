/**
 *   This file is part of Skript.
 * <p>
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * <p>
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * <p>
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Copyright Peter Güttinger, SkriptLang team and contributors
 */
package ch.njol.skript.util.slot;

import org.bukkit.entity.Item;
import net.minecraftforge.eventbus.api.Event;
import net.minecraft.world.item.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.registrations.Classes;

/**
 * Represents an item of dropped item entity.
 */
public class DroppedItemSlot extends Slot {

	private Item entity;
	
	public DroppedItemSlot(Item item) {
		this.entity = item;
	}
	
	@Override
	@Nullable
	public ItemStack getItem() {
		return entity.getItemStack();
	}

	@Override
	public void setItem(@Nullable ItemStack item) {
		assert item != null;
		entity.setItemStack(item);
	}
	
	@Override
	public int getAmount() {
		return entity.getItemStack().getAmount();
	}
	
	@Override
	public void setAmount(int amount) {
		entity.getItemStack().setAmount(amount);
	}
	
	@Override
	public boolean isSameSlot(Slot o) {
		return o instanceof DroppedItemSlot && ((DroppedItemSlot) o).entity.equals(entity);
	}
	
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return Classes.toString(getItem());
	}
	
}
