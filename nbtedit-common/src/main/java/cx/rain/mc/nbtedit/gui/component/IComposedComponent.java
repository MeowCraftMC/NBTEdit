package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.gui.components.events.ContainerEventHandler;

import java.util.List;

public interface IComposedComponent extends ContainerEventHandler {
    void addChild(IComponent child);

    void removeChild(IComponent child);

    void clearChildren();

    List<IComponent> getChildren();
}
