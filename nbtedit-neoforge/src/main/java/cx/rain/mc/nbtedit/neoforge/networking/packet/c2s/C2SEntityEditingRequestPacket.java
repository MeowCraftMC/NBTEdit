package cx.rain.mc.nbtedit.neoforge.networking.packet.c2s;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class C2SEntityEditingRequestPacket implements CustomPacketPayload {
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
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(entityUuid);
        buffer.writeInt(entityId);
        buffer.writeBoolean(isSelf);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return NetworkingConstants.C2S_ENTITY_RAYTRACE_RESULT_PACKET_ID;
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
