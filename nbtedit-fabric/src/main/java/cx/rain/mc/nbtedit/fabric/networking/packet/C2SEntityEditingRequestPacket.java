package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.networking.NBTEditEditingHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.UUID;

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

	public void write(FriendlyByteBuf buf) {
		buf.writeUUID(entityUuid);
		buf.writeInt(entityId);
		buf.writeBoolean(isSelf);
	}

	public static void serverHandle(MinecraftServer minecraftServer, ServerPlayer player,
									ServerGamePacketListenerImpl serverGamePacketListener,
									FriendlyByteBuf friendlyByteBuf, PacketSender packetSender) {
		var packet = new C2SEntityEditingRequestPacket(friendlyByteBuf);
		NBTEditEditingHelper.editEntity(player, packet.entityUuid);
	}
}
