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
//import ch.njol.util.VectorMath;
//import ch.njol.util.coll.CollectionUtils;
//
///**
// * @author bi0qaw
// */
//@Name("Vectors - Spherical Shape")
//@Description("Forms a 'spherical shaped' vector using yaw and pitch to manipulate the current point.")
//@Examples({"loop 360 times:",
//		"	set {_v} to spherical vector radius 1, yaw loop-value, pitch loop-value",
//		"set {_v} to spherical vector radius 1, yaw 45, pitch 90"})
//@Since("2.2-dev28")
//public class ExprVectorSpherical extends SimpleExpression<Vec3> {
//
//	static {
//		Skript.registerExpression(ExprVectorSpherical.class, Vec3.class, ExpressionType.SIMPLE,
//				"[new] spherical vector [(from|with)] [radius] %number%, [yaw] %number%(,| and) [pitch] %number%");
//	}
//
//	@SuppressWarnings("null")
//	private Expression<Number> radius, yaw, pitch;
//
//	@Override
//	@SuppressWarnings({"unchecked", "null"})
//	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
//		radius = (Expression<Number>) exprs[0];
//		yaw = (Expression<Number>) exprs[1];
//		pitch = (Expression<Number>) exprs[2];
//		return true;
//	}
//
//	@Override
//	@SuppressWarnings("null")
//	protected Vec3[] get(Event e) {
//		Number r = radius.getSingle(e);
//		Number y = yaw.getSingle(e);
//		Number p = pitch.getSingle(e);
//		if (r == null || y == null || p == null)
//			return null;
//		return CollectionUtils.array(VectorMath.fromSphericalCoordinates(r.doubleValue(), VectorMath.fromSkriptYaw(y.floatValue()), p.floatValue() + 90));
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
//		return "spherical vector with radius " + radius.toString(e, debug) + ", yaw " + yaw.toString(e, debug) +
//				" and pitch" + pitch.toString(e, debug);
//	}
//
//}
