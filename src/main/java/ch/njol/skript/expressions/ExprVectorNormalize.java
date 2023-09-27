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
//import net.minecraftforge.eventbus.api.Event;
//import org.bukkit.util.Vec3;
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
//import ch.njol.util.Kleenean;
//import ch.njol.util.coll.CollectionUtils;
//
///**
// * @author bi0qaw
// */
//@Name("Vectors - Normalized")
//@Description("Returns the same vector but with length 1.")
//@Examples({"set {_v} to normalized {_v}"})
//@Since("2.2-dev28")
//public class ExprVectorNormalize extends SimpleExpression<Vec3> {
//
//	static {
//		Skript.registerExpression(ExprVectorNormalize.class, Vec3.class, ExpressionType.SIMPLE,
//				"normalize[d] %vector%",
//				"%vector% normalized");
//	}
//
//	@SuppressWarnings("null")
//	private Expression<Vec3> vector;
//
//	@Override
//	@SuppressWarnings({"unchecked", "null"})
//	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
//		vector = (Expression<Vec3>) exprs[0];
//		return true;
//	}
//
//	@Override
//	@SuppressWarnings("null")
//	protected Vec3[] get(Event e) {
//		Vec3 v = vector.getSingle(e);
//		if (v == null)
//			return null;
//		return CollectionUtils.array(v.clone().normalize());
//	}
//
//	@Override
//	public boolean isSingle() {
//		return true;
//	}
//
//	@Override
//	public Class<? extends Vec3> getReturnType() {
//		return Vec3.class;
//	}
//
//	@Override
//	public String toString(@Nullable Event e, boolean debug) {
//		return "normalized " + vector.toString(e, debug);
//	}
//
//}
