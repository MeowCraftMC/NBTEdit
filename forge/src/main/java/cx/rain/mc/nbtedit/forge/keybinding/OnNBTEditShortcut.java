package cx.rain.mc.nbtedit.forge.keybinding;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NBTEdit.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class OnNBTEditShortcut {
    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.Key event) {
        if (NBTEditKeyBindings.NBTEDIT_SHORTCUT.consumeClick()) {
            RayTraceHelper.doRayTrace();
        }
    }
}
