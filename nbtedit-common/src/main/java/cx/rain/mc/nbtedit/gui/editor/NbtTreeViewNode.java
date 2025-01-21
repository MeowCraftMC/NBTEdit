package cx.rain.mc.nbtedit.gui.editor;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.editor.EditorButton;
import cx.rain.mc.nbtedit.editor.NbtTree;
import cx.rain.mc.nbtedit.editor.AccessibilityHelper;
import cx.rain.mc.nbtedit.gui.component.AbstractComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class NbtTreeViewNode extends AbstractComponent {
    public static final ResourceLocation WIDGET_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(NBTEdit.MODID, "textures/gui/widgets.png");

    public static final ResourceLocation ARROW_RIGHT = ResourceLocation.fromNamespaceAndPath(NBTEdit.MODID, "editor/arrow_right");
    public static final ResourceLocation ARROW_DOWN = ResourceLocation.fromNamespaceAndPath(NBTEdit.MODID, "editor/arrow_down");
    public static final ResourceLocation ARROW_RIGHT_HIGHLIGHTED = ResourceLocation.fromNamespaceAndPath(NBTEdit.MODID, "editor/arrow_right_highlighted");
    public static final ResourceLocation ARROW_DOWN_HIGHLIGHTED = ResourceLocation.fromNamespaceAndPath(NBTEdit.MODID, "editor/arrow_down_highlighted");

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

    public boolean isMouseInsideText(double mouseX, double mouseY) {
        return mouseX >= getX() && mouseY >= getY() && mouseX < getX() + getWidth() && mouseY < getY() + getHeight();
    }

    public boolean isMouseInsideSpoiler(double mouseX, double mouseY) {
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

        ResourceLocation arrowSprite;
        if (node.shouldShowChildren()) {
            if (isSpoilerHover) {
                arrowSprite = ARROW_DOWN_HIGHLIGHTED;
            } else {
                arrowSprite = ARROW_DOWN;
            }
        } else {
            if (isSpoilerHover) {
                arrowSprite = ARROW_RIGHT_HIGHLIGHTED;
            } else {
                arrowSprite = ARROW_RIGHT;
            }
        }

        if (node.hasChild()) {
            graphics.blitSprite(arrowSprite, getX() - 9, getY(), 9, getHeight());
        }

        var tagSprite = EditorButton.ofTag(node.getTag()).getSprite();
        graphics.blitSprite(tagSprite, getX() + 1, getY(), 9, getHeight());
        graphics.drawString(getMinecraft().font, getMessage(), getX() + 11, getY() + (getHeight() - 8) / 2, color);
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return this.active
                && this.visible
                && isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return isMouseInsideText(mouseX, mouseY)
                || isMouseInsideSpoiler(mouseX, mouseY);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (isMouseInsideSpoiler(mouseX, mouseY)) {
            node.setShowChildren(!node.shouldShowChildren());
            getParent().update(true);
        }

        if (isMouseInsideText(mouseX, mouseY)) {
            getParent().setFocused(this);
            getParent().update(true);
        }

        super.onClick(mouseX, mouseY);
    }
}
