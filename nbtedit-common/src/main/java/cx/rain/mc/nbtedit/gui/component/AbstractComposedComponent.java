package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractComposedComponent extends AbstractComponent implements ContainerEventHandler {
    private final List<AbstractComponent> children = new ArrayList<>();

    @Nullable
    private GuiEventListener focused;
    private boolean dragging;

    public AbstractComposedComponent(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    public void addChild(AbstractComponent child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(AbstractComponent child) {
        children.remove(child);
        child.setParent(null);
    }

    public void clearChildren() {
        for (var c : children) {
            c.setParent(null);
        }
        children.clear();
    }

    public List<AbstractComponent> getChildren() {
        return List.copyOf(children);
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return getChildren();
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
