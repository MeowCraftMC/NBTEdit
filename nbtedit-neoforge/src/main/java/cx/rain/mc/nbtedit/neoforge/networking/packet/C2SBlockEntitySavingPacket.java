package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.networking.NBTEditSavingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class C2SBlockEntitySavingPacket implements CustomPacketPayload {
    /**
     * The position of the TileEntity.
     */
    protected BlockPos blockPos;

    /**
     * The NBT data of the TileEntity.
     */
    protected CompoundTag compoundTag;

    public C2SBlockEntitySavingPacket(FriendlyByteBuf buf) {
        blockPos = buf.readBlockPos();
        compoundTag = buf.readNbt();
    }

    public C2SBlockEntitySavingPacket(BlockPos pos, CompoundTag tag) {
        blockPos = pos;
        compoundTag = tag;
    }

    public static void handle(C2SBlockEntitySavingPacket packet, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            var optional = context.player();
            if (optional.isPresent()) {
                var player = optional.get();
                if (player instanceof ServerPlayer serverPlayer) {
                    NBTEditSavingHelper.saveBlockEntity(serverPlayer, packet.blockPos, packet.compoundTag);
                }
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(blockPos);
        buffer.writeNbt(compoundTag);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return NBTEditNetworkingImpl.C2S_BLOCK_ENTITY_SAVING_PACKET_ID;
    }
}
