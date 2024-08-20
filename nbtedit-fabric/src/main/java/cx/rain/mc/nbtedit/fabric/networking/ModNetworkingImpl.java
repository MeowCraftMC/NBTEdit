package cx.rain.mc.nbtedit.fabric.networking;

import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import cx.rain.mc.nbtedit.networking.NetworkServerHandler;
import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import cx.rain.mc.nbtedit.networking.packet.IModPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.BlockEntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.EntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.ItemStackRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.common.BlockEntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.EntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.ItemStackEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.s2c.RaytracePacket;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Function;

public class ModNetworkingImpl implements IModNetworking {
	private NBTEditNetworkingClient client;

	public static final ModNetworkingImpl.PacketTypeWrapper<RaytracePacket> S2C_RAYTRACE_TYPE =  new ModNetworkingImpl.PacketTypeWrapper<>(NetworkingConstants.RAYTRACE_REQUEST_ID, RaytracePacket::read);
	public static final ModNetworkingImpl.PacketTypeWrapper<BlockEntityEditingPacket> S2C_BLOCK_ENTITY_EDITING_TYPE =  new ModNetworkingImpl.PacketTypeWrapper<>(NetworkingConstants.BLOCK_ENTITY_EDITING_ID, BlockEntityEditingPacket::read);
	public static final ModNetworkingImpl.PacketTypeWrapper<EntityEditingPacket> S2C_ENTITY_EDITING_TYPE =  new ModNetworkingImpl.PacketTypeWrapper<>(NetworkingConstants.ENTITY_EDITING_ID, EntityEditingPacket::read);
	public static final ModNetworkingImpl.PacketTypeWrapper<ItemStackEditingPacket> S2C_ITEM_STACK_EDITING_TYPE =  new ModNetworkingImpl.PacketTypeWrapper<>(NetworkingConstants.ITEM_STACK_EDITING_ID, ItemStackEditingPacket::read);

	public static final PacketTypeWrapper<BlockEntityRaytraceResultPacket> C2S_BLOCK_ENTITY_RAYTRACE_RESULT_TYPE =  new PacketTypeWrapper<>(NetworkingConstants.BLOCK_ENTITY_RAYTRACE_RESULT_ID, BlockEntityRaytraceResultPacket::read);
	public static final PacketTypeWrapper<EntityRaytraceResultPacket> C2S_ENTITY_RAYTRACE_RESULT_TYPE =  new PacketTypeWrapper<>(NetworkingConstants.ENTITY_RAYTRACE_RESULT_ID, EntityRaytraceResultPacket::read);
	public static final PacketTypeWrapper<ItemStackRaytraceResultPacket> C2S_ITEM_STACK_RAYTRACE_RESULT_TYPE =  new PacketTypeWrapper<>(NetworkingConstants.ITEM_STACK_RAYTRACE_RESULT_ID, ItemStackRaytraceResultPacket::read);
	public static final PacketTypeWrapper<BlockEntityEditingPacket> C2S_BLOCK_ENTITY_EDITING_TYPE =  new PacketTypeWrapper<>(NetworkingConstants.BLOCK_ENTITY_EDITING_ID, BlockEntityEditingPacket::read);
	public static final PacketTypeWrapper<EntityEditingPacket> C2S_ENTITY_EDITING_TYPE =  new PacketTypeWrapper<>(NetworkingConstants.ENTITY_EDITING_ID, EntityEditingPacket::read);
	public static final PacketTypeWrapper<ItemStackEditingPacket> C2S_ITEM_STACK_EDITING_TYPE =  new PacketTypeWrapper<>(NetworkingConstants.ITEM_STACK_EDITING_ID, ItemStackEditingPacket::read);

