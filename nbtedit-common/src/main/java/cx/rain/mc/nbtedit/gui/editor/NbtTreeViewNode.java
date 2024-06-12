package cx.rain.mc.nbtedit.gui.editor;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.editor.NbtTree;
import cx.rain.mc.nbtedit.editor.AccessibilityHelper;
import cx.rain.mc.nbtedit.gui.component.AbstractComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class NbtTreeViewNode extends AbstractComponent {
    public static final ResourceLocation WIDGET_TEXTURE =
            new ResourceLocation(NBTEdit.MODID, "textures/gui/widgets.png");

    private final NbtTreeView treeView;
    private final NbtTree.Node<?> node;

    public NbtTreeViewNode(int x, int y, NbtTree.Node<?> node, @NotNull NbtTreeView parent) {
        super(x, y, 0, Minecraft.getInstance().font.lineHeight, Component.empty());

        this.treeView = parent;
        this.node = node;

        setMessage(AccessibilityHelper.buildText(node));
        setWidth(getMinecraft().font.width(getMessage()) + 12);
    }

    @Override
    public void initialize() {
        super.initialize();

        setTooltip(AccessibilityHelper.buildTooltip(getMinecraft().player, node));
        setTooltipDelay(Duration.ofMillis(200));
    }

    public NbtTreeView getParent() {
        return treeView;
    }

    public NbtTree.Node<?> getNode() {
        return node;
    }

    public boolean isMouseInsideText(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseY >= getY() && mouseX < getX() + getWidth() && mouseY < getY() + getHeight();
    }

    public boolean isMouseInsideSpoiler(int mouseX, int mouseY) {
        return mouseX >= getX() - 9 && mouseY >= getY() && mouseX < getX() && mouseY < getY() + getHeight();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {
        narration.add(NarratedElementType.TITLE, AccessibilityHelper.buildNarration(node));
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        var isSelected = getParent().getFocusedNode() != null
                && getParent().getFocusedNode() == this.getNode();
        var isTextHover = isMouseInsideText(mouseX, mouseY);
        var isSpoilerHover = isMouseInsideSpoiler(mouseX, mouseY);
        var color = isSelected ? 0xFFE0E0E0 : isTextHover ? 0xFFFFFFA0 : (node.hasParent()) ? 0xFFE0E0E0 : 0xFFA0A0A0;

        if (isSelected) {
            graphics.fill(getX() + 11, getY(), getX() + getWidth(), getY() + getHeight(), 0x80000000);
        }

        var w = 18;
        var h = 18;
        var u = 0;
        if (node.shouldShowChildren()) {
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
            graphics.blit(WIDGET_TEXTURE, getX() - 9, getY(), 9, getHeight(), u, 16, w, h, 512, 512);
        }

        graphics.blit(WIDGET_TEXTURE, getX() + 1, getY(), 9, getHeight(), (node.getTag().getId() - 1) * 16, 0, 16, 16, 512, 512);
        graphics.drawString(getMinecraft().font, getMessage(), getX() + 11, getY() + (getHeight() - 8) / 2, color);
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return this.active
                && this.visible
                && mouseX >= getX() - 9
                && mouseY >= getY()
                && mouseX < getX() + getWidth()
                && mouseY < getY() + getHeight();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (isMouseInsideSpoiler((int) mouseX, (int) mouseY)) {
            node.setShowChildren(!node.shouldShowChildren());
            getParent().update(true);
        }

        if (isMouseInsideText((int) mouseX, (int) mouseY)) {
            getParent().setFocused(this);
            getParent().update(true);
        }

        super.onClick(mouseX, mouseY);
    }
}
