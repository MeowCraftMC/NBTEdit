package cx.rain.mc.nbtedit.gui.component;

import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

public class ScrollBarWidget extends AbstractComponent {
    private static final WidgetSprites BACKGROUND_SPRITES = new WidgetSprites(new ResourceLocation("widget/text_field"), new ResourceLocation("widget/text_field_highlighted"));
    private static final ResourceLocation SCROLLER_SPRITE = new ResourceLocation("widget/scroller");

    private final boolean horizontal;
    private final IScrollHandler toScroll;
    private final int contentLength;

    /**
     * Offset between scroll-base to the actual viewport start.
     * (In pixels.)
     */
    private int scrollAmount = 0;
    private boolean dragging = false;

    public ScrollBarWidget(int x, int y, int width, int height, IScrollHandler toScroll, int contentLength) {
        this(x, y, width, height, toScroll, contentLength, false);
    }

    public ScrollBarWidget(int x, int y, int width, int height, IScrollHandler toScroll, int contentLength, boolean horizontal) {
        super(x, y, width, height, Component.translatable(Constants.GUI_TITLE_SCROLL_BAR));

        this.horizontal = horizontal;
        this.toScroll = toScroll;
        this.contentLength = contentLength;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public boolean isVertical() {
        return !horizontal;
    }

    public int getViewStart() {
        if (isHorizontal()) {
            return getX();
        }

        return getY();
    }

    public int getViewTotalLength() {
        if (isHorizontal()) {
            return getWidth();
        }

        return getHeight();
    }

    /**
     * Get scroll rate (value between 0 ~ 1).
     * @return the scroll rate
     */
    public double getScrollRate() {
        return Mth.clamp(((double) scrollAmount) / contentLength, 0, 1);
    }

    /**
     * Set scroll rate (value between 0 ~ 1).
     * @param scrollRate the scroll rate
     */
    public void setScrollRate(double scrollRate) {
        var actual = Mth.clamp(scrollRate, 0, 1);
        var newScrollAmount = (int) (actual * contentLength);
        var delta = newScrollAmount - this.scrollAmount;
        this.scrollAmount = newScrollAmount;
        toScroll.onScroll(delta);
    }

    private int getScrollBarLength() {
        return Mth.clamp((int)((float)(getViewTotalLength() * getViewTotalLength()) / (float)contentLength), 32, getViewTotalLength());
    }

    private int getMaxScrollAmount() {
        return contentLength - getViewTotalLength();
    }

    private int getScrollAmount() {
        return scrollAmount;
    }

    private void setScrollAmount(int amount) {
        scrollAmount = Mth.clamp(amount, 0, getMaxScrollAmount());
    }

    private void addScrollAmount(int value) {
        setScrollAmount(getScrollAmount() + value);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (shouldShow()) {
            ResourceLocation resourceLocation = BACKGROUND_SPRITES.get(false, false);
            guiGraphics.blitSprite(resourceLocation, getX(), getY(), getWidth(), getHeight());

            int barLength = this.getScrollBarLength();
            var barOffset = scrollAmount;

            if (isHorizontal()) {
                guiGraphics.blitSprite(SCROLLER_SPRITE, barOffset, getY(), barLength, getHeight());
            } else {
                guiGraphics.blitSprite(SCROLLER_SPRITE, getX(), barOffset, getWidth(), barLength);
            }
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable(Constants.GUI_TITLE_SCROLL_BAR_NARRATION));
    }

    public boolean shouldShow() {
        return isActive() && contentLength > getViewTotalLength();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (shouldShow() && isMouseOver(mouseX, mouseY)) {
            dragging = true;
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            dragging = false;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (shouldShow()) {
            var mousePrimary = isVertical() ? mouseY : mouseX;
            var dragPrimary = isVertical() ? dragY : dragX;

            if (mousePrimary < (double)getViewStart()) {
                this.setScrollAmount(0);
            } else if (mousePrimary > (double)(getViewStart() + getViewTotalLength())) {
                this.setScrollAmount(getMaxScrollAmount());
            } else {
                var d = Mth.clamp(this.getMaxScrollAmount() / (getViewTotalLength() - getScrollBarLength()), 0, 1);
                this.addScrollAmount((int) (dragPrimary * d));
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (shouldShow() && isMouseOver(mouseX, mouseY)) {
            var scrollPrimary = isVertical() ? scrollY : scrollX;
            addScrollAmount((int) (9 * scrollPrimary));

            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (shouldShow() && isHoveredOrFocused()) {
            if (keyCode == GLFW.GLFW_KEY_UP) {
                addScrollAmount(-9);
                return true;
            } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
                addScrollAmount(9);
                return true;
            }
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void update() {
    }
}
