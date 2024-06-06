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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NbtTreeView extends AbstractWidget implements ContainerEventHandler {

    private final List<NbtTreeViewNode> nodes = new ArrayList<>();
    private final NBTTree tree;
    private final Consumer<NbtTreeView> onFocusedUpdated;

    @Nullable
    private GuiEventListener focused;
    private boolean dragging;

    @Nullable
    private ScrollBarWidget horizontalScrollBar;
    @Nullable
    private ScrollBarWidget verticalScrollBar;

    public NbtTreeView(NBTTree tree, int x, int y, int width, int height, Consumer<NbtTreeView> onFocusedUpdated) {
        super(x, y, width, height, Component.translatable(Constants.GUI_TITLE_TREE_VIEW));

        this.tree = tree;
        this.onFocusedUpdated = onFocusedUpdated;

        update();
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

        if (focused instanceof NbtTreeViewNode) {
            onFocusedUpdated.accept(this);
        }
    }

    public @Nullable NbtTreeViewNode getFocusedChild() {
        if (getFocused() instanceof NbtTreeViewNode node) {
            return node;
        }

        return null;
    }

    public void setFocusedNode(NBTTree.Node<?> node) {
        for (var n : nodes) {
            if (n.getNode() == node) {
                setFocused(n);
                break;
            }
        }
    }

    public @Nullable NBTTree.Node<?> getFocusedNode() {
        var c = getFocusedChild();
        return c != null ? c.getNode() : null;
    }

    public static final int START_X = 10;
    public static final int START_Y = 30;
    public static final int NODE_GAP_X = 10;
    public static final int NODE_GAP_Y = getMinecraft().font.lineHeight + 2;

    private int nodeOffsetX = 0;
    private int nodeOffsetY = 0;
    private int maxOffsetX = 0;
    private int maxOffsetY = 0;

    private double scrollXAmount = 0;
    private double scrollYAmount = 0;

    public void update() {
        update(false);
    }

    public void update(boolean centerFocused) {
        nodes.clear();
        nodeOffsetX = START_X;
        nodeOffsetY = START_Y;
        maxOffsetX = 0;
        maxOffsetY = 0;
        horizontalScrollBar = null;
        verticalScrollBar = null;

        addNodes(tree.getRoot());

        var prevFocused = getFocusedNode();
        if (prevFocused != null) {
            setFocused(null);
            setFocusedNode(prevFocused);
        }

        if (shouldShowVerticalScrollBar()) {
            verticalScrollBar = new ScrollBarWidget(getWidth() - 15, 0, 15, getHeight(), amount -> this.onScroll(0, amount), maxOffsetY);
            verticalScrollBar.setScrollAmount(scrollYAmount);

            if (centerFocused) {
                // Todo.
            }
        }

        if (shouldShowHorizontalScrollBar()) {
            horizontalScrollBar = new ScrollBarWidget(0, getHeight() - 15, getWidth(), 15, amount -> this.onScroll(amount, 0), maxOffsetX, true);
            horizontalScrollBar.setScrollAmount(scrollXAmount);
        }
    }

    private void addNodes(NBTTree.Node<?> root) {
        if (nodeOffsetX < maxOffsetX) {
            maxOffsetX = nodeOffsetX;
        }

        if (nodeOffsetY < maxOffsetY) {
            maxOffsetY = nodeOffsetY;
        }

        nodes.add(new NbtTreeViewNode(this, nodeOffsetX, nodeOffsetY, root));

        nodeOffsetY += NODE_GAP_Y;

        if (root.shouldShowChildren()) {
            nodeOffsetX += NODE_GAP_X;
            for (var child : root.getChildren()) {
                addNodes(child);
            }
            nodeOffsetX -= NODE_GAP_X;
        }
    }

    private boolean shouldShowVerticalScrollBar() {
        return maxOffsetY > height;
    }

    private boolean shouldShowHorizontalScrollBar() {
        return maxOffsetX > width;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Todo: check it.
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
        // Todo
    }

    public void onScroll(double deltaX, double deltaY) {
        if (deltaX != 0) {
            scrollXAmount += deltaX;
        }

        if (deltaY != 0) {
            scrollYAmount += deltaY;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return ContainerEventHandler.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return ContainerEventHandler.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return ContainerEventHandler.super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
}
