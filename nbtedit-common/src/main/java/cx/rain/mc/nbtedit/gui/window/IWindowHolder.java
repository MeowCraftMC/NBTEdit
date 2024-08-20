package cx.rain.mc.nbtedit.gui.window;

import cx.rain.mc.nbtedit.gui.component.IComposedComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IWindowHolder extends IComposedComponent {
    @NotNull
    List<IWindow> getWindows();
    void addWindow(@NotNull IWindow window, boolean mutex, boolean show);
    void closeWindow(@NotNull IWindow window);

    void show(@NotNull IWindow window);
    void hide(@NotNull IWindow window);

    @Nullable
    IWindow getMutexWindow();
    void setMutexWindow(@Nullable IWindow window);
    @Nullable
    IWindow getFocusedWindow();
    void setFocusedWindow(@Nullable IWindow window);

    default void addWindow(@NotNull IWindow window) {
        addWindow(window, false, true);
    }

    default boolean isFocused(@NotNull IWindow window) {
        return getFocusedWindow() == window;
    }

    default boolean isWindowMutex(@NotNull IWindow window) {
        return getMutexWindow() == window;
    }

    default boolean hasMutexWindow() {
        return getMutexWindow() != null;
    }

    default boolean hasWindow() {
        return !getWindows().isEmpty();
    }

    default boolean hasWindow(@NotNull IWindow window) {
        return getWindows().contains(window);
    }

    default void closeWindows() {
        for (var window : getWindows()) {
            closeWindow(window);
        }
    }
}
