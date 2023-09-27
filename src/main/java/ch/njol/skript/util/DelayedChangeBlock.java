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

import ch.njol.skript.Skript;
import ch.njol.skript.bukkitutil.block.BlockCompat;
import com.destroystokyo.paper.block.BlockSoundGroup;
import net.minecraft.world.level.block.Block;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import com.github.ultreon.portutils.Material;
import org.bukkit.SoundGroup;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.block.Biome;
import com.github.ultreon.portutils.BlockInstance;
import org.bukkit.block.BlockFace;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vec3;
import org.bukkit.util.VoxelShape;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * A block that gets all data from the world, but either delays
 * any changes by 1 tick of reflects them on a given BlockState
 * depending on which constructor is used.
 */
public class DelayedChangeBlock implements BlockInstance {

	private static final boolean ISPASSABLE_METHOD_EXISTS = Skript.methodExists(BlockInstance.class, "isPassable");

	final BlockInstance block;
	@Nullable
	private final BlockState newState;
	private final boolean isPassable;

	public DelayedChangeBlock(BlockInstance block) {
		this(block, null);
	}

	public DelayedChangeBlock(BlockInstance block, @Nullable BlockState newState) {
		assert block != null;
		this.block = block;
		this.newState = newState;
		if (ISPASSABLE_METHOD_EXISTS && newState != null)
			this.isPassable = newState.getBlock().isPassable();
		else
			this.isPassable = false;
	}

