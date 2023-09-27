/**
 *   This file is part of Skript.
 * <p>
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * <p>
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * <p>
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Copyright Peter Güttinger, SkriptLang team and contributors
 */
package ch.njol.skript.util;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import net.minecraft.ChatFormatting;
import org.bukkit.DyeColor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.localization.Adjective;
import ch.njol.skript.localization.Language;
import ch.njol.skript.variables.Variables;
import ch.njol.yggdrasil.Fields;

@SuppressWarnings("null")
public enum SkriptColor implements Color {

	BLACK(DyeColor.BLACK, ChatFormatting.BLACK),
	DARK_GREY(DyeColor.GRAY, ChatFormatting.DARK_GRAY),
	// DyeColor.LIGHT_GRAY on 1.13, DyeColor.SILVER on earlier (dye colors were changed in 1.12)
	LIGHT_GREY(DyeColor.LIGHT_GRAY, ChatFormatting.GRAY),
	WHITE(DyeColor.WHITE, ChatFormatting.WHITE),
	
	DARK_BLUE(DyeColor.BLUE, ChatFormatting.DARK_BLUE),
	BROWN(DyeColor.BROWN, ChatFormatting.BLUE),
	DARK_CYAN(DyeColor.CYAN, ChatFormatting.DARK_AQUA),
	LIGHT_CYAN(DyeColor.LIGHT_BLUE, ChatFormatting.AQUA),
	
	DARK_GREEN(DyeColor.GREEN, ChatFormatting.DARK_GREEN),
	LIGHT_GREEN(DyeColor.LIME, ChatFormatting.GREEN),
	
	YELLOW(DyeColor.YELLOW, ChatFormatting.YELLOW),
	ORANGE(DyeColor.ORANGE, ChatFormatting.GOLD),
	
	DARK_RED(DyeColor.RED, ChatFormatting.DARK_RED),
	LIGHT_RED(DyeColor.PINK, ChatFormatting.RED),
	
	DARK_PURPLE(DyeColor.PURPLE, ChatFormatting.DARK_PURPLE),
	LIGHT_PURPLE(DyeColor.MAGENTA, ChatFormatting.LIGHT_PURPLE);

	private final static Map<String, SkriptColor> names = new HashMap<>();
	private final static Set<SkriptColor> colors = new HashSet<>();
	private final static String LANGUAGE_NODE = "colors";
	
	static {
		colors.addAll(Arrays.asList(values()));
		Language.addListener(() -> {
			names.clear();
			for (SkriptColor color : values()) {
				String node = LANGUAGE_NODE + "." + color.name();
				color.setAdjective(new Adjective(node + ".adjective"));
				for (String name : Language.getList(node + ".names"))
					names.put(name.toLowerCase(Locale.ENGLISH), color);
			}
		});
	}
	
	private ChatFormatting chat;
	private DyeColor dye;
	@Nullable
	private Adjective adjective;
	
	SkriptColor(DyeColor dye, ChatFormatting chat) {
		this.chat = chat;
		this.dye = dye;
	}
	
	@Override
	public org.bukkit.Color asBukkitColor() {
		return dye.getColor();
	}
	
	@Override
	public DyeColor asDyeColor() {
		return dye;
	}
	
	@Override
	public String getName() {
		assert adjective != null;
		return adjective.toString();
	}
	
	@Override
	public Fields serialize() throws NotSerializableException {
		return new Fields(this, Variables.yggdrasil);
	}
	
	@Override
	public void deserialize(@NonNull Fields fields) throws StreamCorruptedException {
		dye = fields.getObject("dye", DyeColor.class);
		chat = fields.getObject("chat", ChatFormatting.class);
		try {
			adjective = fields.getObject("adjective", Adjective.class);
		} catch (StreamCorruptedException ignored) {}
	}
	
	public String getFormattedChat() {
		return "" + chat;
	}
	
	@Nullable
	public Adjective getAdjective() {
		return adjective;
	}
	
	public ChatFormatting asChatFormatting() {
		return chat;
	}
	
	@Deprecated
	public byte getWoolData() {
		return dye.getWoolData();
	}
	
	@Deprecated
	public byte getDyeData() {
		return (byte) (15 - dye.getWoolData());
	}
	
	private void setAdjective(@Nullable Adjective adjective) {
		this.adjective = adjective;
	}
	
	
	/**
	 * @param name The String name of the color defined by Skript's .lang files.
	 * @return Skript Color if matched up with the defined name
	 */
	@Nullable
	public static SkriptColor fromName(String name) {
		return names.get(name);
	}
	
	/**
	 * @param dye DyeColor to match against a defined Skript Color.
	 * @return Skript Color if matched up with the defined DyeColor
	 */
	public static SkriptColor fromDyeColor(DyeColor dye) {
		for (SkriptColor color : colors) {
			DyeColor c = color.asDyeColor();
			assert c != null;
			if (c.equals(dye))
				return color;
		}
		assert false;
		return null;
	}
	
	public static SkriptColor fromBukkitColor(org.bukkit.Color color) {
		for (SkriptColor c : colors) {
			if (c.asBukkitColor().equals(color))
				return c;
		}
		assert false;
		return null;
	}
	
	/**
	 * @deprecated Magic numbers
	 * @param data short to match against a defined Skript Color.
	 * @return Skript Color if matched up with the defined short
	 */
	@Deprecated
	@Nullable
	public static SkriptColor fromDyeData(short data) {
		if (data < 0 || data >= 16)
			return null;
		
		for (SkriptColor color : colors) {
			DyeColor c = color.asDyeColor();
			assert c != null;
			if (c.getDyeData() == data)
				return color;
		}
		return null;
	}
	
	/**
	 * @deprecated Magic numbers
	 * @param data short to match against a defined Skript Color.
	 * @return Skript Color if matched up with the defined short
	 */
	@Deprecated
	@Nullable
	public static SkriptColor fromWoolData(short data) {
		if (data < 0 || data >= 16)
			return null;
		for (SkriptColor color : colors) {
			DyeColor c = color.asDyeColor();
			assert c != null;
			if (c.getWoolData() == data)
				return color;
		}
		return null;
	}

	/**
	 * Replace chat color character '§' with '&'
	 * This is an alternative method to {@link ChatFormatting#stripColor(String)}
	 * But does not strip the color code.
	 * @param s string to replace chat color character of.
	 * @return String with replaced chat color character
	 */
	public static String replaceColorChar(String s) {
		return s.replace('\u00A7', '&');
	}

	@Override
	public String toString() {
		return adjective == null ? "" + name() : adjective.toString(-1, 0);
	}
}
