package cx.rain.mc.nbtedit.networking.packet.common;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import cx.rain.mc.nbtedit.networking.packet.IModPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record BlockEntityEditingPacket(CompoundTag tag, boolean readOnly, BlockPos pos) implements IModPacket {
    @Override
    public ResourceLocation getId() {
        return NetworkingConstants.BLOCK_ENTITY_EDITING_ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(tag);
        buf.writeBoolean(readOnly);
        buf.writeBlockPos(pos);
    }

    public static BlockEntityEditingPacket read(FriendlyByteBuf buf) {
        return new BlockEntityEditingPacket(buf.readNbt(), buf.readBoolean(), buf.readBlockPos());
    }
}
