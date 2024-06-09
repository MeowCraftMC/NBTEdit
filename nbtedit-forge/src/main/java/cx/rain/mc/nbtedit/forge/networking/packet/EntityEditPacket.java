package cx.rain.mc.nbtedit.forge.networking.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class EntityEditPacket extends AbstractCompoundTagPacket {
    private final UUID uuid;
    private final int entityId;
    private final boolean self;

    public EntityEditPacket(FriendlyByteBuf buf) {
        super(buf);
        this.uuid = buf.readUUID();
        this.entityId = buf.readInt();
        this.self = buf.readBoolean();
    }

    public EntityEditPacket(CompoundTag tag, boolean readOnly, UUID uuid, int id, boolean self) {
        super(tag, readOnly);
        this.uuid = uuid;
        this.entityId = id;
        this.self = self;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        super.write(buf);
        buf.writeUUID(uuid);
        buf.writeInt(entityId);
        buf.writeBoolean(self);
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getEntityId() {
        return entityId;
    }

    public boolean isSelf() {
        return self;
    }
}
