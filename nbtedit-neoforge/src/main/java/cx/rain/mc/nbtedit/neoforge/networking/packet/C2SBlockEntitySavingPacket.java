package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.networking.NBTEditSavingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

public class C2SBlockEntitySavingPacket {
    /**
     * The position of the TileEntity.
     */
    protected BlockPos blockPos;

    /**
     * The NBT data of the TileEntity.
     */
    protected CompoundTag compoundTag;

    public C2SBlockEntitySavingPacket(FriendlyByteBuf buf) {
        blockPos = buf.readBlockPos();
        compoundTag = buf.readNbt();
    }

    public C2SBlockEntitySavingPacket(BlockPos pos, CompoundTag tag) {
        blockPos = pos;
        compoundTag = tag;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeNbt(compoundTag);
    }

    public void serverHandleOnMain(NetworkEvent.Context context) {
        var player = context.getSender();
        NBTEditSavingHelper.saveBlockEntity(player, blockPos, compoundTag);
    }
}
