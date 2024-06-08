package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface IComponent extends GuiEventListener, Renderable, LayoutElement {
    default void tick() {
    }

    default void update() {
    }

    IComposedComponent getParent();

    void setParent(@Nullable IComposedComponent parent);

    @Override
    default void visitWidgets(Consumer<AbstractWidget> consumer) {
    }

    @Override
    default @NotNull ScreenRectangle getRectangle() {
        return new ScreenRectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
