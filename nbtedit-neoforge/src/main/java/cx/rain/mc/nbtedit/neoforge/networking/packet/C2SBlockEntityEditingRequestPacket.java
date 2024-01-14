package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.networking.NBTEditEditingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class C2SBlockEntityEditingRequestPacket implements CustomPacketPayload {
	/**
	 * The position of the BlockEntity which was requested.
	 */
	private BlockPos pos;

	public C2SBlockEntityEditingRequestPacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
	}

	public C2SBlockEntityEditingRequestPacket(BlockPos posIn) {
		pos = posIn;
	}

	public static void handle(C2SBlockEntityEditingRequestPacket packet, PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
			var optional = context.player();
			if (optional.isPresent()) {
				var player = optional.get();
				if (player instanceof ServerPlayer serverPlayer) {
					NBTEditEditingHelper.editBlockEntity(serverPlayer, packet.pos);
				}
			}
		});
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
	}

	@Override
	public @NotNull ResourceLocation id() {
		return NBTEditNetworkingImpl.C2S_BLOCK_ENTITY_EDITING_PACKET_ID;
	}
}
