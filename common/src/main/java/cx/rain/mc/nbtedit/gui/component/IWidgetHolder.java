package cx.rain.mc.nbtedit.gui.component;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;

import java.util.List;

public interface IWidgetHolder {
    List<AbstractWidget> getWidgets();

    void addWidget(AbstractWidget widget);

    void clearWidgets();

    default void renderWidgets(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        for (var widget : getWidgets()) {
            if (widget instanceof Button button) {
                button.render(graphics, mouseX, mouseY, partialTicks);
            } else {
                widget.render(graphics, mouseX, mouseY, partialTicks);
            }
        }
    };
}
