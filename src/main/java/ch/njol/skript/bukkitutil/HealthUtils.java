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

import ch.njol.util.Math2;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class HealthUtils {

	/**
	 * Get the health of an entity
	 * @param e Entity to get health from
	 * @return The amount of hearts the entity has left
	 */
	public static double getHealth(LivingEntity e) {
		if (e.isDeadOrDying())
			return 0;
		return e.getHealth() / 2;
	}
	
	/**
	 * Set the health of an entity
	 * @param e Entity to set health for
	 * @param health The amount of hearts to set
	 */
	public static void setHealth(LivingEntity e, double health) {
		e.setHealth((float) (Math2.fit(0, health, getMaxHealth(e)) * 2));
	}
	
	/**
	 * Get the max health an entity has
	 * @param e Entity to get max health from
	 * @return How many hearts the entity can have at most
	 */
	public static double getMaxHealth(LivingEntity e) {
		AttributeInstance attributeInstance = (e).getAttribute(Attributes.MAX_HEALTH);
		assert attributeInstance != null;
		return attributeInstance.getValue() / 2;
	}
	
	/**
	 * Set the max health an entity can have
	 * @param e Entity to set max health for
	 * @param health How many hearts the entity can have at most
	 */
	public static void setMaxHealth(LivingEntity e, double health) {
		AttributeInstance attributeInstance = e.getAttribute(Attributes.MAX_HEALTH);
		assert attributeInstance != null;
		attributeInstance.setBaseValue(health * 2);
	}
	
	/**
	 * Apply damage to an entity
	 * @param e Entity to apply damage to
	 * @param d Amount of hearts to damage
	 */
	public static void damage(Entity e, double d) {
		if (d < 0) {
			heal(e, -d);
			return;
		}
		e.hurt(DamageSource.GENERIC, (float) (d * 2));
	}

	/**
	 * Heal an entity
	 * @param e Entity to heal
	 * @param h Amount of hearts to heal
	 */
	public static void heal(Entity e, double h) {
		if (h < 0) {
			damage(e, -h);
			return;
		}
		if (e instanceof LivingEntity livingEntity) {
			setHealth(livingEntity, getHealth(livingEntity) + h);
		}
	}
	
	public static double getDamage(LivingDamageEvent e) {
		return e.getAmount() / 2;
	}
	
	public static double getFinalDamage(LivingDamageEvent e) {
		return (e.getEntity().getHealth() - e.getAmount()) / 2;
	}
	
	public static void setDamage(LivingDamageEvent e, double damage) {
		e.setAmount((float) (damage * 2));
	}
	
	public static void setDamageCause(LivingEntity e, DamageSource cause) {
		e.getCombatTracker().recordDamage(cause, e.getHealth(), 0);
	}
	
}
