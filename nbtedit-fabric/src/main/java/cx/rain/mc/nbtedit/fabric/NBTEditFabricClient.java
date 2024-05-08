package cx.rain.mc.nbtedit.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.fabric.NBTEditFabric;
import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingClient;
import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.utility.Constants;
import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class NBTEditFabricClient implements ClientModInitializer {
    private static final KeyMapping NBTEDIT_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            Constants.KEY_NBTEDIT_SHORTCUT,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            Constants.KEY_CATEGORY));

    @Override
    public void onInitializeClient() {
        ((NBTEditNetworkingImpl) NBTEditPlatformImpl.getInstance().getNetworking()).addClient();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (NBTEDIT_KEY.consumeClick()) {
                RayTraceHelper.doRayTrace();
            }
        });
    }
}
