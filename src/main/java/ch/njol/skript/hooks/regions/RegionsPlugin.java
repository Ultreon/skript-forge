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
package ch.njol.skript.hooks.regions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Location;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.plugin.Plugin;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.hooks.Hook;
import ch.njol.skript.hooks.regions.classes.Region;
import ch.njol.skript.variables.Variables;
import ch.njol.yggdrasil.ClassResolver;

/**
 * @author Peter Güttinger
 */
// REMIND support more plugins?
public abstract class RegionsPlugin<P extends Plugin> extends Hook<P> {
	
	public RegionsPlugin() throws IOException {}
	
	public static Collection<RegionsPlugin<?>> plugins = new ArrayList<>(2);
	
	static {
		Variables.yggdrasil.registerClassResolver(new ClassResolver() {
			@Override
			@Nullable
			public String getID(final Class<?> c) {
				for (final RegionsPlugin<?> p : plugins)
					if (p.getRegionClass() == c)
						return c.getClass().getSimpleName();
				return null;
			}
			
			@Override
			@Nullable
			public Class<?> getClass(final String id) {
				for (final RegionsPlugin<?> p : plugins)
					if (id.equals(p.getRegionClass().getSimpleName()))
						return p.getRegionClass();
				return null;
			}
		});
	}
	
	@Override
	protected boolean init() {
		plugins.add(this);
		return true;
	}
	
	public abstract boolean canBuild_i(ServerPlayer p, Location l);
	
	public static boolean canBuild(final ServerPlayer p, final Location l) {
		for (final RegionsPlugin<?> pl : plugins) {
			if (!pl.canBuild_i(p, l))
				return false;
		}
		return true;
	}
	
	public abstract Collection<? extends Region> getRegionsAt_i(Location l);
	
	public static Set<? extends Region> getRegionsAt(final Location l) {
		final Set<Region> r = new HashSet<>();
		Iterator<RegionsPlugin<?>> it = plugins.iterator();
		while (it.hasNext()) {
			RegionsPlugin<?> pl = it.next();
			try {
				r.addAll(pl.getRegionsAt_i(l));
			} catch (Throwable e) { // Unstable WorldGuard API
				Skript.error(pl.getName() + " hook crashed and was removed to prevent future errors.");
				e.printStackTrace();
				it.remove();
			}
		}
		return r;
	}
	
	@Nullable
	public abstract Region getRegion_i(ServerLevel world, String name);
	
	@Nullable
	public static Region getRegion(final ServerLevel world, final String name) {
		for (final RegionsPlugin<?> pl : plugins) {
			return pl.getRegion_i(world, name);
		}
		return null;
	}
	
	public abstract boolean hasMultipleOwners_i();
	
	public static boolean hasMultipleOwners() {
		for (final RegionsPlugin<?> pl : plugins) {
			if (pl.hasMultipleOwners_i())
				return true;
		}
		return false;
	}
	
	protected abstract Class<? extends Region> getRegionClass();
	
	@Nullable
	public static RegionsPlugin<?> getPlugin(final String name) {
		for (final RegionsPlugin<?> pl : plugins) {
			if (pl.getName().equalsIgnoreCase(name))
				return pl;
		}
		return null;
	}
	
}
