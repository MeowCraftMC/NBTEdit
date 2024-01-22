package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.networking.NBTEditSavingHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

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

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeNbt(compoundTag);
    }

    public static void serverHandle(MinecraftServer minecraftServer, ServerPlayer player,
                                    ServerGamePacketListenerImpl serverGamePacketListener,
                                    FriendlyByteBuf friendlyByteBuf, PacketSender packetSender) {
        var packet = new C2SBlockEntitySavingPacket(friendlyByteBuf);
        NBTEditSavingHelper.saveBlockEntity(player, packet.blockPos, packet.compoundTag);
    }
}
