package cx.rain.mc.nbtedit.forge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.Level;

import java.util.function.Supplier;

public class C2SBlockEntitySavingPacket {
    /**
     * The position of the TileEntity.
     */
    protected BlockPos blockPos;

    /**
     * The NBT data of the TileEntity.
     */
    protected CompoundTag compoundTag;

    public C2SBlockEntitySavingPacket(ByteBuf byteBuf) {
        var buf = new FriendlyByteBuf(byteBuf);
        blockPos = buf.readBlockPos();
        compoundTag = buf.readNbt();
    }

    public C2SBlockEntitySavingPacket(BlockPos pos, CompoundTag tag) {
        blockPos = pos;
        compoundTag = tag;
    }

    public void toBytes(ByteBuf byteBuf) {
        var buf = new FriendlyByteBuf(byteBuf);
        buf.writeBlockPos(blockPos);
        buf.writeNbt(compoundTag);
    }

    public void serverHandleOnMain(Supplier<NetworkEvent.Context> context) {
        var player = context.get().getSender();
        var server = player.getServer();
        var level = player.getLevel();
        server.execute(() -> {
            var blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity != null) {
                try {
                    blockEntity.load(compoundTag);
                    blockEntity.setChanged();	// Ensure changes gets saved to disk later on. (qyl27: In MCP it is markDirty.)
                    if (blockEntity.hasLevel() && blockEntity.getLevel() instanceof ServerLevel) {
                        ((ServerLevel) blockEntity.getLevel()).getChunkSource().blockChanged(blockPos);	// Broadcast changes.
                    }

                    NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                            " successfully edited the tag of a BlockEntity at " +
                            blockPos.getX() + " " +
                            blockPos.getY() + " " +
                            blockPos.getZ() + ".");
                    NBTEdit.getInstance().getLogger().debug(compoundTag.getAsString());

                    player.sendSystemMessage(Component.translatable(Constants.MESSAGE_SAVING_SUCCESSFUL)
                            .withStyle(ChatFormatting.GREEN));
                } catch (Exception ex) {
                    player.sendSystemMessage(Component.translatable(Constants.MESSAGE_SAVING_FAILED_INVALID_NBT)
                            .withStyle(ChatFormatting.RED));

                    NBTEdit.getInstance().getLogger().error("Player " + player.getName().getString() +
                            " edited the tag of BlockEntity at XYZ " +
                            blockPos.getX() + " " +
                            blockPos.getY() + " " +
                            blockPos.getZ() + " and caused an exception!");
                    NBTEdit.getInstance().getLogger().error("NBT data: " + compoundTag.getAsString());
                    NBTEdit.getInstance().getLogger().error(new RuntimeException(ex).toString());
                }
            } else {
                NBTEdit.getInstance().getLogger().info("Player " + player.getName() +
                        " tried to edit a non-existent BlockEntity at " +
                        blockPos.getX() + " " +
                        blockPos.getY() + " " +
                        blockPos.getZ() + ".");
                player.sendSystemMessage(Component.translatable(Constants.MESSAGE_SAVING_FAILED_BLOCK_ENTITY_NOT_EXISTS)
                        .withStyle(ChatFormatting.RED));
            }
        });
    }
}
