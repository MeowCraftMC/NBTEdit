package cx.rain.mc.nbtedit.forge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.ScreenHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2COpenBlockEntityEditingGuiPacket {
    private BlockPos blockPos;
    private CompoundTag compoundTag;

    public S2COpenBlockEntityEditingGuiPacket(ByteBuf byteBuf) {
        var buf = new FriendlyByteBuf(byteBuf);
        blockPos = buf.readBlockPos();
        compoundTag = buf.readNbt();
    }

    public S2COpenBlockEntityEditingGuiPacket(BlockPos pos, CompoundTag tag) {
        blockPos = pos;
        compoundTag = tag;
    }

    public void toBytes(ByteBuf byteBuf) {
        var buf = new FriendlyByteBuf(byteBuf);
        buf.writeBlockPos(blockPos);
        buf.writeNbt(compoundTag);
    }

    public void clientHandle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            NBTEdit.getInstance().getLogger().info("Editing BlockEntity at XYZ " +
                    blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ() + ".");
            ScreenHelper.showNBTEditScreen(blockPos, compoundTag);
        });
    }
}
