package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.networking.NBTEditSavingHelper;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2SEntitySavingPacket implements FabricPacket {
	public static final PacketType<C2SEntitySavingPacket> PACKET_TYPE = PacketType.create(NBTEditNetworkingImpl.C2S_ENTITY_SAVING_PACKET_ID, C2SEntitySavingPacket::new);

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

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeUUID(entityUuid);
		buf.writeInt(entityId);
		buf.writeNbt(compoundTag);
		buf.writeBoolean(isSelf);
	}

	@Override
	public PacketType<?> getType() {
		return PACKET_TYPE;
	}

	public static void serverHandle(C2SEntitySavingPacket packet,
									ServerPlayer player, PacketSender responseSender) {
		NBTEditSavingHelper.saveEntity(player, packet.entityUuid, packet.compoundTag);
	}
}
