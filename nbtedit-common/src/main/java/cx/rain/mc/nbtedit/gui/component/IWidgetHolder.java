package cx.rain.mc.nbtedit.gui.component;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;

import java.util.List;

public interface IWidgetHolder {
    List<AbstractWidget> getWidgets();

    void addWidget(AbstractWidget widget);

    void clearWidgets();

    default void renderWidgets(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        for (AbstractWidget widget : getWidgets()) {
            if (widget instanceof Button) {
                Button button = (Button) widget;
                button.render(poseStack, mouseX, mouseY, partialTicks);
            } else {
                widget.render(poseStack, mouseX, mouseY, partialTicks);
            }
        }
    };
}
