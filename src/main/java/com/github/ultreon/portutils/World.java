package com.github.ultreon.portutils;

import com.google.common.collect.Streams;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class World {
	private final ResourceLocation location;

	public World(Level level) {
		this.location = level.dimension().location();
	}

	public World(ResourceKey<Level> key) {
		this.location = key.location();
	}

	public World(ResourceLocation location) {
		this.location = location;
	}

	public World(String location) {
		this.location = new ResourceLocation(location);
	}

	public ResourceLocation getLocation() {
		return location;
	}

	public ResourceKey<Level> getKey() {
		return ResourceKey.create(Registry.DIMENSION_REGISTRY, location);
	}

	public ServerLevel getLevel() {
		return ServerLifecycleHooks.getCurrentServer().getLevel(getKey());
	}

	public String getName() {
		return location.toString();
	}

	public Iterable<Entity> getEntities() {
		return getLevel().getEntities().getAll();
	}

	public Entity getEntity(UUID uuid) {
		return getLevel().getEntity(uuid);
	}

	@SuppressWarnings("unchecked")
	public <E extends Entity> E spawn(Location loc, Class<E> type, Consumer<E> consumer) {
		EntityType<E> entityType = (EntityType<E>) ForgeRegistries.ENTITY_TYPES.getValues().stream().filter(aClass -> aClass.getBaseClass().equals(type)).findFirst().orElse(null);
		E o = null;
		if (entityType != null) {
			o = entityType.create(getLevel());
			consumer.accept(o);
			if (o != null) {
				getLevel().addFreshEntity(o);
			}
		}
		return o;
	}

	@SuppressWarnings("unchecked")
	public <E extends Entity> E spawn(Location loc, Class<? extends E> type) {
		EntityType<E> entityType = (EntityType<E>) ForgeRegistries.ENTITY_TYPES.getValues().stream().filter(aClass -> aClass.getBaseClass().equals(type)).findFirst().orElse(null);
		E o = null;
		if (entityType != null) {
			o = entityType.create(getLevel());
			if (o != null) {
				getLevel().addFreshEntity(o);
			}
		}
		return o;
	}

	@SuppressWarnings("unchecked")
	public <E extends Entity> List<E> getEntitiesByClass(Class<? extends E> type) {
		LevelEntityGetter<Entity> entities = getLevel().getEntities();
		return (List<E>) Streams.stream(entities.getAll()).filter(type::isInstance).toList();
	}
}
