package cx.rain.mc.nbtedit.editor;

import cx.rain.mc.nbtedit.utility.ModConstants;
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

    public static Component buildText(NbtTree.Node<?> node) {
        return Component.literal(node.getAsString());
    }

    public static Component buildNarration(NbtTree.Node<?> node) {
        return Component.translatable(ModConstants.GUI_TITLE_TREE_VIEW_NODE_NARRATION, node.getAsString());
    }

    public static @Nullable Tooltip buildTooltip(NbtTree.Node<?> node) {
        var tag = node.getTag();
        var showPreview = false;
        var previewTitle = "";
        var previewNarrationTitle = "";
        var previewContent = Component.empty();

        var item = TagReadingHelper.tryReadItem(tag);
        if (item != null) {
            showPreview = true;
            previewTitle = ModConstants.GUI_TOOLTIP_PREVIEW_ITEM;
            previewNarrationTitle = ModConstants.GUI_TOOLTIP_PREVIEW_ITEM_NARRATION;

            var lines = item.getTooltipLines(getMinecraft().player, TooltipFlag.ADVANCED);
            for (int i = 0; i < lines.size(); i++) {
                previewContent.append(lines.get(i));
                if (i != lines.size() - 1) {
                    previewContent.append("\n");
                }
            }
        }

        var uuid = TagReadingHelper.tryReadUuid(tag);
        if (!showPreview && uuid != null) {
            showPreview = true;
            previewTitle = ModConstants.GUI_TOOLTIP_PREVIEW_UUID;
            previewNarrationTitle = ModConstants.GUI_TOOLTIP_PREVIEW_UUID_NARRATION;
            previewContent.append(uuid.toString());
        }

        var text = TagReadingHelper.tryReadText(tag);
        if (!showPreview && text != null) {
            showPreview = true;
            previewTitle = ModConstants.GUI_TOOLTIP_PREVIEW_COMPONENT;
            previewNarrationTitle = ModConstants.GUI_TOOLTIP_PREVIEW_COMPONENT_NARRATION;
            previewContent.append(text);
        }

        if (showPreview) {
            var preview = Component.translatable(previewTitle)
                    .append(Component.literal("\n").withStyle(ChatFormatting.RESET)
                            .append(previewContent));
            var previewNarration = Component.translatable(previewNarrationTitle)
                    .append(Component.literal("\n").withStyle(ChatFormatting.RESET)
                            .append(previewContent));

            return Tooltip.create(preview, previewNarration);
        }

        return null;
    }
}
