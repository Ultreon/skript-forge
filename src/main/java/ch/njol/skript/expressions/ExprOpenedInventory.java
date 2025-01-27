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
//import org.bukkit.entity.Player;
//import net.minecraftforge.eventbus.api.Event;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import org.eclipse.jdt.annotation.Nullable;
//
//import ch.njol.skript.Skript;
//import ch.njol.skript.doc.Description;
//import ch.njol.skript.doc.Examples;
//import ch.njol.skript.doc.Name;
//import ch.njol.skript.doc.Since;
//import ch.njol.skript.expressions.base.PropertyExpression;
//import ch.njol.skript.lang.Expression;
//import ch.njol.skript.lang.ExpressionType;
//import ch.njol.skript.lang.SkriptParser.ParseResult;
//import ch.njol.skript.util.Getter;
//import ch.njol.util.Kleenean;
//
//@Name("Opened AbstractContainerMenu")
//@Description({"Return the currently opened inventory of a player.",
//	"If no inventory is open, it returns the own player's crafting inventory."})
//@Examples({"set slot 1 of player's current inventory to diamond sword"})
//@Since("2.2-dev24, 2.2-dev35 (Just 'current inventory' works in player events)")
//public class ExprOpenedInventory extends PropertyExpression<Player, AbstractContainerMenu> {
//
//	static {
//		Skript.registerExpression(ExprOpenedInventory.class, AbstractContainerMenu.class, ExpressionType.PROPERTY, "[the] (current|open|top) inventory [of %players%]", "%players%'[s] (current|open|top) inventory");
//	}
//
//	@Override
//	public Class<? extends AbstractContainerMenu> getReturnType() {
//		return AbstractContainerMenu.class;
//	}
//
//	@SuppressWarnings({"unchecked", "null"})
//	@Override
//	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
//		setExpr((Expression<Player>) exprs[0]);
//		return true;
//	}
//
//	@Override
//	protected AbstractContainerMenu[] get(Event event, Player[] source) {
//		return get(source, new Getter<AbstractContainerMenu, Player>() {
//			@SuppressWarnings("null")
//			@Override
//			public AbstractContainerMenu get(final Player player) {
//				return player.getOpenInventory() != null ? player.getOpenInventory().getTopInventory() : null;
//			}
//		});
//	}
//
//	@Override
//	public String toString(@Nullable Event event, boolean debug) {
//		return "current inventory" + (getExpr().isDefault() ? "" : " of " + getExpr().toString(event, debug));
//	}
//
//}