	@Override
	public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
		block.setMetadata(metadataKey, newMetadataValue);
	}

	@Override
	public List<MetadataValue> getMetadata(String metadataKey) {
		return block.getMetadata(metadataKey);
	}

	@Override
	public boolean hasMetadata(String metadataKey) {
		return block.hasMetadata(metadataKey);
	}

	@Override
	public void removeMetadata(String metadataKey, Plugin owningPlugin) {
		block.removeMetadata(metadataKey, owningPlugin);
	}

	@SuppressWarnings("deprecation")
	@Override
	public byte getData() {
		return block.getData();
	}

	@Override
	public BlockInstance getRelative(int modX, int modY, int modZ) {
		return block.getRelative(modX, modY, modZ);
	}

	@Override
	public BlockInstance getRelative(BlockFace face) {
		return block.getRelative(face);
	}

	@Override
	public BlockInstance getRelative(BlockFace face, int distance) {
		return block.getRelative(face, distance);
	}

	@Override
	public Block getType() {
		return block.getType();
	}

	@Override
	public byte getLightLevel() {
		return block.getLightLevel();
	}

	@Override
	public byte getLightFromSky() {
		return block.getLightFromSky();
	}

	@Override
	public byte getLightFromBlocks() {
		return block.getLightFromBlocks();
	}

	@Override
	public ServerLevel getWorld() {
		return block.getWorld();
	}

	@Override
	public int getX() {
		return block.getX();
	}

	@Override
	public int getY() {
		return block.getY();
	}

	@Override
	public int getZ() {
		return block.getZ();
	}

	@Override
	public Location getLocation() {
		return block.getLocation();
	}

	@Override
	public Chunk getChunk() {
		return block.getChunk();
	}

	@Override
	public void setType(Material type) {
		if (newState != null) {
			newState.setType(type);
		} else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Skript.getInstance(), new Runnable() {
				@Override
				public void run() {
					block.setType(type);
				}
			});
		}
	}

	@Nullable
	@Override
	public BlockFace getFace(BlockInstance block) {
		return block.getFace(block);
	}

	@Override
	public BlockState getState() {
		return block.getState();
	}

	@Override
	public BlockState getState(boolean useSnapshot) {
		return block.getState(useSnapshot);
	}

	@Override
	public Biome getBiome() {
		return block.getBiome();
	}

	@Override
	public @NotNull Biome getComputedBiome() {
		return block.getComputedBiome();
	}

	@Override
	public void setBiome(Biome bio) {
		block.setBiome(bio);
	}

	@Override
	public boolean isBlockPowered() {
		return block.isBlockPowered();
	}

	@Override
	public boolean isBlockIndirectlyPowered() {
		return block.isBlockIndirectlyPowered();
	}

	@Override
	public boolean isBlockFacePowered(BlockFace face) {
		return block.isBlockFacePowered(face);
	}

	@Override
	public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
		return block.isBlockFaceIndirectlyPowered(face);
	}

	@Override
	public int getBlockPower(BlockFace face) {
		return block.getBlockPower(face);
	}

	@Override
	public int getBlockPower() {
		return block.getBlockPower();
	}

	@Override
	public boolean isEmpty() {
		Material type = getType();
		assert type != null;
		return BlockCompat.INSTANCE.isEmpty(type);
	}

	@Override
	public boolean isLiquid() {
		Material type = getType();
		assert type != null;
		return BlockCompat.INSTANCE.isLiquid(type);
	}

	@Override
	public boolean isBuildable() {
		return block.isBuildable();
	}

	@Override
	public boolean isBurnable() {
		return block.isBurnable();
	}

	@Override
	public boolean isReplaceable() {
		return block.isReplaceable();
	}

	@Override
	public boolean isSolid() {
		return block.isSolid();
	}

	@Override
	public boolean isCollidable() {
		return block.isCollidable();
	}

	@Override
	public double getTemperature() {
		return block.getTemperature();
	}

	@Override
	public double getHumidity() {
		return block.getHumidity();
	}

	@Override
	public PistonMoveReaction getPistonMoveReaction() {
		return block.getPistonMoveReaction();
	}

	@Override
	public boolean breakNaturally() {
		if (newState != null) {
			return false;
		} else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Skript.getInstance(), new Runnable() {
				@Override
				public void run() {
					block.breakNaturally();
				}
			});
			return true;
		}
	}

	@Override
	public boolean breakNaturally(@Nullable ItemStack tool) {
		if (newState != null) {
			return false;
		} else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Skript.getInstance(), new Runnable() {
				@Override
				public void run() {
					block.breakNaturally(tool);
				}
			});
			return true;
		}
	}

	@Override
	public boolean breakNaturally(boolean triggerEffect) {
		if (newState != null) {
			return false;
		} else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Skript.getInstance(), new Runnable() {
				@Override
				public void run() {
					block.breakNaturally(triggerEffect);
				}
			});
			return true;
		}
	}

	@Override
	public boolean breakNaturally(ItemStack tool, boolean triggerEffect) {
		if (newState != null) {
			return false;
		} else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Skript.getInstance(), new Runnable() {
				@Override
				public void run() {
					block.breakNaturally(tool, triggerEffect);
				}
			});
			return true;
		}
	}

	@Override
	public void tick() {
		block.tick();
	}

	@Override
	public void randomTick() {
		block.randomTick();
	}

	@Override
	public boolean applyBoneMeal(BlockFace blockFace) {
		return block.applyBoneMeal(blockFace);
	}

	@Override
	public Collection<ItemStack> getDrops() {
		return block.getDrops();
	}

	@Override
	public Collection<ItemStack> getDrops(@Nullable ItemStack tool) {
		return block.getDrops(tool);
	}

	@Override
	public Collection<ItemStack> getDrops(ItemStack tool, @Nullable Entity entity) {
		return block.getDrops(tool, entity);
	}

	@Nullable
	@Override
	public Location getLocation(@Nullable Location loc) {
		if (loc != null) {
			loc.setWorld(getWorld());
			loc.setX(getX());
			loc.setY(getY());
			loc.setZ(getZ());
			loc.setPitch(0);
			loc.setYaw(0);
		}
		return loc;
	}

	@Override
	public void setType(Material type, boolean applyPhysics) {
		if (newState != null) {
			newState.setType(type);
		} else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Skript.getInstance(), new Runnable() {
				@Override
				public void run() {
					block.setType(type, applyPhysics);
				}
			});
		}
	}

	@Override
	public BlockData getBlockData() {
		return block.getBlockData();
	}

	@Override
	public void setBlockData(BlockData data) {
		setBlockData(data, true);
	}

	@Override
	public void setBlockData(BlockData data, boolean applyPhysics) {
		if (newState != null) {
			newState.setBlockData(data);
		} else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Skript.getInstance(), () -> block.setBlockData(data, applyPhysics));
		}
	}

	@Nullable
	@Override
	public RayTraceResult rayTrace(Location start, Vec3 direction, double maxDistance, FluidCollisionMode fluidCollisionMode) {
		return block.rayTrace(start, direction, maxDistance, fluidCollisionMode);
	}

	@Override
	public boolean isPassable() {
		return isPassable;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return block.getBoundingBox();
	}

	@Override
	public BlockSoundGroup getSoundGroup() {
		return block.getSoundGroup();
	}

	@Override
	public @NotNull SoundGroup getBlockSoundGroup() {
		return block.getBlockSoundGroup();
	}

	@Override
	public String getTranslationKey() {
		return block.getTranslationKey();
	}

	@Override
	public float getDestroySpeed(ItemStack itemStack) {
		return block.getDestroySpeed(itemStack);
	}

	@Override
	public boolean isPreferredTool(@NotNull ItemStack tool) {
		return block.isPreferredTool(tool);
	}

	@Override
	public boolean isValidTool(@NotNull ItemStack itemStack) {
		return block.isValidTool(itemStack);
	}

	@Override
	public float getDestroySpeed(@NotNull ItemStack itemStack, boolean considerEnchants) {
		return block.getDestroySpeed(itemStack, considerEnchants);
	}

	@Override
	@NotNull
	public VoxelShape getCollisionShape() {
		return block.getCollisionShape();
	}

	@Override
	public boolean canPlace(@NotNull BlockData data) {
		return block.canPlace(data);
	}

	@Override
	public float getBreakSpeed(@NotNull ServerPlayer player) {
		return block.getBreakSpeed(player);
	}

	@Override
	public @NotNull String translationKey() {
		return block.getTranslationKey();
	}

	@Override
	public boolean breakNaturally(boolean triggerEffect, boolean dropExperience) {
		return block.breakNaturally(triggerEffect, dropExperience);
	}

	@Override
	public boolean breakNaturally(@NotNull ItemStack tool, boolean triggerEffect, boolean dropExperience) {
		return block.breakNaturally(tool, triggerEffect, dropExperience);
	}

}
