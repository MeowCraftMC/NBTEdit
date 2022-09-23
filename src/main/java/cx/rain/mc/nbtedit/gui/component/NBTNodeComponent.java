package cx.rain.mc.nbtedit.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.NBTEditGui;
import cx.rain.mc.nbtedit.nbt.NBTTree;
import cx.rain.mc.nbtedit.nbt.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class NBTNodeComponent extends AbstractWidget {
    public static final ResourceLocation WIDGET_TEXTURE =
            new ResourceLocation(NBTEdit.MODID, "textures/gui/widgets.png");

    protected String text;
    protected NBTTree.Node<?> node;
    protected NBTEditGui gui;

    private final Minecraft minecraft = Minecraft.getInstance();

    public NBTNodeComponent(int x, int y, Component textIn, NBTEditGui guiIn, NBTTree.Node<?> nodeIn) {
        super(x, y, 0, Minecraft.getInstance().font.lineHeight, textIn);

        text = textIn.getString();

        gui = guiIn;
        node = nodeIn;

        update();
    }

    protected Minecraft getMinecraft() {
        return minecraft;
    }

    public NBTTree.Node<?> getNode() {
        return node;
    }

    protected void update() {
        text = NBTHelper.getNBTNameSpecial(node);
        width = minecraft.font.width(text) + 12;
    }

    public boolean isMouseInsideText(int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < width + x && mouseY < height + y;
    }

    public boolean isMouseInsideSpoiler(int mouseX, int mouseY) {
        return mouseX >= x - 9 && mouseY >= y && mouseX < x && mouseY < y + height;
    }

    public boolean shouldShowChildren() {
        return node.shouldShowChildren();
    }

    public boolean isTextClicked(int mouseX, int mouseY) {
        return isMouseInsideText(mouseX, mouseY);
    }

    public boolean isSpoilerClicked(int mouseX, int mouseY) {
        return isMouseInsideSpoiler(mouseX, mouseY);
    }

    public boolean spoilerClicked(int mouseX, int mouseY) {
        if (node.hasChild() && isMouseInsideSpoiler(mouseX, mouseY)) {
            node.setShowChildren(!node.shouldShowChildren());
            return true;
        }
        return false;
    }

    public void shiftY(int offsetY) {
        y += offsetY;
    }

    public boolean shouldRender(int top, int bottom) {
        return y + height >= top && y <= bottom;
    }

    @Override
    public void updateNarration(NarrationElementOutput narration) {
        narration.add(NarratedElementType.TITLE, text);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        var isSelected = gui.getFocused() == node;
        var isTextHover = isMouseInsideText(mouseX, mouseY);
        var isSpoilerHover = isMouseInsideSpoiler(mouseX, mouseY);
        var color = isSelected ? 0xff : isTextHover ? 16777120 : (node.hasParent()) ? 14737632 : -6250336;

        RenderSystem.setShaderTexture(0, WIDGET_TEXTURE);

        if (isSelected) {
            Gui.fill(stack, x + 11, y, x + width, y + height, Integer.MIN_VALUE);
        }

        if (node.hasChild()) {
            blit(stack, x - 9, y, (node.shouldShowChildren()) ? 9 : 0, (isSpoilerHover) ? height : 0, 9, height);
        }

        blit(stack, x + 1, y, (node.getTag().getId() - 1) * 9, 18, 9, 9);
        drawString(stack, getMinecraft().font, text, x + 11, y + (this.height - 8) / 2, color);
    }
}
