package cx.rain.mc.nbtedit.gui.editor;

import cx.rain.mc.nbtedit.editor.NbtTree;
import cx.rain.mc.nbtedit.gui.component.AbstractComposedComponent;
import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NbtTreeView extends AbstractComposedComponent {

    private final List<NbtTreeViewNode> nodes = new ArrayList<>();
    private final NbtTree tree;
    private final Consumer<NbtTreeView> onFocusedUpdated;

    public NbtTreeView(NbtTree tree, int x, int y, Consumer<NbtTreeView> onFocusedUpdated) {
        super(x, y, 100, 100, Component.translatable(ModConstants.GUI_TITLE_TREE_VIEW));

        this.tree = tree;
        this.onFocusedUpdated = onFocusedUpdated;

        update();
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        super.setFocused(focused);

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

    public void setFocusedNode(@Nullable NbtTree.Node<?> node) {
        setFocused(null);

        if (node != null) {
            for (var n : nodes) {
                if (n.getNode() == node) {
                    setFocused(n);
                    break;
                }
            }
        }
    }

    public @Nullable NbtTree.Node<?> getFocusedNode() {
        var c = getFocusedChild();
        return c != null ? c.getNode() : null;
    }

    public static final int START_X = 10;
    public static final int START_Y = 30;
    public static final int NODE_GAP_X = 10;
    public static final int NODE_GAP_Y = Minecraft.getInstance().font.lineHeight + 2;

    private int nodeOffsetX = 0;
    private int nodeOffsetY = 0;

    private int maxWidth = 0;
    private int maxHeight = 0;

    public void update(boolean callParent) {
        if (callParent) {
            getParent().update();
        } else {
            update();
        }
    }

    @Override
    public void update() {
        super.update();

        nodes.clear();
        clearChildren();
        nodeOffsetX = START_X;
        nodeOffsetY = START_Y;
        maxWidth = 0;
        maxHeight = 0;

        addNodes(tree.getRoot());
        setFocusedNode(getFocusedNode());

        setWidth(maxWidth);
        setHeight(maxHeight);
    }

    private void addNodes(NbtTree.Node<?> root) {
        var node = new NbtTreeViewNode(nodeOffsetX, nodeOffsetY, root, this);
        nodes.add(node);
        addChild(node);

        var w = node.getX() + node.getWidth();
        if (w > maxWidth) {
            maxWidth = w;
        }

        var h = node.getY() + node.getHeight();
        if (h > maxHeight) {
            maxHeight = h;
        }

        nodeOffsetY += NODE_GAP_Y;

        if (root.shouldShowChildren()) {
            nodeOffsetX += NODE_GAP_X;
            for (var child : root.getChildren()) {
                addNodes(child);
            }
            nodeOffsetX -= NODE_GAP_X;
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // Todo
    }
}
