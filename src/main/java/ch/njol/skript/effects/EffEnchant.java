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
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraftforge.eventbus.api.Event;
//import net.minecraft.world.item.ItemStack;
//import org.eclipse.jdt.annotation.Nullable;
//
//import ch.njol.skript.Skript;
//import ch.njol.skript.aliases.ItemType;
//import ch.njol.skript.classes.Changer.ChangeMode;
//import ch.njol.skript.classes.Changer.ChangerUtils;
//import ch.njol.skript.doc.Description;
//import ch.njol.skript.doc.Examples;
//import ch.njol.skript.doc.Name;
//import ch.njol.skript.doc.Since;
//import ch.njol.skript.lang.Effect;
//import ch.njol.skript.lang.Expression;
//import ch.njol.skript.lang.SkriptParser.ParseResult;
//import ch.njol.skript.util.EnchantmentType;
//import ch.njol.util.Kleenean;
//
///**
// * @author Peter Güttinger
// */
//@Name("Enchant/Disenchant")
//@Description("Enchant or disenchant an existing item.")
//@Examples({"enchant the player's tool with sharpness 5",
//		"disenchant the player's tool"})
//@Since("2.0")
//public class EffEnchant extends Effect {
//	static {
//		Skript.registerEffect(EffEnchant.class,
//				"enchant %~itemtypes% with %enchantmenttypes%",
//				"disenchant %~itemtypes%");
//	}
//	
//	@SuppressWarnings("null")
//	private Expression<ItemType> item;
//	@Nullable
//	private Expression<EnchantmentType> enchs;
//	
//	@SuppressWarnings({"unchecked", "null"})
//	@Override
//	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
//		item = (Expression<ItemType>) exprs[0];
//		if (!ChangerUtils.acceptsChange(item, ChangeMode.SET, ItemStack.class)) {
//			Skript.error(item + " cannot be changed, thus it cannot be (dis)enchanted");
//			return false;
//		}
//		if (matchedPattern == 0)
//			enchs = (Expression<EnchantmentType>) exprs[1];
//		return true;
//	}
//	
//	@Override
//	protected void execute(final Event e) {
//		final ItemType i = item.getSingle(e);
//		if (i == null)
//			return;
//		if (enchs != null) {
//			final EnchantmentType[] types = enchs.getArray(e);
//			if (types.length == 0)
//				return;
//			
//			for (final EnchantmentType type : types) {
//				Enchantment ench = type.getType();
//				assert ench != null;
//				i.addEnchantments(new EnchantmentType(ench, type.getLevel()));
//			}
//			item.change(e, new ItemType[] {i}, ChangeMode.SET);
//		} else {
//			final EnchantmentType[] types = i.getEnchantmentTypes();
//			if (types == null)
//				return;
//			
//			for (final EnchantmentType ench : types) {
//				assert ench != null;
//				i.removeEnchantments(ench);
//			}
//			item.change(e, new ItemType[] {i}, ChangeMode.SET);
//		}
//	}
//	
//	@Override
//	public String toString(final @Nullable Event e, final boolean debug) {
//		return enchs == null ? "disenchant " + item.toString(e, debug) : "enchant " + item.toString(e, debug) + " with " + enchs;
//	}
//	
//}
