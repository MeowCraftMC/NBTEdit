package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
public class ScrollableViewport extends AbstractComposedComponent {

    private final int scrollBarWidth;

    private int contentWidth = 0;
    private int contentHeight = 0;

    private double scrollXAmount = 0;
    private double scrollYAmount = 0;

    public ScrollableViewport(int x, int y, int width, int height, int scrollBarWidth) {
        super(x, y, width, height, Component.empty());

        this.scrollBarWidth = scrollBarWidth;
    }

    @Override
    public void update() {
        contentWidth = 0;
        contentHeight = 0;
        scrollXAmount = 0;
        scrollYAmount = 0;

        for (var c : getChildren()) {
            var cw = c.getX() + c.getWidth();
            var ch = c.getY() + c.getHeight();
            if (contentWidth < cw) {
                contentWidth = cw;
            }

            if (contentHeight < ch) {
                contentHeight = ch;
            }
        }

        if (shouldShowVerticalBar()) {
            var verticalScrollBar = new ScrollBarWidget(getX() + getWidth() - getScrollBarWidth(), getY(), getX() + getScrollBarWidth(), getY() + getHeight(), amount -> this.onScroll(amount, 0), contentHeight);
            verticalScrollBar.setScrollRate(scrollYAmount);
            addChild(verticalScrollBar);

//            if (false) {
                // Todo. scroll to focused
//            }
        }

        if (shouldShowHorizontalBar()) {
            var horizontalScrollBar = new ScrollBarWidget(getX(), getY() + getHeight() - getScrollBarWidth(), getX() + getWidth(), getY() + getScrollBarWidth(), amount -> this.onScroll(0, amount), contentWidth, true);
            horizontalScrollBar.setScrollRate(scrollXAmount);
            addChild(horizontalScrollBar);
        }
    }

    public boolean shouldShowVerticalBar() {
        return contentHeight > getHeight();
    }

    public boolean shouldShowHorizontalBar() {
        return contentWidth > (getWidth() - (shouldShowVerticalBar() ? getScrollBarWidth() : 0));
    }

    public void onScroll(double deltaX, double deltaY) {
        if (deltaX != 0) {
            scrollXAmount += deltaX;
        }

        if (deltaY != 0) {
            scrollYAmount += deltaY;
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Todo: check it.

        int scale = (int) Minecraft.getInstance().getWindow().getGuiScale();
        guiGraphics.enableScissor(getX() * scale, getY() * scale,
                (getX() + getWidth()) * scale, (getY() + getHeight()) * scale);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(-this.scrollXAmount, -this.scrollYAmount, 0.0);

        for (var c : getChildren()) {
            c.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        guiGraphics.pose().popPose();
        guiGraphics.disableScissor();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    public int getScrollBarWidth() {
        return scrollBarWidth;
    }

    public double getScrollXAmount() {
        return scrollXAmount;
    }

    public void setScrollXAmount(double scrollXAmount) {
        this.scrollXAmount = scrollXAmount;
    }

    public double getScrollYAmount() {
        return scrollYAmount;
    }

    public void setScrollYAmount(double scrollYAmount) {
        this.scrollYAmount = scrollYAmount;
    }
}
