package cx.rain.mc.nbtedit.fabric.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;
import cx.rain.mc.nbtedit.fabric.networking.packet.*;
import cx.rain.mc.nbtedit.utility.Constants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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
		if (NBTEdit.getInstance().getPermission().hasPermission(player)) {
			if (entity instanceof Player && !NBTEdit.getInstance().getConfig().canEditOthers()) {
				NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
						" tried to use /nbtedit on a player. But config is not allow that.");
				player.createCommandSourceStack().sendFailure(Component
						.translatable(Constants.MESSAGE_CANNOT_EDIT_OTHER_PLAYER)
						.withStyle(ChatFormatting.RED));
				return;
			}

			NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
					" is editing entity " + entity.getUUID() + ".");
			player.getServer().execute(() -> {
				var tag = new CompoundTag();
				entity.save(tag);
				ServerPlayNetworking.send(player, new S2COpenEntityEditingGuiPacket(entity.getUUID(), entity.getId(), tag, false));
			});
		} else {
			player.createCommandSourceStack().sendFailure(Component.translatable(Constants.MESSAGE_NO_PERMISSION)
					.withStyle(ChatFormatting.RED));
		}
	}

	public void serverOpenClientGui(ServerPlayer player, BlockPos pos) {
		if (NBTEdit.getInstance().getPermission().hasPermission(player)) {
			NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
					" is editing block at XYZ " + pos.getX() + " " +	pos.getY() + " " + pos.getZ() + ".");
			var blockEntity = player.serverLevel().getBlockEntity(pos);
			if (blockEntity != null) {
				var tag = blockEntity.saveWithFullMetadata();
				ServerPlayNetworking.send(player, new S2COpenBlockEntityEditingGuiPacket(pos, tag));
			} else {
				player.createCommandSourceStack().sendFailure(Component.translatable(Constants.MESSAGE_TARGET_IS_NOT_BLOCK_ENTITY)
						.withStyle(ChatFormatting.RED));
			}
		} else {
			player.createCommandSourceStack().sendFailure(Component.translatable(Constants.MESSAGE_NO_PERMISSION)
					.withStyle(ChatFormatting.RED));
		}
	}

	public void serverOpenClientGui(ServerPlayer player) {
		if (NBTEdit.getInstance().getPermission().hasPermission(player)) {
			NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() + " is editing itself.");
			player.getServer().execute(() -> {
				var tag = new CompoundTag();
				player.saveWithoutId(tag);
				ServerPlayNetworking.send(player, new S2COpenEntityEditingGuiPacket(player.getUUID(), player.getId(), tag, true));
			});
		} else {
			player.createCommandSourceStack().sendFailure(Component.translatable(Constants.MESSAGE_NO_PERMISSION)
					.withStyle(ChatFormatting.RED));
		}
	}

	public void serverOpenClientGui(ServerPlayer player, ItemStack stack) {
		if (NBTEdit.getInstance().getPermission().hasPermission(player)) {
			NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
					" is editing ItemStack named " + stack.getDisplayName().getString() + ".");
			player.getServer().execute(() -> {
				var tag = new CompoundTag();
				stack.save(tag);
				ServerPlayNetworking.send(player, new S2COpenItemStackEditingGuiPacket(stack, tag));
			});
		} else {
			player.createCommandSourceStack().sendFailure(Component.translatable(Constants.MESSAGE_NO_PERMISSION)
					.withStyle(ChatFormatting.RED));
		}
	}
}
