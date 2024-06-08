package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class EditBoxComponent extends EditBox implements IComponent {
    @Nullable
    private AbstractComposedComponent parent;

    public EditBoxComponent(Font font, int x, int y, int width, int height, Component message) {
        super(font, x, y, width, height, message);
    }

    @Override
    public AbstractComposedComponent getParent() {
        return parent;
    }

    @Override
    public void setParent(@Nullable AbstractComposedComponent parent) {
        this.parent = parent;
    }
}
