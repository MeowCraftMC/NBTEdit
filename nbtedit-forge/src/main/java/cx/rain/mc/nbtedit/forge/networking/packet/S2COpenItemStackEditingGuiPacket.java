package cx.rain.mc.nbtedit.forge.networking.packet;

import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

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
        buf.writeItemStack(itemStack, true);
        buf.writeNbt(compoundTag);
    }

    public void clientHandleOnMain(CustomPayloadEvent.Context context) {
        ScreenHelper.showNBTEditScreen(itemStack, compoundTag);
    }
}