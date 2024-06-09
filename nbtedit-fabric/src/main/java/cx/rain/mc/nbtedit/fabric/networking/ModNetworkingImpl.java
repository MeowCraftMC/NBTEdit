package cx.rain.mc.nbtedit.fabric.networking;

import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ModNetworkingImpl implements IModNetworking {
	private final NBTEditNetworkingServer server;
	private NBTEditNetworkingClient client;

	public ModNetworkingImpl() {
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
	public void serverOpenClientGui(ServerPlayer player, Entity entity, boolean readOnly) {
		server.serverOpenClientGui(player, entity, readOnly);
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, BlockPos pos, BlockEntity blockEntity, boolean readOnly) {
		server.serverOpenClientGui(player, pos, blockEntity, readOnly);
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, ItemStack stack, boolean readOnly) {
		server.serverOpenClientGui(player, stack, readOnly);
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
