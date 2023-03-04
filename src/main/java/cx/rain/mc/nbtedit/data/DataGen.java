package cx.rain.mc.nbtedit.data;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.data.provider.language.LanguageProviderENUS;
import cx.rain.mc.nbtedit.data.provider.language.LanguageProviderZHCN;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NBTEdit.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        var exHelper = event.getExistingFileHelper();
        var packOutput = generator.getPackOutput();

        if (event.includeClient()) {
        }

        if (event.includeServer()) {
            generator.addProvider(true, new LanguageProviderENUS(packOutput));
            generator.addProvider(true, new LanguageProviderZHCN(packOutput));
        }
    }
}