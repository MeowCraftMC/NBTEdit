package cx.rain.mc.nbtedit.gui.component;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;

import java.util.List;

public interface IWidgetHolder {
    public List<AbstractWidget> getWidgets();

    public void addWidget(AbstractWidget widget);

    public void clearWidgets();

    public default void renderWidgets(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        for (var widget : getWidgets()) {
            if (widget instanceof Button button) {
                button.renderButton(stack, mouseX, mouseY, partialTicks);
            } else {
                widget.render(stack, mouseX, mouseY, partialTicks);
            }
        }
    };
}
