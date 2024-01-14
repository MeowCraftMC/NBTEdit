package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.networking.NBTEditEditingHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class C2SEntityEditingRequestPacket implements CustomPacketPayload {
    /**
     * The UUID of the entity being requested.
     */
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

    public static void handle(C2SEntityEditingRequestPacket packet, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            var optional = context.player();
            if (optional.isPresent()) {
                var player = optional.get();
                if (player instanceof ServerPlayer serverPlayer) {
                    NBTEditEditingHelper.editEntity(serverPlayer, packet.entityUuid);
                }
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(entityUuid);
        buffer.writeInt(entityId);
        buffer.writeBoolean(isSelf);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return NBTEditNetworkingImpl.C2S_ENTITY_EDITING_PACKET_ID;
    }
}
