package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

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

    public FriendlyByteBuf write() {
        var buf = PacketByteBufs.create();
        buf.writeItem(itemStack);
        buf.writeNbt(compoundTag);
        return buf;
    }

    public static void clientHandle(Minecraft client, ClientPacketListener handler,
                                    FriendlyByteBuf buf, PacketSender responseSender) {
        var itemStack = buf.readItem();
        var compoundTag = buf.readNbt();

        NBTEdit.getInstance().getLogger().info("Editing ItemStack "
                + itemStack.getDisplayName().getString() + "in hand.");
        client.execute(() -> {
            ScreenHelper.showNBTEditScreen(itemStack, compoundTag);
        });
    }
}
