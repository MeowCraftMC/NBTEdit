package cx.rain.mc.nbtedit.neoforge.networking;

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
import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber(modid = NBTEdit.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetworkingImpl implements IModNetworking {

	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event) {
		var registrar = event.registrar(NBTEdit.VERSION);

		registrar.playToServer(RaytracePacket.TYPE, RaytracePacket.CODEC, ModNetworkingImpl::clientHandle);

		registrar.playToClient(BlockEntityRaytraceResultPacket.TYPE, BlockEntityRaytraceResultPacket.CODEC, ModNetworkingImpl::serverHandle);
		registrar.playToClient(EntityRaytraceResultPacket.TYPE, EntityRaytraceResultPacket.CODEC, ModNetworkingImpl::serverHandle);
		registrar.playToClient(ItemStackRaytraceResultPacket.TYPE, ItemStackRaytraceResultPacket.CODEC, ModNetworkingImpl::serverHandle);

		registrar.playBidirectional(BlockEntityEditingPacket.TYPE, BlockEntityEditingPacket.CODEC, ModNetworkingImpl::handle);
		registrar.playBidirectional(EntityEditingPacket.TYPE, EntityEditingPacket.CODEC, ModNetworkingImpl::handle);
		registrar.playBidirectional(ItemStackEditingPacket.TYPE, ItemStackEditingPacket.CODEC, ModNetworkingImpl::handle);
	}

	private static void clientHandle(RaytracePacket packet, IPayloadContext context) {
		context.enqueueWork(() -> NetworkClientHandler.handleRaytrace(packet));
	}

	private static void serverHandle(BlockEntityRaytraceResultPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			var player = context.player();
			if (player instanceof ServerPlayer serverPlayer) {
				NetworkServerHandler.handleBlockEntityResult(serverPlayer, packet);
			}
		});
	}

	private static void serverHandle(EntityRaytraceResultPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			var player = context.player();
			if (player instanceof ServerPlayer serverPlayer) {
				NetworkServerHandler.handleEntityResult(serverPlayer, packet);
			}
		});
	}

	private static void serverHandle(ItemStackRaytraceResultPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			var player = context.player();
			if (player instanceof ServerPlayer serverPlayer) {
				NetworkServerHandler.handleItemStackResult(serverPlayer, packet);
			}
		});
	}

	private static void handle(BlockEntityEditingPacket packet, IPayloadContext context) {
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> NetworkClientHandler.handleBlockEntityEditing(packet));
			return;
		}

		if (context.flow().isServerbound()) {
			context.enqueueWork(() -> {
				var player = context.player();
				if (player instanceof ServerPlayer serverPlayer) {
					NetworkServerHandler.saveBlockEntity(serverPlayer, packet);
				}
			});
		}
	}

	private static void handle(EntityEditingPacket packet, IPayloadContext context) {
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> NetworkClientHandler.handleEntityEditing(packet));
			return;
		}

		if (context.flow().isServerbound()) {
			context.enqueueWork(() -> {
				var player = context.player();
				if (player instanceof ServerPlayer serverPlayer) {
					NetworkServerHandler.saveEntity(serverPlayer, packet);
				}
			});
		}
	}

	private static void handle(ItemStackEditingPacket packet, IPayloadContext context) {
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> NetworkClientHandler.handleItemStackEditing(packet));
			return;
		}

		if (context.flow().isServerbound()) {
			context.enqueueWork(() -> {
				var player = context.player();
				if (player instanceof ServerPlayer serverPlayer) {
					NetworkServerHandler.saveItemStack(serverPlayer, packet);
				}
			});
		}
	}

	public ModNetworkingImpl() {
	}

	@Override
	public void sendTo(ServerPlayer player, CustomPacketPayload packet) {
		player.connection.send(packet);
	}

	@Override
	public void sendToServer(CustomPacketPayload packet) {
		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(packet);
		}
	}
}
