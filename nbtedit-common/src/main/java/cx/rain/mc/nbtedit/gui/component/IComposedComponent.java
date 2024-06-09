package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.gui.components.events.ContainerEventHandler;

import java.util.List;

public interface IComposedComponent extends IComponent, ContainerEventHandler {
    void addChild(IComponent child);

    void removeChild(IComponent child);

    List<IComponent> getChildren();

    default void clearChildren() {
        for (var c : getChildren()) {
            c.setParent(null);
            removeChild(c);
        }
    }

    @Override
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        return ContainerEventHandler.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        return ContainerEventHandler.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return ContainerEventHandler.super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    default boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return ContainerEventHandler.super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return ContainerEventHandler.super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return ContainerEventHandler.super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    default boolean charTyped(char codePoint, int modifiers) {
        return ContainerEventHandler.super.charTyped(codePoint, modifiers);
    }
}
