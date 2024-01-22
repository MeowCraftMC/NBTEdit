package cx.rain.mc.nbtedit.gui.component.window;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public abstract class SubWindowComponent extends AbstractWidget {
    protected ISubWindowHolder parentHolder;

    protected boolean isActive;
    protected boolean isFocused;

    public SubWindowComponent(int x, int y, int width, int height,
                              Component title, ISubWindowHolder parent) {
        super(x, y, width, height, title);

        parentHolder = parent;
    }

    public abstract void close();

    public void onFocus() {
        isFocused = true;

        active();
    }

    public void active() {
        isActive = true;
    }

    public void inactive() {
        isActive = false;
    }

    @Override
    protected void onDrag(double startX, double startY, double dragX, double dragY) {
        super.onDrag(startX, startY, dragX, dragY);

        x += (int) (startX + dragX);
        y += (int) (startY + dragY);
    }

    public void tick() {
    }
}
