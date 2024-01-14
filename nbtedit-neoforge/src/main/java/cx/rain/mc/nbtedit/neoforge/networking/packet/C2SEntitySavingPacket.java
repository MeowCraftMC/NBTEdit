package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.networking.NBTEditSavingHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class C2SEntitySavingPacket implements CustomPacketPayload {
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

    public static void handle(C2SEntitySavingPacket packet, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            var optional = context.player();
            if (optional.isPresent()) {
                var player = optional.get();
                if (player instanceof ServerPlayer serverPlayer) {
                    NBTEditSavingHelper.saveEntity(serverPlayer, packet.entityUuid, packet.compoundTag);
                }
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(entityUuid);
        buffer.writeInt(entityId);
        buffer.writeNbt(compoundTag);
        buffer.writeBoolean(isSelf);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return NBTEditNetworkingImpl.C2S_ENTITY_SAVING_PACKET_ID;
    }
}
