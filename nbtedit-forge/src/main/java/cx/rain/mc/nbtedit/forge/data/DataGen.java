package cx.rain.mc.nbtedit.forge.data;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.forge.data.provider.LanguageProviderENUS;
import cx.rain.mc.nbtedit.forge.data.provider.LanguageProviderZHCN;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = NBTEdit.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        if (event.includeClient()) {
        }

        if (event.includeServer()) {
            generator.addProvider(new LanguageProviderENUS(generator));
            generator.addProvider(new LanguageProviderZHCN(generator));
        }
    }
}