package cx.rain.mc.nbtedit.forge.command;

import com.mojang.brigadier.CommandDispatcher;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.command.NBTEditCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NBTEdit.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NBTEditCommandRegister {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(NBTEditCommand.NBTEDIT);
    }
}
