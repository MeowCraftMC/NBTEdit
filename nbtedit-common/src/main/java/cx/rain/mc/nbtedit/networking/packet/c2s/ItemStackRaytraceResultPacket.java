package cx.rain.mc.nbtedit.networking.packet.c2s;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import cx.rain.mc.nbtedit.networking.packet.IModPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record ItemStackRaytraceResultPacket(ItemStack itemStack) implements IModPacket {
    @Override
    public ResourceLocation getId() {
        return NetworkingConstants.ITEM_STACK_RAYTRACE_RESULT_ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeItem(itemStack);
    }

    public static ItemStackRaytraceResultPacket read(FriendlyByteBuf buf) {
        return new ItemStackRaytraceResultPacket(buf.readItem());
    }
}
