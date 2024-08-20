package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class AbstractComponent extends AbstractWidget implements IComponent {

    @Nullable
    private IComposedComponent parent;

    public AbstractComponent(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    protected final Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    protected void setHeight(int value) {
        height = value;
    }

    @Override
    public @Nullable IComposedComponent getParent() {
        return parent;
    }

    @Override
    public void setParent(@Nullable IComposedComponent parent) {
        this.parent = parent;
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {
        consumer.accept(this);
    }
}
