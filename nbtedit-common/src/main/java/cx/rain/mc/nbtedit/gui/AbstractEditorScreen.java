package cx.rain.mc.nbtedit.gui;

import cx.rain.mc.nbtedit.gui.component.NbtTreeView;
import cx.rain.mc.nbtedit.gui.window.IWindow;
import cx.rain.mc.nbtedit.gui.window.IWindowHolder;
import cx.rain.mc.nbtedit.nbt.NBTTree;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEditorScreen extends Screen implements IWindowHolder {

    /**
     * Map<IWindow window, Pair<Boolean mutex, Boolean shown>>
     */
    private final Map<IWindow, Boolean> windows = new HashMap<>();

    @Nullable
    private IWindow mutexWindow = null;

    @Nullable
    private IWindow focusedWindow = null;

    private final NBTTree tree;
    private final NbtTreeView treeView;

    public AbstractEditorScreen(CompoundTag tag, Component title) {
        super(title);

        tree = NBTTree.root(tag);
        treeView = new NbtTreeView(tree, 0, 29, width, height - 35);
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(treeView);
    }

    @Override
    public @NotNull List<IWindow> getWindows() {
        return List.copyOf(windows.keySet());
    }

    @Override
    public void addWindow(@NotNull IWindow window, boolean mutex, boolean show) {
        windows.put(window, false);

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
    public void removeWindow(@NotNull IWindow window) {
        if (hasWindow(window)) {
            window.onClose();
            windows.remove(window);
        }
    }

    @Override
    public void show(@NotNull IWindow window) {
        if (windows.containsKey(window)) {
            if (!windows.get(window)) {
                windows.put(window, true);
                window.onShown();
            }
        }
    }

    @Override
    public void hide(@NotNull IWindow window) {
        if (windows.containsKey(window)) {
            if (windows.get(window)) {
                windows.put(window, false);
                window.onHidden();
            }
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
}
