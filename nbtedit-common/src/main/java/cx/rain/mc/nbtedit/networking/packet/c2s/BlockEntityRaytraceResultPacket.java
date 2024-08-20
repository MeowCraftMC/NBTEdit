package cx.rain.mc.nbtedit.networking.packet.c2s;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import cx.rain.mc.nbtedit.networking.packet.IModPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record BlockEntityRaytraceResultPacket(BlockPos pos) implements IModPacket {
    @Override
    public ResourceLocation getId() {
        return NetworkingConstants.BLOCK_ENTITY_RAYTRACE_RESULT_ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public static BlockEntityRaytraceResultPacket read(FriendlyByteBuf buf) {
        return new BlockEntityRaytraceResultPacket(buf.readBlockPos());
    }
}
