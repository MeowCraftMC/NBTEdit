package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.networking.NBTEditSavingHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.NetworkEvent;

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

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(itemStack);
        buf.writeNbt(compoundTag);
    }

    public void serverHandleOnMain(NetworkEvent.Context context) {
        var player = context.getSender();
        NBTEditSavingHelper.saveItemStack(player, itemStack, compoundTag);
    }
}
