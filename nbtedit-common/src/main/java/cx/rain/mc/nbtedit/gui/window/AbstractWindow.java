package cx.rain.mc.nbtedit.gui.window;

import cx.rain.mc.nbtedit.gui.component.AbstractComposedComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public abstract class AbstractWindow extends AbstractComposedComponent implements IWindow {
    public AbstractWindow(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    public IWindowHolder getHolder() {
        return (IWindowHolder) getParent();
    }
}
