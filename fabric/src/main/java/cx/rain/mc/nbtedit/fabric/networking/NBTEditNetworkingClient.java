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
		ClientPlayNetworking.send(new C2SEntityEditingRequestPacket(entity.getUUID(), entity.getId(), self));
	}

	public void clientOpenGuiRequest(BlockPos pos) {
		ClientPlayNetworking.send(new C2SBlockEntityEditingRequestPacket(pos));
	}

	public void clientOpenGuiRequest(ItemStack stack) {
		ClientPlayNetworking.send(new C2SItemStackEditingRequestPacket(stack));
	}

	public void saveEditing(Entity entity, CompoundTag tag, boolean self) {
		ClientPlayNetworking.send(new C2SEntitySavingPacket(entity.getUUID(), entity.getId(), tag, self));
	}

	public void saveEditing(BlockPos pos, CompoundTag tag) {
		ClientPlayNetworking.send(new C2SBlockEntitySavingPacket(pos, tag));
	}

	public void saveEditing(ItemStack stack, CompoundTag tag) {
		ClientPlayNetworking.send(new C2SItemStackSavingPacket(stack, tag));
	}
}
