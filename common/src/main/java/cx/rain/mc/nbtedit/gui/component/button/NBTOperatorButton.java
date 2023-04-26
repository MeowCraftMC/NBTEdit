package cx.rain.mc.nbtedit.gui.component.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.NBTEditGui;
import cx.rain.mc.nbtedit.nbt.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class NBTOperatorButton extends Button {
    public static final ResourceLocation BUTTONS_TEXTURE =
            new ResourceLocation(NBTEdit.MODID, "textures/gui/widgets.png");

    protected int buttonId;

    private long hoverTime;

    private NBTEditGui gui;

    public NBTOperatorButton(int id, int x, int y, NBTEditGui parent, OnPress onPressed, CreateNarration createNarration) {
        super(x, y, 9, 9, Component.literal(NBTHelper.getNameByButton((byte) id)), onPressed, createNarration);

        buttonId = id;
        gui = parent;
    }

    protected Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public boolean isMouseInside(int mouseX, int mouseY) {
        return isActive() && mouseX >= getX()&& mouseY >= getY()&& mouseX < getX()+ width && mouseY < getY()+ height;
    }

    public byte getButtonId() {
        return (byte) buttonId;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShaderTexture(0, BUTTONS_TEXTURE);

        if (isMouseInside(mouseX, mouseY)) {    // checks if the mouse is over the button
            Gui.fill(stack, getX(), getY(), getX()+ width, getY()+ height, 0x80ffffff);   // draw a grayish background
            if (hoverTime == -1)
                hoverTime = System.currentTimeMillis();
        } else {
            hoverTime = -1;
        }

        if (isActive()) {
            // qyl27: A very hacky way to draw button's texture.
            blit(stack, getX(), getY(), (buttonId - 1) * 9, 18, width, height); //Draw the texture
        }

        if (hoverTime != -1 && System.currentTimeMillis() - hoverTime > 300) {
            renderToolTip(stack, mouseX, mouseY);
        }
    }

//    @Override
    public void renderToolTip(PoseStack stack, int mouseX, int mouseY) {
        var str = getMessage();
        var width = getMinecraft().font.width(str);
        fill(stack, mouseX + 4, mouseY + 7, mouseX + 5 + width, mouseY + 17, 0xff000000);
        getMinecraft().font.draw(stack, str, mouseX + 5, mouseY + 8, 0xffffff);
    }
}
