package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractComposedComponent extends AbstractComponent implements IComposedComponent, ContainerEventHandler {
    private final List<IComponent> children = new ArrayList<>();

    @Nullable
    private GuiEventListener focused;
    private boolean dragging;

    public AbstractComposedComponent(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    public void addChild(@NotNull IComponent child) {
        children.add(child);
        child.setParent(this);
    }

    @Override
    public void removeChild(@NotNull IComponent child) {
        children.remove(child);
        child.setParent(null);
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

    protected abstract void createChildren();

    @Override
    public final void initialize() {
        createChildren();

        for (var child : getChildren()) {
            child.initialize();
        }

        super.initialize();
    }

    @Override
    public void unInitialize() {
        for (var child : getChildren()) {
            child.unInitialize();
        }

        clearChildren();

        super.unInitialize();
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

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return IComposedComponent.super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return IComposedComponent.super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return IComposedComponent.super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return IComposedComponent.super.charTyped(codePoint, modifiers);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (var c : getChildren()) {
            c.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }
}
