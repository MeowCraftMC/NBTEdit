package cx.rain.mc.nbtedit.networking.packet.c2s;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record EntityRaytraceResultPacket(UUID uuid, int id) implements CustomPacketPayload {
    public static final Type<EntityRaytraceResultPacket> TYPE = new Type<>(NetworkingConstants.ENTITY_RAYTRACE_RESULT_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityRaytraceResultPacket> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, EntityRaytraceResultPacket::uuid,
            ByteBufCodecs.VAR_INT, EntityRaytraceResultPacket::id,
            EntityRaytraceResultPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
