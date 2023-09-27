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
//import ch.njol.skript.classes.Changer;
//import ch.njol.skript.classes.Changer.ChangeMode;
//import ch.njol.skript.doc.Description;
//import ch.njol.skript.doc.Examples;
//import ch.njol.skript.doc.Name;
//import ch.njol.skript.doc.Since;
//import ch.njol.skript.expressions.base.SimplePropertyExpression;
//import ch.njol.skript.lang.Expression;
//import ch.njol.skript.lang.SkriptParser.ParseResult;
//import ch.njol.util.Kleenean;
//import ch.njol.util.coll.CollectionUtils;
////
///**
// * @author bi0qaw
// */
//@Name("Vectors - XYZ Component")
//@Description("Gets or changes the x, y or z component of a vector.")
//@Examples({"set {_v} to vector 1, 2, 3",
//		"send \"%x of {_v}%, %y of {_v}%, %z of {_v}%\"",
//		"add 1 to x of {_v}",
//		"add 2 to y of {_v}",
//		"add 3 to z of {_v}",
//		"send \"%x of {_v}%, %y of {_v}%, %z of {_v}%\"",
//		"set x component of {_v} to 1",
//		"set y component of {_v} to 2",
//		"set z component of {_v} to 3",
//		"send \"%x component of {_v}%, %y component of {_v}%, %z component of {_v}%\"",})
//@Since("2.2-dev28")
//public class ExprVectorXYZ extends SimplePropertyExpression<Vec3, Number> {
////
//	static {
//		register(ExprVectorXYZ.class, Number.class, "[vector] (0¦x|1¦y|2¦z) [component[s]]", "vectors");
//	}
////
//	private final static Character[] axes = new Character[] {'x', 'y', 'z'};
////
//	private int axis;
////
//	@Override
//	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
//		super.init(exprs, matchedPattern, isDelayed, parseResult);
//		axis = parseResult.mark;
//		return true;
//	}
////
//	@Override
//	public Number convert(Vec3 v) {
//		return axis == 0 ? v.getX() : (axis == 1 ? v.getY() : v.getZ());
//	}
////
//	@Override
//	@SuppressWarnings("null")
//	public Class<?>[] acceptChange(ChangeMode mode) {
//		if ((mode == ChangeMode.ADD || mode == ChangeMode.REMOVE || mode == ChangeMode.SET)
//				&& getExpr().isSingle() && Changer.ChangerUtils.acceptsChange(getExpr(), ChangeMode.SET, Vec3.class))
//			return CollectionUtils.array(Number.class);
//		return null;
//	}
////
//	@Override
//	public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
//		assert delta != null;
//		final Vec3 v = getExpr().getSingle(e);
//		if (v == null)
//			return;
//		double n = ((Number) delta[0]).doubleValue();
//		switch (mode) {
//			case REMOVE:
//				n = -n;
//				//$FALL-THROUGH$
//			case ADD:
//				if (axis == 0)
//					v.setX(v.getX() + n);
//				else if (axis == 1)
//					v.setY(v.getY() + n);
//				else
//					v.setZ(v.getZ() + n);
//				getExpr().change(e, new Vec3[] {v}, ChangeMode.SET);
//				break;
//			case SET:
//				if (axis == 0)
//					v.setX(n);
//				else if (axis == 1)
//					v.setY(n);
//				else
//					v.setZ(n);
//				getExpr().change(e, new Vec3[] {v}, ChangeMode.SET);
//		}
//	}
////
//	@Override
//	protected String getPropertyName() {
//		return axes[axis] + " component";
//	}
////
//	@Override
//	public Class<Number> getReturnType() {
//		return Number.class;
//	}
////
//}
