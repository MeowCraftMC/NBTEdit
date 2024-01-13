package cx.rain.mc.nbtedit.forge.networking.packet;

import cx.rain.mc.nbtedit.networking.NBTEditEditingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class C2SBlockEntityEditingRequestPacket {
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

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
	}

	public void serverHandleOnMain(CustomPayloadEvent.Context context) {
		ServerPlayer player = context.getSender();
		NBTEditEditingHelper.editBlockEntity(player, pos);
	}
}
