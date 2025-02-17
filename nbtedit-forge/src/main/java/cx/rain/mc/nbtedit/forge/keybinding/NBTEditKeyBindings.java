package cx.rain.mc.nbtedit.forge.keybinding;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.ModConstants;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
