package cx.rain.mc.nbtedit.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.NBTEditGui;
import cx.rain.mc.nbtedit.nbt.NBTTree;
import cx.rain.mc.nbtedit.nbt.NBTHelper;
import net.minecraft.client.Minecraft;
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
        return mouseX >= getX() && mouseY >= getY() && mouseX < width + getX() && mouseY < height + getY();
    }

    public boolean isMouseInsideSpoiler(int mouseX, int mouseY) {
        return mouseX >= getX() - 9 && mouseY >= getY() && mouseX < getX() && mouseY < getY() + height;
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
        setY(getY() + offsetY);
    }

    public boolean shouldRender(int top, int bottom) {
        return getY() + height >= top && getY() <= bottom;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {
        narration.add(NarratedElementType.TITLE, text);
    }

    @Override
    public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        var isSelected = gui.getFocused() == node;
        var isTextHover = isMouseInsideText(mouseX, mouseY);
        var isSpoilerHover = isMouseInsideSpoiler(mouseX, mouseY);
        var color = isSelected ? 0xff : isTextHover ? 16777120 : (node.hasParent()) ? 14737632 : -6250336;

        if (isSelected) {
            fill(poseStack, getX() + 11, getY(), getX() + width, getY() + height, Integer.MIN_VALUE);
        }

        var w = 18;
        var h = 16;
        var u = 0;
        if (node.shouldShowChildren()) {
            w = 16;
            h = 18;
            if (isSpoilerHover) {
                u = 18 + 18 + 18;
            } else {
                u = 18;
            }
        } else {
            if (isSpoilerHover) {
                u = 18 + 16;
            }
        }

        if (node.hasChild()) {
            RenderSystem.setShaderTexture(0, WIDGET_TEXTURE);
            blit(poseStack, getX() - 9, getY(), 9, height, u, 16, w, h, 512, 512);
        }

        RenderSystem.setShaderTexture(0, WIDGET_TEXTURE);
        blit(poseStack, getX() + 1, getY(), 9, height, (node.getTag().getId() - 1) * 16, 0, 16, 16, 512, 512);
        getMinecraft().font.draw(poseStack, text, getX() + 11, getY() + (this.height - 8) / 2, color);
    }
}
