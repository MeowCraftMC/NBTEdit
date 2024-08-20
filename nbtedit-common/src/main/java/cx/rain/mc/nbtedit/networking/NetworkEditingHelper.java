package cx.rain.mc.nbtedit.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.networking.packet.common.BlockEntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.EntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.ItemStackEditingPacket;
import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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

            NBTEdit.getInstance().getLogger().debug("Player {} requested BlockEntity at {} {} {}.",
                    player.getName().getString(), pos.getX(), pos.getY(), pos.getZ());

            var blockEntity = player.serverLevel().getBlockEntity(pos);
            if (blockEntity == null) {
                player.createCommandSourceStack().sendFailure(Component
                        .translatable(ModConstants.MESSAGE_TARGET_IS_NOT_BLOCK_ENTITY)
                        .withStyle(ChatFormatting.RED));
                return;
            }

            player.sendSystemMessage(Component
                    .translatable(ModConstants.MESSAGE_EDITING_BLOCK_ENTITY, pos.getX(), pos.getY(), pos.getZ())
                    .withStyle(ChatFormatting.GREEN));

            var tag = blockEntity.saveWithFullMetadata();
            NBTEditPlatform.getNetworking().sendTo(player, new BlockEntityEditingPacket(tag, NBTEditPlatform.getPermission().isReadOnly(player), pos));
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
                NBTEdit.getInstance().getLogger().info("Player {} tried to use nbtedit on a player which is not allowed.",
                        player.getName().getString());
                return;
            }

            NBTEdit.getInstance().getLogger().debug("Player {} is editing entity {}.",
                    player.getName().getString(), player == entity ? "themself" : entity.getUUID());

            player.sendSystemMessage(Component
                    .translatable(ModConstants.MESSAGE_EDITING_ENTITY, entityUuid.toString())
                    .withStyle(ChatFormatting.GREEN));

            var tag = new CompoundTag();
            if (entity instanceof Player) {
                entity.saveWithoutId(tag);
            } else {
                entity.saveAsPassenger(tag);
            }
            NBTEditPlatform.getNetworking().sendTo(player, new EntityEditingPacket(tag, NBTEditPlatform.getPermission().isReadOnly(player), entity.getUUID(), entity.getId()));
        });
    }

    public static void editItemStack(ServerPlayer player, ItemStack stack) {
        player.getServer().execute(() -> {
            if (!NetworkingHelper.checkReadPermission(player)) {
                return;
            }

            NBTEdit.getInstance().getLogger().debug("Player {} is editing ItemStack named {}.",
                    player.getName().getString(), stack.getDisplayName().getString());

            player.sendSystemMessage(Component
                    .translatable(ModConstants.MESSAGE_EDITING_ITEM_STACK, stack.getDisplayName().getString())
                    .withStyle(ChatFormatting.GREEN));

            var tag = stack.save(new CompoundTag());
            NBTEditPlatform.getNetworking().sendTo(player, new ItemStackEditingPacket(tag, NBTEditPlatform.getPermission().isReadOnly(player), stack));
        });
    }
}
