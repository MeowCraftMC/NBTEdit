package cx.rain.mc.nbtedit.utility;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class RenderHelper {

    public static void drawGrayBackground(GuiGraphics graphics) {
        graphics.pose().last().pose().translate(0, 0, -1);
        graphics.fillGradient(0, 0, graphics.guiWidth(), graphics.guiHeight(), -1072689136, -804253680);
        graphics.pose().last().pose().translate(0, 0, 1);
    }

    public static void blitTexture(GuiGraphics graphics, ResourceLocation texture, int x, int y,
                                   int width, int height, int blitOffset) {
        blitTexture(graphics, texture, x, y, width, height, blitOffset,
                0, 0, width, height, 256, 256);
    }

    public static void blitTexture(GuiGraphics graphics, ResourceLocation texture, int x, int y,
                                   int width, int height,
                                   int uStart, int vStart, int uWidth, int vHeight,
                                   int textureWidth, int textureHeight) {
        blitTexture(graphics, texture, x, y, width, height, 1,
                uStart, vStart, uWidth, vHeight, textureWidth, textureHeight);
    }

    public static void blitTexture(GuiGraphics graphics, ResourceLocation texture, int x, int y,
                                   int width, int height, int blitOffset,
                                   int uStart, int vStart, int uWidth, int vHeight,
                                   int textureWidth, int textureHeight) {
        var x1 = (float) x;
        var x2 = (float) x + width;
        var y1 = (float) y;
        var y2 = (float) y + height;

        var minU = ((float) uStart) / textureWidth;
        var minV = ((float) vStart) / textureHeight;
        var maxU = ((float) (uStart + uWidth)) / textureWidth;
        var maxV = ((float) (vStart + vHeight)) / textureHeight;

        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix4f, x1, y1, blitOffset).uv(minU, minV).endVertex();
        bufferBuilder.vertex(matrix4f, x1, y2, blitOffset).uv(minU, maxV).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y2, blitOffset).uv(maxU, maxV).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y1, blitOffset).uv(maxU, minV).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
    }

    public static void prepareDrawText(GuiGraphics graphics) {
        var poseStack = graphics.pose();
        var pose = poseStack.last().pose();
        var vec4 = new Vector4f(0, 0, 0, 2);
        pose.add(new Matrix4f(vec4, vec4, vec4, vec4));
    }
}
