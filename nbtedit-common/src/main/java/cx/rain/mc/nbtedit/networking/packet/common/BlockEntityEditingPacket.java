package cx.rain.mc.nbtedit.networking.packet.common;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record BlockEntityEditingPacket(CompoundTag tag, boolean readOnly, BlockPos pos) implements CustomPacketPayload {
    public static final Type<BlockEntityEditingPacket> TYPE = new Type<>(NetworkingConstants.BLOCK_ENTITY_EDITING_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockEntityEditingPacket> CODEC = StreamCodec.composite(
            NetworkingConstants.TAG, BlockEntityEditingPacket::tag,
            ByteBufCodecs.BOOL, BlockEntityEditingPacket::readOnly,
            BlockPos.STREAM_CODEC, BlockEntityEditingPacket::pos,
            BlockEntityEditingPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
