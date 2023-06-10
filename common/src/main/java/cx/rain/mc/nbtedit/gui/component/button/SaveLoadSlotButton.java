package cx.rain.mc.nbtedit.gui.component.button;

import com.mojang.blaze3d.platform.InputConstants;
import cx.rain.mc.nbtedit.utility.Constants;
import cx.rain.mc.nbtedit.utility.nbt.ClipboardStates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class SaveLoadSlotButton extends Button {
    private static final int HEIGHT = 20;
    private static final int MAX_WIDTH = 150;
    private static final int MIN_WIDTH = 82;

    protected int rightX;
    protected String text;
    protected boolean isVisible;
    protected ClipboardStates.Clipboard save;

    private int tickCount = -1;

    public SaveLoadSlotButton(ClipboardStates.Clipboard saveIn, int xRight, int y, int id, OnPress onPressed) {
        // Todo: Save slot component i18n.
        super(0, y, 0, HEIGHT, Component.literal("Save #" + id), onPressed, componentSupplier -> componentSupplier.get().append(Component.literal("Save #" + id)));

        save = saveIn;
        rightX = xRight;
        updateText();
        isVisible = !save.tag.isEmpty();

        updatePosition();
    }

    protected Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public void updatePosition() {
        width = getMinecraft().font.width(text) + 24;

        if (width % 2 == 1) {
            width += 1;
        }

        width = Mth.clamp(width, MIN_WIDTH, MAX_WIDTH);
        setX(rightX - width);
    }

    public boolean isMouseInside(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
    }

    public ClipboardStates.Clipboard getSave() {
        return save;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int textColor = ((isMouseInside(mouseX, mouseY))) ? 16777120 : 0xffffff;

        renderButton(graphics, getMinecraft(), getX(), getY(), 0, 66, width, height);

        graphics.drawCenteredString(getMinecraft().font, text, getX() + width / 2, getY() + 6, textColor);
        if (tickCount != -1 && tickCount / 6 % 2 == 0) {
            graphics.drawString(getMinecraft().font, "_",
                    getX() + (width + getMinecraft().font.width(text)) / 2 + 1, getY() + 6, 0xffffff, true);
        }

        if (isVisible) {
            textColor = (isMouseInside(mouseX, mouseY)) ? 16777120 : 0xffffff;
            renderButton(graphics, getMinecraft(), mouseX, mouseY, 0, 66, width, height);
            graphics.drawCenteredString(getMinecraft().font, "x",
                    getX() - width / 2, getY() + 6, textColor);
        }
    }

    protected void renderButton(GuiGraphics graphics, Minecraft mc, int xLoc, int yLoc, int u, int v, int width, int height) {
        graphics.blit(WIDGETS_LOCATION, xLoc, yLoc, u, v, width / 2, height / 2);
        graphics.blit(WIDGETS_LOCATION, xLoc + width / 2, yLoc, u + 200 - width / 2, v, width / 2, height / 2);
        graphics.blit(WIDGETS_LOCATION, xLoc, yLoc + height / 2, u, v + 20 - height / 2, width / 2, height / 2);
        graphics.blit(WIDGETS_LOCATION, xLoc + width / 2, yLoc + height / 2, u + 200 - width / 2, v + 20 - height / 2, width / 2, height / 2);
    }

    public void reset() {
        isVisible = false;
        save.tag = new CompoundTag();
        text = "Save " + save.name;
        updatePosition();
    }

    public void saved() {
        isVisible = true;
        text = "Load " + save.name;
        updatePosition();
    }

    @Override
    public boolean charTyped(char character, int keyId) {
        if (keyId == InputConstants.KEY_BACKSPACE) {
            backSpace();
        }

        if (Character.isDigit(character) || Character.isLetter(character)) {
            save.name += character;
            updateText();
            updatePosition();
        }

        return true;
    }

    private void updateText() {
        text = (save.tag.isEmpty()
                ? Component.translatable(Constants.GUI_BUTTON_SAVE)
                : Component.translatable(Constants.GUI_BUTTON_LOAD)) + save.name;
    }

    public void backSpace() {
        if (save.name.length() > 0) {
            save.name = save.name.substring(0, save.name.length() - 1);
            updateText();
            updatePosition();
        }
    }

    public void startEditing() {
        tickCount = 0;
    }

    public void stopEditing() {
        tickCount = -1;
    }

    public void update() {
        ++tickCount;
    }
}
