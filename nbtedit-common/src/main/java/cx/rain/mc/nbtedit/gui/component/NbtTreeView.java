package cx.rain.mc.nbtedit.gui.component;

import cx.rain.mc.nbtedit.nbt.NBTTree;
import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NbtTreeView extends AbstractWidget implements ContainerEventHandler, IScrollable {

    private final List<NbtTreeViewNode> nodes = new ArrayList<>();

    private final NBTTree tree;

    @Nullable
    private GuiEventListener focused;
    private boolean dragging;

    @Nullable
    private ScrollBarWidget horizontalScrollBar;
    @Nullable
    private ScrollBarWidget verticalScrollBar;

    public NbtTreeView(NBTTree tree, int x, int y, int width, int height) {
        super(x, y, width, height, Component.translatable(Constants.GUI_TITLE_TREE_VIEW));

        this.tree = tree;
    }

    protected static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return List.copyOf(nodes);
    }

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        if (this.focused != null) {
            this.focused.setFocused(false);
        }

        if (focused != null) {
            focused.setFocused(true);
        }

        this.focused = focused;
    }

    public @Nullable NbtTreeViewNode getFocusedNode() {
        if (getFocused() instanceof NbtTreeViewNode node) {
            return node;
        }

        return null;
    }

    public static final int START_X = 10;
    public static final int START_Y = 30;
    public static final int NODE_GAP_X = 10;
    public static final int NODE_GAP_Y = getMinecraft().font.lineHeight + 2;

    private int nodeOffsetX = 0;
    private int nodeOffsetY = 0;
    private double scrollXAmount = 0;
    private double scrollYAmount = 0;

    protected void update() {
        nodes.clear();
        nodeOffsetX = START_X;
        nodeOffsetY = START_Y;
        horizontalScrollBar = null;
        verticalScrollBar = null;

        addNodes(tree.getRoot());

        var prevFocused = getFocusedNode();
        if (prevFocused != null) {
            setFocused(null);
            for (var n : nodes) {
                if (prevFocused.getNode() == n.getNode()) {
                    setFocused(n);
                }
            }
        }

        if (shouldShowVerticalScrollBar()) {
            verticalScrollBar = new ScrollBarWidget(getWidth() - 15, 0, 15, getHeight(), this, nodeOffsetY);
            verticalScrollBar.setScrollAmount(scrollYAmount);
        }

        if (shouldShowHorizontalScrollBar()) {
            horizontalScrollBar = new ScrollBarWidget(0, getHeight() - 15, getWidth(), 15, this, nodeOffsetX, true);
            horizontalScrollBar.setScrollAmount(scrollXAmount);
        }
    }

    private void addNodes(NBTTree.Node<?> root) {
        nodes.add(new NbtTreeViewNode(this, nodeOffsetX, nodeOffsetY, root));

        nodeOffsetX += NODE_GAP_X;
        nodeOffsetY += NODE_GAP_Y;

        if (root.shouldShowChildren()) {
            for (var child : root.getChildren()) {
                addNodes(child);
            }
        }
    }

    private boolean shouldShowVerticalScrollBar() {
        return nodeOffsetY > height;
    }

    private boolean shouldShowHorizontalScrollBar() {
        return nodeOffsetX > width;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(-this.scrollXAmount, -this.scrollYAmount, 0.0);

        for (var n : nodes) {
            if (n.shouldRender(getX(), getY(), getX() + getWidth(), getY() + getHeight())) {
                n.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }

        guiGraphics.pose().popPose();
        guiGraphics.disableScissor();

        if (verticalScrollBar != null) {
            verticalScrollBar.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        if (horizontalScrollBar != null) {
            horizontalScrollBar.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void onScroll(double deltaX, double deltaY) {
        if (deltaX != 0) {
            scrollXAmount += deltaX;
        }

        if (deltaY != 0) {
            scrollYAmount += deltaY;
        }
    }
}
