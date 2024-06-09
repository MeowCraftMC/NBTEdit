package cx.rain.mc.nbtedit.neoforge.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import cx.rain.mc.nbtedit.neoforge.networking.packet.*;
import cx.rain.mc.nbtedit.neoforge.networking.packet.c2s.C2SBlockEntityEditingRequestPacket;
import cx.rain.mc.nbtedit.neoforge.networking.packet.c2s.C2SEntityEditingRequestPacket;
import cx.rain.mc.nbtedit.neoforge.networking.packet.c2s.C2SItemStackEditingRequestPacket;
import cx.rain.mc.nbtedit.neoforge.networking.packet.s2c.S2CRayTracePacket;
import cx.rain.mc.nbtedit.networking.NetworkEditingHelper;
import cx.rain.mc.nbtedit.networking.NetworkSavingHelper;
import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

@Mod.EventBusSubscriber(modid = NBTEdit.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModNetworkingImpl implements IModNetworking {

	@SubscribeEvent
	public static void register(RegisterPayloadHandlerEvent event) {
		var registrar = event.registrar(NBTEdit.MODID);
		registrar.play(NetworkingConstants.S2C_RAYTRACE_REQUEST_PACKET_ID, S2CRayTracePacket::new, handler -> handler.client(ModNetworkingImpl::clientHandle));

		registrar.play(NetworkingConstants.C2S_BLOCK_ENTITY_RAYTRACE_RESULT_PACKET_ID, C2SBlockEntityEditingRequestPacket::new, handler -> handler.server(ModNetworkingImpl::serverHandle));
		registrar.play(NetworkingConstants.C2S_ENTITY_RAYTRACE_RESULT_PACKET_ID, C2SEntityEditingRequestPacket::new, handler -> handler.server(ModNetworkingImpl::serverHandle));
		registrar.play(NetworkingConstants.C2S_ITEM_STACK_RAYTRACE_RESULT_PACKET_ID, C2SItemStackEditingRequestPacket::new, handler -> handler.server(ModNetworkingImpl::serverHandle));

		registrar.play(NetworkingConstants.BLOCK_ENTITY_EDITING_PACKET_ID, BlockEntityEditPacket::new, handler -> handler.server(ModNetworkingImpl::serverHandle).client(ModNetworkingImpl::clientHandle));
		registrar.play(NetworkingConstants.ENTITY_EDITING_PACKET_ID, EntityEditPacket::new, handler -> handler.server(ModNetworkingImpl::serverHandle).client(ModNetworkingImpl::clientHandle));
		registrar.play(NetworkingConstants.ITEM_STACK_EDITING_PACKET_ID, ItemStackEditPacket::new, handler -> handler.server(ModNetworkingImpl::serverHandle).client(ModNetworkingImpl::clientHandle));
	}

	private static void clientHandle(S2CRayTracePacket packet, PlayPayloadContext context) {
		context.workHandler().execute(RayTraceHelper::doRayTrace);
	}

	private static void serverHandle(C2SBlockEntityEditingRequestPacket packet, PlayPayloadContext context) {
		context.workHandler().execute(() -> {
			var optional = context.player();
			if (optional.isPresent()) {
				var player = optional.get();
				if (player instanceof ServerPlayer serverPlayer) {
					NetworkEditingHelper.editBlockEntity(serverPlayer, packet.getPos());
				}
			}
		});
	}

	private static void serverHandle(C2SEntityEditingRequestPacket packet, PlayPayloadContext context) {
		context.workHandler().execute(() -> {
			var optional = context.player();
			if (optional.isPresent()) {
				var player = optional.get();
				if (player instanceof ServerPlayer serverPlayer) {
					NetworkEditingHelper.editEntity(serverPlayer, packet.getEntityUuid());
				}
			}
		});
	}

	private static void serverHandle(C2SItemStackEditingRequestPacket packet, PlayPayloadContext context) {
		context.workHandler().execute(() -> {
			var optional = context.player();
			if (optional.isPresent()) {
				var player = optional.get();
				if (player instanceof ServerPlayer serverPlayer) {
					NetworkEditingHelper.editItemStack(serverPlayer, packet.getItemStack());
				}
			}
		});
	}

	private static void clientHandle(BlockEntityEditPacket packet, PlayPayloadContext context) {
		context.workHandler().execute(() -> ScreenHelper.showNBTEditScreen(packet.getBlockPos(), packet.getTag(), packet.isReadOnly()));
	}

	private static void clientHandle(EntityEditPacket packet, PlayPayloadContext context) {
		context.workHandler().execute(() -> ScreenHelper.showNBTEditScreen(packet.getUuid(), packet.getEntityId(), packet.getTag(), packet.isSelf(), packet.isReadOnly()));
	}

	private static void clientHandle(ItemStackEditPacket packet, PlayPayloadContext context) {
		context.workHandler().execute(() -> ScreenHelper.showNBTEditScreen(packet.getItemStack(), packet.getTag(), packet.isReadOnly()));
	}

	private static void serverHandle(BlockEntityEditPacket packet, PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
			var optional = context.player();
			if (optional.isPresent()) {
				var player = optional.get();
				if (player instanceof ServerPlayer serverPlayer) {
					NetworkSavingHelper.saveBlockEntity(serverPlayer, packet.getBlockPos(), packet.getTag());
				}
			}
		});
	}

	private static void serverHandle(EntityEditPacket packet, PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
			var optional = context.player();
			if (optional.isPresent()) {
				var player = optional.get();
				if (player instanceof ServerPlayer serverPlayer) {
					NetworkSavingHelper.saveEntity(serverPlayer, packet.getUuid(), packet.getTag());
				}
			}
		});
	}

	private static void serverHandle(ItemStackEditPacket packet, PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
			var optional = context.player();
			if (optional.isPresent()) {
				var player = optional.get();
				if (player instanceof ServerPlayer serverPlayer) {
					NetworkSavingHelper.saveItemStack(serverPlayer, packet.getItemStack(), packet.getTag());
				}
			}
		});
	}

	public ModNetworkingImpl() {
	}

	@Override
	public void serverRayTraceRequest(ServerPlayer player) {
		player.connection.send(new S2CRayTracePacket());
	}

	@Override
	public void clientOpenGuiRequest(BlockPos pos) {
		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(new C2SBlockEntityEditingRequestPacket(pos));
		}
	}

	@Override
	public void clientOpenGuiRequest(Entity entity, boolean self) {
		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(new C2SEntityEditingRequestPacket(entity.getUUID(), entity.getId(), self));
		}
	}

	@Override
	public void clientOpenGuiRequest(ItemStack stack) {
		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(new C2SItemStackEditingRequestPacket(stack));
		}
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, BlockPos pos, BlockEntity blockEntity, boolean readOnly) {
		var tag = blockEntity.serializeNBT();
		player.connection.send(new BlockEntityEditPacket(tag, readOnly, pos));
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, Entity entity, boolean readOnly) {
		var tag = entity.serializeNBT();
		player.connection.send(new EntityEditPacket(tag, readOnly, player.getUUID(), player.getId(), player == entity));
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, ItemStack stack, boolean readOnly) {
		var tag = stack.save(new CompoundTag());
		player.connection.send(new ItemStackEditPacket(tag, readOnly, stack));
	}

	@Override
	public void saveEditing(BlockPos pos, CompoundTag tag) {
		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(new BlockEntityEditPacket(tag, false, pos));
		}
	}

	@Override
	public void saveEditing(Entity entity, CompoundTag tag, boolean self) {
		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(new EntityEditPacket(tag, false, entity.getUUID(), entity.getId(), self));
		}
	}

	@Override
	public void saveEditing(ItemStack stack, CompoundTag tag) {
		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(new ItemStackEditPacket(tag, false, stack));
		}
	}
}
