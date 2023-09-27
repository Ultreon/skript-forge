package com.github.ultreon.portutils;

import ch.njol.skript.Skript;
import com.github.ultreon.portutils.utils.Worlds;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Forge {
	public static List<World> getWorlds() {
		return Streams.stream(ServerLifecycleHooks.getCurrentServer().getAllLevels()).map(World::new).toList();
	}

	public static List<ServerLevel> getLevels() {
		return Lists.newArrayList(ServerLifecycleHooks.getCurrentServer().getAllLevels());
	}

	public static void scheduleSyncDelayedTask(Skript skript, Runnable runnable) {
		ServerLifecycleHooks.getCurrentServer().doRunTask(new TickTask(0, runnable));
	}

	public static MinecraftServer getServer() {
		return ServerLifecycleHooks.getCurrentServer();
	}

	public static void stopServer() {
		MinecraftServer server = getServer();
		if (server instanceof DedicatedServer dedicatedServer) {
			dedicatedServer.stopServer();
		} else {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> Worlds::saveLevelThenOpenTitle);
		}
	}

	public static World getWorld(String s) {
		if (Objects.equals(s, "world")) {
			return new World(ServerLevel.OVERWORLD);
		}
		if (Objects.equals(s, "world_nether")) {
			return new World(ServerLevel.NETHER);
		}
		if (Objects.equals(s, "world_the_end")) {
			return new World(ServerLevel.END);
		}
		return new World(s);
	}

	public static ServerLevel getLevel(String s) {
		if (Objects.equals(s, "world")) {
			return new World(ServerLevel.OVERWORLD).getLevel();
		}
		if (Objects.equals(s, "world_nether")) {
			return new World(ServerLevel.NETHER).getLevel();
		}
		if (Objects.equals(s, "world_the_end")) {
			return new World(ServerLevel.END).getLevel();
		}
		return new World(s).getLevel();
	}

	public static boolean hasLore(ItemStack stack) {
		return getLoreTag(stack) != null;
	}

	public static List<String> getLore(ItemStack stack) {
		ListTag loreTag = getLoreTag(stack);
		if (loreTag != null) {
			List<String> lines = new ArrayList<>();
			for (Tag tag : loreTag) {
				if (tag instanceof StringTag stringTag) {
					lines.add(stringTag.getAsString());
				}
			}
			return lines;
		}
		return null;
	}

	@Nullable
	public static ListTag getLoreTag(ItemStack stack) {
		@NotNull
		CompoundTag tag;
		if (stack.hasTag()) {
			tag = Objects.requireNonNull(stack.getTag());
		} else {
			return null;
		}

		CompoundTag displayTag;
		if (tag.contains("display", 10)) {
			displayTag = tag.getCompound("display");
		} else {
			return null;
		}

		if (displayTag.contains("Lore", 9)) {
			return displayTag.getList("Lore", 8);
		} else {
			return null;
		}
	}

	public static Entity getEntity(UUID uuid) {
		return getWorlds().stream().map(world -> world.getEntity(uuid)).filter(Objects::nonNull).findFirst().orElse(null);
	}

	public static String getVersion() {
		ModContainer modContainer = ModList.get().getModContainerByObject(ForgeMod.getInstance()).orElseThrow();
		return MavenVersionStringHelper.artifactVersionToString(modContainer.getModInfo().getVersion());
	}

	public static void broadcast(String message, String permission) {
		MinecraftServer server = getServer();
		if (server != null) {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				player.sendSystemMessage(Component.nullToEmpty(message), false);
			}
		}
	}

	public static void runTaskLater(Skript skript, Runnable task, int tick) {
		MinecraftServer server = getServer();
		if (server != null) {
			server.doRunTask(new TickTask(tick, task));
		}
	}

	public static Item getItemFromMinecraftId(String id) {
		return ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
	}

	public static Block getBlockFromMinecraftId(String id) {
		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id));
	}

	public static List<ServerPlayer> getOnlinePlayers() {
		MinecraftServer server = getServer();
		if (server != null) {
			return server.getPlayerList().getPlayers();
		}

		return new ArrayList<>();
	}

	public static ServerPlayer getPlayer(GameProfile p) {
		return getPlayer(p.getId());
	}

	public static ServerPlayer getPlayer(UUID id) {
		MinecraftServer server = getServer();
		if (server != null) {
			server.getPlayerList().getPlayer(id);
		}
		return null;
	}

	public static Location getEntityLocation(Entity e) {
		World level = new World(e.level);
		return new Location(level, e.getX(), e.getY(), e.getZ(), e.getXRot(), e.getYRot());
	}

//	public static ServerLevel getLevel(String name) {
//		MinecraftServer server = getServer();
//		ResourceKey<Level> levelResourceKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(name));
//		return server.getLevel(levelResourceKey);
//	}
}
