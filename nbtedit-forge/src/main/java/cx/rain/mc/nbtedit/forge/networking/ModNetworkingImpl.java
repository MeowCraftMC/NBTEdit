package cx.rain.mc.nbtedit.forge.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import cx.rain.mc.nbtedit.networking.NetworkClientHandler;
import cx.rain.mc.nbtedit.networking.NetworkServerHandler;
import cx.rain.mc.nbtedit.networking.packet.c2s.BlockEntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.EntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.ItemStackRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.common.BlockEntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.EntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.ItemStackEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.s2c.RaytracePacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.*;

public class ModNetworkingImpl implements IModNetworking {
	private static final ResourceLocation CHANNEL_ID = ResourceLocation.fromNamespaceAndPath(NBTEdit.MODID, "editing");

	private final SimpleChannel channel;

	public ModNetworkingImpl() {
		channel = ChannelBuilder.named(CHANNEL_ID)
				.networkProtocolVersion(NBTEdit.VERSION.hashCode())
				.optionalClient()
				.optionalServer()
				.simpleChannel();

		registerMessages();
	}

	private void registerMessages() {
		channel.play()
				.clientbound()
				.add(RaytracePacket.class, RaytracePacket.CODEC, this::clientHandle);

		channel.play()
				.serverbound()
				.add(BlockEntityRaytraceResultPacket.class, BlockEntityRaytraceResultPacket.CODEC, this::serverHandle)
				.add(EntityRaytraceResultPacket.class, EntityRaytraceResultPacket.CODEC, this::serverHandle)
				.add(ItemStackRaytraceResultPacket.class, ItemStackRaytraceResultPacket.CODEC, this::serverHandle);

		channel.play()
				.bidirectional()
				.add(BlockEntityEditingPacket.class, BlockEntityEditingPacket.CODEC, this::handle)
				.add(EntityEditingPacket.class, EntityEditingPacket.CODEC, this::handle)
				.add(ItemStackEditingPacket.class, ItemStackEditingPacket.CODEC, this::handle);
	}

	private void clientHandle(RaytracePacket packet, CustomPayloadEvent.Context context) {
		context.enqueueWork(() -> NetworkClientHandler.handleRaytrace(packet));
	}

	private void serverHandle(BlockEntityRaytraceResultPacket packet, CustomPayloadEvent.Context context) {
		context.enqueueWork(() -> NetworkServerHandler.handleBlockEntityResult(context.getSender(), packet));
	}

	private void serverHandle(EntityRaytraceResultPacket packet, CustomPayloadEvent.Context context) {
		context.enqueueWork(() -> NetworkServerHandler.handleEntityResult(context.getSender(), packet));
	}

	private void serverHandle(ItemStackRaytraceResultPacket packet, CustomPayloadEvent.Context context) {
		context.enqueueWork(() -> NetworkServerHandler.handleItemStackResult(context.getSender(), packet));
	}

	private void handle(BlockEntityEditingPacket packet, CustomPayloadEvent.Context context) {
		if (context.isClientSide()) {
			context.enqueueWork(() -> NetworkClientHandler.handleBlockEntityEditing(packet));
		} else {
			context.enqueueWork(() -> NetworkServerHandler.saveBlockEntity(context.getSender(), packet));
		}
	}

	private void handle(EntityEditingPacket packet, CustomPayloadEvent.Context context) {
		if (context.isClientSide()) {
			context.enqueueWork(() -> NetworkClientHandler.handleEntityEditing(packet));
		} else {
			context.enqueueWork(() -> NetworkServerHandler.saveEntity(context.getSender(), packet));
		}
	}

	private void handle(ItemStackEditingPacket packet, CustomPayloadEvent.Context context) {
		if (context.isClientSide()) {
			context.enqueueWork(() -> NetworkClientHandler.handleItemStackEditing(packet));
		} else {
			context.enqueueWork(() -> NetworkServerHandler.saveItemStack(context.getSender(), packet));
		}
	}

	@Override
	public void sendTo(ServerPlayer player, CustomPacketPayload packet) {
		channel.send(packet, player.connection.getConnection());
	}

	@Override
	public void sendToServer(CustomPacketPayload packet) {
		channel.send(packet, PacketDistributor.SERVER.noArg());
	}
}
