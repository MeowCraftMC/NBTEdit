package cx.rain.mc.nbtedit.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.fabric.networking.ModNetworkingImpl;
import cx.rain.mc.nbtedit.utility.ModConstants;
import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class NBTEditFabricClient implements ClientModInitializer {
    private static KeyMapping NBTEDIT_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            ModConstants.KEY_NBTEDIT_SHORTCUT,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            ModConstants.KEY_CATEGORY));

    @Override
    public void onInitializeClient() {
        ((ModNetworkingImpl) NBTEditPlatform.getNetworking()).addClient();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (NBTEDIT_KEY.consumeClick()) {
                RayTraceHelper.doRayTrace();
            }
        });
    }
}
