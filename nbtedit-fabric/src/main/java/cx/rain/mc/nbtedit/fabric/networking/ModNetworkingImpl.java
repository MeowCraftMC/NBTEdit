package cx.rain.mc.nbtedit.fabric.networking;

import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import cx.rain.mc.nbtedit.networking.NetworkEditingHelper;
import cx.rain.mc.nbtedit.networking.NetworkServerHandler;
import cx.rain.mc.nbtedit.networking.packet.c2s.BlockEntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.EntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.ItemStackRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.common.BlockEntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.EntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.ItemStackEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.s2c.RaytracePacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class ModNetworkingImpl implements IModNetworking {
	private NBTEditNetworkingClient client;

	public ModNetworkingImpl() {
		PayloadTypeRegistry.playS2C().register(RaytracePacket.TYPE, RaytracePacket.CODEC);
		PayloadTypeRegistry.playS2C().register(BlockEntityEditingPacket.TYPE, BlockEntityEditingPacket.CODEC);
		PayloadTypeRegistry.playS2C().register(EntityEditingPacket.TYPE, EntityEditingPacket.CODEC);
		PayloadTypeRegistry.playS2C().register(ItemStackEditingPacket.TYPE, ItemStackEditingPacket.CODEC);

		PayloadTypeRegistry.playC2S().register(BlockEntityRaytraceResultPacket.TYPE, BlockEntityRaytraceResultPacket.CODEC);
		PayloadTypeRegistry.playC2S().register(EntityRaytraceResultPacket.TYPE, EntityRaytraceResultPacket.CODEC);
		PayloadTypeRegistry.playC2S().register(ItemStackRaytraceResultPacket.TYPE, ItemStackRaytraceResultPacket.CODEC);
		PayloadTypeRegistry.playC2S().register(BlockEntityEditingPacket.TYPE, BlockEntityEditingPacket.CODEC);
		PayloadTypeRegistry.playC2S().register(EntityEditingPacket.TYPE, EntityEditingPacket.CODEC);
		PayloadTypeRegistry.playC2S().register(ItemStackEditingPacket.TYPE, ItemStackEditingPacket.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(BlockEntityRaytraceResultPacket.TYPE, this::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(EntityRaytraceResultPacket.TYPE, this::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(ItemStackRaytraceResultPacket.TYPE, this::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(BlockEntityEditingPacket.TYPE, this::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(EntityEditingPacket.TYPE, this::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(ItemStackEditingPacket.TYPE, this::serverHandle);
	}

	public void addClient() {
		client = new NBTEditNetworkingClient();
	}

	private void serverHandle(BlockEntityRaytraceResultPacket packet, ServerPlayNetworking.Context context) {
		context.player().getServer().execute(() -> NetworkServerHandler.handleBlockEntityResult(context.player(), packet));
	}

	private void serverHandle(EntityRaytraceResultPacket packet, ServerPlayNetworking.Context context) {
		context.player().getServer().execute(() -> NetworkServerHandler.handleEntityResult(context.player(), packet));
	}

	private void serverHandle(ItemStackRaytraceResultPacket packet, ServerPlayNetworking.Context context) {
		context.player().getServer().execute(() -> NetworkServerHandler.handleItemStackResult(context.player(), packet));
	}

	private void serverHandle(BlockEntityEditingPacket packet, ServerPlayNetworking.Context context) {
		context.player().getServer().execute(() -> NetworkServerHandler.saveBlockEntity(context.player(), packet));
	}

	private void serverHandle(EntityEditingPacket packet, ServerPlayNetworking.Context context) {
		context.player().getServer().execute(() -> NetworkServerHandler.saveEntity(context.player(), packet));
	}

	private void serverHandle(ItemStackEditingPacket packet, ServerPlayNetworking.Context context) {
		context.player().getServer().execute(() -> NetworkServerHandler.saveItemStack(context.player(), packet));
	}

	@Override
	public void sendTo(ServerPlayer player, CustomPacketPayload packet) {
		ServerPlayNetworking.send(player, packet);
	}

	@Override
	public void sendToServer(CustomPacketPayload packet) {
		if (client != null) {
			client.send(packet);
		}
	}
}
