package cx.rain.mc.nbtedit.gui.component.button;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import cx.rain.mc.nbtedit.utility.nbt.ClipboardStates;
import cx.rain.mc.nbtedit.utility.translation.TranslatableLanguage;
import cx.rain.mc.nbtedit.utility.translation.TranslateKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Button;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;

public class SaveSlotButton extends Button {
    private static final int HEIGHT = 20;
    private static final int MAX_WIDTH = 150;
    private static final int MIN_WIDTH = 82;

    protected int rightX;
    protected String text;
    protected boolean isVisible;
    protected ClipboardStates.Clipboard save;

    private int tickCount = -1;

    public SaveSlotButton(ClipboardStates.Clipboard saveIn, int xRight, int y, OnPress onPressed) {
        super(0, y, 0, HEIGHT, new TextComponent(""), onPressed);

        save = saveIn;
        rightX = xRight;
        text = (save.tag.isEmpty()
                ? TranslatableLanguage.get().getOrDefault(TranslateKeys.BUTTON_SAVE.getKey())
                : TranslatableLanguage.get().getOrDefault(TranslateKeys.BUTTON_LOAD.getKey())) + save.name;
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
        x = rightX - width;
    }

    public boolean isMouseInside(int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public ClipboardStates.Clipboard getSave() {
        return save;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        int textColor = ((isMouseInside(mouseX, mouseY))) ? 16777120 : 0xffffff;

        renderButton(stack, getMinecraft(), x, y, 0, 66, width, height);

        drawCenteredString(stack, getMinecraft().font, text, x + width / 2, y + 6, textColor);
        if (tickCount != -1 && tickCount / 6 % 2 == 0) {
            getMinecraft().font.drawShadow(stack, "_",
                    x + (width + getMinecraft().font.width(text)) / 2 + 1, y + 6, 0xffffff);
        }

        if (isVisible) {
            textColor = (isMouseInside(mouseX, mouseY)) ? 16777120 : 0xffffff;
            renderButton(stack, getMinecraft(), mouseX, mouseY, 0, 66, width, height);
            drawCenteredString(stack, getMinecraft().font, "x",
                    x - width / 2, y + 6, textColor);
        }
    }

    protected void renderButton(PoseStack stack, Minecraft mc, int xLoc, int yLoc, int u, int v, int width, int height) {
        mc.textureManager.bindForSetup(WIDGETS_LOCATION);

        blit(stack, xLoc, yLoc, u, v, width / 2, height / 2);
        blit(stack, xLoc + width / 2, yLoc, u + 200 - width / 2, v, width / 2, height / 2);
        blit(stack, xLoc, yLoc + height / 2, u, v + 20 - height / 2, width / 2, height / 2);
        blit(stack, xLoc + width / 2, yLoc + height / 2, u + 200 - width / 2, v + 20 - height / 2, width / 2, height / 2);
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
            text = (save.tag.isEmpty()
                    ? TranslatableLanguage.get().getOrDefault(TranslateKeys.BUTTON_SAVE.getKey())
                    : TranslatableLanguage.get().getOrDefault(TranslateKeys.BUTTON_LOAD.getKey())) + save.name;
            updatePosition();
        }

        return true;
    }

    public void backSpace() {
        if (save.name.length() > 0) {
            save.name = save.name.substring(0, save.name.length() - 1);
            text = (save.tag.isEmpty()
                    ? TranslatableLanguage.get().getOrDefault(TranslateKeys.BUTTON_SAVE.getKey())
                    : TranslatableLanguage.get().getOrDefault(TranslateKeys.BUTTON_LOAD.getKey())) + save.name;
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
