package cx.rain.mc.nbtedit.utility;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.EditorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ScreenHelper {
    public static void showNBTEditScreen(UUID uuid, int id, CompoundTag tag, boolean self) {
        Minecraft.getInstance().setScreen(new EditorScreen(tag,
                Component.translatable(Constants.GUI_TITLE_EDITOR_ENTITY, id),
                newTag -> NBTEdit.getInstance().getNetworking().saveEditing(getEntity(id), newTag, self)));
    }

    private static Entity getEntity(int id) {
        return Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getEntity(id) : null;
    }

    public static void showNBTEditScreen(BlockPos pos, CompoundTag tag) {
        Minecraft.getInstance().setScreen(new EditorScreen(tag,
                Component.translatable(Constants.GUI_TITLE_EDITOR_BLOCK_ENTITY, pos.getX(), pos.getY(), pos.getZ()),
                newTag -> NBTEdit.getInstance().getNetworking().saveEditing(pos, newTag)));
    }

    public static void showNBTEditScreen(ItemStack itemStack, CompoundTag tag) {
        Minecraft.getInstance().setScreen(new EditorScreen(tag,
                Component.translatable(Constants.GUI_TITLE_EDITOR_ITEM_STACK, itemStack.getDisplayName().getString()),
                newTag -> NBTEdit.getInstance().getNetworking().saveEditing(itemStack, newTag)));
    }
}
