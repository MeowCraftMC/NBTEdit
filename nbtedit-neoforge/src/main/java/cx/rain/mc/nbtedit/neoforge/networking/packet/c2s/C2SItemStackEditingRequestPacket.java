package cx.rain.mc.nbtedit.neoforge.networking.packet.c2s;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class C2SItemStackEditingRequestPacket implements CustomPacketPayload {
    private final ItemStack itemStack;

    public C2SItemStackEditingRequestPacket(ItemStack stack) {
        itemStack = stack;
    }

    public C2SItemStackEditingRequestPacket(FriendlyByteBuf buf) {
        itemStack = buf.readItem();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(itemStack);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return NetworkingConstants.C2S_ITEM_STACK_RAYTRACE_RESULT_PACKET_ID;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
