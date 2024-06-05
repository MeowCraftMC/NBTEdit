package cx.rain.mc.nbtedit.gui.legacy.component.window;

import java.util.List;

public interface ISubWindowHolder {
    public List<SubWindowComponent> getWindows();

    public SubWindowComponent getActiveWindow();
    public void setActiveWindow(SubWindowComponent window);

    public boolean hasWindow();
    public boolean hasWindow(SubWindowComponent window);
    public boolean hasMutexWindow(String name);

    public void addWindow(SubWindowComponent window);
    public void addMutexWindow(String name, SubWindowComponent window);

    public boolean isMutexWindow(SubWindowComponent window);

    public void closeWindow(SubWindowComponent window);
    public void closeAll();

    public void focus(SubWindowComponent window);
}
