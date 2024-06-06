package cx.rain.mc.nbtedit.gui.component;

import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

public class ScrollBarWidget extends AbstractWidget {
    private static final WidgetSprites BACKGROUND_SPRITES = new WidgetSprites(new ResourceLocation("widget/text_field"), new ResourceLocation("widget/text_field_highlighted"));
    private static final ResourceLocation SCROLLER_SPRITE = new ResourceLocation("widget/scroller");

    private final boolean horizontal;
    private final IScrollHandler toScroll;

    private int contentLength = 0;
    private double scrollAmount = 0;
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

    public int getScrollBase() {
        if (isHorizontal()) {
            return getX();
        }

        return getY();
    }

    public int getScrollableLength() {
        if (isHorizontal()) {
            return getWidth();
        }

        return getHeight();
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int length) {
        contentLength = length;
    }

    public double getScrollAmount() {
        return scrollAmount;
    }

    public void setScrollAmount(double scrollAmount) {
        var actual = Mth.clamp(scrollAmount, 0, 1);
        var delta = actual - this.scrollAmount;
        this.scrollAmount = actual;

        toScroll.onScroll(delta);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (shouldShow()) {
            ResourceLocation resourceLocation = BACKGROUND_SPRITES.get(false, false);
            guiGraphics.blitSprite(resourceLocation, getX(), getY(), getWidth(), getHeight());

            int barLength = this.getScrollBarLength();
            if (isHorizontal()) {
                int x = getY() + getHeight();
                int y = Math.max(getX(), (int)getScrollAmount() * (getWidth() - barLength) / this.getMaxScrollAmount() + this.getX());
                guiGraphics.blitSprite(SCROLLER_SPRITE, x, y, barLength, getHeight());
            } else {
                int x = getX() + getWidth();
                int y = Math.max(getY(), (int)getScrollAmount() * (getHeight() - barLength) / this.getMaxScrollAmount() + this.getY());
                guiGraphics.blitSprite(SCROLLER_SPRITE, x, y, getWidth(), barLength);
            }
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable(Constants.GUI_TITLE_SCROLL_BAR_NARRATION));
    }

    public boolean shouldShow() {
        return getContentLength() > getScrollableLength();
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
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        super.onDrag(mouseX, mouseY, dragX, dragY);

        int i = this.getScrollBarLength();
        double d = Math.max(1, this.getMaxScrollAmount() / (getScrollableLength() - i));

        if (isHorizontal()) {
            this.setScrollAmount(getScrollAmount() + dragX * d);
        } else {
            this.setScrollAmount(getScrollAmount() + dragY * d);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (visible) {
            if (isHorizontal()) {
                this.setScrollAmount(getScrollAmount() - scrollX * getScrollRate());
            } else {
                this.setScrollAmount(getScrollAmount() - scrollY * getScrollRate());
            }

            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_UP) {
            this.setScrollAmount(getScrollAmount() - getScrollRate());
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
            this.setScrollAmount(getScrollAmount() + getScrollRate());
            return true;
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    public int getMaxScrollAmount() {
        return Math.max(0, this.getContentLength() - getScrollableLength());
    }

    private int getScrollBarLength() {
        return Mth.clamp((int)((float)(getScrollableLength() * getScrollableLength()) / (float)this.getContentLength()), 32, getScrollableLength());
    }

    public double getScrollRate() {
        return 9;
    }
}
