///**
// *   This file is part of Skript.
// * <p>
// *  Skript is free software: you can redistribute it and/or modify
// *  it under the terms of the GNU General Public License as published by
// *  the Free Software Foundation, either version 3 of the License, or
// *  (at your option) any later version.
// * <p>
// *  Skript is distributed in the hope that it will be useful,
// *  but WITHOUT ANY WARRANTY; without even the implied warranty of
// *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// *  GNU General Public License for more details.
// * <p>
// *  You should have received a copy of the GNU General Public License
// *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
// * <p>
// * Copyright Peter Güttinger, SkriptLang team and contributors
// */
//package ch.njol.skript.effects;
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
//import ch.njol.skript.lang.Effect;
//import ch.njol.skript.lang.Expression;
//import ch.njol.skript.lang.SkriptParser.ParseResult;
//import ch.njol.util.Kleenean;
//import ch.njol.util.VectorMath;
//
///**
// * @author bi0qaw
// */
//@Name("Vectors - Rotate around XYZ")
//@Description("Rotates a vector around x, y, or z axis by some degrees")
//@Examples({"rotate {_v} around x-axis by 90",
//		"rotate {_v} around y-axis by 90",
//		"rotate {_v} around z-axis by 90 degrees"})
//@Since("2.2-dev28")
//public class EffVectorRotateXYZ extends Effect {
//
//	static {
//		Skript.registerEffect(EffVectorRotateXYZ.class, "rotate %vectors% around (1¦x|2¦y|3¦z)(-| )axis by %number% [degrees]");
//	}
//
//	private final static Character[] axes = new Character[] {'x', 'y', 'z'};
//
//	@SuppressWarnings("null")
//	private Expression<Vec3> vectors;
//
//	@SuppressWarnings("null")
//	private Expression<Number> degree;
//	private int axis;
//
//	@Override
//	@SuppressWarnings({"unchecked", "null"})
//	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
//		vectors = (Expression<Vec3>) expressions[0];
//		degree = (Expression<Number>) expressions[1];
//		axis = parseResult.mark;
//		return true;
//	}
//
//	@Override
//	@SuppressWarnings("null")
//	protected void execute(Event e) {
//		Number d = degree.getSingle(e);
//		if (d == null)
//			return;
//		switch (axis) {
//			case 1:
//				for (Vec3 v : vectors.getArray(e))
//					VectorMath.rotX(v, d.doubleValue());
//				break;
//			case 2:
//				for (Vec3 v : vectors.getArray(e))
//					VectorMath.rotY(v, d.doubleValue());
//				break;
//			case 3:
//				for (Vec3 v : vectors.getArray(e))
//					VectorMath.rotZ(v, d.doubleValue());
//		}
//	}
//
//	@Override
//	public String toString(@Nullable Event e, boolean debug) {
//		return "rotate " + vectors.toString(e, debug) + " around " + axes[axis] + "-axis" + " by " + degree + "degrees";
//	}
//
//}
