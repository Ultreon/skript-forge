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
////
//import org.bukkit.Location;
//import com.github.ultreon.portutils.BlockInstance;
//import net.minecraftforge.eventbus.api.Event;
//import org.eclipse.jdt.annotation.Nullable;
////
//import ch.njol.skript.Skript;
//import ch.njol.skript.classes.Converter;
//import ch.njol.skript.classes.Converter.ConverterInfo;
//import ch.njol.skript.doc.Description;
//import ch.njol.skript.doc.Examples;
//import ch.njol.skript.doc.Name;
//import ch.njol.skript.doc.Since;
//import ch.njol.skript.expressions.base.EventValueExpression;
//import ch.njol.skript.expressions.base.WrapperExpression;
//import ch.njol.skript.lang.Expression;
//import ch.njol.skript.lang.ExpressionType;
//import ch.njol.skript.lang.SkriptParser.ParseResult;
//import ch.njol.skript.lang.util.ConvertedExpression;
//import ch.njol.skript.util.Direction;
//import ch.njol.util.Kleenean;
////
///**
// * @author Peter Güttinger
// */
//@Name("Block")
//@Description({"The block involved in the event, e.g. the clicked block or the placed block.",
//		"Can optionally include a direction as well, e.g. 'block above' or 'block in front of the player'."})
//@Examples({"block is ore",
//		"set block below to air",
//		"spawn a creeper above the block",
//		"loop blocks in radius 4:",
//		"	loop-block is obsidian",
//		"	set loop-block to water",
//		"block is a chest:",
//		"	clear the inventory of the block"})
//@Since("1.0")
//public class ExprBlock extends WrapperExpression<BlockInstance> {
//	static {
//		Skript.registerExpression(ExprBlock.class, BlockInstance.class, ExpressionType.SIMPLE, "[the] [event-]block");
//		Skript.registerExpression(ExprBlock.class, BlockInstance.class, ExpressionType.COMBINED, "[the] block %direction% [%location%]");
//	}
////
//	@SuppressWarnings({"unchecked", "null"})
//	@Override
//	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
//		if (exprs.length > 0) {
//			setExpr(new ConvertedExpression<>(Direction.combine((Expression<? extends Direction>) exprs[0],
//					(Expression<? extends Location>) exprs[1]), BlockInstance.class,
//					new ConverterInfo<>(Location.class, BlockInstance.class, new Converter<Location, BlockInstance>() {
//				@Override
//				public BlockInstance convert(final Location l) {
//					return l.getBlock();
//				}
//			}, 0)));
//			return true;
//		} else {
//			setExpr(new EventValueExpression<>(BlockInstance.class));
//			return ((EventValueExpression<BlockInstance>) getExpr()).init();
//		}
//	}
////
//	@Override
//	public String toString(final @Nullable Event e, final boolean debug) {
//		return getExpr() instanceof EventValueExpression ? "the block" : "the block " + getExpr().toString(e, debug);
//	}
////
//}
