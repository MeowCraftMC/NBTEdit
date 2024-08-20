package cx.rain.mc.nbtedit.networking.packet.c2s;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record BlockEntityRaytraceResultPacket(BlockPos pos) implements CustomPacketPayload {
    public static final Type<BlockEntityRaytraceResultPacket> TYPE = new Type<>(NetworkingConstants.BLOCK_ENTITY_RAYTRACE_RESULT_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockEntityRaytraceResultPacket> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BlockEntityRaytraceResultPacket::pos,
            BlockEntityRaytraceResultPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
