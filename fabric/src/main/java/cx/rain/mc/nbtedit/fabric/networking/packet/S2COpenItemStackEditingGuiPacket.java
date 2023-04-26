package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class S2COpenItemStackEditingGuiPacket implements FabricPacket {
    public static final PacketType<S2COpenItemStackEditingGuiPacket> PACKET_TYPE = PacketType.create(NBTEditNetworkingImpl.S2C_OPEN_ITEM_STACK_EDITING_PACKET_ID, S2COpenItemStackEditingGuiPacket::new);

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

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeItem(itemStack);
        buf.writeNbt(compoundTag);
    }

    @Override
    public PacketType<?> getType() {
        return PACKET_TYPE;
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
