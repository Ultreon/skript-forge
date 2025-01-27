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
package ch.njol.skript.tests.runner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.tests.TestResults;

/**
 * Tracks failed and succeeded tests.
 */
public class TestTracker {
	
	/**
	 * Started tests.
	 */
	private static final Set<String> startedTests = new HashSet<>();
		
	/**
	 * Failed tests to failure assert messages.
	 */
	private static final Map<String, String> failedTests = new HashMap<>();
	
	@Nullable
	private static String currentTest;
	
	public static void testStarted(String name) {
		startedTests.add(name);
		currentTest = name;
	}
	
	public static void testFailed(String msg) {
		failedTests.put(currentTest, msg);
	}
	
	public static Map<String, String> getFailedTests() {
		return new HashMap<>(failedTests);
	}
	
	public static Set<String> getSucceededTests() {
		Set<String> tests = new HashSet<>(startedTests);
		tests.removeAll(failedTests.keySet());
		return tests;
	}
	
	public static TestResults collectResults() {
		TestResults results = new TestResults(getSucceededTests(), getFailedTests(), TestMode.docsFailed);
		startedTests.clear();
		failedTests.clear();
		return results;
	}
	
}
