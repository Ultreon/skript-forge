package com.github.ultreon.portutils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.server.ServerLifecycleHooks;

public class GameUtils {
	public static String getVersion() {
		return DistExecutor.unsafeRunForDist(() -> () -> Minecraft.getInstance().getGame().getVersion().getName(), () -> () -> ServerLifecycleHooks.getCurrentServer().getServerVersion());
	}
}
