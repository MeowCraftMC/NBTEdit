package cx.rain.mc.nbtedit.utility;

import cx.rain.mc.nbtedit.gui.legacy.screen.NBTEditScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ScreenHelper {
    public static void showNBTEditScreen(UUID uuid, int id, CompoundTag tag, boolean self) {
        Minecraft.getInstance().setScreen(new NBTEditScreen(uuid, id, tag, self));
    }

    public static void showNBTEditScreen(BlockPos pos, CompoundTag tag) {
        Minecraft.getInstance().setScreen(new NBTEditScreen(pos, tag));
    }

    public static void showNBTEditScreen(ItemStack itemStack, CompoundTag tag) {
        Minecraft.getInstance().setScreen(new NBTEditScreen(itemStack, tag));
    }
}
