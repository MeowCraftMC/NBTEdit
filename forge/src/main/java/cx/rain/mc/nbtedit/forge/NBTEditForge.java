package cx.rain.mc.nbtedit.forge;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.INBTEditPlatform;
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
public class NBTEditForge implements INBTEditPlatform {
    private NBTEdit nbtedit;
    private INBTEditNetworking networking;
    private INBTEditConfig config;
    private INBTEditCommandPermission permission;

    public NBTEditForge() {
        nbtedit = new NBTEdit();
        nbtedit.init(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NBTEditConfigImpl.CONFIG, "nbtedit.toml");

        networking = new NBTEditNetworkingImpl();
        config = new NBTEditConfigImpl();
        permission = new NBTEditPermissionImpl();

        final var bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::setupClient);

        nbtedit.getLogger().info("NBTEdit loaded!");
    }

    private void setup(FMLCommonSetupEvent event) {
    }

    private void setupClient(FMLClientSetupEvent event) {
//        nbtedit.getLogger().info("Initializing client.");
//        client = new NBTEditClient();
//        nbtedit.getLogger().info("Client initialized.");
    }

    @Override
    public INBTEditNetworking getNetworking() {
        return networking;
    }

    @Override
    public INBTEditConfig getConfig() {
        return config;
    }

    @Override
    public INBTEditCommandPermission getPermission() {
        return permission;
    }
}
