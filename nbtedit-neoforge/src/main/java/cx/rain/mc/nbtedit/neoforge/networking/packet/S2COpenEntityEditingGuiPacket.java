package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.UUID;

public class S2COpenEntityEditingGuiPacket {
    protected UUID entityUuid;
    protected int entityId;
    protected CompoundTag compoundTag;
    protected boolean isSelf;

    public S2COpenEntityEditingGuiPacket(FriendlyByteBuf buf) {
        entityUuid = buf.readUUID();
        entityId = buf.readInt();
        compoundTag = buf.readNbt();
        isSelf = buf.readBoolean();
    }

    public S2COpenEntityEditingGuiPacket(UUID uuid, int id, CompoundTag tag, boolean self) {
        entityUuid = uuid;
        entityId = id;
        compoundTag = tag;
        isSelf = self;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(entityUuid);
        buf.writeInt(entityId);
        buf.writeNbt(compoundTag);
        buf.writeBoolean(isSelf);
    }

    public void clientHandleOnMain(NetworkEvent.Context context) {
        ScreenHelper.showNBTEditScreen(entityUuid, entityId, compoundTag, isSelf);
    }
}
