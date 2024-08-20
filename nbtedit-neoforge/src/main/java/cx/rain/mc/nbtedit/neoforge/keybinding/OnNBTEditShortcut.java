package cx.rain.mc.nbtedit.neoforge.keybinding;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = NBTEdit.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class OnNBTEditShortcut {
    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.Key event) {
        if (NBTEditKeyBindings.NBTEDIT_SHORTCUT.consumeClick()) {
            RayTraceHelper.doRayTrace();
        }
    }
}
