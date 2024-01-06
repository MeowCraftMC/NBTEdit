package cx.rain.mc.nbtedit.fabric;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.command.NBTEditCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class NBTEditFabric implements ModInitializer {
    private static NBTEditFabric INSTANCE;

    protected NBTEdit nbtedit;

    public NBTEditFabric() {
        INSTANCE = this;
    }

    public static NBTEditFabric getInstance() {
        return INSTANCE;
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) ->
                dispatcher.register(NBTEditCommand.NBTEDIT)));

        nbtedit = new NBTEdit();
    }
}
