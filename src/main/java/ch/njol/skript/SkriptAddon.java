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
package ch.njol.skript;

import ch.njol.skript.localization.Language;
import ch.njol.skript.util.Utils;
import ch.njol.skript.util.Version;
import ch.njol.util.coll.iterator.EnumerationIterable;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.forgespi.language.IModInfo;
import org.eclipse.jdt.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for Skript addons. Use {@link Skript#registerAddon(Object)} to create a SkriptAddon instance for your plugin.
 * 
 * @author Peter Güttinger
 */
public final class SkriptAddon {
	
	public final ModContainer modContainer;
	public final Version version;
	private final String name;
	private final IModInfo modInfo;

	/**
	 * Package-private constructor. Use {@link Skript#registerAddon(Object)} to get a SkriptAddon for your plugin.
	 * 
	 * @param container
	 */
	SkriptAddon(final ModContainer container) {
		modContainer = container;
		modInfo = container.getModInfo();
		name = "" + container.getModInfo().getModId();
		Version v;
		String version = MavenVersionStringHelper.artifactVersionToString(modInfo.getVersion());
		try {
			v = new Version("" + version);
		} catch (final IllegalArgumentException e) {
			final Matcher m = Pattern.compile("(\\d+)(?:\\.(\\d+)(?:\\.(\\d+))?)?").matcher(version);
			if (!m.find())
				throw new IllegalArgumentException("The version of the mod " + modInfo.getDisplayName() + " (" + modInfo.getModId() + ") does not contain any numbers: " + version);
			v = new Version(Utils.parseInt("" + m.group(1)), m.group(2) == null ? 0 : Utils.parseInt("" + m.group(2)), m.group(3) == null ? 0 : Utils.parseInt("" + m.group(3)));
			Skript.warning("The mod " + modInfo.getDisplayName() + " (" + modInfo.getModId() + ") uses a non-standard version syntax: '" + version + "'. Skript will use " + v + " instead.");
		}
		this.version = v;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Loads classes of the plugin by package. Useful for registering many syntax elements like Skript does it.
	 * 
	 * @param basePackage The base package to add to all sub packages, e.g. <tt>"ch.njol.skript"</tt>.
	 * @param subPackages Which subpackages of the base package should be loaded, e.g. <tt>"expressions", "conditions", "effects"</tt>. Subpackages of these packages will be loaded
	 *            as well. Use an empty array to load all subpackages of the base package.
	 * @throws IOException If some error occurred attempting to read the plugin's jar file.
	 * @return This SkriptAddon
	 */
	@SuppressWarnings({"ConstantValue", "ThrowableNotThrown"})
	public SkriptAddon loadClasses(String basePackage, String... subPackages) throws IOException {
		assert subPackages != null;
		JarFile jar = new JarFile(Objects.requireNonNull(getFile()));
		for (int i = 0; i < subPackages.length; i++)
			subPackages[i] = subPackages[i].replace('.', '/') + "/";
		basePackage = basePackage.replace('.', '/') + "/";
		try {
			List<String> classNames = new ArrayList<>();

			for (JarEntry e : new EnumerationIterable<>(jar.entries())) {
				if (e.getName().startsWith(basePackage) && e.getName().endsWith(".class")) {
					boolean load = subPackages.length == 0;
					for (String sub : subPackages) {
						if (e.getName().startsWith(sub, basePackage.length())) {
							load = true;
							break;
						}
					}

					if (load)
						classNames.add(e.getName().replace('/', '.').substring(0, e.getName().length() - ".class".length()));
				}
			}

			classNames.sort(String::compareToIgnoreCase);

			for (String c : classNames) {
				try {
					Class.forName(c, true, modContainer.getClass().getClassLoader());
				} catch (ClassNotFoundException ex) {
					Skript.exception(ex, "Cannot load class " + c + " from " + this);
				} catch (ExceptionInInitializerError err) {
					Skript.exception(err.getCause(), this + "'s class " + c + " generated an exception while loading");
				}
			}
		} finally {
			try {
				jar.close();
			} catch (IOException ignored) {}
		}
		return this;
	}
	
	@Nullable
	private String languageFileDirectory = null;
	
	/**
	 * Makes Skript load language files from the specified directory, e.g. "lang" or "skript lang" if you have a lang folder yourself. Localised files will be read from the
	 * plugin's jar and the plugin's data folder, but the default English file is only taken from the jar and <b>must</b> exist!
	 * 
	 * @param directory Directory name
	 * @return This SkriptAddon
	 */
	public SkriptAddon setLanguageFileDirectory(String directory) {
		if (languageFileDirectory != null)
			throw new IllegalStateException();
		directory = "" + directory.replace('\\', '/');
		if (directory.endsWith("/"))
			directory = "" + directory.substring(0, directory.length() - 1);
		languageFileDirectory = directory;
		Language.loadDefault(this);
		return this;
	}
	
	@Nullable
	public String getLanguageFileDirectory() {
		return languageFileDirectory;
	}
	
	@Nullable
	private File file = null;
	
	@Nullable
	public File getFile() {
		if (file != null)
			return file;
		try {
			file = modContainer.getModInfo().getOwningFile().getFile().getFilePath().toFile();
			return file;
		} catch (final IllegalArgumentException e) {
			Skript.outdatedError(e);
		} catch (final SecurityException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
}
