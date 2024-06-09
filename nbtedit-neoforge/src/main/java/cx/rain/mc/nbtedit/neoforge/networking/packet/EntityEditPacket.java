package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EntityEditPacket extends AbstractCompoundTagPacket implements CustomPacketPayload {
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
    public void write(@NotNull FriendlyByteBuf buf) {
        super.write(buf);
        buf.writeUUID(uuid);
        buf.writeInt(entityId);
        buf.writeBoolean(self);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return NetworkingConstants.ENTITY_EDITING_PACKET_ID;
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
