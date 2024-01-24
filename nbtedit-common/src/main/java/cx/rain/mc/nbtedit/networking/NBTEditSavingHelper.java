package cx.rain.mc.nbtedit.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

import java.util.UUID;

public class NBTEditSavingHelper {
    public static void saveBlockEntity(ServerPlayer player, BlockPos pos, CompoundTag tag) {
        if (!NBTEditNetworkingHelper.checkPermission(player)) {
            return;
        }

        if (!NBTEditNetworkingHelper.checkPosLoaded(player, pos)) {
            return;
        }

        var server = player.getServer();
        var level = player.getLevel();
        server.execute(() -> {
            var blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                try {
                    blockEntity.load(blockEntity.getBlockState(), tag);
                    blockEntity.setChanged();	// Ensure changes gets saved to disk later on. (qyl27: In MCP it is markDirty.)
                    if (blockEntity.hasLevel() && blockEntity.getLevel() instanceof ServerLevel) {
                        ((ServerLevel) blockEntity.getLevel()).getChunkSource().blockChanged(pos);	// Broadcast changes.
                    }

                    NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                            " successfully edited the tag of a BlockEntity at " +
                            pos.getX() + " " +
                            pos.getY() + " " +
                            pos.getZ() + ".");


                    if (NBTEditNetworkingHelper.isDebug()) {
                        NBTEdit.getInstance().getLogger().debug(tag.getAsString());
                    }

                    player.sendMessage(new TranslatableComponent(Constants.MESSAGE_SAVING_SUCCESSFUL)
                            .withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
                } catch (Exception ex) {
                    player.sendMessage(new TranslatableComponent(Constants.MESSAGE_SAVING_FAILED_INVALID_NBT)
                            .withStyle(ChatFormatting.RED), Util.NIL_UUID);

                    NBTEdit.getInstance().getLogger().error("Player " + player.getName().getString() +
                            " edited the tag of BlockEntity at XYZ " +
                            pos.getX() + " " +
                            pos.getY() + " " +
                            pos.getZ() + " and caused an exception!");

                    if (NBTEditNetworkingHelper.isDebug()) {
                        NBTEdit.getInstance().getLogger().error("NBT data: " + tag.getAsString());
                        NBTEdit.getInstance().getLogger().error(new RuntimeException(ex).toString());
                    }
                }
            } else {
                NBTEdit.getInstance().getLogger().info("Player " + player.getName() +
                        " tried to edit a non-existent BlockEntity at " +
                        pos.getX() + " " +
                        pos.getY() + " " +
                        pos.getZ() + ".");
                player.sendMessage(new TranslatableComponent(Constants.MESSAGE_SAVING_FAILED_BLOCK_ENTITY_NOT_EXISTS)
                        .withStyle(ChatFormatting.RED), Util.NIL_UUID);
            }
        });
    }

    public static void saveEntity(ServerPlayer player, UUID entityUuid, CompoundTag tag) {
        if (!NBTEditNetworkingHelper.checkPermission(player)) {
            return;
        }

        var server = player.getServer();
        var level = player.getLevel();
        server.execute(() -> {
            var entity = level.getEntity(entityUuid);

            if (entity != null) {
                if (entity instanceof ServerPlayer
                        && entity != player
                        && !NBTEditNetworkingHelper.canEditOthers()) {
                    NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                            " tried to use /nbtedit on a player. But server config is not allow that.");
                    player.createCommandSourceStack().sendFailure(
                            new TranslatableComponent(Constants.MESSAGE_CANNOT_EDIT_OTHER_PLAYER)
                                    .withStyle(ChatFormatting.RED));
                    return;
                }

                try {
                    GameType prevGameMode = null;
                    if (entity instanceof ServerPlayer) {
                        prevGameMode = ((ServerPlayer) entity).gameMode.getGameModeForPlayer();
                    }
                    entity.load(tag);
                    NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                            " edited the tag of Entity with UUID " + entityUuid + " .");

                    if (NBTEditNetworkingHelper.isDebug()) {
                        NBTEdit.getInstance().getLogger().debug("New NBT of entity " + entityUuid +
                                " is " + tag.getAsString());
                    }

                    if (entity instanceof ServerPlayer) {
                        // qyl27: if anyone found bugs with it, please open an issue.
                        // Update player info
                        // This is fairly hacky.
                        // Consider swapping to an event driven system, where classes can register to
                        // receive entity edit events and provide feedback/send packets as necessary.
                        var targetPlayer = (ServerPlayer) entity;
                        targetPlayer.initMenu();
                        var gameMode = targetPlayer.gameMode.getGameModeForPlayer();
                        if (prevGameMode != gameMode) {
                            targetPlayer.setGameMode(gameMode);
                        }
                        targetPlayer.connection.send(new ClientboundSetHealthPacket(targetPlayer.getHealth(),
                                targetPlayer.getFoodData().getFoodLevel(),
                                targetPlayer.getFoodData().getSaturationLevel()));
                        targetPlayer.connection.send(new ClientboundSetExperiencePacket(
                                targetPlayer.experienceProgress,
                                targetPlayer.totalExperience,
                                targetPlayer.experienceLevel));

                        targetPlayer.onUpdateAbilities();
                        targetPlayer.setPos(targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ());
                    }

                    player.sendMessage(new TranslatableComponent(Constants.MESSAGE_SAVING_SUCCESSFUL)
                            .withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
                } catch (Exception ex) {
                    player.sendMessage(new TranslatableComponent(Constants.MESSAGE_SAVING_FAILED_INVALID_NBT)
                            .withStyle(ChatFormatting.RED), Util.NIL_UUID);

                    NBTEdit.getInstance().getLogger().error("Player " + player.getName().getString() +
                            " edited the tag of entity " + entityUuid + " and caused an exception!");

                    if (NBTEditNetworkingHelper.isDebug()) {
                        NBTEdit.getInstance().getLogger().error("NBT data: " + tag.getAsString());
                        NBTEdit.getInstance().getLogger().error(new RuntimeException(ex).toString());
                    }
                }
            } else {
                NBTEdit.getInstance().getLogger().info("Player " + player.getName() +
                        " tried to edit a non-existent entity " + entityUuid + ".");
                player.sendMessage(new TranslatableComponent(Constants.MESSAGE_SAVING_FAILED_ENTITY_NOT_EXISTS)
                        .withStyle(ChatFormatting.RED), Util.NIL_UUID);
            }
        });
    }

    public static void saveItemStack(ServerPlayer player, ItemStack itemStack, CompoundTag tag) {
        var server = player.getServer();

        server.execute(() -> {
            try {
                var item = ItemStack.of(tag);
                player.setItemInHand(InteractionHand.MAIN_HAND, item);

                NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                        " successfully edited the tag of a ItemStack named "
                        + itemStack.getDisplayName().getString() + ".");

                if (NBTEditNetworkingHelper.isDebug()) {
                    NBTEdit.getInstance().getLogger().debug(tag.getAsString());
                }

                player.sendMessage(new TranslatableComponent(Constants.MESSAGE_SAVING_SUCCESSFUL)
                        .withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
            } catch (Exception ex) {
                player.sendMessage(new TranslatableComponent(Constants.MESSAGE_SAVING_FAILED_INVALID_NBT)
                        .withStyle(ChatFormatting.RED), Util.NIL_UUID);

                NBTEdit.getInstance().getLogger().error("Player " + player.getName().getString() +
                        " edited the tag of ItemStack named "
                        + itemStack.getDisplayName().getString() +" and caused an exception!");

                if (NBTEditNetworkingHelper.isDebug()) {
                    NBTEdit.getInstance().getLogger().error("NBT data: " + tag.getAsString());
                    NBTEdit.getInstance().getLogger().error(new RuntimeException(ex).toString());
                }
            }
        });
    }
}
