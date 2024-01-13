package cx.rain.mc.nbtedit.neoforge.command;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.command.NBTEditCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod.EventBusSubscriber(modid = NBTEdit.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NBTEditCommandRegister {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(NBTEditCommand.NBTEDIT);
    }
}
