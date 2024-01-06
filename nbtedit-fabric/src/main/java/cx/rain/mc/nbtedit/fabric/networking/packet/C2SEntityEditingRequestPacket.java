package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.networking.NBTEditEditingHelper;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2SEntityEditingRequestPacket implements FabricPacket {
	public static final PacketType<C2SEntityEditingRequestPacket> PACKET_TYPE = PacketType.create(NBTEditNetworkingImpl.C2S_ENTITY_EDITING_PACKET_ID, C2SEntityEditingRequestPacket::new);

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

	public static void serverHandle(C2SEntityEditingRequestPacket packet,
									ServerPlayer player, PacketSender responseSender) {
		NBTEditEditingHelper.editEntity(player, packet.entityUuid);
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
}
