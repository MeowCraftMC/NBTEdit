package cx.rain.mc.nbtedit.gui.unused;

import cx.rain.mc.nbtedit.gui.unused.layout.Position;
import net.minecraft.client.gui.GuiGraphics;

public interface IRenderable {
    void render(GuiGraphics guiGraphics, Position position, float partialTick);
}
