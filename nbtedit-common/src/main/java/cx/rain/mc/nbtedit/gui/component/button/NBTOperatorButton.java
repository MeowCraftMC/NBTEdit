package cx.rain.mc.nbtedit.gui.component.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.NBTEditGui;
import cx.rain.mc.nbtedit.nbt.NBTHelper;
import cx.rain.mc.nbtedit.utility.RenderHelper;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;
import java.util.function.Supplier;

public class NBTOperatorButton extends Button {
    public static final ResourceLocation BUTTONS_TEXTURE =
            new ResourceLocation(NBTEdit.MODID, "textures/gui/widgets.png");

    protected int buttonId;

    private final NBTEditGui gui;

    public NBTOperatorButton(int id, int x, int y, NBTEditGui parent, OnPress onPressed, Function<Supplier<MutableComponent>, Component> createNarration) {
        super(x, y, 9, 9, Component.literal(NBTHelper.getNameByButton((byte) id)), onPressed, RenderHelper.getTooltip(createNarration.apply(Component::empty)));

        buttonId = id;
        gui = parent;
    }

    public boolean isMouseInside(int mouseX, int mouseY) {
        return isActive() && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public byte getButtonId() {
        return (byte) buttonId;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (isMouseInside(mouseX, mouseY)) {    // checks if the mouse is over the button
            fill(poseStack, x, y, x + width, y + height, 0x80ffffff);   // draw a grayish background
        }

        if (isActive()) {
            RenderSystem.setShaderTexture(0, BUTTONS_TEXTURE);
            blit(poseStack, x, y, width, height, (buttonId - 1) * 16, 0, 16, 16, 512, 512); //Draw the texture
        }
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        this.onTooltip.narrateTooltip((component) -> narrationElementOutput.add(NarratedElementType.HINT, component));
    }
}
