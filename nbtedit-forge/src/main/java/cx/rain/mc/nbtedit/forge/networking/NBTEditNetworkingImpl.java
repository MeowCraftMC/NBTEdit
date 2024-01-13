package cx.rain.mc.nbtedit.forge.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;
import cx.rain.mc.nbtedit.forge.networking.packet.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.*;

/**
 * Created by Jay113355 on 6/28/2016.
 * Edited by qyl27 on 2022.9.19.
 */
public class NBTEditNetworkingImpl implements INBTEditNetworking {
	public static final int CHANNEL_VERSION = 1;

	private static SimpleChannel CHANNEL;

	private static final ResourceLocation CHANNEL_RL = new ResourceLocation(NBTEdit.MODID, "editing");

	private static final Channel.VersionTest CLIENT_VERSION_TEST = (status, version) -> status == Channel.VersionTest.Status.PRESENT;
	private static final Channel.VersionTest SERVER_VERSION_TEST = (status, version) -> true;

	private static int ID = 0;

	public NBTEditNetworkingImpl() {
		CHANNEL = ChannelBuilder.named(CHANNEL_RL)
				.serverAcceptedVersions(SERVER_VERSION_TEST)
				.clientAcceptedVersions(CLIENT_VERSION_TEST)
				.simpleChannel();

		registerMessages();
	}

	private static synchronized int nextId() {
		return ID++;
	}

	private void registerMessages() {
		CHANNEL.messageBuilder(S2CRayTracePacket.class, nextId()).encoder(S2CRayTracePacket::toBytes).decoder(S2CRayTracePacket::new).consumerMainThread(S2CRayTracePacket::clientHandleOnMain).add();

		CHANNEL.messageBuilder(C2SEntityEditingRequestPacket.class, nextId()).encoder(C2SEntityEditingRequestPacket::toBytes).decoder(C2SEntityEditingRequestPacket::new).consumerMainThread(C2SEntityEditingRequestPacket::serverHandleOnMain).add();
		CHANNEL.messageBuilder(C2SBlockEntityEditingRequestPacket.class, nextId()).encoder(C2SBlockEntityEditingRequestPacket::toBytes).decoder(C2SBlockEntityEditingRequestPacket::new).consumerMainThread(C2SBlockEntityEditingRequestPacket::serverHandleOnMain).add();
		CHANNEL.messageBuilder(C2SItemStackEditingRequestPacket.class, nextId()).encoder(C2SItemStackEditingRequestPacket::toBytes).decoder(C2SItemStackEditingRequestPacket::new).consumerMainThread(C2SItemStackEditingRequestPacket::serverHandleOnMain).add();

		CHANNEL.messageBuilder(S2COpenEntityEditingGuiPacket.class, nextId()).encoder(S2COpenEntityEditingGuiPacket::toBytes).decoder(S2COpenEntityEditingGuiPacket::new).consumerMainThread(S2COpenEntityEditingGuiPacket::clientHandleOnMain).add();
		CHANNEL.messageBuilder(S2COpenBlockEntityEditingGuiPacket.class, nextId()).encoder(S2COpenBlockEntityEditingGuiPacket::toBytes).decoder(S2COpenBlockEntityEditingGuiPacket::new).consumerMainThread(S2COpenBlockEntityEditingGuiPacket::clientHandleOnMain).add();
		CHANNEL.messageBuilder(S2COpenItemStackEditingGuiPacket.class, nextId()).encoder(S2COpenItemStackEditingGuiPacket::toBytes).decoder(S2COpenItemStackEditingGuiPacket::new).consumerMainThread(S2COpenItemStackEditingGuiPacket::clientHandleOnMain).add();

		CHANNEL.messageBuilder(C2SEntitySavingPacket.class, nextId()).encoder(C2SEntitySavingPacket::toBytes).decoder(C2SEntitySavingPacket::new).consumerMainThread(C2SEntitySavingPacket::serverHandleOnMain).add();
		CHANNEL.messageBuilder(C2SBlockEntitySavingPacket.class, nextId()).encoder(C2SBlockEntitySavingPacket::toBytes).decoder(C2SBlockEntitySavingPacket::new).consumerMainThread(C2SBlockEntitySavingPacket::serverHandleOnMain).add();
		CHANNEL.messageBuilder(C2SItemStackSavingPacket.class, nextId()).encoder(C2SItemStackSavingPacket::toBytes).decoder(C2SItemStackSavingPacket::new).consumerMainThread(C2SItemStackSavingPacket::serverHandleOnMain).add();
	}

	@Override
	public void serverRayTraceRequest(ServerPlayer player) {
		CHANNEL.send(new S2CRayTracePacket(), player.connection.getConnection());
	}

	@Override
	public void clientOpenGuiRequest(Entity entity, boolean self) {
		CHANNEL.send(new C2SEntityEditingRequestPacket(entity.getUUID(), entity.getId(), self), PacketDistributor.SERVER.noArg());
	}

	@Override
	public void clientOpenGuiRequest(BlockPos pos) {
		CHANNEL.send(new C2SBlockEntityEditingRequestPacket(pos), PacketDistributor.SERVER.noArg());
	}

	@Override
	public void clientOpenGuiRequest(ItemStack stack) {
		CHANNEL.send(new C2SItemStackEditingRequestPacket(stack), PacketDistributor.SERVER.noArg());
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, Entity entity) {
		player.getServer().execute(() -> {
			var tag = entity.serializeNBT();
			CHANNEL.send(new S2COpenEntityEditingGuiPacket(entity.getUUID(), entity.getId(), tag, false),
					player.connection.getConnection());
		});
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, BlockPos pos, BlockEntity blockEntity) {
		player.getServer().execute(() -> {
			var tag = blockEntity.serializeNBT();
			CHANNEL.send(new S2COpenBlockEntityEditingGuiPacket(pos, tag),
					player.connection.getConnection());
		});
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player) {
		player.getServer().execute(() -> {
			var tag = player.serializeNBT();
			CHANNEL.send(new S2COpenEntityEditingGuiPacket(player.getUUID(), player.getId(), tag, true),
					player.connection.getConnection());
		});
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, ItemStack stack) {
		player.getServer().execute(() -> {
			var tag = stack.serializeNBT();
			CHANNEL.send(new S2COpenItemStackEditingGuiPacket(stack, tag),
					player.connection.getConnection());
		});
	}

	@Override
	public void saveEditing(Entity entity, CompoundTag tag, boolean self) {
		CHANNEL.send(new C2SEntitySavingPacket(entity.getUUID(), entity.getId(), tag, self), PacketDistributor.SERVER.noArg());
	}

	@Override
	public void saveEditing(BlockPos pos, CompoundTag tag) {
		CHANNEL.send(new C2SBlockEntitySavingPacket(pos, tag), PacketDistributor.SERVER.noArg());
	}

	@Override
	public void saveEditing(ItemStack stack, CompoundTag tag) {
		CHANNEL.send(new C2SItemStackSavingPacket(stack, tag), PacketDistributor.SERVER.noArg());
	}
}
