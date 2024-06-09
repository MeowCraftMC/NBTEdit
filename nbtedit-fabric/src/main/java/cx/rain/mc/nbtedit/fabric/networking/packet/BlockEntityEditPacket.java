package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class BlockEntityEditPacket extends AbstractCompoundTagPacket implements FabricPacket {
    public static final PacketType<BlockEntityEditPacket> PACKET_TYPE = PacketType.create(NetworkingConstants.BLOCK_ENTITY_EDITING_PACKET_ID, BlockEntityEditPacket::new);

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

    @Override
    public PacketType<?> getType() {
        return PACKET_TYPE;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
