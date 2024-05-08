package cx.rain.mc.nbtedit.neoforge;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.config.NBTEditConfigImpl;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(value = NBTEdit.MODID)
public class NBTEditNeoForge {
    private final NBTEdit nbtedit;

    public NBTEditNeoForge(IEventBus bus) {
        nbtedit = new NBTEdit(new NBTEditPlatformImpl());

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NBTEditConfigImpl.CONFIG, "nbtedit.toml");

        bus.addListener(this::setup);
        bus.addListener(this::setupClient);
    }

    private void setup(FMLCommonSetupEvent event) {
        nbtedit.getLogger().info("NBTEdit loaded!");
    }

    private void setupClient(FMLClientSetupEvent event) {
    }
}
