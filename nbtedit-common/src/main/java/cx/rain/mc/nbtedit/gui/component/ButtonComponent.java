package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ButtonComponent extends Button implements IComponent {
    @Nullable
    private IComposedComponent parent;

    public ButtonComponent(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration) {
        super(x, y, width, height, message, onPress, createNarration);
    }

    @Override
    public @Nullable IComposedComponent getParent() {
        return parent;
    }

    @Override
    public void setParent(@Nullable IComposedComponent parent) {
        this.parent = parent;
    }

    public static Builder getBuilder(Component message, OnPress onPress) {
        return new Builder(message, onPress);
    }

    public static class Builder {
        private final Component message;
        private final OnPress onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private CreateNarration createNarration;

        public Builder(Component message, OnPress onPress) {
            this.createNarration = Button.DEFAULT_NARRATION;
            this.message = message;
            this.onPress = onPress;
        }

        public Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder bounds(int x, int y, int width, int height) {
            return this.pos(x, y).size(width, height);
        }

        public Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder createNarration(CreateNarration createNarration) {
            this.createNarration = createNarration;
            return this;
        }

        public ButtonComponent build() {
            var button = new ButtonComponent(this.x, this.y, this.width, this.height, this.message, this.onPress, this.createNarration);
            button.setTooltip(this.tooltip);
            return button;
        }
    }
}
