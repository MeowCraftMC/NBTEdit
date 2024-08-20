package cx.rain.mc.nbtedit.forge.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import cx.rain.mc.nbtedit.networking.NetworkClientHandler;
import cx.rain.mc.nbtedit.networking.NetworkServerHandler;
import cx.rain.mc.nbtedit.networking.packet.IModPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.BlockEntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.EntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.ItemStackRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.common.BlockEntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.EntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.ItemStackEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.s2c.RaytracePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ModNetworkingImpl implements IModNetworking {
	private static final ResourceLocation CHANNEL_ID = new ResourceLocation(NBTEdit.MODID, "editing");

	private final SimpleChannel channel;

	public ModNetworkingImpl() {
		channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_ID)
				.networkProtocolVersion(() -> NBTEdit.VERSION)
				.clientAcceptedVersions(version -> true)
				.serverAcceptedVersions(version -> true)
				.simpleChannel();

		registerMessages();
	}

	private int id = 0;

	private int nextId() {
		return id++;
	}

	private void registerMessages() {
		channel.messageBuilder(RaytracePacket.class, nextId()).encoder(RaytracePacket::write).decoder(RaytracePacket::read).consumerNetworkThread(this::clientHandle).add();

		channel.messageBuilder(BlockEntityRaytraceResultPacket.class, nextId()).encoder(BlockEntityRaytraceResultPacket::write).decoder(BlockEntityRaytraceResultPacket::read).consumerNetworkThread(this::serverHandleBlockEntity).add();
		channel.messageBuilder(EntityRaytraceResultPacket.class, nextId()).encoder(EntityRaytraceResultPacket::write).decoder(EntityRaytraceResultPacket::read).consumerNetworkThread(this::serverHandleEntity).add();
		channel.messageBuilder(ItemStackRaytraceResultPacket.class, nextId()).encoder(ItemStackRaytraceResultPacket::write).decoder(ItemStackRaytraceResultPacket::read).consumerNetworkThread(this::serverHandleItemStack).add();

		channel.messageBuilder(BlockEntityEditingPacket.class, nextId()).encoder(BlockEntityEditingPacket::write).decoder(BlockEntityEditingPacket::read).consumerNetworkThread(this::handleBlockEntity).add();
		channel.messageBuilder(EntityEditingPacket.class, nextId()).encoder(EntityEditingPacket::write).decoder(EntityEditingPacket::read).consumerNetworkThread(this::handleEntity).add();
		channel.messageBuilder(ItemStackEditingPacket.class, nextId()).encoder(ItemStackEditingPacket::write).decoder(ItemStackEditingPacket::read).consumerNetworkThread(this::handleItemStack).add();
	}

	private void clientHandle(RaytracePacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> NetworkClientHandler.handleRaytrace(packet));
	}

	private void serverHandleBlockEntity(BlockEntityRaytraceResultPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> NetworkServerHandler.handleBlockEntityResult(context.get().getSender(), packet));
	}

	private void serverHandleEntity(EntityRaytraceResultPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> NetworkServerHandler.handleEntityResult(context.get().getSender(), packet));
	}

	private void serverHandleItemStack(ItemStackRaytraceResultPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> NetworkServerHandler.handleItemStackResult(context.get().getSender(), packet));
	}

	private void handleBlockEntity(BlockEntityEditingPacket packet, Supplier<NetworkEvent.Context> context) {
		if (context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
			context.get().enqueueWork(() -> NetworkClientHandler.handleBlockEntityEditing(packet));
		} else {
			context.get().enqueueWork(() -> NetworkServerHandler.saveBlockEntity(context.get().getSender(), packet));
		}
	}

	private void handleEntity(EntityEditingPacket packet, Supplier<NetworkEvent.Context> context) {
		if (context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
			context.get().enqueueWork(() -> NetworkClientHandler.handleEntityEditing(packet));
		} else {
			context.get().enqueueWork(() -> NetworkServerHandler.saveEntity(context.get().getSender(), packet));
		}
	}

	private void handleItemStack(ItemStackEditingPacket packet, Supplier<NetworkEvent.Context> context) {
		if (context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
			context.get().enqueueWork(() -> NetworkClientHandler.handleItemStackEditing(packet));
		} else {
			context.get().enqueueWork(() -> NetworkServerHandler.saveItemStack(context.get().getSender(), packet));
		}
	}

	@Override
	public void sendTo(ServerPlayer player, IModPacket packet) {
		channel.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}

	@Override
	public void sendToServer(IModPacket packet) {
		channel.sendToServer(packet);
	}
}
