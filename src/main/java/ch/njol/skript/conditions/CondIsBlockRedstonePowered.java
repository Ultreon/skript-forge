// /**
//  *   This file is part of Skript.
//  *
//  *  Skript is free software: you can redistribute it and/or modify
//  *  it under the terms of the GNU General Public License as published by
//  *  the Free Software Foundation, either version 3 of the License, or
//  *  (at your option) any later version.
//  *
//  *  Skript is distributed in the hope that it will be useful,
//  *  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  *  GNU General Public License for more details.
//  *
//  *  You should have received a copy of the GNU General Public License
//  *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
//  *
//  * Copyright Peter Güttinger, SkriptLang team and contributors
//  */
// package ch.njol.skript.conditions;
// 
// import com.github.ultreon.portutils.BlockInstance;
// import net.minecraftforge.eventbus.api.Event;
// import org.eclipse.jdt.annotation.Nullable;
// 
// import ch.njol.skript.Skript;
// import ch.njol.skript.conditions.base.PropertyCondition;
// import ch.njol.skript.doc.Description;
// import ch.njol.skript.doc.Examples;
// import ch.njol.skript.doc.Name;
// import ch.njol.skript.doc.Since;
// import ch.njol.skript.lang.Condition;
// import ch.njol.skript.lang.Expression;
// import ch.njol.skript.lang.SkriptParser.ParseResult;
// import ch.njol.util.Kleenean;
// 
// @Name("Is Block Redstone Powered")
// @Description("Checks if a block is indirectly or directly powered by redstone")
// @Examples({"if clicked block is redstone powered:",
// 	"\tsend \"This block is well-powered by redstone!\"",
// 	"if clicked block is indirectly redstone powered:",
// 	"\tsend \"This block is indirectly redstone powered.\""})
// @Since("2.5")
// public class CondIsBlockRedstonePowered extends Condition {
// 	
// 	static {
// 		Skript.registerCondition(CondIsBlockRedstonePowered.class,
// 			"%blocks% (is|are) redstone powered",
// 			"%blocks% (is|are) indirectly redstone powered",
// 			"%blocks% (is|are)(n't| not) redstone powered",
// 			"%blocks% (is|are)(n't| not) indirectly redstone powered");
// 	}
// 	
// 	@SuppressWarnings("null")
// 	private Expression<BlockInstance> blocks;
// 	private boolean isIndirectlyPowered;
// 	
// 	@SuppressWarnings({"unchecked", "null"})
// 	@Override
// 	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
// 		blocks = (Expression<BlockInstance>) exprs[0];
// 		isIndirectlyPowered = matchedPattern % 2 == 1;
// 		setNegated(matchedPattern > 1);
// 		return true;
// 	}
// 	
// 	@Override
// 	public boolean check(Event e) {
// 		return isIndirectlyPowered
// 			? blocks.check(e, BlockInstance::isBlockIndirectlyPowered, isNegated())
// 			: blocks.check(e, BlockInstance::isBlockPowered, isNegated());
// 	}
// 	
// 	@Override
// 	public String toString(@Nullable Event e, boolean debug) {
// 		return PropertyCondition.toString(this, PropertyCondition.PropertyType.BE, e, debug, blocks, (isIndirectlyPowered ? "indirectly " : "") + "powered");
// 	}
// 	
// }
