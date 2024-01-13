package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.networking.NBTEditSavingHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

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

	public C2SEntitySavingPacket(FriendlyByteBuf buf) {
		entityUuid = buf.readUUID();
		entityId = buf.readInt();
		compoundTag = buf.readNbt();
		isSelf = buf.readBoolean();
	}

	public C2SEntitySavingPacket(UUID uuid, int id, CompoundTag tag, boolean self) {
		entityUuid = uuid;
		compoundTag = tag;
		entityId = id;
		isSelf = self;
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeUUID(entityUuid);
		buf.writeInt(entityId);
		buf.writeNbt(compoundTag);
		buf.writeBoolean(isSelf);
	}

	public void serverHandleOnMain(NetworkEvent.Context context) {
		var player = context.getSender();
		NBTEditSavingHelper.saveEntity(player, entityUuid, compoundTag);
	}
}
