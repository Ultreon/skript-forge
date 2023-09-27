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
package ch.njol.skript.command;

import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.eventbus.api.Event;
import org.bukkit.event.HandlerList;
import org.eclipse.jdt.annotation.Nullable;

/**
 * @author Peter Güttinger
 */
public class CommandEvent extends Event {

	private final CommandSourceStack sender;
	String command;

	@Nullable
	private final String[] args;

	public CommandEvent(CommandSourceStack sender, String command, @Nullable String[] args) {
		this.sender = sender;
		this.command = command;
		this.args = args;
	}

	public CommandSourceStack getSender() {
		return sender;
	}

	public String getCommand() {
		return command;
	}

	@Nullable
	public String[] getArgs() {
		return args;
	}

	// Bukkit stuff
	private final static HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
