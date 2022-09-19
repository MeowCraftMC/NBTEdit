package cx.rain.mc.nbtedit;

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

//	public static NamedNBT CLIPBOARD = null;

	private static NBTEdit INSTANCE;

	private NBTEditNetworking networkManager;

	private final Logger logger = LoggerFactory.getLogger(NAME);
//	private final Logger internalLog = LoggerFactory.getLogger("NBTEditInternal");

//	private ClipboardStates clipboardSaves;

	public NBTEdit() {
		INSTANCE = this;

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NBTEditConfigs.CONFIG, "nbtedit.toml");

		final var bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::setup);
		bus.addListener(this::setupClient);

		MinecraftForge.EVENT_BUS.addListener(NBTEditCommand::onRegisterCommands);
	}

//	public Logger getInternalLogger() {
//		return internalLog;
//	}

	private void setup(FMLCommonSetupEvent event) {
		logger.info("NBTEdit initializing.");

		networkManager = new NBTEditNetworking();

		logger.info("NBTEdit loaded!");
	}

	private void setupClient(FMLClientSetupEvent event) {
		logger.info("Initializing client.");

//		clipboardSaves = new ClipboardStates(new File(new File("."), "NBTEdit.dat"));

//		ClipboardStates clipboard = getClipboardSaves();
//		clipboard.load();
//		clipboard.save();

		logger.info("Client initialized.");
	}

//	public static ClipboardStates getClipboardSaves() {
//		return INSTANCE.clipboardSaves;
//	}

	public static NBTEdit getInstance() {
		return INSTANCE;
	}

	public Logger getLogger() {
		return logger;
	}

	public NBTEditNetworking getNetworkManager() {
		return networkManager;
	}
}
