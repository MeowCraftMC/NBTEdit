package cx.rain.mc.nbtedit.fabric.networking;

import cx.rain.mc.nbtedit.networking.NetworkClientHandler;
import cx.rain.mc.nbtedit.networking.packet.IModPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.BlockEntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.EntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.ItemStackRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.common.BlockEntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.EntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.ItemStackEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.s2c.RaytracePacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class NBTEditNetworkingClient {
	public NBTEditNetworkingClient() {
		ClientPlayNetworking.registerGlobalReceiver(ModNetworkingImpl.S2C_RAYTRACE_TYPE.getType(), (wrapper, player, responseSender) -> clientHandle(wrapper.packet(), player, responseSender));
		ClientPlayNetworking.registerGlobalReceiver(ModNetworkingImpl.S2C_BLOCK_ENTITY_EDITING_TYPE.getType(), (wrapper, player, responseSender) -> clientHandle(wrapper.packet(), player, responseSender));
		ClientPlayNetworking.registerGlobalReceiver(ModNetworkingImpl.S2C_ENTITY_EDITING_TYPE.getType(), (wrapper, player, responseSender) -> clientHandle(wrapper.packet(), player, responseSender));
		ClientPlayNetworking.registerGlobalReceiver(ModNetworkingImpl.S2C_ITEM_STACK_EDITING_TYPE.getType(), (wrapper, player, responseSender) -> clientHandle(wrapper.packet(), player, responseSender));
	}

	private void clientHandle(RaytracePacket packet, LocalPlayer player, PacketSender responseSender) {
		Minecraft.getInstance().execute(() -> NetworkClientHandler.handleRaytrace(packet));
	}

	private void clientHandle(BlockEntityEditingPacket packet, LocalPlayer player, PacketSender responseSender) {
		Minecraft.getInstance().execute(() -> NetworkClientHandler.handleBlockEntityEditing(packet));
	}

	private void clientHandle(EntityEditingPacket packet, LocalPlayer player, PacketSender responseSender) {
		Minecraft.getInstance().execute(() -> NetworkClientHandler.handleEntityEditing(packet));
	}

	private void clientHandle(ItemStackEditingPacket packet, LocalPlayer player, PacketSender responseSender) {
		Minecraft.getInstance().execute(() -> NetworkClientHandler.handleItemStackEditing(packet));
	}

	public void send(IModPacket packet) {
		if (packet instanceof BlockEntityRaytraceResultPacket p) {
			ClientPlayNetworking.send(new ModNetworkingImpl.FabricPacketWrapper<>(ModNetworkingImpl.C2S_BLOCK_ENTITY_RAYTRACE_RESULT_TYPE, p));
		} else if (packet instanceof EntityRaytraceResultPacket p) {
			ClientPlayNetworking.send(new ModNetworkingImpl.FabricPacketWrapper<>(ModNetworkingImpl.C2S_ENTITY_RAYTRACE_RESULT_TYPE, p));
		} else if (packet instanceof ItemStackRaytraceResultPacket p) {
			ClientPlayNetworking.send(new ModNetworkingImpl.FabricPacketWrapper<>(ModNetworkingImpl.C2S_ITEM_STACK_RAYTRACE_RESULT_TYPE, p));
		} else if (packet instanceof BlockEntityEditingPacket p) {
			ClientPlayNetworking.send(new ModNetworkingImpl.FabricPacketWrapper<>(ModNetworkingImpl.C2S_BLOCK_ENTITY_EDITING_TYPE, p));
		} else if (packet instanceof EntityEditingPacket p) {
			ClientPlayNetworking.send(new ModNetworkingImpl.FabricPacketWrapper<>(ModNetworkingImpl.C2S_ENTITY_EDITING_TYPE, p));
		} else if (packet instanceof ItemStackEditingPacket p) {
			ClientPlayNetworking.send(new ModNetworkingImpl.FabricPacketWrapper<>(ModNetworkingImpl.C2S_ITEM_STACK_EDITING_TYPE, p));
		}
	}
}
