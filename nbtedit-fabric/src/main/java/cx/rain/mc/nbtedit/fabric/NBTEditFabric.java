package cx.rain.mc.nbtedit.fabric;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.INBTEditPlatform;
import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import cx.rain.mc.nbtedit.api.config.INBTEditConfig;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;
import cx.rain.mc.nbtedit.command.NBTEditCommand;
import cx.rain.mc.nbtedit.fabric.command.NBTEditPermissionImpl;
import cx.rain.mc.nbtedit.fabric.config.NBTEditConfigImpl;
import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

public class NBTEditFabric implements ModInitializer, INBTEditPlatform {
    private static NBTEditFabric INSTANCE;

    protected NBTEdit nbtedit;

    protected NBTEditNetworkingImpl networking;
    protected NBTEditConfigImpl config;
    protected NBTEditPermissionImpl permission;

    public NBTEditFabric() {
        INSTANCE = this;

        networking = new NBTEditNetworkingImpl();
        config = new NBTEditConfigImpl(FabricLoader.getInstance().getGameDir().toFile());
        permission = new NBTEditPermissionImpl(config.getPermissionLevel());
    }

    public static NBTEditFabric getInstance() {
        return INSTANCE;
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) ->
                dispatcher.register(NBTEditCommand.NBTEDIT)));

        nbtedit = new NBTEdit();
        nbtedit.init(this);
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
