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
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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
		ServerPlayNetworking.send(player, NBTEditNetworkingImpl.S2C_RAY_TRACE_REQUEST_PACKET_ID, new S2CRayTracePacket().write());
	}

	public void serverOpenClientGui(ServerPlayer player, Entity entity) {
		if (NBTEdit.getInstance().getPermission().hasPermission(player)) {
			if (entity instanceof Player
					&& !NBTEdit.getInstance().getConfig().canEditOthers()) {
				NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
						" tried to use /nbtedit on a player. But config is not allow that.");
				player.createCommandSourceStack().sendFailure(new TranslatableComponent(Constants.MESSAGE_CANNOT_EDIT_OTHER_PLAYER)
						.withStyle(ChatFormatting.RED));
				return;
			}

			NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
					" is editing entity " + entity.getUUID() + ".");
			player.getServer().execute(() -> {
				var tag = new CompoundTag();
				if (entity instanceof Player) {
					entity.saveWithoutId(tag);
				} else {
					entity.save(tag);
				}
				ServerPlayNetworking.send(player, NBTEditNetworkingImpl.S2C_OPEN_ENTITY_EDITING_PACKET_ID, new S2COpenEntityEditingGuiPacket(entity.getUUID(), entity.getId(), tag, false).write());
			});
		} else {
			player.createCommandSourceStack().sendFailure(new TranslatableComponent(Constants.MESSAGE_NO_PERMISSION)
					.withStyle(ChatFormatting.RED));
		}
	}

	public void serverOpenClientGui(ServerPlayer player, BlockPos pos) {
		if (NBTEdit.getInstance().getPermission().hasPermission(player)) {
			NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
					" is editing block at XYZ " + pos.getX() + " " +	pos.getY() + " " + pos.getZ() + ".");
			var blockEntity = player.getLevel().getBlockEntity(pos);
			if (blockEntity != null) {
				var tag = blockEntity.saveWithFullMetadata();
				ServerPlayNetworking.send(player, NBTEditNetworkingImpl.S2C_OPEN_BLOCK_ENTITY_EDITING_PACKET_ID, new S2COpenBlockEntityEditingGuiPacket(pos, tag).write());
			} else {
				player.createCommandSourceStack().sendFailure(new TranslatableComponent(Constants.MESSAGE_TARGET_IS_NOT_BLOCK_ENTITY)
						.withStyle(ChatFormatting.RED));
			}
		} else {
			player.createCommandSourceStack().sendFailure(new TranslatableComponent(Constants.MESSAGE_NO_PERMISSION)
					.withStyle(ChatFormatting.RED));
		}
	}

	public void serverOpenClientGui(ServerPlayer player) {
		if (NBTEdit.getInstance().getPermission().hasPermission(player)) {
			NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() + " is editing itself.");
			player.getServer().execute(() -> {
				var tag = new CompoundTag();
				player.saveWithoutId(tag);
				ServerPlayNetworking.send(player, NBTEditNetworkingImpl.S2C_OPEN_ENTITY_EDITING_PACKET_ID, new S2COpenEntityEditingGuiPacket(player.getUUID(), player.getId(), tag, true).write());
			});
		} else {
			player.createCommandSourceStack().sendFailure(new TranslatableComponent(Constants.MESSAGE_NO_PERMISSION)
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
				ServerPlayNetworking.send(player, NBTEditNetworkingImpl.S2C_OPEN_ITEM_STACK_EDITING_PACKET_ID, new S2COpenItemStackEditingGuiPacket(stack, tag).write());
			});
		} else {
			player.createCommandSourceStack().sendFailure(new TranslatableComponent(Constants.MESSAGE_NO_PERMISSION)
					.withStyle(ChatFormatting.RED));
		}
	}
}
