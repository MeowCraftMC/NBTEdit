package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

public class S2COpenBlockEntityEditingGuiPacket {
    private BlockPos blockPos;
    private CompoundTag compoundTag;

    public S2COpenBlockEntityEditingGuiPacket(FriendlyByteBuf buf) {
        blockPos = buf.readBlockPos();
        compoundTag = buf.readNbt();
    }

    public S2COpenBlockEntityEditingGuiPacket(BlockPos pos, CompoundTag tag) {
        blockPos = pos;
        compoundTag = tag;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeNbt(compoundTag);
    }

    public void clientHandleOnMain(NetworkEvent.Context context) {
        ScreenHelper.showNBTEditScreen(blockPos, compoundTag);
    }
}
