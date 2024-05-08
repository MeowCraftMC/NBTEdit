package cx.rain.mc.nbtedit.forge;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import cx.rain.mc.nbtedit.api.config.INBTEditConfig;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;
import cx.rain.mc.nbtedit.forge.command.NBTEditPermissionImpl;
import cx.rain.mc.nbtedit.forge.config.NBTEditConfigImpl;
import cx.rain.mc.nbtedit.forge.networking.NBTEditNetworkingImpl;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = NBTEdit.MODID)
public class NBTEditForge {
    private final NBTEdit nbtedit;

    public NBTEditForge() {
        nbtedit = new NBTEdit(new NBTEditPlatformImpl());

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NBTEditConfigImpl.CONFIG, "nbtedit.toml");

        final var bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::setupClient);
    }

    private void setup(FMLCommonSetupEvent event) {
        nbtedit.getLogger().info("NBTEdit loaded!");
    }

    private void setupClient(FMLClientSetupEvent event) {
    }
}
