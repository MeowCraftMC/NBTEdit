package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BlockEntityEditPacket extends AbstractCompoundTagPacket implements CustomPacketPayload {
    private final BlockPos blockPos;

    public BlockEntityEditPacket(FriendlyByteBuf buf) {
        super(buf);
        blockPos = buf.readBlockPos();
    }

    public BlockEntityEditPacket(CompoundTag tag, boolean readOnly, BlockPos pos) {
        super(tag, readOnly);
        blockPos = pos;
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf) {
        super.write(buf);
        buf.writeBlockPos(blockPos);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return NetworkingConstants.BLOCK_ENTITY_EDITING_PACKET_ID;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
