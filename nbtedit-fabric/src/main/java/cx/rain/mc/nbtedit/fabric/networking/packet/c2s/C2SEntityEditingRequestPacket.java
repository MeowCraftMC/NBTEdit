package cx.rain.mc.nbtedit.fabric.networking.packet.c2s;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class C2SEntityEditingRequestPacket implements FabricPacket {
	public static final PacketType<C2SEntityEditingRequestPacket> PACKET_TYPE = PacketType.create(NetworkingConstants.C2S_ENTITY_RAYTRACE_RESULT_PACKET_ID, C2SEntityEditingRequestPacket::new);

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

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeUUID(entityUuid);
		buf.writeInt(entityId);
		buf.writeBoolean(isSelf);
	}

	@Override
	public PacketType<?> getType() {
		return PACKET_TYPE;
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
