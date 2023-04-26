package cx.rain.mc.nbtedit.gui.component.button;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SpecialCharacterButton extends Button {
    public static final ResourceLocation WIDGET_TEXTURE =
            new ResourceLocation(NBTEdit.MODID, "textures/gui/widgets.png");

    protected int buttonId;

    public SpecialCharacterButton(int id, int x, int y, OnPress onPress) {
        super(x, y, 14, 14, Component.empty(), onPress, componentSupplier -> componentSupplier.get().append(Component.translatable(Constants.GUI_NARRATION_BUTTON_SPECIAL_CHARACTER)));

        buttonId = id;
    }

    protected Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShaderTexture(0, WIDGET_TEXTURE);

        if (isMouseInside(mouseX, mouseY))
            Gui.fill(stack, getX(), getY(), getX() + width, getY() + height, 0x80ffffff);

        if (isActive()) {
            GlStateManager._clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            GlStateManager._clearColor(0.5F, 0.5F, 0.5F, 1.0F);
        }

        blit(stack, getX(), getY(), buttonId * width, 27, width, height);
    }

    public boolean isMouseInside(int mouseX, int mouseY) {
        return isActive() && mouseX >= getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
    }
}
