package cx.rain.mc.nbtedit.gui.component;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.nbt.NBTTree;
import cx.rain.mc.nbtedit.utility.AccessibilityHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class NbtTreeViewNode extends AbstractWidget {
    public static final ResourceLocation WIDGET_TEXTURE =
            new ResourceLocation(NBTEdit.MODID, "textures/gui/widgets.png");

    protected final NbtTreeView treeView;
    protected final NBTTree.Node<?> node;

    public NbtTreeViewNode(NbtTreeView treeView, int x, int y, NBTTree.Node<?> node) {
        super(x, y, 0, getMinecraft().font.lineHeight, Component.empty());

        this.treeView = treeView;
        this.node = node;

        setMessage(AccessibilityHelper.buildText(node));
        setWidth(getMinecraft().font.width(getMessage()) + 12);

        updateTooltip();
    }

    protected static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public NbtTreeView getParent() {
        return treeView;
    }

    public NBTTree.Node<?> getNode() {
        return node;
    }

    public boolean isMouseInsideText(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseY >= getY() && mouseX < width + getX() && mouseY < height + getY();
    }

    public boolean isMouseInsideSpoiler(int mouseX, int mouseY) {
        return mouseX >= getX() - 9 && mouseY >= getY() && mouseX < getX() && mouseY < getY() + height;
    }

    public boolean shouldRender(int xMin, int yMin, int xMax, int yMax) {
        return getX() > xMin
                || getY() > yMin
                || getX() + getWidth() < xMax
                || getY() + getHeight() < yMax;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {
        narration.add(NarratedElementType.TITLE, AccessibilityHelper.buildNarration(node));
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        var isSelected = getParent().getFocusedNode() == this;
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
        graphics.drawString(getMinecraft().font, getMessage(), getX() + 11, getY() + (this.height - 8) / 2, color);
    }

    private void updateTooltip() {
        setTooltip(AccessibilityHelper.buildTooltip(node));
        setTooltipDelay(200);
    }
}
