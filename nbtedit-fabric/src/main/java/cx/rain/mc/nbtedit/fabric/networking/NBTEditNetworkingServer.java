package cx.rain.mc.nbtedit.fabric.networking;

import cx.rain.mc.nbtedit.fabric.networking.packet.*;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NBTEditNetworkingServer {

	public NBTEditNetworkingServer() {
		ServerPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.C2S_ENTITY_EDITING_PACKET_ID, C2SEntityEditingRequestPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.C2S_ENTITY_SAVING_PACKET_ID, C2SEntitySavingPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.C2S_BLOCK_ENTITY_EDITING_PACKET_ID, C2SBlockEntityEditingRequestPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.C2S_BLOCK_ENTITY_SAVING_PACKET_ID, C2SBlockEntitySavingPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.C2S_ITEM_STACK_EDITING_PACKET_ID, C2SItemStackEditingRequestPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(NBTEditNetworkingImpl.C2S_ITEM_STACK_SAVING_PACKET_ID, C2SItemStackSavingPacket::serverHandle);
	}

	public void serverRayTraceRequest(ServerPlayer player) {
		var buf = new FriendlyByteBuf(Unpooled.buffer());
		new S2CRayTracePacket().write(buf);
		ServerPlayNetworking.send(player, NBTEditNetworkingImpl.S2C_RAY_TRACE_REQUEST_PACKET_ID, buf);
	}

	public void serverOpenClientGui(ServerPlayer player, Entity entity) {
		player.getServer().execute(() -> {
			var buf = new FriendlyByteBuf(Unpooled.buffer());
			var tag = new CompoundTag();
			if (entity instanceof Player) {
				entity.saveWithoutId(tag);
			} else {
				entity.save(tag);
			}
			new S2COpenEntityEditingGuiPacket(entity.getUUID(), entity.getId(), tag, false).write(buf);
			ServerPlayNetworking.send(player, NBTEditNetworkingImpl.S2C_OPEN_ENTITY_EDITING_PACKET_ID, buf);
		});
	}

	public void serverOpenClientGui(ServerPlayer player, BlockPos pos, BlockEntity blockEntity) {
		player.getServer().execute(() -> {
			var buf = new FriendlyByteBuf(Unpooled.buffer());
			var tag = blockEntity.save(new CompoundTag());
			new S2COpenBlockEntityEditingGuiPacket(pos, tag).write(buf);
			ServerPlayNetworking.send(player, NBTEditNetworkingImpl.S2C_OPEN_BLOCK_ENTITY_EDITING_PACKET_ID, buf);
		});
	}

	public void serverOpenClientGui(ServerPlayer player) {
		player.getServer().execute(() -> {
			var buf = new FriendlyByteBuf(Unpooled.buffer());
			var tag = new CompoundTag();
			player.saveWithoutId(tag);
			new S2COpenEntityEditingGuiPacket(player.getUUID(), player.getId(), tag, true).write(buf);
			ServerPlayNetworking.send(player, NBTEditNetworkingImpl.S2C_OPEN_ENTITY_EDITING_PACKET_ID, buf);
		});
	}

	public void serverOpenClientGui(ServerPlayer player, ItemStack stack) {
		player.getServer().execute(() -> {
			var buf = new FriendlyByteBuf(Unpooled.buffer());
			var tag = new CompoundTag();
			stack.save(tag);
			new S2COpenItemStackEditingGuiPacket(stack, tag).write(buf);
			ServerPlayNetworking.send(player, NBTEditNetworkingImpl.S2C_OPEN_ITEM_STACK_EDITING_PACKET_ID, buf);
		});
	}
}
