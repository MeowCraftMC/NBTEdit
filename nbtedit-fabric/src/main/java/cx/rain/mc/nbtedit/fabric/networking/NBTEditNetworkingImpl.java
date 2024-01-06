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
import net.minecraft.world.level.block.entity.BlockEntity;

public class NBTEditNetworkingImpl implements INBTEditNetworking {
	public static final ResourceLocation S2C_RAY_TRACE_REQUEST_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "s2c_ray_trace_request");
	public static final ResourceLocation C2S_ENTITY_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_entity_editing_request");
	public static final ResourceLocation C2S_ENTITY_SAVING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_entity_saving_request");
	public static final ResourceLocation C2S_BLOCK_ENTITY_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_block_entity_editing_request");
	public static final ResourceLocation C2S_BLOCK_ENTITY_SAVING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_block_entity_saving_request");
	public static final ResourceLocation C2S_ITEM_STACK_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_item_stack_editing_request");
	public static final ResourceLocation C2S_ITEM_STACK_SAVING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_item_stack_saving_request");
	public static final ResourceLocation S2C_OPEN_BLOCK_ENTITY_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "s2c_open_block_entity_editing");
	public static final ResourceLocation S2C_OPEN_ENTITY_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "s2c_open_entity_editing");
	public static final ResourceLocation S2C_OPEN_ITEM_STACK_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "s2c_open_item_stack_editing");

	private NBTEditNetworkingClient client;
	private NBTEditNetworkingServer server;

	public NBTEditNetworkingImpl() {
		server = new NBTEditNetworkingServer();
	}

	public void addClient() {
		client = new NBTEditNetworkingClient();
	}

	@Override
	public void serverRayTraceRequest(ServerPlayer player) {
		server.serverRayTraceRequest(player);
	}

	@Override
	public void clientOpenGuiRequest(Entity entity, boolean self) {
		if (client != null) {
			client.clientOpenGuiRequest(entity, self);
		}
	}

	@Override
	public void clientOpenGuiRequest(BlockPos pos) {
		if (client != null) {
			client.clientOpenGuiRequest(pos);
		}
	}

	@Override
	public void clientOpenGuiRequest(ItemStack stack) {
		if (client != null) {
			client.clientOpenGuiRequest(stack);
		}
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, Entity entity) {
		server.serverOpenClientGui(player, entity);
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, BlockPos pos, BlockEntity blockEntity) {
		server.serverOpenClientGui(player, pos, blockEntity);
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player) {
		server.serverOpenClientGui(player);
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, ItemStack stack) {
		server.serverOpenClientGui(player, stack);
	}

	@Override
	public void saveEditing(Entity entity, CompoundTag tag, boolean self) {
		if (client != null) {
			client.saveEditing(entity, tag, self);
		}
	}

	@Override
	public void saveEditing(BlockPos pos, CompoundTag tag) {
		if (client != null) {
			client.saveEditing(pos, tag);
		}
	}

	@Override
	public void saveEditing(ItemStack stack, CompoundTag tag) {
		if (client != null) {
			client.saveEditing(stack, tag);
		}
	}
}
