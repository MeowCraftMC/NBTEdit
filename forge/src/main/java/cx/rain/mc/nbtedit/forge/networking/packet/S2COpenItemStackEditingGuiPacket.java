package cx.rain.mc.nbtedit.forge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.ScreenHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2COpenItemStackEditingGuiPacket {
    private ItemStack itemStack;
    private CompoundTag compoundTag;

    public S2COpenItemStackEditingGuiPacket(ByteBuf byteBuf) {
        var buf = new FriendlyByteBuf(byteBuf);
        itemStack = buf.readItem();
        compoundTag = buf.readNbt();
    }

    public S2COpenItemStackEditingGuiPacket(ItemStack stack, CompoundTag tag) {
        itemStack = stack;
        compoundTag = tag;
    }

    public void toBytes(ByteBuf byteBuf) {
        var buf = new FriendlyByteBuf(byteBuf);
        buf.writeItemStack(itemStack, true);
        buf.writeNbt(compoundTag);
    }

    public void clientHandle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            NBTEdit.getInstance().getLogger().info("Editing ItemStack "
                    + itemStack.getDisplayName().getString() + "in hand.");
            ScreenHelper.showNBTEditScreen(itemStack, compoundTag);
        });
    }
}
