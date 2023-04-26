package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class S2COpenBlockEntityEditingGuiPacket implements FabricPacket {
    public static final PacketType<S2COpenBlockEntityEditingGuiPacket> PACKET_TYPE = PacketType.create(NBTEditNetworkingImpl.S2C_OPEN_BLOCK_ENTITY_EDITING_PACKET_ID, S2COpenBlockEntityEditingGuiPacket::new);

    private BlockPos blockPos;
    private CompoundTag compoundTag;

    public S2COpenBlockEntityEditingGuiPacket(FriendlyByteBuf buf) {
        blockPos = buf.readBlockPos();
        compoundTag = buf.readNbt();
    }

    public S2COpenBlockEntityEditingGuiPacket(BlockPos pos, CompoundTag tag) {
        blockPos = pos;
        compoundTag = tag;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeNbt(compoundTag);
    }

    @Override
    public PacketType<?> getType() {
        return PACKET_TYPE;
    }

    public static void clientHandle(Minecraft client, ClientPacketListener handler,
                                    FriendlyByteBuf buf, PacketSender responseSender) {
        var blockPos = buf.readBlockPos();
        var compoundTag = buf.readNbt();

        NBTEdit.getInstance().getLogger().info("Editing BlockEntity at XYZ " +
                blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ() + ".");
        client.execute(() -> {
            ScreenHelper.showNBTEditScreen(blockPos, compoundTag);
        });
    }
}
