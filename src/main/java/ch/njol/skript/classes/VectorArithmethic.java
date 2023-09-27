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
package ch.njol.skript.classes;

import org.bukkit.util.Vec3;

/**
 * @author bi0qaw
 */
public class VectorArithmethic implements Arithmetic<Vec3, Vec3> {
	
	@Override
	public Vec3 difference(final Vec3 first, final Vec3 second) {
		return new Vec3(Math.abs(first.getX() - second.getX()), Math.abs(first.getY() - second.getY()), Math.abs(first.getZ() - second.getZ()));
	}
	
	@Override
	public Vec3 add(final Vec3 value, final Vec3 difference) {
		return new Vec3().add(value).add(difference);
	}
	
	@Override
	public Vec3 subtract(Vec3 value, Vec3 difference) {
		return new Vec3().add(value).subtract(difference);
	}

	@Override
	public Vec3 multiply(Vec3 value, Vec3 multiplier) {
		return value.clone().multiply(multiplier);
	}

	@Override
	public Vec3 divide(Vec3 value, Vec3 divider) {
		return value.clone().divide(divider);
	}

	@Override
	public Vec3 power(Vec3 value, Vec3 exponent) {
		return new Vec3(Math.pow(value.getX(), exponent.getX()), Math.pow(value.getY(), exponent.getY()), Math.pow(value.getZ(), exponent.getZ()));
	}
}
