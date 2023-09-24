package cx.rain.mc.nbtedit.gui.component.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.NBTEditGui;
import cx.rain.mc.nbtedit.nbt.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class NBTOperatorButton extends Button {
    public static final ResourceLocation BUTTONS_TEXTURE =
            new ResourceLocation(NBTEdit.MODID, "textures/gui/widgets.png");

    protected int buttonId;

    private long hoverTime;

    private NBTEditGui gui;

    public NBTOperatorButton(int id, int x, int y, NBTEditGui parent, OnPress onPressed) {
        this(id, x, y, parent, onPressed, new TextComponent(NBTHelper.getNameByButton((byte) id)));
    }

    public NBTOperatorButton(int id, int x, int y, NBTEditGui parent, OnPress onPressed, Component title) {
        super(x, y, 9, 9, title, onPressed);

        buttonId = id;
        gui = parent;
    }

    protected Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public boolean isMouseInside(int mouseX, int mouseY) {
        return isActive() && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public byte getButtonId() {
        return (byte) buttonId;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (isMouseInside(mouseX, mouseY)) {    // checks if the mouse is over the button
            fill(poseStack, x, y, x + width, y + height, 0x80ffffff);   // draw a grayish background
            if (hoverTime == -1) {
                hoverTime = System.currentTimeMillis();
            }
        } else {
            hoverTime = -1;
        }

        if (isActive()) {
            RenderSystem.setShaderTexture(0, BUTTONS_TEXTURE);
            blit(poseStack, x, y, width, height, (buttonId - 1) * 16, 0, 16, 16, 512, 512); //Draw the texture
        }

        if (hoverTime != -1 && System.currentTimeMillis() - hoverTime > 300) {
            renderTooltip(poseStack, getMessage(), mouseX, mouseY);
        }
    }

    private void renderTooltip(PoseStack poseStack, Component message, int mouseX, int mouseY) {
        var font = getMinecraft().font;
        var width = font.width(message);
        fill(poseStack, mouseX + 4, mouseY + 7, mouseX + 5 + width, mouseY + 17, 0xff000000);
        font.draw(poseStack, message, mouseX + 5, mouseY + 8, 0xffffff);
    }
}
