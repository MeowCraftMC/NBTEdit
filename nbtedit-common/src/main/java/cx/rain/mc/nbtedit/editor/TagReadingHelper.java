package cx.rain.mc.nbtedit.editor;

import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Client only.
 */
public class TagReadingHelper {
    public static @Nullable ItemStack tryReadItem(Player player, @Nullable Tag tag) {
        if (tag instanceof CompoundTag compoundTag) {
            try {
                var optional = ItemStack.CODEC
                        .parse(player.registryAccess().createSerializationContext(NbtOps.INSTANCE), compoundTag)
                        .result();
                if (optional.isPresent()) {
                    var itemStack = optional.get();
                    if (!itemStack.isEmpty()) {
                        return itemStack;
                    }
                }
            } catch (Exception ignored) {
            }
        }

        return null;
    }

    public static @Nullable UUID tryReadUuid(@Nullable Tag tag) {
        if (tag instanceof IntArrayTag intArrayTag) {
            try {
                return NbtUtils.loadUUID(intArrayTag);
            } catch (Exception ignored) {
            }
        }

        return null;
    }

    public static @Nullable Component tryReadText(Player player, @Nullable Tag tag) {
        if (tag instanceof StringTag stringTag) {
            try {
                return Component.Serializer.fromJson(stringTag.getAsString(), player.registryAccess());
            } catch (Exception ignored) {
            }
        }

        return null;
    }
}
