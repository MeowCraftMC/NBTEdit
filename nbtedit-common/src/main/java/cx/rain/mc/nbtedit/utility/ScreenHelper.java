package cx.rain.mc.nbtedit.utility;

import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.gui.EditorScreen;
import cx.rain.mc.nbtedit.networking.packet.common.BlockEntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.EntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.ItemStackEditingPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ScreenHelper {
    public static void showNBTEditScreen(BlockPos pos, CompoundTag tag, boolean readOnly) {
        Minecraft.getInstance().setScreen(new EditorScreen(tag,
                Component.translatable(ModConstants.GUI_TITLE_EDITOR_BLOCK_ENTITY, pos.getX(), pos.getY(), pos.getZ()),
                newTag -> NBTEditPlatform.getNetworking().sendToServer(new BlockEntityEditingPacket(newTag, false, pos)), readOnly));
    }

    public static void showNBTEditScreen(UUID uuid, int id, CompoundTag tag, boolean readOnly) {
        Minecraft.getInstance().setScreen(new EditorScreen(tag,
                Component.translatable(ModConstants.GUI_TITLE_EDITOR_ENTITY, id),
                newTag -> NBTEditPlatform.getNetworking().sendToServer(new EntityEditingPacket(newTag, false, uuid, id)), readOnly));
    }

    public static void showNBTEditScreen(ItemStack itemStack, CompoundTag tag, boolean readOnly) {
        Minecraft.getInstance().setScreen(new EditorScreen(tag,
                Component.translatable(ModConstants.GUI_TITLE_EDITOR_ITEM_STACK, itemStack.getDisplayName().getString()),
                newTag -> NBTEditPlatform.getNetworking().sendToServer(new ItemStackEditingPacket(newTag, false, itemStack)), readOnly));
    }
}
