package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.NetworkEvent;

public class S2COpenItemStackEditingGuiPacket {
    private ItemStack itemStack;
    private CompoundTag compoundTag;

    public S2COpenItemStackEditingGuiPacket(FriendlyByteBuf buf) {
        itemStack = buf.readItem();
        compoundTag = buf.readNbt();
    }

    public S2COpenItemStackEditingGuiPacket(ItemStack stack, CompoundTag tag) {
        itemStack = stack;
        compoundTag = tag;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(itemStack);
        buf.writeNbt(compoundTag);
    }

    public void clientHandleOnMain(NetworkEvent.Context context) {
        ScreenHelper.showNBTEditScreen(itemStack, compoundTag);
    }
}
