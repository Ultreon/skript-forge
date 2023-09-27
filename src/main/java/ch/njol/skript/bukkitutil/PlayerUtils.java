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
package ch.njol.skript.bukkitutil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import com.github.ultreon.portutils.Material;
import net.minecraft.server.level.ServerPlayer;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import ch.njol.skript.Skript;
import ch.njol.skript.util.Task;

/**
 * Contains utility methods related to players
 */
public abstract class PlayerUtils {

	private static final Set<ServerPlayer> inventoryUpdateList = Collections.synchronizedSet(new HashSet<>());

	/**
	 * Updates the clients inventory within a tick, using {@link Player#updateInventory()}.
	 * Recommended over directly calling the update method,
	 * as multiple calls to this method within a short timespan will not send multiple updates to the client.
	 */
	public static void updateInventory(@Nullable ServerPlayer player) {
		if (player != null)
			inventoryUpdateList.add(player);
	}

	static {
		new Task(Skript.getInstance(), 1, 1) {
			@Override
			public void run() {
				for (ServerPlayer p : inventoryUpdateList)
					p.updateInventory();

				inventoryUpdateList.clear();
			}
		};
	}

	/**
	 * @deprecated use {@link Bukkit#getOnlinePlayers()} instead
	 */
	@Deprecated
	public static Collection<? extends ServerPlayer> getOnlinePlayers() {
		return ImmutableList.copyOf(Bukkit.getOnlinePlayers());
	}

	public static boolean canEat(ServerPlayer p, Material food) {
		GameMode gm = p.getGameMode();
		if (gm == GameMode.CREATIVE || gm == GameMode.SPECTATOR)
			return false; // Can't eat anything in those gamemodes
		
		boolean edible = food.isEdible();
		if (!edible)
			return false;
		boolean special;
		switch (food) {
			case GOLDEN_APPLE:
			case CHORUS_FRUIT:
				special = true;
				break;
				//$CASES-OMITTED$
			default:
				special = false;
		}
		if (p.getFoodLevel() < 20 || special)
			return true;

		return false;
	}
}