	public ModNetworkingImpl() {
		ServerPlayNetworking.registerGlobalReceiver(C2S_BLOCK_ENTITY_RAYTRACE_RESULT_TYPE.getType(), (packet, player, responseSender) -> serverHandle(packet.packet(), player, responseSender));
		ServerPlayNetworking.registerGlobalReceiver(C2S_ENTITY_RAYTRACE_RESULT_TYPE.getType(), (packet, player, responseSender) -> serverHandle(packet.packet(), player, responseSender));
		ServerPlayNetworking.registerGlobalReceiver(C2S_ITEM_STACK_RAYTRACE_RESULT_TYPE.getType(), (packet, player, responseSender) -> serverHandle(packet.packet(), player, responseSender));
		ServerPlayNetworking.registerGlobalReceiver(C2S_BLOCK_ENTITY_EDITING_TYPE.getType(), (packet, player, responseSender) -> serverHandle(packet.packet(), player, responseSender));
		ServerPlayNetworking.registerGlobalReceiver(C2S_ENTITY_EDITING_TYPE.getType(), (packet, player, responseSender) -> serverHandle(packet.packet(), player, responseSender));
		ServerPlayNetworking.registerGlobalReceiver(C2S_ITEM_STACK_EDITING_TYPE.getType(), (packet, player, responseSender) -> serverHandle(packet.packet(), player, responseSender));
	}

	public void addClient() {
		client = new NBTEditNetworkingClient();
	}

	private void serverHandle(BlockEntityRaytraceResultPacket packet, ServerPlayer player, PacketSender responseSender) {
		player.getServer().execute(() -> NetworkServerHandler.handleBlockEntityResult(player, packet));
	}

	private void serverHandle(EntityRaytraceResultPacket packet, ServerPlayer player, PacketSender responseSender) {
		player.getServer().execute(() -> NetworkServerHandler.handleEntityResult(player, packet));
	}

	private void serverHandle(ItemStackRaytraceResultPacket packet, ServerPlayer player, PacketSender responseSender) {
		player.getServer().execute(() -> NetworkServerHandler.handleItemStackResult(player, packet));
	}

	private void serverHandle(BlockEntityEditingPacket packet, ServerPlayer player, PacketSender responseSender) {
		player.getServer().execute(() -> NetworkServerHandler.saveBlockEntity(player, packet));
	}

	private void serverHandle(EntityEditingPacket packet, ServerPlayer player, PacketSender responseSender) {
		player.getServer().execute(() -> NetworkServerHandler.saveEntity(player, packet));
	}

	private void serverHandle(ItemStackEditingPacket packet, ServerPlayer player, PacketSender responseSender) {
		player.getServer().execute(() -> NetworkServerHandler.saveItemStack(player, packet));
	}

	@Override
	public void sendTo(ServerPlayer player, IModPacket packet) {
		if (packet instanceof RaytracePacket p) {
			ServerPlayNetworking.send(player, new FabricPacketWrapper<>(S2C_RAYTRACE_TYPE, p));
		} else if (packet instanceof BlockEntityEditingPacket p) {
			ServerPlayNetworking.send(player, new FabricPacketWrapper<>(S2C_BLOCK_ENTITY_EDITING_TYPE, p));
		} else if (packet instanceof EntityEditingPacket p) {
			ServerPlayNetworking.send(player, new FabricPacketWrapper<>(S2C_ENTITY_EDITING_TYPE, p));
		} else if (packet instanceof ItemStackEditingPacket p) {
			ServerPlayNetworking.send(player, new FabricPacketWrapper<>(S2C_ITEM_STACK_EDITING_TYPE, p));
		}
	}

	@Override
	public void sendToServer(IModPacket packet) {
		if (client != null) {
			client.send(packet);
		}
	}

	public static class PacketTypeWrapper<T extends IModPacket> {
		private final PacketType<FabricPacketWrapper<T>> type;
		private final Function<FriendlyByteBuf, T> constructor;

		public PacketTypeWrapper(ResourceLocation id, Function<FriendlyByteBuf, T> constructor) {
			this.constructor = constructor;
			type = PacketType.create(id, this::read);
		}

		private FabricPacketWrapper<T> read(FriendlyByteBuf buf) {
			return new FabricPacketWrapper<>(this, constructor.apply(buf));
		}

		public PacketType<FabricPacketWrapper<T>> getType() {
			return type;
		}
	}

	public record FabricPacketWrapper<T extends IModPacket>(PacketTypeWrapper<T> typeWrapper, T packet) implements FabricPacket {
		@Override
		public void write(FriendlyByteBuf buf) {
			packet.write(buf);
		}

		@Override
		public PacketType<?> getType() {
			return typeWrapper.type;
		}
	}
}
