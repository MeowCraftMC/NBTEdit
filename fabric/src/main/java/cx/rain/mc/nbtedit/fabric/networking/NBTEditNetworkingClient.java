package cx.rain.mc.nbtedit.fabric.networking;

import cx.rain.mc.nbtedit.fabric.networking.packet.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class NBTEditNetworkingClient {
	public NBTEditNetworkingClient() {
		ClientPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.S2C_RAY_TRACE_REQUEST_PACKET_ID, S2CRayTracePacket::clientHandle);

		ClientPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.S2C_OPEN_BLOCK_ENTITY_EDITING_PACKET_ID, S2COpenBlockEntityEditingGuiPacket::clientHandle);
		ClientPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.S2C_OPEN_ENTITY_EDITING_PACKET_ID, S2COpenEntityEditingGuiPacket::clientHandle);
		ClientPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.S2C_OPEN_ITEM_STACK_EDITING_PACKET_ID, S2COpenItemStackEditingGuiPacket::clientHandle);
	}

	public void clientOpenGuiRequest(Entity entity, boolean self) {
		ClientPlayNetworking.send(NBTEditNetworkingImpl.C2S_ENTITY_EDITING_PACKET_ID, new C2SEntityEditingRequestPacket(entity.getUUID(), entity.getId(), self).write());
	}

	public void clientOpenGuiRequest(BlockPos pos) {
		ClientPlayNetworking.send(NBTEditNetworkingImpl.C2S_BLOCK_ENTITY_EDITING_PACKET_ID, new C2SBlockEntityEditingRequestPacket(pos).write());
	}

	public void clientOpenGuiRequest(ItemStack stack) {
		ClientPlayNetworking.send(NBTEditNetworkingImpl.C2S_ITEM_STACK_EDITING_PACKET_ID, new C2SItemStackEditingRequestPacket(stack).write());
	}

	public void saveEditing(Entity entity, CompoundTag tag, boolean self) {
		ClientPlayNetworking.send(NBTEditNetworkingImpl.C2S_ENTITY_SAVING_PACKET_ID, new C2SEntitySavingPacket(entity.getUUID(), entity.getId(), tag, self).write());
	}

	public void saveEditing(BlockPos pos, CompoundTag tag) {
		ClientPlayNetworking.send(NBTEditNetworkingImpl.C2S_BLOCK_ENTITY_SAVING_PACKET_ID, new C2SBlockEntitySavingPacket(pos, tag).write());
	}

	public void saveEditing(ItemStack stack, CompoundTag tag) {
		ClientPlayNetworking.send(NBTEditNetworkingImpl.C2S_ITEM_STACK_SAVING_PACKET_ID, new C2SItemStackSavingPacket(stack, tag).write());
	}
}
