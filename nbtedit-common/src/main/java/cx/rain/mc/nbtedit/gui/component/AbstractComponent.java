package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractComponent extends AbstractWidget {

    @Nullable
    private AbstractComposedComponent parent;

    public AbstractComponent(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    public void update() {
    }

    public @Nullable AbstractComposedComponent getParent() {
        return parent;
    }

    public void setParent(@Nullable AbstractComposedComponent parent) {
        this.parent = parent;
    }
}
