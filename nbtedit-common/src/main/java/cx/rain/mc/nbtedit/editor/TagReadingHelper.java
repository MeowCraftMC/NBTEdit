package cx.rain.mc.nbtedit.editor;

import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TagReadingHelper {
    public static @Nullable ItemStack tryReadItem(@Nullable Tag tag) {
        if (tag instanceof CompoundTag compoundTag) {
            try {
                var itemStack = ItemStack.of(compoundTag);
                if (!itemStack.isEmpty()) {
                    return itemStack;
                }
            } catch (Exception ex) {
                return null;
            }
        }

        return null;
    }

    public static @Nullable UUID tryReadUuid(@Nullable Tag tag) {
        if (tag instanceof IntArrayTag intArrayTag) {
            try {
                return NbtUtils.loadUUID(intArrayTag);
            } catch (Exception ex) {
                return null;
            }
        }

        return null;
    }

    public static @Nullable Component tryReadText(@Nullable Tag tag) {
        if (tag instanceof StringTag stringTag) {
            try {
                return Component.Serializer.fromJson(stringTag.getAsString());
            } catch (Exception ex) {
                return null;
            }
        }

        return null;
    }
}
