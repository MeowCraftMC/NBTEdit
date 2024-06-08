package cx.rain.mc.nbtedit.neoforge.keybinding;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.ModConstants;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = NBTEdit.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NBTEditKeyBindings {
    public static final KeyMapping NBTEDIT_SHORTCUT = new KeyMapping(ModConstants.KEY_NBTEDIT_SHORTCUT,
            KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N, ModConstants.KEY_CATEGORY);

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        NBTEdit.getInstance().getLogger().info("Register key binding.");
        event.register(NBTEDIT_SHORTCUT);
    }
}
