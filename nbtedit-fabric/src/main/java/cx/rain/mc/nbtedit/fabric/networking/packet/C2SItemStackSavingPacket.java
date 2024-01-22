package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.networking.NBTEditSavingHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;

public class C2SItemStackSavingPacket {

    protected ItemStack itemStack;

    protected CompoundTag compoundTag;

    public C2SItemStackSavingPacket(FriendlyByteBuf buf) {
        itemStack = buf.readItem();
        compoundTag = buf.readNbt();
    }

    public C2SItemStackSavingPacket(ItemStack stack, CompoundTag tag) {
        itemStack = stack;
        compoundTag = tag;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeItem(itemStack);
        buf.writeNbt(compoundTag);
    }

    public static void serverHandle(MinecraftServer minecraftServer, ServerPlayer player,
                                    ServerGamePacketListenerImpl serverGamePacketListener,
                                    FriendlyByteBuf friendlyByteBuf, PacketSender packetSender) {
        var packet = new C2SItemStackSavingPacket(friendlyByteBuf);
        NBTEditSavingHelper.saveItemStack(player, packet.itemStack, packet.compoundTag);
    }
}
