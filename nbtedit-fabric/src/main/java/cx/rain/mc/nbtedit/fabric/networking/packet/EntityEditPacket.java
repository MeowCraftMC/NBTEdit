package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class EntityEditPacket extends AbstractCompoundTagPacket implements FabricPacket {
    public static final PacketType<EntityEditPacket> PACKET_TYPE = PacketType.create(NetworkingConstants.ENTITY_EDITING_PACKET_ID, EntityEditPacket::new);

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

    @Override
    public PacketType<?> getType() {
        return PACKET_TYPE;
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
