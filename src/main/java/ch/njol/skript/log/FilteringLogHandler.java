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
package ch.njol.skript.log;

import java.util.logging.Level;

/**
 * @author Peter Güttinger
 */
public class FilteringLogHandler extends LogHandler {
	
	private final int minimum;
	
	public FilteringLogHandler(Level minimum) {
		this.minimum = minimum.intValue();
	}
	
	@Override
	public LogResult log(LogEntry entry) {
		return entry.level.intValue() >= minimum ? LogResult.LOG : LogResult.DO_NOT_LOG;
	}
	
	@Override
	public FilteringLogHandler start() {
		SkriptLogger.startLogHandler(this);
		return this;
	}
	
}
