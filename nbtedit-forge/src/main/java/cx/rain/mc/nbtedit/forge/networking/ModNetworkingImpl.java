package cx.rain.mc.nbtedit.forge.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import cx.rain.mc.nbtedit.forge.networking.packet.BlockEntityEditPacket;
import cx.rain.mc.nbtedit.forge.networking.packet.EntityEditPacket;
import cx.rain.mc.nbtedit.forge.networking.packet.ItemStackEditPacket;
import cx.rain.mc.nbtedit.forge.networking.packet.c2s.C2SBlockEntityEditingRequestPacket;
import cx.rain.mc.nbtedit.forge.networking.packet.c2s.C2SEntityEditingRequestPacket;
import cx.rain.mc.nbtedit.forge.networking.packet.c2s.C2SItemStackEditingRequestPacket;
import cx.rain.mc.nbtedit.forge.networking.packet.s2c.S2CRayTracePacket;
import cx.rain.mc.nbtedit.networking.NetworkEditingHelper;
import cx.rain.mc.nbtedit.networking.NetworkSavingHelper;
import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.*;

/**
 * Created by Jay113355 on 6/28/2016.
 * Edited by qyl27 on 2022.9.19.
 */
public class ModNetworkingImpl implements IModNetworking {
	public static final int CHANNEL_VERSION = 1;

	private static SimpleChannel CHANNEL;

	private static final ResourceLocation CHANNEL_RL = new ResourceLocation(NBTEdit.MODID, "editing");

	private static final Channel.VersionTest CLIENT_VERSION_TEST = (status, version) -> status == Channel.VersionTest.Status.PRESENT;
	private static final Channel.VersionTest SERVER_VERSION_TEST = (status, version) -> true;

	private static int ID = 0;

