package cx.rain.mc.nbtedit.fabric.networking;

import cx.rain.mc.nbtedit.fabric.networking.packet.*;
import cx.rain.mc.nbtedit.fabric.networking.packet.c2s.C2SBlockEntityEditingRequestPacket;
import cx.rain.mc.nbtedit.fabric.networking.packet.c2s.C2SEntityEditingRequestPacket;
import cx.rain.mc.nbtedit.fabric.networking.packet.c2s.C2SItemStackEditingRequestPacket;
import cx.rain.mc.nbtedit.fabric.networking.packet.s2c.S2CRayTracePacket;
import cx.rain.mc.nbtedit.networking.NetworkEditingHelper;
import cx.rain.mc.nbtedit.networking.NetworkSavingHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
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
		ServerPlayNetworking.registerGlobalReceiver(C2SBlockEntityEditingRequestPacket.PACKET_TYPE, this::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(C2SEntityEditingRequestPacket.PACKET_TYPE, this::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(C2SItemStackEditingRequestPacket.PACKET_TYPE, this::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(BlockEntityEditPacket.PACKET_TYPE, this::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(EntityEditPacket.PACKET_TYPE, this::serverHandle);
		ServerPlayNetworking.registerGlobalReceiver(ItemStackEditPacket.PACKET_TYPE, this::serverHandle);
	}

	private void serverHandle(C2SBlockEntityEditingRequestPacket packet, ServerPlayer player, PacketSender sender) {
		NetworkEditingHelper.editBlockEntity(player, packet.getPos());
	}

	private void serverHandle(C2SEntityEditingRequestPacket packet, ServerPlayer player, PacketSender sender) {
		NetworkEditingHelper.editEntity(player, packet.getEntityUuid());
	}

	private void serverHandle(C2SItemStackEditingRequestPacket packet, ServerPlayer player, PacketSender sender) {
		NetworkEditingHelper.editItemStack(player, packet.getItemStack());
	}

	private void serverHandle(BlockEntityEditPacket packet, ServerPlayer player, PacketSender sender) {
		NetworkSavingHelper.saveBlockEntity(player, packet.getBlockPos(), packet.getTag());
	}

	private void serverHandle(EntityEditPacket packet, ServerPlayer player, PacketSender sender) {
		NetworkSavingHelper.saveEntity(player, packet.getUuid(), packet.getTag());
	}

	private void serverHandle(ItemStackEditPacket packet, ServerPlayer player, PacketSender sender) {
		NetworkSavingHelper.saveItemStack(player, packet.getItemStack(), packet.getTag());
	}

	public void serverRayTraceRequest(ServerPlayer player) {
		ServerPlayNetworking.send(player, new S2CRayTracePacket());
	}

	public void serverOpenClientGui(ServerPlayer player, Entity entity, boolean readOnly) {
		player.getServer().execute(() -> {
			var tag = new CompoundTag();
			if (entity instanceof Player) {
				entity.saveWithoutId(tag);
			} else {
				entity.save(tag);
			}
			ServerPlayNetworking.send(player, new EntityEditPacket(tag, readOnly, entity.getUUID(), entity.getId(), player == entity));
		});
	}

	public void serverOpenClientGui(ServerPlayer player, BlockPos pos, BlockEntity blockEntity, boolean readOnly) {
		player.getServer().execute(() -> {
			var tag = blockEntity.saveWithFullMetadata();
			ServerPlayNetworking.send(player, new BlockEntityEditPacket(tag, readOnly, pos));
		});
	}

	public void serverOpenClientGui(ServerPlayer player, ItemStack stack, boolean readOnly) {
		player.getServer().execute(() -> {
			var tag = new CompoundTag();
			stack.save(tag);
			ServerPlayNetworking.send(player, new ItemStackEditPacket(tag, readOnly, stack));
		});
	}
}
