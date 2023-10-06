package cx.rain.mc.nbtedit.fabric.networking;

import cx.rain.mc.nbtedit.fabric.networking.packet.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NBTEditNetworkingServer {

	public NBTEditNetworkingServer() {
		ServerPlayNetworking.registerGlobalReceiver(C2SEntityEditingRequestPacket.PACKET_TYPE, C2SEntityEditingRequestPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(C2SEntitySavingPacket.PACKET_TYPE, C2SEntitySavingPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(C2SBlockEntityEditingRequestPacket.PACKET_TYPE, C2SBlockEntityEditingRequestPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(C2SBlockEntitySavingPacket.PACKET_TYPE, C2SBlockEntitySavingPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(C2SItemStackEditingRequestPacket.PACKET_TYPE, C2SItemStackEditingRequestPacket::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(C2SItemStackSavingPacket.PACKET_TYPE, C2SItemStackSavingPacket::serverHandle);
	}

	public void serverRayTraceRequest(ServerPlayer player) {
		ServerPlayNetworking.send(player, new S2CRayTracePacket());
	}

	public void serverOpenClientGui(ServerPlayer player, Entity entity) {
		player.getServer().execute(() -> {
			var tag = new CompoundTag();
			if (entity instanceof Player) {
				entity.saveWithoutId(tag);
			} else {
				entity.save(tag);
			}
			ServerPlayNetworking.send(player, new S2COpenEntityEditingGuiPacket(entity.getUUID(), entity.getId(), tag, false));
		});
	}

	public void serverOpenClientGui(ServerPlayer player, BlockPos pos, BlockEntity blockEntity) {
		player.getServer().execute(() -> {
			var tag = blockEntity.saveWithFullMetadata();
			ServerPlayNetworking.send(player, new S2COpenBlockEntityEditingGuiPacket(pos, tag));
		});
	}

	public void serverOpenClientGui(ServerPlayer player) {
		player.getServer().execute(() -> {
			var tag = new CompoundTag();
			player.saveWithoutId(tag);
			ServerPlayNetworking.send(player, new S2COpenEntityEditingGuiPacket(player.getUUID(), player.getId(), tag, true));
		});
	}

	public void serverOpenClientGui(ServerPlayer player, ItemStack stack) {
		player.getServer().execute(() -> {
			var tag = new CompoundTag();
			stack.save(tag);
			ServerPlayNetworking.send(player, new S2COpenItemStackEditingGuiPacket(stack, tag));
		});
	}
}
