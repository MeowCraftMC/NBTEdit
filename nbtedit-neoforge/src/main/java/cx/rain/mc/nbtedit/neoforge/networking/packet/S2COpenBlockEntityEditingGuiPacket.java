package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class S2COpenBlockEntityEditingGuiPacket implements CustomPacketPayload {
    private final BlockPos blockPos;
    private final CompoundTag compoundTag;

    public S2COpenBlockEntityEditingGuiPacket(FriendlyByteBuf buf) {
        blockPos = buf.readBlockPos();
        compoundTag = buf.readNbt();
    }

    public S2COpenBlockEntityEditingGuiPacket(BlockPos pos, CompoundTag tag) {
        blockPos = pos;
        compoundTag = tag;
    }

    public static void handle(S2COpenBlockEntityEditingGuiPacket packet, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            ScreenHelper.showNBTEditScreen(packet.blockPos, packet.compoundTag);
        });
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(blockPos);
        buffer.writeNbt(compoundTag);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return NBTEditNetworkingImpl.S2C_OPEN_BLOCK_ENTITY_EDITING_PACKET_ID;
    }
}
