package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

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

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeNbt(compoundTag);
    }

    public static void clientHandle(Minecraft client, ClientPacketListener handler,
                                    FriendlyByteBuf buf, PacketSender responseSender) {
        var blockPos = buf.readBlockPos();
        var compoundTag = buf.readNbt();

        client.execute(() -> ScreenHelper.showNBTEditScreen(blockPos, compoundTag));
    }
}
