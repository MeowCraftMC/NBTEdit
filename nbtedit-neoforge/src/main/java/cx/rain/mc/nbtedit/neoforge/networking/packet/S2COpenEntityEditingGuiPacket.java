package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class S2COpenEntityEditingGuiPacket implements CustomPacketPayload {
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

    public static void handle(S2COpenEntityEditingGuiPacket packet, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            ScreenHelper.showNBTEditScreen(packet.entityUuid, packet.entityId, packet.compoundTag, packet.isSelf);
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
        return NBTEditNetworkingImpl.S2C_OPEN_ENTITY_EDITING_PACKET_ID;
    }
}
