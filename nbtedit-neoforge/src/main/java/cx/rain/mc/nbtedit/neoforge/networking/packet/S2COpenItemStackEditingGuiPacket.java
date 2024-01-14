package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class S2COpenItemStackEditingGuiPacket implements CustomPacketPayload {
    private final ItemStack itemStack;
    private final CompoundTag compoundTag;

    public S2COpenItemStackEditingGuiPacket(FriendlyByteBuf buf) {
        itemStack = buf.readItem();
        compoundTag = buf.readNbt();
    }

    public S2COpenItemStackEditingGuiPacket(ItemStack stack, CompoundTag tag) {
        itemStack = stack;
        compoundTag = tag;
    }

    public static void handle(S2COpenItemStackEditingGuiPacket packet, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            ScreenHelper.showNBTEditScreen(packet.itemStack, packet.compoundTag);
        });
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(itemStack);
        buffer.writeNbt(compoundTag);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return NBTEditNetworkingImpl.S2C_OPEN_ITEM_STACK_EDITING_PACKET_ID;
    }
}
