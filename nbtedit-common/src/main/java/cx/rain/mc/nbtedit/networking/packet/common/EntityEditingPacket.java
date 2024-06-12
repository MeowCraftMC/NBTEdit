package cx.rain.mc.nbtedit.networking.packet.common;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record EntityEditingPacket(CompoundTag tag, boolean readOnly, UUID uuid, int id) implements CustomPacketPayload {
    public static final Type<EntityEditingPacket> TYPE = new Type<>(NetworkingConstants.ENTITY_EDITING_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityEditingPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, EntityEditingPacket::tag,
            ByteBufCodecs.BOOL, EntityEditingPacket::readOnly,
            UUIDUtil.STREAM_CODEC, EntityEditingPacket::uuid,
            ByteBufCodecs.VAR_INT, EntityEditingPacket::id,
            EntityEditingPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
