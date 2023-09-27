package com.github.ultreon.portutils;

import ch.njol.skript.util.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Location {
	double x;
	double y;
	double z;
	float xRot;
	float yRot;
	@Nullable World world;

	public Location(double x, double y, double z) {
		this((World) null, x, y, z);
	}

	public Location(@Nullable World world, double x, double y, double z) {
		this(world, x, y, z, 0.0F, 0.0F);
	}

	public Location(double x, double y, double z, float xRot, float yRot) {
		this((World) null, x, y, z, xRot, yRot);
	}

	public Location(@Nullable World world, double x, double y, double z, float xRot, float yRot) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.xRot = xRot;
		this.yRot = yRot;
		this.world = world;
	}

	public Location(Level level, Vec3 position) {
		this(level, position.x, position.y, position.z);
	}

	public Location(Level level, Vec3 position, float xRot, float yRot) {
		this(level, position.x, position.y, position.z, xRot, yRot);
	}

	public Location(Level level, double x, double y, double z) {
		this(new World(level), x, y, z);
	}

	public Location(Level level, double x, double y, double z, float xRot, float yRot) {
		this(new World(level), x, y, z, xRot, yRot);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getXRot() {
		return xRot;
	}

	public void setXRot(float xRot) {
		this.xRot = xRot;
	}

	public float getYRot() {
		return yRot;
	}

	public void setYRot(float yRot) {
		this.yRot = yRot;
	}

	@Nullable
	public World getWorld() {
		return world;
	}

	public void setWorld(@Nullable World world) {
		this.world = world;
	}

	public BlockPos getPos() {
		return new BlockPos(getVec());
	}

	private Vec3 getVec() {
		return new Vec3(x, y, z);
	}

	public Location copy() {
		return new Location(world, x, y, z, xRot, yRot);
	}

	public Location add(Direction direction) {
		return direction.getRelative(this);
	}

	public void setWorld(Level level) {
		this.world = new World(level);
	}

	public Location add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;

		return this;
	}

	public Location add(Vec3 direction) {
		this.x += direction.x;
		this.y += direction.y;
		this.z += direction.z;

		return this;
	}
}
