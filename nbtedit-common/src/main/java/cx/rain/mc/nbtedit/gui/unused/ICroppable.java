package cx.rain.mc.nbtedit.gui.unused;

import cx.rain.mc.nbtedit.gui.unused.layout.Bound;
import cx.rain.mc.nbtedit.gui.unused.layout.Position;
import cx.rain.mc.nbtedit.gui.unused.layout.Size;
import net.minecraft.client.gui.GuiGraphics;

@Deprecated(forRemoval = true)
public interface ICroppable extends IComponent, IRenderable {
    void setViewportSize(Size size);
    Size getViewportSize();

    void renderCropped(GuiGraphics guiGraphics, Bound viewport, float partialTick);

    @Override
    default void render(GuiGraphics guiGraphics, Position position, float partialTick) {
        renderCropped(guiGraphics, new Bound(getPosition(), getViewportSize()), partialTick);
    }
}
