package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.networking.NBTEditSavingHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.UUID;

public class C2SEntitySavingPacket {

	/**
	 * The id of the entity being edited.
	 */
	protected UUID entityUuid;
	/**
	 * The nbt data of the entity.
	 */
	protected CompoundTag compoundTag;

	protected int entityId;
	protected boolean isSelf;

	public C2SEntitySavingPacket(UUID uuid, int id, CompoundTag tag, boolean self) {
		entityUuid = uuid;
		compoundTag = tag;
		entityId = id;
		isSelf = self;
	}

	public C2SEntitySavingPacket(FriendlyByteBuf buf) {
		entityUuid = buf.readUUID();
		entityId = buf.readInt();
		compoundTag = buf.readNbt();
		isSelf = buf.readBoolean();
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeUUID(entityUuid);
		buf.writeInt(entityId);
		buf.writeNbt(compoundTag);
		buf.writeBoolean(isSelf);
	}

	public static void serverHandle(MinecraftServer minecraftServer, ServerPlayer player,
									ServerGamePacketListenerImpl serverGamePacketListener,
									FriendlyByteBuf friendlyByteBuf, PacketSender packetSender) {
		var packet = new C2SEntitySavingPacket(friendlyByteBuf);
		NBTEditSavingHelper.saveEntity(player, packet.entityUuid, packet.compoundTag);
	}
}
