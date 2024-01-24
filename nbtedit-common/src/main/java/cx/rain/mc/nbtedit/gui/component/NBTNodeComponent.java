package cx.rain.mc.nbtedit.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.NBTEditGui;
import cx.rain.mc.nbtedit.nbt.NBTTree;
import cx.rain.mc.nbtedit.nbt.NBTHelper;
import cx.rain.mc.nbtedit.utility.Constants;
import cx.rain.mc.nbtedit.utility.RenderHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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

    private Button.OnTooltip tooltip;

    private MutableComponent narration = new TextComponent("");

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
        return mouseX >= x && mouseY >= y && mouseX < width + x && mouseY < height + y;
    }

    public boolean isMouseInsideSpoiler(int mouseX, int mouseY) {
        return mouseX >= x - 9 && mouseY >= y && mouseX < x && mouseY < y + height;
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
        y += offsetY;
    }

    public boolean shouldRender(int top, int bottom) {
        return y + height >= top && y <= bottom;
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        return narration;
    }

    @Override
    public void renderToolTip(PoseStack poseStack, int mouseX, int mouseY) {
        if (tooltip != null) {
            tooltip.onTooltip(null, poseStack, mouseX, mouseY);
        }
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        var isSelected = gui.getFocused() == node;
        var isTextHover = isMouseInsideText(mouseX, mouseY);
        var isSpoilerHover = isMouseInsideSpoiler(mouseX, mouseY);
        var color = isSelected ? 0xff : isTextHover ? 16777120 : (node.hasParent()) ? 14737632 : -6250336;

        if (isSelected) {
            fill(poseStack, x + 11, y, x + width, y + height, Integer.MIN_VALUE);
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

        getMinecraft().getTextureManager().bind(WIDGET_TEXTURE);
        if (node.hasChild()) {
            blit(poseStack, x - 9, y, 9, height, u, 16, w, h, 512, 512);
        }

        blit(poseStack, x + 1, y, 9, height, (node.getTag().getId() - 1) * 16, 0, 16, 16, 512, 512);
        drawString(poseStack, getMinecraft().font, text, x + 11, y + (this.height - 8) / 2, color);

        if (isMouseInsideSpoiler(mouseX, mouseY) || isMouseInsideText(mouseX, mouseY)) {
            renderToolTip(poseStack, mouseX, mouseY);
        }
    }

    private void updateTooltip() {
        var tag = node.getTag();
        try {
            if (tag instanceof StringTag stringTag) {
                var preview = new TranslatableComponent(Constants.GUI_TOOLTIP_PREVIEW_COMPONENT).append("\n");
                var previewNarration = new TranslatableComponent(Constants.GUI_NARRATION_TOOLTIP_PREVIEW_COMPONENT).append("\n");

                var content = Component.Serializer.fromJson(stringTag.getAsString());

                if (content != null && !content.getString().isBlank()) {
                    preview.append(new TextComponent("").withStyle(ChatFormatting.RESET).append(content));
                    previewNarration.append(new TextComponent("").withStyle(ChatFormatting.RESET).append(content));

                    tooltip = RenderHelper.getTooltip(gui.screen, preview);
                    narration = previewNarration;
                    return;
                }
            }
        } catch (Exception ignored) {
        }

        try {
            if (tag instanceof CompoundTag compoundTag) {
                var itemStack = ItemStack.of(compoundTag);
                if (!itemStack.isEmpty()) {
                    var preview = new TranslatableComponent(Constants.GUI_TOOLTIP_PREVIEW_ITEM).append("\n");
                    var previewNarration = new TranslatableComponent(Constants.GUI_NARRATION_TOOLTIP_PREVIEW_ITEM).append("\n");

                    var lines = itemStack.getTooltipLines(getMinecraft().player, TooltipFlag.Default.ADVANCED);
                    var content = new TextComponent("");
                    for (int i = 0; i < lines.size(); i++) {
                        content.append(lines.get(i));
                        if (i != lines.size() - 1) {
                            content.append("\n");
                        }
                    }

                    preview.append(new TextComponent("").withStyle(ChatFormatting.RESET).append(content));
                    previewNarration.append(new TextComponent("").withStyle(ChatFormatting.RESET).append(content));

                    tooltip = RenderHelper.getTooltip(gui.screen, preview);
                    narration = previewNarration;
                    return;
                }
            }
        } catch (Exception ignored) {
        }

        try {
            if (tag instanceof IntArrayTag intArrayTag) {
                var preview = new TranslatableComponent(Constants.GUI_TOOLTIP_PREVIEW_UUID).append("\n");
                var previewNarration = new TranslatableComponent(Constants.GUI_NARRATION_TOOLTIP_PREVIEW_UUID).append("\n");

                var content = NbtUtils.loadUUID(intArrayTag).toString();

                preview.append(new TextComponent("").withStyle(ChatFormatting.RESET).append(content));
                previewNarration.append(new TextComponent("").withStyle(ChatFormatting.RESET).append(content));

                tooltip = RenderHelper.getTooltip(gui.screen, preview);
                narration = previewNarration;
            }
        } catch (Exception ignored) {
        }
    }
}
