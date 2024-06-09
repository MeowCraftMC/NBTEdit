package cx.rain.mc.nbtedit.forge.networking.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class BlockEntityEditPacket extends AbstractCompoundTagPacket {
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
    public void write(FriendlyByteBuf buf) {
        super.write(buf);
        buf.writeBlockPos(blockPos);
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
