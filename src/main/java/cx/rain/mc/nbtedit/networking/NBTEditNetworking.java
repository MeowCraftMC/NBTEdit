package cx.rain.mc.nbtedit.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.command.NBTEditPermissions;
import cx.rain.mc.nbtedit.config.NBTEditConfigs;
import cx.rain.mc.nbtedit.networking.packet.*;
import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Created by Jay113355 on 6/28/2016.
 * Edited by qyl27 on 2022.9.19.
 */
public class NBTEditNetworking {
	private static SimpleChannel CHANNEL;

	private static final ResourceLocation CHANNEL_RL = new ResourceLocation(NBTEdit.MODID, "editing");

	private static int ID = 0;

	public NBTEditNetworking() {
		CHANNEL = NetworkRegistry.newSimpleChannel(CHANNEL_RL,
				() -> NBTEdit.VERSION,
				(version) -> version.equals(NBTEdit.VERSION),
				(version) -> version.equals(NBTEdit.VERSION)
		);

		registerMessages();
	}

	private static synchronized int nextId() {
		return ID++;
	}

	public SimpleChannel getChannel() {
		return CHANNEL;
	}

	private void registerMessages() {
		NBTEdit.getInstance().getLogger().info("Register networking.");

		CHANNEL.messageBuilder(S2CRayTracePacket.class, nextId())
				.encoder(S2CRayTracePacket::toBytes)
				.decoder(S2CRayTracePacket::new)
				.consumerMainThread(S2CRayTracePacket::clientHandleOnMain)
				.add();

		CHANNEL.messageBuilder(C2SEntityEditingRequestPacket.class, nextId())
				.encoder(C2SEntityEditingRequestPacket::toBytes)
				.decoder(C2SEntityEditingRequestPacket::new)
				.consumerMainThread(C2SEntityEditingRequestPacket::serverHandleOnMain)
				.add();

		CHANNEL.messageBuilder(C2SBlockEntityEditingRequestPacket.class, nextId())
				.encoder(C2SBlockEntityEditingRequestPacket::toBytes)
				.decoder(C2SBlockEntityEditingRequestPacket::new)
				.consumerMainThread(C2SBlockEntityEditingRequestPacket::serverHandleOnMain)
				.add();

		CHANNEL.messageBuilder(C2SNothingToEditPacket.class, nextId())
				.encoder(C2SNothingToEditPacket::toBytes)
				.decoder(C2SNothingToEditPacket::new)
				.consumerMainThread(C2SNothingToEditPacket::serverHandleOnMain)
				.add();

		CHANNEL.messageBuilder(S2COpenEntityEditingGuiPacket.class, nextId())
				.encoder(S2COpenEntityEditingGuiPacket::toBytes)
				.decoder(S2COpenEntityEditingGuiPacket::new)
				.consumerMainThread(S2COpenEntityEditingGuiPacket::clientHandleOnMain)
				.add();

		CHANNEL.messageBuilder(S2COpenBlockEntityEditingGuiPacket.class, nextId())
				.encoder(S2COpenBlockEntityEditingGuiPacket::toBytes)
				.decoder(S2COpenBlockEntityEditingGuiPacket::new)
				.consumerMainThread(S2COpenBlockEntityEditingGuiPacket::clientHandleOnMain)
				.add();

		CHANNEL.messageBuilder(C2SEntitySavingPacket.class, nextId())
				.encoder(C2SEntitySavingPacket::toBytes)
				.decoder(C2SEntitySavingPacket::new)
				.consumerMainThread(C2SEntitySavingPacket::serverHandleOnMain)
				.add();

		CHANNEL.messageBuilder(C2SBlockEntitySavingPacket.class, nextId())
				.encoder(C2SBlockEntitySavingPacket::toBytes)
				.decoder(C2SBlockEntitySavingPacket::new)
				.consumerMainThread(C2SBlockEntitySavingPacket::serverHandleOnMain)
				.add();
	}

	public void serverRayTraceRequest(ServerPlayer player) {
		CHANNEL.sendTo(new S2CRayTracePacket(), player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
	}

	public void clientOpenGuiRequest(Entity entity, boolean self) {
		CHANNEL.sendToServer(new C2SEntityEditingRequestPacket(entity.getUUID(), entity.getId(), self));
	}

	public void clientOpenGuiRequest(BlockPos pos) {
		CHANNEL.sendToServer(new C2SBlockEntityEditingRequestPacket(pos));
	}

	public void clientOpenGuiRequest() {
		CHANNEL.sendToServer(new C2SNothingToEditPacket());
	}

	public void serverOpenClientGui(ServerPlayer player, Entity entity) {
		if (NBTEditPermissions.hasPermission(player)) {
			if (entity instanceof Player && !NBTEditConfigs.CAN_EDIT_OTHER_PLAYERS.get()) {
				NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
						" tried to use /nbtedit on a player. But config is not allow that.");
				player.createCommandSourceStack().sendFailure(Component
						.translatable(Constants.MESSAGE_CANNOT_EDIT_OTHER_PLAYER)
						.withStyle(ChatFormatting.RED));
				return;
			}

			NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
					" is editing entity " + entity.getUUID() + ".");
			player.getServer().execute(() -> {
				var tag = entity.serializeNBT();
				CHANNEL.sendTo(new S2COpenEntityEditingGuiPacket(entity.getUUID(), entity.getId(), tag, false),
						player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			});
		} else {
			player.createCommandSourceStack().sendFailure(Component.translatable(Constants.MESSAGE_NO_PERMISSION)
					.withStyle(ChatFormatting.RED));
		}
	}

	public void serverOpenClientGui(ServerPlayer player, BlockPos pos) {
		if (NBTEditPermissions.hasPermission(player)) {
			NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
					"is editing block at XYZ " + pos.getX() + " " +	pos.getY() + " " + pos.getZ() + ".");
			var blockEntity = player.getLevel().getBlockEntity(pos);
			if (blockEntity != null) {
				var tag = blockEntity.serializeNBT();
				// Todo: qyl27: 2022.9.19.
				CHANNEL.sendTo(new S2COpenBlockEntityEditingGuiPacket(pos, tag),
						player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			} else {
				player.createCommandSourceStack().sendFailure(Component.translatable(Constants.MESSAGE_TARGET_IS_NOT_BLOCK_ENTITY)
						.withStyle(ChatFormatting.RED));
			}
		} else {
			player.createCommandSourceStack().sendFailure(Component.translatable(Constants.MESSAGE_NO_PERMISSION)
					.withStyle(ChatFormatting.RED));
		}
	}

	public void serverOpenClientGui(ServerPlayer player) {
		if (NBTEditPermissions.hasPermission(player)) {
			NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() + "is editing itself.");
			player.getServer().execute(() -> {
				var tag = player.serializeNBT();
				CHANNEL.sendTo(new S2COpenEntityEditingGuiPacket(player.getUUID(), player.getId(), tag, true),
						player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			});
		} else {
			player.createCommandSourceStack().sendFailure(Component.translatable(Constants.MESSAGE_NO_PERMISSION)
					.withStyle(ChatFormatting.RED));
		}
	}

	public void saveEditing(Entity entity, CompoundTag tag, boolean self) {
		CHANNEL.sendToServer(new C2SEntitySavingPacket(entity.getUUID(), entity.getId(), tag, self));
	}

	public void saveEditing(BlockPos pos, CompoundTag tag) {
		CHANNEL.sendToServer(new C2SBlockEntitySavingPacket(pos, tag));
	}
}
