package cx.rain.mc.nbtedit.forge.networking.packet.c2s;

import cx.rain.mc.nbtedit.networking.NetworkEditingHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.UUID;

public class C2SEntityEditingRequestPacket {
	private final UUID entityUuid;
	private final int entityId;
	private final boolean isSelf;

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

	public void write(FriendlyByteBuf buf) {
		buf.writeUUID(entityUuid);
		buf.writeInt(entityId);
		buf.writeBoolean(isSelf);
	}

	public UUID getEntityUuid() {
		return entityUuid;
	}

	public int getEntityId() {
		return entityId;
	}

	public boolean isSelf() {
		return isSelf;
	}
}
