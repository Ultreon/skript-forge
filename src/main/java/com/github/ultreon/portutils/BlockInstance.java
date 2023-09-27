package com.github.ultreon.portutils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class BlockInstance extends Location {
	private final Location location;

	public BlockInstance(Location location) {
		super(location.world, location.x, location.y, location.z, location.xRot, location.yRot);
		this.location = location;
	}

	public Location getLocation() {
		return location.copy();
	}

	public Block getType() {
		return getState().getBlock();
	}

	public BlockState getState() {
		return Objects.requireNonNull(location.getWorld()).getLevel().getBlockState(location.getPos());
	}

	public BlockInstance getRelative(Direction face) {
		BlockPos relative = location.getPos().relative(face);
		Location result = new Location(location.world, location.x, location.y, location.z, location.xRot, location.yRot);
		return new BlockInstance(location);
	}
}
