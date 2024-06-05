package cx.rain.mc.nbtedit.gui.legacy.component;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.legacy.NBTEditGui;
import cx.rain.mc.nbtedit.nbt.NBTTree;
import cx.rain.mc.nbtedit.nbt.NBTHelper;
import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class NBTNodeComponent extends AbstractWidget {
    public static final ResourceLocation WIDGET_TEXTURE =
            new ResourceLocation(NBTEdit.MODID, "textures/gui/widgets.png");

    protected String text;
    protected NBTTree.Node<?> node;
    protected NBTEditGui gui;

    private final Minecraft minecraft = Minecraft.getInstance();

    public NBTNodeComponent(int x, int y, Component textIn, NBTEditGui guiIn, NBTTree.Node<?> nodeIn) {
        super(x, y, 0, Minecraft.getInstance().font.lineHeight, textIn);

        text = textIn.getString();

        gui = guiIn;
        node = nodeIn;

        init();
    }

    protected Minecraft getMinecraft() {
        return minecraft;
    }

    public NBTTree.Node<?> getNode() {
        return node;
    }

    protected void init() {
        text = NBTHelper.getNBTNameSpecial(node);
        width = minecraft.font.width(text) + 12;

        updateTooltip();
    }

    public boolean isMouseInsideText(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseY >= getY() && mouseX < width + getX() && mouseY < height + getY();
    }

    public boolean isMouseInsideSpoiler(int mouseX, int mouseY) {
        return mouseX >= getX() - 9 && mouseY >= getY() && mouseX < getX() && mouseY < getY() + height;
    }

    public boolean shouldShowChildren() {
        return node.shouldShowChildren();
    }

    public boolean isTextClicked(int mouseX, int mouseY) {
        return isMouseInsideText(mouseX, mouseY);
    }

    public boolean spoilerClicked(int mouseX, int mouseY) {
        if (node.hasChild() && isMouseInsideSpoiler(mouseX, mouseY)) {
            node.setShowChildren(!node.shouldShowChildren());
            return true;
        }
        return false;
    }

    public void shiftY(int offsetY) {
        setY(getY() + offsetY);
    }

    public boolean shouldRender(int top, int bottom) {
        return getY() + height >= top && getY() <= bottom;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {
        narration.add(NarratedElementType.TITLE, text);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        var isSelected = gui.getFocused() == node;
        var isTextHover = isMouseInsideText(mouseX, mouseY);
        var isSpoilerHover = isMouseInsideSpoiler(mouseX, mouseY);
        var color = isSelected ? 0xff : isTextHover ? 16777120 : (node.hasParent()) ? 14737632 : -6250336;

        if (isSelected) {
            graphics.fill(getX() + 11, getY(), getX() + width, getY() + height, Integer.MIN_VALUE);
        }

        var w = 18;
        var h = 16;
        var u = 0;
        if (node.shouldShowChildren()) {
            w = 16;
            h = 18;
            if (isSpoilerHover) {
                u = 18 + 18 + 18;
            } else {
                u = 18;
            }
        } else {
            if (isSpoilerHover) {
                u = 18 + 18;
            }
        }

        if (node.hasChild()) {
            graphics.blit(WIDGET_TEXTURE, getX() - 9, getY(), 9, height, u, 16, w, h, 512, 512);
        }

        graphics.blit(WIDGET_TEXTURE, getX() + 1, getY(), 9, height, (node.getTag().getId() - 1) * 16, 0, 16, 16, 512, 512);
        graphics.drawString(getMinecraft().font, text, getX() + 11, getY() + (this.height - 8) / 2, color);
    }

    private void updateTooltip() {
        var tag = node.getTag();
        try {
            if (tag instanceof StringTag stringTag) {
                var preview = Component.translatable(Constants.GUI_TOOLTIP_PREVIEW_COMPONENT).append("\n");
                var previewNarration = Component.translatable(Constants.GUI_TOOLTIP_PREVIEW_COMPONENT_NARRATION).append("\n");

                var content = Component.Serializer.fromJson(stringTag.getAsString());

                if (content != null && !content.getString().isBlank()) {
                    preview.append(Component.empty().withStyle(ChatFormatting.RESET).append(content));
                    previewNarration.append(Component.empty().withStyle(ChatFormatting.RESET).append(content));

                    setTooltip(Tooltip.create(preview, previewNarration));
                    return;
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

                    // Todo: Mixin render itemStack over the tooltip.
//                    item = itemStack;
//                    preview.append("\n\n");

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

                    setTooltip(Tooltip.create(preview, previewNarration));
                    return;
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

                setTooltip(Tooltip.create(preview, previewNarration));
            }
        } catch (Exception ignored) {
        }

        setTooltipDelay(200);
    }
}
