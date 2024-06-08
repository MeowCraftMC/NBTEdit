package cx.rain.mc.nbtedit.gui.component;

import cx.rain.mc.nbtedit.gui.window.IWindow;
import cx.rain.mc.nbtedit.gui.window.IWindowHolder;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractScreen extends Screen implements IComponent, IWindowHolder {
    protected AbstractScreen(Component title) {
        super(title);
    }

    @Override
    public AbstractComposedComponent getParent() {
        return null;
    }

    @Override
    public void setParent(@Nullable IComposedComponent parent) {
        throw new RuntimeException(new OperationNotSupportedException());
    }

    @Override
    public void setX(int x) {
        throw new RuntimeException(new OperationNotSupportedException());
    }

    @Override
    public void setY(int y) {
        throw new RuntimeException(new OperationNotSupportedException());
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    /// <editor-fold desc="Windows holder.">

    /**
     * Map<IWindow window, Pair<Boolean mutex, Boolean shown>>
     */
    private final Map<IWindow, Boolean> windows = new HashMap<>();

    @Nullable
    private IWindow mutexWindow = null;

    @Nullable
    private IWindow focusedWindow = null;

    @Override
    public @NotNull List<IWindow> getWindows() {
        return List.copyOf(windows.keySet());
    }

    @Override
    public void addWindow(@NotNull IWindow window, boolean mutex, boolean show) {
        addChild(window);
        windows.put(window, false);
        window.onOpen();

        if (mutex) {
            if (mutexWindow == null) {
                mutexWindow = window;
            } else {
                throw new IllegalStateException();
            }
        }

        if (show) {
            show(window);
        }
    }

    @Override
    public void closeWindow(@NotNull IWindow window) {
        removeChild(window);
        windows.remove(window);
        window.onClose();
    }

    @Override
    public void show(@NotNull IWindow window) {
        if (!windows.get(window)) {
            windows.put(window, true);
            window.onShown();
        }
    }

    @Override
    public void hide(@NotNull IWindow window) {
        if (windows.get(window)) {
            windows.put(window, false);
            window.onHidden();
        }
    }

    @Override
    public @Nullable IWindow getMutexWindow() {
        return mutexWindow;
    }

    @Override
    public void mutex(@Nullable IWindow window) {
        if (getMutexWindow() == null && window != null && hasWindow(window)) {
            mutexWindow = window;
        } else if (window == null) {
            mutexWindow = null;
        }
    }

    @Override
    public @Nullable IWindow getFocusedWindow() {
        return focusedWindow;
    }

    @Override
    public void focus(@Nullable IWindow window) {
        setFocused(window);
        for (var w : getWindows()) {
            w.setFocused(w == window);
        }
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        super.setFocused(focused);

        if (focused instanceof IWindow window) {
            this.focusedWindow = window;
        } else {
            this.focusedWindow = null;
        }
    }

    private final List<IComponent> children = new ArrayList<>();

    @Override
    public void addChild(IComponent child) {
        children.add(child);
        child.setParent(this);
    }

    @Override
    public void removeChild(IComponent child) {
        children.remove(child);
        child.setParent(null);
    }

    @Override
    public void clearChildren() {
        for (var c : children) {
            c.setParent(null);
        }
        children.clear();
    }

    @Override
    public List<IComponent> getChildren() {
        return List.copyOf(children);
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return List.copyOf(getChildren());
    }

    /// </editor-fold>
}
