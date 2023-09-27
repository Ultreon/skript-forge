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
package ch.njol.skript.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ch.njol.skript.bukkitutil.WorldUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import net.minecraft.server.level.ServerLevel;
import com.github.ultreon.portutils.BlockInstance;
import org.bukkit.util.Vec3;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.util.Math2;

/**
 * AABB = Axis-Aligned Bounding Box
 * 
 * @author Peter Güttinger
 */
public class AABB implements Iterable<BlockInstance> {
	
	final ServerLevel world;
	final Vec3 lowerBound, upperBound;

	//	private final static Vec3 EPSILON = new Vec3(Skript.EPSILON, Skript.EPSILON, Skript.EPSILON);
	
	@SuppressWarnings("null")
	public AABB(final Location l1, final Location l2) {
		if (l1.getWorld() != l2.getWorld())
			throw new IllegalArgumentException("Locations must be in the same world");
		world = l1.getWorld();
		lowerBound = new Vec3(Math.min(l1.getBlockX(), l2.getBlockX()), Math.min(l1.getBlockY(), l2.getBlockY()), Math.min(l1.getBlockZ(), l2.getBlockZ()));
		upperBound = new Vec3(Math.max(l1.getBlockX(), l2.getBlockX()), Math.max(l1.getBlockY(), l2.getBlockY()), Math.max(l1.getBlockZ(), l2.getBlockZ()));
	}
	
	public AABB(final BlockInstance b1, final BlockInstance b2) {
		if (b1.getWorld() != b2.getWorld())
			throw new IllegalArgumentException("Blocks must be in the same world");
		world = b1.getWorld();
		lowerBound = new Vec3(Math.min(b1.getX(), b2.getX()), Math.min(b1.getY(), b2.getY()), Math.min(b1.getZ(), b2.getZ()));
		upperBound = new Vec3(Math.max(b1.getX(), b2.getX()), Math.max(b1.getY(), b2.getY()), Math.max(b1.getZ(), b2.getZ()));
	}
	
	@SuppressWarnings("null")
	public AABB(final Location center, final double rX, final double rY, final double rZ) {
		assert rX >= 0 && rY >= 0 && rZ >= 0 : rX + "," + rY + "," + rY;
		world = center.getWorld();
		int min = WorldUtils.getWorldMinHeight(world);
		lowerBound = new Vec3(center.getX() - rX, Math.max(center.getY() - rY, min), center.getZ() - rZ);
		upperBound = new Vec3(center.getX() + rX, Math.min(center.getY() + rY, world.getMaxHeight() - 1), center.getZ() + rZ);
	}
	
	public AABB(final ServerLevel w, final Vec3 v1, final Vec3 v2) {
		world = w;
		lowerBound = new Vec3(Math.min(v1.getX(), v2.getX()), Math.min(v1.getY(), v2.getY()), Math.min(v1.getZ(), v2.getZ()));
		upperBound = new Vec3(Math.max(v1.getX(), v2.getX()), Math.max(v1.getY(), v2.getY()), Math.max(v1.getZ(), v2.getZ()));
	}
	
	public AABB(final Chunk c) {
		world = c.getWorld();
		int min = WorldUtils.getWorldMinHeight(world);
		lowerBound = c.getBlock(0, min, 0).getLocation().toVector();
		upperBound = c.getBlock(15, world.getMaxHeight() - 1, 15).getLocation().toVector();
	}
	
	public boolean contains(final Location l) {
		if (l.getWorld() != world)
			return false;
		return lowerBound.getX() - Skript.EPSILON < l.getX() && l.getX() < upperBound.getX() + Skript.EPSILON
				&& lowerBound.getY() - Skript.EPSILON < l.getY() && l.getY() < upperBound.getY() + Skript.EPSILON
				&& lowerBound.getZ() - Skript.EPSILON < l.getZ() && l.getZ() < upperBound.getZ() + Skript.EPSILON;
	}
	
	public boolean contains(final BlockInstance b) {
		return contains(b.getLocation()) && contains(b.getLocation().add(1, 1, 1));
	}
	
	public Vec3 getDimensions() {
		return upperBound.clone().subtract(lowerBound);
	}
	
	public ServerLevel getWorld() {
		return world;
	}
	
	/**
	 * Returns an iterator which iterates over all blocks that are in this AABB
	 */
	@Override
	public Iterator<BlockInstance> iterator() {
		return new Iterator<BlockInstance>() {
			private final int minX = Math2.ceilI(lowerBound.getX() - Skript.EPSILON),
					minY = Math2.ceilI(lowerBound.getY() - Skript.EPSILON),
					minZ = Math2.ceilI(lowerBound.getZ() - Skript.EPSILON);
			private final int maxX = Math2.floorI(upperBound.getX() + Skript.EPSILON),
					maxY = Math2.floorI(upperBound.getY() + Skript.EPSILON),
					maxZ = Math2.floorI(upperBound.getZ() + Skript.EPSILON);
			
			private int x = minX - 1,// next() increases x by one immediately
					y = minY,
					z = minZ;
			
			@Override
			public boolean hasNext() {
				return y <= maxY && (x != maxX || y != maxY || z != maxZ);
			}
			
			@Override
			public BlockInstance next() {
				if (!hasNext())
					throw new NoSuchElementException();
				x++;
				if (x > maxX) {
					x = minX;
					z++;
					if (z > maxZ) {
						z = minZ;
						y++;
					}
				}
				if (y > maxY)
					throw new NoSuchElementException();
				return world.getBlockAt(x, y, z);
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lowerBound.hashCode();
		result = prime * result + upperBound.hashCode();
		result = prime * result + world.hashCode();
		return result;
	}
	
	@Override
	public boolean equals(final @Nullable Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AABB))
			return false;
		final AABB other = (AABB) obj;
		if (!lowerBound.equals(other.lowerBound))
			return false;
		if (!upperBound.equals(other.upperBound))
			return false;
		if (!world.equals(other.world))
			return false;
		return true;
	}
	
}
