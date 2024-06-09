package cx.rain.mc.nbtedit.fabric.networking;

import cx.rain.mc.nbtedit.fabric.networking.packet.*;
import cx.rain.mc.nbtedit.fabric.networking.packet.c2s.C2SBlockEntityEditingRequestPacket;
import cx.rain.mc.nbtedit.fabric.networking.packet.c2s.C2SEntityEditingRequestPacket;
import cx.rain.mc.nbtedit.fabric.networking.packet.c2s.C2SItemStackEditingRequestPacket;
import cx.rain.mc.nbtedit.fabric.networking.packet.s2c.S2CRayTracePacket;
import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class NBTEditNetworkingClient {
	public NBTEditNetworkingClient() {
		ClientPlayNetworking.registerGlobalReceiver(S2CRayTracePacket.PACKET_TYPE, this::clientHandle);

		ClientPlayNetworking.registerGlobalReceiver(BlockEntityEditPacket.PACKET_TYPE, this::clientHandle);
		ClientPlayNetworking.registerGlobalReceiver(EntityEditPacket.PACKET_TYPE, this::clientHandle);
		ClientPlayNetworking.registerGlobalReceiver(ItemStackEditPacket.PACKET_TYPE, this::clientHandle);
	}

	private void clientHandle(S2CRayTracePacket packet, LocalPlayer player, PacketSender sender) {
		RayTraceHelper.doRayTrace();
	}

	private void clientHandle(BlockEntityEditPacket packet, LocalPlayer player, PacketSender sender) {
		ScreenHelper.showNBTEditScreen(packet.getBlockPos(), packet.getTag(), packet.isReadOnly());
	}

	private void clientHandle(EntityEditPacket packet, LocalPlayer player, PacketSender sender) {
		ScreenHelper.showNBTEditScreen(packet.getUuid(), packet.getEntityId(), packet.getTag(), packet.isSelf(), packet.isReadOnly());
	}

	private void clientHandle(ItemStackEditPacket packet, LocalPlayer player, PacketSender sender) {
		ScreenHelper.showNBTEditScreen(packet.getItemStack(), packet.getTag(), packet.isReadOnly());
	}

	public void clientOpenGuiRequest(Entity entity, boolean self) {
		ClientPlayNetworking.send(new C2SEntityEditingRequestPacket(entity.getUUID(), entity.getId(), self));
	}

	public void clientOpenGuiRequest(BlockPos pos) {
		ClientPlayNetworking.send(new C2SBlockEntityEditingRequestPacket(pos));
	}

	public void clientOpenGuiRequest(ItemStack stack) {
		ClientPlayNetworking.send(new C2SItemStackEditingRequestPacket(stack));
	}

	public void saveEditing(Entity entity, CompoundTag tag, boolean self) {
		ClientPlayNetworking.send(new EntityEditPacket(tag, false, entity.getUUID(), entity.getId(), self));
	}

	public void saveEditing(BlockPos pos, CompoundTag tag) {
		ClientPlayNetworking.send(new BlockEntityEditPacket(tag, false, pos));
	}

	public void saveEditing(ItemStack stack, CompoundTag tag) {
		ClientPlayNetworking.send(new ItemStackEditPacket(tag, false, stack));
	}
}
