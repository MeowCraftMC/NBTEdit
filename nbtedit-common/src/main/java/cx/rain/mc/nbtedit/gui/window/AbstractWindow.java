package cx.rain.mc.nbtedit.gui.window;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public abstract class AbstractWindow extends AbstractWidget implements IWindow {
    public AbstractWindow(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }
}