	public ModNetworkingImpl() {
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
		CHANNEL.messageBuilder(S2CRayTracePacket.class, nextId())
				.encoder(S2CRayTracePacket::write)
				.decoder(S2CRayTracePacket::new)
				.consumerMainThread(this::clientHandle)
				.add();

		CHANNEL.messageBuilder(C2SBlockEntityEditingRequestPacket.class, nextId())
				.encoder(C2SBlockEntityEditingRequestPacket::write)
				.decoder(C2SBlockEntityEditingRequestPacket::new)
				.consumerMainThread(this::serverHandle)
				.add();
		CHANNEL.messageBuilder(C2SEntityEditingRequestPacket.class, nextId())
				.encoder(C2SEntityEditingRequestPacket::write)
				.decoder(C2SEntityEditingRequestPacket::new)
				.consumerMainThread(this::serverHandle)
				.add();
		CHANNEL.messageBuilder(C2SItemStackEditingRequestPacket.class, nextId())
				.encoder(C2SItemStackEditingRequestPacket::write)
				.decoder(C2SItemStackEditingRequestPacket::new)
				.consumerMainThread(this::serverHandle)
				.add();


		CHANNEL.messageBuilder(BlockEntityEditPacket.class, nextId(), NetworkDirection.PLAY_TO_CLIENT)
				.encoder(BlockEntityEditPacket::write)
				.decoder(BlockEntityEditPacket::new)
				.consumerMainThread(this::clientHandle)
				.add();
		CHANNEL.messageBuilder(EntityEditPacket.class, nextId(), NetworkDirection.PLAY_TO_CLIENT)
				.encoder(EntityEditPacket::write)
				.decoder(EntityEditPacket::new)
				.consumerMainThread(this::clientHandle)
				.add();
		CHANNEL.messageBuilder(ItemStackEditPacket.class, nextId(), NetworkDirection.PLAY_TO_CLIENT)
				.encoder(ItemStackEditPacket::write)
				.decoder(ItemStackEditPacket::new)
				.consumerMainThread(this::clientHandle)
				.add();

		CHANNEL.messageBuilder(BlockEntityEditPacket.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
				.encoder(BlockEntityEditPacket::write)
				.decoder(BlockEntityEditPacket::new)
				.consumerMainThread(this::serverHandle)
				.add();
		CHANNEL.messageBuilder(EntityEditPacket.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
				.encoder(EntityEditPacket::write)
				.decoder(EntityEditPacket::new)
				.consumerMainThread(this::serverHandle)
				.add();
		CHANNEL.messageBuilder(ItemStackEditPacket.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
				.encoder(ItemStackEditPacket::write)
				.decoder(ItemStackEditPacket::new)
				.consumerMainThread(this::serverHandle)
				.add();

	}

	private void clientHandle(S2CRayTracePacket packet, CustomPayloadEvent.Context context) {
		RayTraceHelper.doRayTrace();
	}

	private void serverHandle(C2SBlockEntityEditingRequestPacket packet, CustomPayloadEvent.Context context) {
		var player = context.getSender();
		NetworkEditingHelper.editBlockEntity(player, packet.getPos());
	}

	private void serverHandle(C2SEntityEditingRequestPacket packet, CustomPayloadEvent.Context context) {
		var player = context.getSender();
		NetworkEditingHelper.editEntity(player, packet.getEntityUuid());
	}

	private void serverHandle(C2SItemStackEditingRequestPacket packet, CustomPayloadEvent.Context context) {
		var player = context.getSender();
		NetworkEditingHelper.editItemStack(player, packet.getItemStack());
	}

	private void clientHandle(BlockEntityEditPacket packet, CustomPayloadEvent.Context context) {
		ScreenHelper.showNBTEditScreen(packet.getBlockPos(), packet.getTag(), packet.isReadOnly());
	}

	private void clientHandle(EntityEditPacket packet, CustomPayloadEvent.Context context) {
		ScreenHelper.showNBTEditScreen(packet.getUuid(), packet.getEntityId(), packet.getTag(), packet.isSelf(), packet.isReadOnly());
	}

	private void clientHandle(ItemStackEditPacket packet, CustomPayloadEvent.Context context) {
		ScreenHelper.showNBTEditScreen(packet.getItemStack(), packet.getTag(), packet.isReadOnly());
	}

	private void serverHandle(BlockEntityEditPacket packet, CustomPayloadEvent.Context context) {
		var player = context.getSender();
		NetworkSavingHelper.saveBlockEntity(player, packet.getBlockPos(), packet.getTag());
	}

	private void serverHandle(EntityEditPacket packet, CustomPayloadEvent.Context context) {
		var player = context.getSender();
		NetworkSavingHelper.saveEntity(player, packet.getUuid(), packet.getTag());
	}

	private void serverHandle(ItemStackEditPacket packet, CustomPayloadEvent.Context context) {
		var player = context.getSender();
		NetworkSavingHelper.saveItemStack(player, packet.getItemStack(), packet.getTag());
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
	public void serverOpenClientGui(ServerPlayer player, BlockPos pos, BlockEntity blockEntity, boolean readOnly) {
		player.getServer().execute(() -> {
			var tag = blockEntity.serializeNBT();
			CHANNEL.send(new BlockEntityEditPacket(tag, readOnly, pos),
					player.connection.getConnection());
		});
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, Entity entity, boolean readOnly) {
		player.getServer().execute(() -> {
			var tag = entity.serializeNBT();
			CHANNEL.send(new EntityEditPacket(tag, readOnly, entity.getUUID(), entity.getId(), player == entity),
					player.connection.getConnection());
		});
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, ItemStack stack, boolean readOnly) {
		player.getServer().execute(() -> {
			var tag = stack.serializeNBT();
			CHANNEL.send(new ItemStackEditPacket(tag, readOnly, stack),
					player.connection.getConnection());
		});
	}

	@Override
	public void saveEditing(BlockPos pos, CompoundTag tag) {
		CHANNEL.send(new BlockEntityEditPacket(tag, false, pos), PacketDistributor.SERVER.noArg());
	}

	@Override
	public void saveEditing(Entity entity, CompoundTag tag, boolean self) {
		CHANNEL.send(new EntityEditPacket(tag, false, entity.getUUID(), entity.getId(), self), PacketDistributor.SERVER.noArg());
	}

	@Override
	public void saveEditing(ItemStack stack, CompoundTag tag) {
		CHANNEL.send(new ItemStackEditPacket(tag, false, stack), PacketDistributor.SERVER.noArg());
	}
}
