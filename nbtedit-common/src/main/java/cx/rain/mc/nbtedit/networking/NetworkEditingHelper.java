package cx.rain.mc.nbtedit.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class NetworkEditingHelper {
    public static void editBlockEntity(ServerPlayer player, BlockPos pos) {
        player.getServer().execute(() -> {
            if (!NetworkingHelper.checkReadPermission(player)) {
                return;
            }

            if (!NetworkingHelper.checkPosLoaded(player, pos)) {
                return;
            }

            NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() + " requested BlockEntity at " +
                    pos.getX() + " " + pos.getY() + " " + pos.getZ() + ".");

            var blockEntity = player.serverLevel().getBlockEntity(pos);
            if (blockEntity == null) {
                player.createCommandSourceStack().sendFailure(Component.translatable(ModConstants.MESSAGE_TARGET_IS_NOT_BLOCK_ENTITY)
                        .withStyle(ChatFormatting.RED));
                return;
            }

            player.sendSystemMessage(Component.translatable(ModConstants.MESSAGE_EDITING_BLOCK_ENTITY,
                    pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.GREEN));
            NBTEditPlatform.getNetworking().serverOpenClientGui(player, pos, blockEntity, NBTEditPlatform.getPermission().isReadOnly(player));
        });
    }

    public static void editEntity(ServerPlayer player, UUID entityUuid) {
        player.getServer().execute(() -> {
            if (!NetworkingHelper.checkReadPermission(player)) {
                return;
            }

            var entity = player.serverLevel().getEntity(entityUuid);
            assert entity != null;

            if (entity instanceof Player
                    && entity != player
                    && !NetworkingHelper.checkEditOnPlayerPermission(player)) {
                NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                        " tried to use nbtedit on a player which is not allowed.");
                return;
            }

            player.sendSystemMessage(Component.translatable(ModConstants.MESSAGE_EDITING_ENTITY, entityUuid)
                    .withStyle(ChatFormatting.GREEN));

            if (player == entity) {
                NBTEditPlatform.getNetworking().serverOpenClientGui(player, NBTEditPlatform.getPermission().isReadOnly(player));
                NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() + " is editing itself.");
            } else {
                NBTEditPlatform.getNetworking().serverOpenClientGui(player, entity, NBTEditPlatform.getPermission().isReadOnly(player));
                NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                        " is editing entity " + entity.getUUID() + ".");
            }
        });
    }

    public static void editItemStack(ServerPlayer player, ItemStack stack) {
        player.getServer().execute(() -> {
            if (!NetworkingHelper.checkReadPermission(player)) {
                return;
            }

            player.sendSystemMessage(Component.translatable(ModConstants.MESSAGE_EDITING_ITEM_STACK,
                    stack.getDisplayName().getString()).withStyle(ChatFormatting.GREEN));
            NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                    " is editing ItemStack named " + stack.getDisplayName().getString() + ".");
            NBTEditPlatform.getNetworking().serverOpenClientGui(player, stack, NBTEditPlatform.getPermission().isReadOnly(player));
        });
    }
}
