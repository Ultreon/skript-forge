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
package ch.njol.skript.bukkitutil;

import ch.njol.skript.Skript;
import ch.njol.skript.entity.EntityData;
import com.github.ultreon.portutils.Location;
import com.github.ultreon.portutils.World;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.registries.ForgeRegistries;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Function;

/**
 * Utility class for quick {@link Entity} methods
 */
public class EntityUtils {
	
	private static final boolean HAS_PIGLINS = Skript.classExists("org.bukkit.entity.Piglin");

	/**
	 * Cache Skript EntityData -> Bukkit EntityType
	 */
	private static final BiMap<EntityData<?>, EntityType<?>> SPAWNER_TYPES = HashBiMap.create();

	static {
		for (EntityType<?> e : ForgeRegistries.ENTITY_TYPES.getValues()) {
			Class<? extends Entity> c = e.getBaseClass();
			SPAWNER_TYPES.put(EntityData.fromClass(c), e);
		}
	}
	
	/**
	 * Check if an entity is ageable.
	 * Some entities, such as zombies, do not have an age but can be a baby/adult.
	 *
	 * @param entity Entity to check
	 * @return True if entity is ageable
	 */
	public static boolean isAgeable(Entity entity) {
		if (entity instanceof AgeableMob || entity instanceof Zombie)
			return true;
		return HAS_PIGLINS && (entity instanceof Piglin || entity instanceof Zoglin);
	}
	
	/**
	 * Get the age of an ageable entity.
	 * Entities such as zombies do not have an age, this will return -1 if baby, 0 if adult.
	 *
	 * @param entity Entity to grab age for
	 * @return Age of entity (if zombie/piglin/zoglin -1 = baby, 0 = adult) (if not ageable, will return 0)
	 */
	public static int getAge(Entity entity) {
		if (entity instanceof AgeableMob)
			return ((AgeableMob) entity).getAge();
		else if (entity instanceof Zombie)
			return ((Zombie) entity).isBaby() ? -1 : 0;
		else if (HAS_PIGLINS) {
			if (entity instanceof Piglin)
				return ((Piglin) entity).isBaby() ? -1 : 0;
			else if (entity instanceof Zoglin)
				return ((Zoglin) entity).isBaby() ? -1 : 0;
		}
		return 0;
	}
	
	/**
	 * Set the age of an entity.
	 * Entities such as zombies do not have an age, setting below 0 will make them a baby otherwise adult.
	 *
	 * @param entity Entity to set age for
	 * @param age    Age to set
	 */
	public static void setAge(Entity entity, int age) {
		if (entity instanceof AgeableMob)
			((AgeableMob) entity).setAge(age);
		else if (entity instanceof Zombie)
			((Zombie) entity).setBaby(age < 0);
		else if (HAS_PIGLINS) {
			if (entity instanceof Piglin)
				((Piglin) entity).setBaby(age < 0);
			else if (entity instanceof Zoglin)
				((Zoglin) entity).setBaby(age < 0);
		}
	}
	
	/**
	 * Quick method for making an entity a baby.
	 * Ageable entities (such as sheep or pigs) will set their default baby age to -24000.
	 *
	 * @param entity Entity to make baby
	 */
	public static void setBaby(Entity entity) {
		setAge(entity, -24000);
	}
	
	/**
	 * Quick method for making an entity an adult.
	 *
	 * @param entity Entity to make adult
	 */
	public static void setAdult(Entity entity) {
		setAge(entity, 0);
	}
	
	/**
	 * Quick method to check if entity is an adult.
	 *
	 * @param entity Entity to check
	 * @return True if entity is an adult
	 */
	public static boolean isAdult(Entity entity) {
		return getAge(entity) >= 0;
	}

	/**
	 * Convert from Skript's EntityData to Bukkit's EntityType
	 * @param e Skript's EntityData
	 * @return Bukkit's EntityType
	 */
	public static EntityType<?> toBukkitEntityType(EntityData<?> e) {
		return SPAWNER_TYPES.get(EntityData.fromClass(e.getType())); // Fix Comparison Issues
	}

	/**
	 * Convert from Bukkit's EntityType to Skript's EntityData
	 * @param e Bukkit's EntityType
	 * @return Skript's EntityData
	 */
	public static EntityData<?> toSkriptEntityData(EntityType<?> e) {
		return SPAWNER_TYPES.inverse().get(e);
	}

	/**
	 * Teleports the given entity to the given location.
	 * Teleports to the given location in the entity's world if the location's world is null.
	 */
	public static void teleport(Entity entity, Location location) {
		@Nullable World world = location.getWorld();
		if (world == null) {
			location = location.copy();
			location.setWorld(entity.level);
		}

		assert world != null;
		if (entity.level != world.getLevel()) {
			Location finalLocation = location;
			entity.changeDimension(world.getLevel(), new ITeleporter() {
				@Override
				public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
					return false;
				}

				@Override
				public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
					entity.teleportToWithTicket(finalLocation.getX(), finalLocation.getY(), finalLocation.getZ());
					entity.setXRot(finalLocation.getXRot());
					entity.setXRot(finalLocation.getYRot());
					return entity;
				}
			});
		}
	}

}
