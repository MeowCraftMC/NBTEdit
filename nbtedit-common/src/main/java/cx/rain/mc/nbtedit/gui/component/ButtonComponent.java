package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ButtonComponent extends Button implements IComponent {
    @Nullable
    private AbstractComposedComponent parent;

    public ButtonComponent(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration) {
        super(x, y, width, height, message, onPress, createNarration);
    }

    @Override
    public @Nullable AbstractComposedComponent getParent() {
        return parent;
    }

    @Override
    public void setParent(@Nullable AbstractComposedComponent parent) {
        this.parent = parent;
    }
}
