package cx.rain.mc.nbtedit.gui.component;

import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

public class ScrollBar extends AbstractComponent {
    private static final WidgetSprites BACKGROUND_SPRITES = new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/text_field"), ResourceLocation.withDefaultNamespace("widget/text_field_highlighted"));
    private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.withDefaultNamespace("widget/scroller");

    private final int scrollUnit = getMinecraft().font.lineHeight + 2;

    private final boolean horizontal;
    private final IScrollHandler toScroll;
    private final int contentLength;

    /**
     * Offset between scroll-base to the actual viewport start.
     * (In pixels.)
     */
    private int scrollAmount = 0;
    private boolean dragging = false;

    public ScrollBar(int x, int y, int width, int height, IScrollHandler toScroll, int contentLength) {
        this(x, y, width, height, toScroll, contentLength, false);
    }

    public ScrollBar(int x, int y, int width, int height, IScrollHandler toScroll, int contentLength, boolean horizontal) {
        super(x, y, width, height, Component.translatable(ModConstants.GUI_TITLE_SCROLL_BAR));

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

    public int getPrimaryStart() {
        if (isHorizontal()) {
            return getX();
        }

        return getY();
    }

    public int getPrimaryLength() {
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
        return Mth.clamp(((double) scrollAmount) / (contentLength - getPrimaryLength()), 0, 1);
    }

    /**
     * Set scroll rate (value between 0 ~ 1).
     * @param scrollRate the scroll rate
     */
    public void setScrollRate(double scrollRate) {
        var actual = Mth.clamp(scrollRate, 0, 1);
        var newScrollAmount = (int) (actual * (contentLength - getPrimaryLength()));
        setScrollAmount(newScrollAmount);
    }

    private int getScrollBarLength() {
        return Mth.clamp((int)((float)(getPrimaryLength() * getPrimaryLength()) / (float)contentLength), 32, getPrimaryLength());
    }

    private int getMaxScrollAmount() {
        return contentLength - getPrimaryLength();
    }

    public int getScrollAmount() {
        return scrollAmount;
    }

    public void setScrollAmount(int amount) {
        scrollAmount = Mth.clamp(amount, 0, getMaxScrollAmount());
    }

    private void addScrollAmount(int value) {
        setScrollAmount(getScrollAmount() + value);
        toScroll.onScroll(value);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation resourceLocation = BACKGROUND_SPRITES.get(false, false);
        guiGraphics.blitSprite(resourceLocation, getX(), getY(), getWidth(), getHeight());

        var barLength = this.getScrollBarLength();
        var barOffset = (int) (getScrollRate() * (getPrimaryLength() - barLength));

        if (isHorizontal()) {
            guiGraphics.blitSprite(SCROLLER_SPRITE, getX() + barOffset, getY(), barLength, getHeight());
        } else {
            guiGraphics.blitSprite(SCROLLER_SPRITE, getX(), getY() + barOffset, getWidth(), barLength);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable(ModConstants.GUI_TITLE_SCROLL_BAR_NARRATION));
    }

    public boolean isDragging() {
        return dragging;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
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
        if (isActive() && dragging) {
            var mousePrimary = isVertical() ? mouseY : mouseX;
            var dragPrimary = isVertical() ? dragY : dragX;

            if (mousePrimary < (double) getPrimaryStart()) {
                this.addScrollAmount(-scrollUnit);
            } else if (mousePrimary > (double)(getPrimaryStart() + getPrimaryLength())) {
                this.addScrollAmount(scrollUnit);
            } else {
                var d = Mth.clamp(this.getMaxScrollAmount() / (getPrimaryLength() - getScrollBarLength()), 0, 1);
                this.addScrollAmount((int) (dragPrimary * d));
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        var scrollPrimary = isVertical() ? scrollY : scrollX;
        addScrollAmount((int) (scrollUnit * -scrollPrimary));

        return true;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (isHoveredOrFocused()) {
            if (keyCode == GLFW.GLFW_KEY_UP) {
                addScrollAmount(-scrollUnit);
                return true;
            } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
                addScrollAmount(scrollUnit);
                return true;
            }
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}
