package cx.rain.mc.nbtedit.fabric.networking;

import cx.rain.mc.nbtedit.fabric.networking.packet.*;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class NBTEditNetworkingClient {
	public NBTEditNetworkingClient() {
		ServerPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.C2S_ENTITY_EDITING_PACKET_ID, C2SEntityEditingRequestPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.C2S_ENTITY_SAVING_PACKET_ID, C2SEntitySavingPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.C2S_BLOCK_ENTITY_EDITING_PACKET_ID, C2SBlockEntityEditingRequestPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.C2S_BLOCK_ENTITY_SAVING_PACKET_ID, C2SBlockEntitySavingPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.C2S_ITEM_STACK_EDITING_PACKET_ID, C2SItemStackEditingRequestPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.C2S_ITEM_STACK_SAVING_PACKET_ID, C2SItemStackSavingPacket::serverHandle);
	}

	public void clientOpenGuiRequest(Entity entity, boolean self) {
		var buf = new FriendlyByteBuf(Unpooled.buffer());
		new C2SEntityEditingRequestPacket(entity.getUUID(), entity.getId(), self).write(buf);
		ClientPlayNetworking.send(NBTEditNetworkingImpl.C2S_ENTITY_EDITING_PACKET_ID, buf);
	}

	public void clientOpenGuiRequest(BlockPos pos) {
		var buf = new FriendlyByteBuf(Unpooled.buffer());
		new C2SBlockEntityEditingRequestPacket(pos).write(buf);
		ClientPlayNetworking.send(NBTEditNetworkingImpl.C2S_BLOCK_ENTITY_EDITING_PACKET_ID, buf);
	}

	public void clientOpenGuiRequest(ItemStack stack) {
		var buf = new FriendlyByteBuf(Unpooled.buffer());
		new C2SItemStackEditingRequestPacket(stack).write(buf);
		ClientPlayNetworking.send(NBTEditNetworkingImpl.C2S_ITEM_STACK_EDITING_PACKET_ID, buf);
	}

	public void saveEditing(Entity entity, CompoundTag tag, boolean self) {
		var buf = new FriendlyByteBuf(Unpooled.buffer());
		new C2SEntitySavingPacket(entity.getUUID(), entity.getId(), tag, self).write(buf);
		ClientPlayNetworking.send(NBTEditNetworkingImpl.C2S_ENTITY_SAVING_PACKET_ID, buf);
	}

	public void saveEditing(BlockPos pos, CompoundTag tag) {
		var buf = new FriendlyByteBuf(Unpooled.buffer());
		new C2SBlockEntitySavingPacket(pos, tag).write(buf);
		ClientPlayNetworking.send(NBTEditNetworkingImpl.C2S_BLOCK_ENTITY_SAVING_PACKET_ID, buf);
	}

	public void saveEditing(ItemStack stack, CompoundTag tag) {
		var buf = new FriendlyByteBuf(Unpooled.buffer());
		new C2SItemStackSavingPacket(stack, tag).write(buf);
		ClientPlayNetworking.send(NBTEditNetworkingImpl.C2S_ITEM_STACK_SAVING_PACKET_ID, buf);
	}
}
