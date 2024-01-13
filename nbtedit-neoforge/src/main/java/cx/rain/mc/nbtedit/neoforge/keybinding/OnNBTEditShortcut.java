package cx.rain.mc.nbtedit.neoforge.keybinding;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;

@Mod.EventBusSubscriber(modid = NBTEdit.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class OnNBTEditShortcut {
    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.Key event) {
        if (NBTEditKeyBindings.NBTEDIT_SHORTCUT.consumeClick()) {
            RayTraceHelper.doRayTrace();
        }
    }
}
