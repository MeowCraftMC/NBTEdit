package cx.rain.mc.nbtedit.fabric;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.command.NBTEditCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NBTEditFabric implements ModInitializer {
    private static NBTEditFabric INSTANCE;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected NBTEdit nbtedit;

    public NBTEditFabric() {
        INSTANCE = this;
    }

    public static NBTEditFabric getInstance() {
        return INSTANCE;
    }

    public Logger getLogger() {
        return log;
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) ->
                dispatcher.register(NBTEditCommand.NBTEDIT)));

        nbtedit = new NBTEdit();
    }
}
