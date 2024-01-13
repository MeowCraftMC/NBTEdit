package cx.rain.mc.nbtedit.neoforge;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.config.NBTEditConfigImpl;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = NBTEdit.MODID)
public class NBTEditNeoForge {
    private final NBTEdit nbtedit;

    public NBTEditNeoForge() {
        NBTEditPlatformImpl.load();

        nbtedit = new NBTEdit();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NBTEditConfigImpl.CONFIG, "nbtedit.toml");

        final var bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::setupClient);

        nbtedit.getLogger().info("NBTEdit loaded!");
    }

    private void setup(FMLCommonSetupEvent event) {
    }

    private void setupClient(FMLClientSetupEvent event) {
    }
}
