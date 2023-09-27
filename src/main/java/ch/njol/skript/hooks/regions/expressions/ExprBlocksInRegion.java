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
package ch.njol.skript.hooks.regions.expressions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.github.ultreon.portutils.BlockInstance;
import net.minecraftforge.eventbus.api.Event;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.hooks.regions.classes.Region;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.iterator.ArrayIterator;
import ch.njol.util.coll.iterator.EmptyIterator;

/**
 * @author Peter Güttinger
 */
@Name("Blocks in Region")
@Description({"All blocks in a <a href='../classes.html#region'>region</a>.",
		"This expression requires a supported regions plugin to be installed."})
@Examples({"loop all blocks in the region {arena.%{faction.%player%}%}:",
		"	clear the loop-block"})
@Since("2.1")
public class ExprBlocksInRegion extends SimpleExpression<BlockInstance> {
	static {
		Skript.registerExpression(ExprBlocksInRegion.class, BlockInstance.class, ExpressionType.COMBINED,
				"[(all|the)] blocks (in|of) [[the] region[s]] %regions%");
	}
	
	@SuppressWarnings("null")
	private Expression<Region> regions;
	
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		regions = (Expression<Region>) exprs[0];
		return true;
	}
	
	@SuppressWarnings("null")
	@Override
	protected BlockInstance[] get(final Event e) {
		final Iterator<BlockInstance> iter = iterator(e);
		final ArrayList<BlockInstance> r = new ArrayList<>();
		while (iter.hasNext())
			r.add(iter.next());
		return r.toArray(new BlockInstance[r.size()]);
	}
	
	@Override
	@NonNull
	public Iterator<BlockInstance> iterator(final Event e) {
		final Region[] rs = regions.getArray(e);
		if (rs.length == 0)
			return EmptyIterator.get();
		return new Iterator<BlockInstance>() {
			private Iterator<BlockInstance> current = rs[0].getBlocks();
			private final Iterator<Region> iter = new ArrayIterator<>(rs, 1);
			
			@Override
			public boolean hasNext() {
				while (!current.hasNext() && iter.hasNext()) {
					final Region r = iter.next();
					if (r != null)
						current = r.getBlocks();
				}
				return current.hasNext();
			}
			
			@SuppressWarnings("null")
			@Override
			public BlockInstance next() {
				if (!hasNext())
					throw new NoSuchElementException();
				return current.next();
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	@Override
	public boolean isSingle() {
		return false;
	}
	
	@Override
	public Class<? extends BlockInstance> getReturnType() {
		return BlockInstance.class;
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "all blocks in " + regions.toString(e, debug);
	}
	
}
