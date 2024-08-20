package cx.rain.mc.nbtedit.networking.packet.common;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ItemStackEditingPacket(CompoundTag tag, boolean readOnly, ItemStack itemStack) implements CustomPacketPayload {
    public static final Type<ItemStackEditingPacket> TYPE = new Type<>(NetworkingConstants.ITEM_STACK_EDITING_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackEditingPacket> CODEC = StreamCodec.composite(
            NetworkingConstants.TAG, ItemStackEditingPacket::tag,
            ByteBufCodecs.BOOL, ItemStackEditingPacket::readOnly,
            ItemStack.STREAM_CODEC, ItemStackEditingPacket::itemStack,
            ItemStackEditingPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
