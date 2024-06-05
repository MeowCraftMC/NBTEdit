package cx.rain.mc.nbtedit.gui.legacy.component.button;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.legacy.NBTEditGui;
import cx.rain.mc.nbtedit.nbt.NBTHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class NBTOperatorButton extends Button {
    public static final ResourceLocation BUTTONS_TEXTURE =
            new ResourceLocation(NBTEdit.MODID, "textures/gui/widgets.png");

    protected int buttonId;

    private final NBTEditGui gui;

    public NBTOperatorButton(int id, int x, int y, NBTEditGui parent, OnPress onPressed, CreateNarration createNarration) {
        super(x, y, 9, 9, Component.literal(NBTHelper.getNameByButton((byte) id)), onPressed, createNarration);

        buttonId = id;
        gui = parent;

        setTooltip(new Tooltip(getMessage(), getMessage()));
        setTooltipDelay(200);
    }

    public boolean isMouseInside(int mouseX, int mouseY) {
        return isActive() && mouseX >= getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
    }

    public byte getButtonId() {
        return (byte) buttonId;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (isMouseInside(mouseX, mouseY)) {    // checks if the mouse is over the button
            graphics.fill(getX(), getY(), getX() + width, getY() + height, 0x80ffffff);   // draw a grayish background
        }

        if (isActive()) {
            graphics.blit(BUTTONS_TEXTURE, getX(), getY(), width, height, (buttonId - 1) * 16, 0, 16, 16, 512, 512); //Draw the texture
        }
    }
}
