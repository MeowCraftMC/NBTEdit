package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class C2SBlockEntitySavingPacket {
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

    public FriendlyByteBuf write() {
        var buf = PacketByteBufs.create();
        buf.writeBlockPos(blockPos);
        buf.writeNbt(compoundTag);
        return buf;
    }

    public static void serverHandle(MinecraftServer server, ServerPlayer player,
                                    ServerGamePacketListenerImpl serverGamePacketListener,
                                    FriendlyByteBuf buf, PacketSender sender) {
        var packet = new C2SBlockEntitySavingPacket(buf);

        var level = player.getLevel();
        server.execute(() -> {
            var blockEntity = level.getBlockEntity(packet.blockPos);
            if (blockEntity != null) {
                try {
                    blockEntity.load(packet.compoundTag);
                    blockEntity.setChanged();	// Ensure changes gets saved to disk later on. (qyl27: In MCP it is markDirty.)
                    if (blockEntity.hasLevel() && blockEntity.getLevel() instanceof ServerLevel) {
                        ((ServerLevel) blockEntity.getLevel()).getChunkSource().blockChanged(packet.blockPos);	// Broadcast changes.
                    }

                    NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                            " successfully edited the tag of a BlockEntity at " +
                            packet.blockPos.getX() + " " +
                            packet.blockPos.getY() + " " +
                            packet.blockPos.getZ() + ".");
                    NBTEdit.getInstance().getLogger().debug(packet.compoundTag.getAsString());

                    player.sendSystemMessage(Component.translatable(Constants.MESSAGE_SAVING_SUCCESSFUL)
                            .withStyle(ChatFormatting.GREEN));
                } catch (Exception ex) {
                    player.sendSystemMessage(Component.translatable(Constants.MESSAGE_SAVING_FAILED_INVALID_NBT)
                            .withStyle(ChatFormatting.RED));

                    NBTEdit.getInstance().getLogger().error("Player " + player.getName().getString() +
                            " edited the tag of BlockEntity at XYZ " +
                            packet.blockPos.getX() + " " +
                            packet.blockPos.getY() + " " +
                            packet.blockPos.getZ() + " and caused an exception!");
                    NBTEdit.getInstance().getLogger().error("NBT data: " + packet.compoundTag.getAsString());
                    NBTEdit.getInstance().getLogger().error(new RuntimeException(ex).toString());
                }
            } else {
                NBTEdit.getInstance().getLogger().info("Player " + player.getName() +
                        " tried to edit a non-existent BlockEntity at " +
                        packet.blockPos.getX() + " " +
                        packet.blockPos.getY() + " " +
                        packet.blockPos.getZ() + ".");
                player.sendSystemMessage(Component.translatable(Constants.MESSAGE_SAVING_FAILED_BLOCK_ENTITY_NOT_EXISTS)
                        .withStyle(ChatFormatting.RED));
            }
        });
    }
}
