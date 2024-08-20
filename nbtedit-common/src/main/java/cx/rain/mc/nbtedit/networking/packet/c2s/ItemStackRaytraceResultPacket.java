package cx.rain.mc.nbtedit.networking.packet.c2s;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ItemStackRaytraceResultPacket(ItemStack itemStack) implements CustomPacketPayload {
    public static final Type<ItemStackRaytraceResultPacket> TYPE = new Type<>(NetworkingConstants.ITEM_STACK_RAYTRACE_RESULT_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackRaytraceResultPacket> CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, ItemStackRaytraceResultPacket::itemStack,
            ItemStackRaytraceResultPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
