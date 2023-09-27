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
//import net.minecraftforge.eventbus.api.Event;
//import org.bukkit.util.Vec3;
//import org.eclipse.jdt.annotation.Nullable;
////
//import ch.njol.skript.Skript;
//import ch.njol.skript.doc.Description;
//import ch.njol.skript.doc.Examples;
//import ch.njol.skript.doc.Name;
//import ch.njol.skript.doc.Since;
//import ch.njol.skript.lang.Expression;
//import ch.njol.skript.lang.ExpressionType;
//import ch.njol.skript.lang.SkriptParser.ParseResult;
//import ch.njol.skript.lang.util.SimpleExpression;
//import ch.njol.skript.util.Patterns;
//import ch.njol.util.Kleenean;
//import ch.njol.util.coll.CollectionUtils;
////
///**
// * @author bi0qaw
// */
//@Name("Vectors - Arithmetic")
//@Description("Arithmetic expressions for vectors.")
//@Examples({"set {_v} to vector 1, 2, 3 // 5",
//		"set {_v} to {_v} ++ {_v}",
//		"set {_v} to {_v} ++ 5",
//		"set {_v} to {_v} -- {_v}",
//		"set {_v} to {_v} -- 5",
//		"set {_v} to {_v} ** {_v}",
//		"set {_v} to {_v} ** 5",
//		"set {_v} to {_v} // {_v}",
//		"set {_v} to {_v} // 5"})
//@Since("2.2-dev28")
//public class ExprVectorArithmetic extends SimpleExpression<Vec3> {
////
//	private enum Operator {
//		PLUS("++") {
//			@Override
//			public Vec3 calculate(final Vec3 v1, final Vec3 v2) {
//				return v1.clone().add(v2);
//			}
//		},
//		MINUS("--") {
//			@Override
//			public Vec3 calculate(final Vec3 v1, final Vec3 v2) {
//				return v1.clone().subtract(v2);
//			}
//		},
//		MULT("**") {
//			@Override
//			public Vec3 calculate(final Vec3 v1, final Vec3 v2) {
//				return v1.clone().multiply(v2);
//			}
//		},
//		DIV("//") {
//			@Override
//			public Vec3 calculate(final Vec3 v1, final Vec3 v2) {
//				return v1.clone().divide(v2);
//			}
//		};
////
//		public final String sign;
////
//		Operator(final String sign) {
//			this.sign = sign;
//		}
////
//		public abstract Vec3 calculate(Vec3 v1, Vec3 v2);
////
//		@Override
//		public String toString() {
//			return sign;
//		}
//	}
////
//	private final static Patterns<Operator> patterns = new Patterns<>(new Object[][] {
//			{"%vector%[ ]++[ ]%vector%", Operator.PLUS},
//			{"%vector%[ ]--[ ]%vector%", Operator.MINUS},
//			{"%vector%[ ]**[ ]%vector%", Operator.MULT},
//			{"%vector%[ ]//[ ]%vector%", Operator.DIV}
//	});
////
//	static {
//		Skript.registerExpression(ExprVectorArithmetic.class, Vec3.class, ExpressionType.SIMPLE, patterns.getPatterns());
//	}
////
//	@SuppressWarnings("null")
//	private Expression<Vec3> first, second;
////
//	@SuppressWarnings("null")
//	private Operator op;
////
//	@Override
//	@SuppressWarnings({"unchecked", "null"})
//	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
//		first = (Expression<Vec3>) exprs[0];
//		second = (Expression<Vec3>) exprs[1];
//		op = patterns.getInfo(matchedPattern);
//		return true;
//	}
////
//	@Override
//	protected Vec3[] get(Event e) {
//		Vec3 v1 = first.getSingle(e), v2 = second.getSingle(e);
//		if (v1 == null)
//			v1 = new Vec3();
//		if (v2 == null)
//			v2 = new Vec3();
//		return CollectionUtils.array(op.calculate(v1, v2));
//	}
////
//	@Override
//	public boolean isSingle() {
//		return true;
//	}
////
//	@Override
//	public Class<? extends Vec3> getReturnType() {
//		return Vec3.class;
//	}
////
//	@Override
//	public String toString(@Nullable Event e, boolean debug) {
//		return first.toString(e, debug) + " " + op +  " " + second.toString(e, debug);
//	}
////
//}
