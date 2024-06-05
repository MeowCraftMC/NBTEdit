package cx.rain.mc.nbtedit.gui.window;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IWindowHolder {
    @NotNull
    List<IWindow> getWindows();
    void addWindow(@NotNull IWindow window, boolean mutex, boolean show);
    void removeWindow(@NotNull IWindow window);

    void show(@NotNull IWindow window);
    void hide(@NotNull IWindow window);

    @Nullable
    IWindow getMutexWindow();
    void mutex(@Nullable IWindow window);
    @Nullable
    IWindow getFocusedWindow();
    void focus(@Nullable IWindow window);

    default void addWindow(@NotNull IWindow window) {
        addWindow(window, false, true);
    }

    default boolean isFocused(@NotNull IWindow window) {
        return getFocusedWindow() == window;
    }

    default boolean isWindowMutex(@NotNull IWindow window) {
        return getMutexWindow() == window;
    }

    default boolean hasWindow(boolean mutex) {
        return !getWindows().isEmpty();
    }

    default boolean hasWindow(@NotNull IWindow window) {
        return getWindows().contains(window);
    }

    default void closeAll() {
        for (var window : getWindows()) {
            removeWindow(window);
        }
    }
}
