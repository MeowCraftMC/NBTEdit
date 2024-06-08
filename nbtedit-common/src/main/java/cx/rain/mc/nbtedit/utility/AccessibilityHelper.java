package cx.rain.mc.nbtedit.utility;

import cx.rain.mc.nbtedit.nbt.NBTTree;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

public class AccessibilityHelper {
    private static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public static Component buildText(NBTTree.Node<?> node) {
        return Component.literal(node.getAsString());
    }

    public static Component buildNarration(NBTTree.Node<?> node) {
        return Component.translatable(Constants.GUI_TITLE_TREE_VIEW_NODE_NARRATION, node.getAsString());
    }

    public static @Nullable Tooltip buildTooltip(NBTTree.Node<?> node) {
        var tag = node.getTag();
        try {
            if (tag instanceof StringTag stringTag) {
                var preview = Component.translatable(Constants.GUI_TOOLTIP_PREVIEW_COMPONENT).append("\n");
                var previewNarration = Component.translatable(Constants.GUI_TOOLTIP_PREVIEW_COMPONENT_NARRATION).append("\n");

                var content = Component.Serializer.fromJson(stringTag.getAsString());

                if (content != null && !content.getString().isBlank()) {
                    preview.append(Component.empty().withStyle(ChatFormatting.RESET).append(content));
                    previewNarration.append(Component.empty().withStyle(ChatFormatting.RESET).append(content));

                    return Tooltip.create(preview, previewNarration);
                }
            }
        } catch (Exception ignored) {
        }

        try {
            if (tag instanceof CompoundTag compoundTag) {
                var itemStack = ItemStack.of(compoundTag);
                if (!itemStack.isEmpty()) {
                    var preview = Component.translatable(Constants.GUI_TOOLTIP_PREVIEW_ITEM).append("\n");
                    var previewNarration = Component.translatable(Constants.GUI_TOOLTIP_PREVIEW_ITEM_NARRATION).append("\n");

                    var lines = itemStack.getTooltipLines(getMinecraft().player, TooltipFlag.ADVANCED);
                    var content = Component.empty();
                    for (int i = 0; i < lines.size(); i++) {
                        content.append(lines.get(i));
                        if (i != lines.size() - 1) {
                            content.append("\n");
                        }
                    }

                    preview.append(Component.empty().withStyle(ChatFormatting.RESET).append(content));
                    previewNarration.append(Component.empty().withStyle(ChatFormatting.RESET).append(content));

                    return Tooltip.create(preview, previewNarration);
                }
            }
        } catch (Exception ignored) {
        }

        try {
            if (tag instanceof IntArrayTag intArrayTag) {
                var preview = Component.translatable(Constants.GUI_TOOLTIP_PREVIEW_UUID).append("\n");
                var previewNarration = Component.translatable(Constants.GUI_TOOLTIP_PREVIEW_UUID_NARRATION).append("\n");

                var content = NbtUtils.loadUUID(intArrayTag).toString();

                preview.append(Component.empty().withStyle(ChatFormatting.RESET).append(content));
                previewNarration.append(Component.empty().withStyle(ChatFormatting.RESET).append(content));

                return Tooltip.create(preview, previewNarration);
            }
        } catch (Exception ignored) {
        }

        return null;
    }
}
