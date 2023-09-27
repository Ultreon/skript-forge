///**
// *   This file is part of Skript.
// *
// *  Skript is free software: you can redistribute it and/or modify
// *  it under the terms of the GNU General Public License as published by
// *  the Free Software Foundation, either version 3 of the License, or
// *  (at your option) any later version.
// *
// *  Skript is distributed in the hope that it will be useful,
// *  but WITHOUT ANY WARRANTY; without even the implied warranty of
// *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// *  GNU General Public License for more details.
// *
// *  You should have received a copy of the GNU General Public License
// *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
// *
// * Copyright Peter Güttinger, SkriptLang team and contributors
// */
//package ch.njol.skript.expressions;
//
//import ch.njol.util.coll.CollectionUtils;
//import com.github.ultreon.portutils.BlockInstance;
//import net.minecraftforge.eventbus.api.Event;
//import org.bukkit.event.block.BlockPistonExtendEvent;
//import org.bukkit.event.block.BlockPistonRetractEvent;
//import org.eclipse.jdt.annotation.Nullable;
//
//import ch.njol.skript.Skript;
//import ch.njol.skript.doc.Description;
//import ch.njol.skript.doc.Examples;
//import ch.njol.skript.doc.Name;
//import ch.njol.skript.doc.Since;
//import ch.njol.skript.lang.Expression;
//import ch.njol.skript.lang.ExpressionType;
//import ch.njol.skript.lang.SkriptParser.ParseResult;
//import ch.njol.skript.lang.util.SimpleExpression;
//import ch.njol.skript.log.ErrorQuality;
//import ch.njol.util.Kleenean;
//
//@Name("Moved blocks")
//@Description("Blocks which are moved in a piston event. Cannot be used outside of piston events.")
//@Examples("the moved blocks")
//@Since("2.2-dev27")
//public class ExprPushedBlocks extends SimpleExpression<BlockInstance> {
//
//	static {
//		Skript.registerExpression(ExprPushedBlocks.class, BlockInstance.class, ExpressionType.SIMPLE, "[the] moved blocks");
//	}
//
//	@Override
//	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
//		if (!getParser().isCurrentEvent(BlockPistonExtendEvent.class, BlockPistonRetractEvent.class)) {
//			Skript.error("The moved blocks are only usable in piston extend and retract events", ErrorQuality.SEMANTIC_ERROR);
//			return false;
//		}
//
//		return true;
//	}
//
//	@Override
//	@Nullable
//	protected BlockInstance[] get(Event e) {
//		if (!CollectionUtils.isAnyInstanceOf(e, BlockPistonExtendEvent.class, BlockPistonRetractEvent.class))
//			return null;
//
//		return (e instanceof BlockPistonExtendEvent) ? ((BlockPistonExtendEvent) e).getBlocks().toArray(new BlockInstance[0])
//				: ((BlockPistonRetractEvent) e).getBlocks().toArray(new BlockInstance[0]);
//	}
//
//	@Override
//	public Class<? extends BlockInstance> getReturnType() {
//		return BlockInstance.class;
//	}
//
//	@Override
//	public boolean isSingle() {
//		return false;
//	}
//
//	@Override
//	public String toString(@Nullable Event e, boolean debug) {
//		return "moved blocks";
//	}
//
//}
