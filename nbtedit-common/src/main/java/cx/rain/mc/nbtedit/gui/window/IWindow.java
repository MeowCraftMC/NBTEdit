package cx.rain.mc.nbtedit.gui.window;

import net.minecraft.client.gui.components.events.GuiEventListener;

public interface IWindow extends GuiEventListener {
    default void onOpen() {
    }
    default void onClose() {
    }

    default void onShown() {
    }
    default void onHidden() {
    }
}
