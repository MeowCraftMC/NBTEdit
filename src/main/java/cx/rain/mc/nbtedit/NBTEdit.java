package cx.rain.mc.nbtedit;

import cx.rain.mc.nbtedit.client.NBTEditClient;
import cx.rain.mc.nbtedit.command.NBTEditCommand;
import cx.rain.mc.nbtedit.config.NBTEditConfigs;
import cx.rain.mc.nbtedit.networking.NBTEditNetworking;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(value = NBTEdit.MODID)
public class NBTEdit {
	public static final String MODID = "nbtedit";
	public static final String NAME = "In-game NBTEdit Reborn";
	public static final String VERSION = "@version@";

	private static NBTEdit INSTANCE;

	private NBTEditNetworking networkManager;
	private NBTEditClient clientManager;

	private final Logger logger = LoggerFactory.getLogger(NAME);

	public NBTEdit() {
		INSTANCE = this;

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NBTEditConfigs.CONFIG, "nbtedit.toml");

		final var bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::setup);
		bus.addListener(this::setupClient);

		MinecraftForge.EVENT_BUS.addListener(NBTEditCommand::onRegisterCommands);
	}

	private void setup(FMLCommonSetupEvent event) {
		logger.info("NBTEdit initializing.");

		networkManager = new NBTEditNetworking();

		logger.info("NBTEdit loaded!");
	}

	private void setupClient(FMLClientSetupEvent event) {
		logger.info("Initializing client.");
		clientManager = new NBTEditClient();
		logger.info("Client initialized.");
	}

	public static NBTEdit getInstance() {
		return INSTANCE;
	}

	public Logger getLogger() {
		return logger;
	}

	public NBTEditNetworking getNetworkManager() {
		return networkManager;
	}

	public NBTEditClient getClientManager() {
		return clientManager;
	}
}
