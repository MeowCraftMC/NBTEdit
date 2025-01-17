package cx.rain.mc.nbtedit.neoforge.data;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.data.provider.LanguageProviderENUS;
import cx.rain.mc.nbtedit.neoforge.data.provider.LanguageProviderZHCN;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = NBTEdit.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        var packOutput = generator.getPackOutput();

        generator.addProvider(true, new LanguageProviderENUS(packOutput));
        generator.addProvider(true, new LanguageProviderZHCN(packOutput));
    }
}