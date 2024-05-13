package cx.rain.mc.nbtedit.forge.networking.packet;

import cx.rain.mc.nbtedit.networking.NBTEditEditingHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class C2SEntityEditingRequestPacket {
	/**
	 * The UUID of the entity being requested.
	 */
	private UUID entityUuid;

	private int entityId;

	private boolean isSelf;

	public C2SEntityEditingRequestPacket(UUID uuid, int id, boolean self) {
		entityUuid = uuid;
		entityId = id;
		isSelf = self;
	}

	public C2SEntityEditingRequestPacket(FriendlyByteBuf buf) {
		entityUuid = buf.readUUID();
		entityId = buf.readInt();
		isSelf = buf.readBoolean();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeUUID(entityUuid);
		buf.writeInt(entityId);
		buf.writeBoolean(isSelf);
	}

	public void serverHandleOnMain(Supplier<NetworkEvent.Context> context) {
		ServerPlayer player = context.get().getSender();
		NBTEditEditingHelper.editEntity(player, entityUuid);
	}
}
