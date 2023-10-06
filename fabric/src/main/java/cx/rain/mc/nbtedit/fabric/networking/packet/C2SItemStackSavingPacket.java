package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.networking.NBTEditSavingHelper;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class C2SItemStackSavingPacket implements FabricPacket {
    public static final PacketType<C2SItemStackSavingPacket> PACKET_TYPE = PacketType.create(NBTEditNetworkingImpl.C2S_ITEM_STACK_SAVING_PACKET_ID, C2SItemStackSavingPacket::new);

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

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeItem(itemStack);
        buf.writeNbt(compoundTag);
    }

    @Override
    public PacketType<?> getType() {
        return PACKET_TYPE;
    }

    public static void serverHandle(C2SItemStackSavingPacket packet,
                                   ServerPlayer player, PacketSender responseSender) {
        NBTEditSavingHelper.saveItemStack(player, packet.itemStack, packet.compoundTag);
    }
}
