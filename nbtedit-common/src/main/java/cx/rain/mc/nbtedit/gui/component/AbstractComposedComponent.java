package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractComposedComponent extends AbstractComponent implements IComposedComponent {
    private final List<IComponent> children = new ArrayList<>();

    @Nullable
    private GuiEventListener focused;
    private boolean dragging;

    public AbstractComposedComponent(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    public void addChild(IComponent child) {
        children.add(child);
        child.setParent(this);
    }

    @Override
    public void removeChild(IComponent child) {
        children.remove(child);
        child.setParent(null);
    }

    @Override
    public void clearChildren() {
        for (var c : children) {
            c.setParent(null);
        }
        children.clear();
    }

    @Override
    public List<IComponent> getChildren() {
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
    public void tick() {
        super.tick();

        for (var child : getChildren()) {
            child.tick();
        }
    }

    @Override
    public void update() {
        super.update();

        for (var child : getChildren()) {
            child.update();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return IComposedComponent.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return IComposedComponent.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return IComposedComponent.super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
}
