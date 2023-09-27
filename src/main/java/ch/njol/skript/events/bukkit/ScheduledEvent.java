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
package ch.njol.skript.events.bukkit;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.eventbus.api.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

/**
 * @author Peter Güttinger
 */
public class ScheduledEvent extends Event {
	static {
		EventValues.registerEventValue(ScheduledEvent.class, ServerLevel.class, new Getter<>() {
			@Override
			@Nullable
			public ServerLevel get(final ScheduledEvent e) {
				return e.getWorld();
			}
		}, 0, "There's no world in a periodic event if no world is given in the event (e.g. like 'every hour in \"world\"')", ScheduledNoWorldEvent.class);
	}
	
	@Nullable
	private final ServerLevel world;
	
	public ScheduledEvent(final @Nullable ServerLevel world) {
		this.world = world;
	}
	
	@Nullable
	public final ServerLevel getWorld() {
		return world;
	}
}
