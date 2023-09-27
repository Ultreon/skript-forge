/*
 * Copyright (c) 2022. - Qboi SMP Development Team
 * Do NOT redistribute, or copy in any way, and do NOT modify in any way.
 * It is not allowed to hack into the code, use cheats against the code and/or compiled form.
 * And it is not allowed to decompile, modify or/and patch parts of code or classes or in full form.
 * Sharing this file isn't allowed either, and is hereby strictly forbidden.
 * Sharing decompiled code on social media or an online platform will cause in a report on that account.
 *
 * ONLY the owner can bypass these rules.
 */

package com.github.ultreon.portutils.utils;

import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;

import java.util.Objects;

@SuppressWarnings("UnusedReturnValue")
public final class Worlds {
	private Worlds() {
		throw new UnsupportedOperationException();
	}

    public static boolean saveLevelThenOpenTitle() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) return false;

        boolean flag = mc.isLocalServer();
        boolean flag1 = mc.isConnectedToRealms();
        clearLevelAndDisconnect(mc, flag);
        gotoDefaultReturnScreen(mc, flag, flag1);

        return true;
    }

    public static boolean saveLevelThen(Runnable runnable) {
        if (saveLevel0()) return false;

        runnable.run();
        return true;
    }

    public static boolean saveLevelThenOpen(Screen screen) {
        if (saveLevel0()) return false;

        Minecraft mc = Minecraft.getInstance();
        mc.pushGuiLayer(screen);
        return true;
    }

    public static void saveLevelThenQuitGame() {
        saveLevelThen(() -> Minecraft.getInstance().stop());
    }

    private static void clearLevelAndDisconnect(Minecraft mc, boolean flag) {
        Objects.requireNonNull(mc.level).disconnect();
        if (flag) {
            mc.clearLevel(new GenericDirtMessageScreen(Component.translatable("menu.savingLevel")));
        } else {
            mc.clearLevel();
        }
    }

    private static boolean saveLevel0() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) return true;

        boolean flag = mc.isLocalServer();
        clearLevelAndDisconnect(mc, flag);
        return false;
    }

    private static void gotoDefaultReturnScreen(Minecraft mc, boolean flag, boolean flag1) {
        TitleScreen titlescreen = new TitleScreen();
        if (flag) {
            mc.pushGuiLayer(titlescreen);
        } else if (flag1) {
            mc.pushGuiLayer(new RealmsMainScreen(titlescreen));
        } else {
            mc.pushGuiLayer(new JoinMultiplayerScreen(titlescreen));
        }
    }
}
