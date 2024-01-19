package cx.rain.mc.nbtedit.utility;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;

public class RenderHelper {

    public static void drawGrayBackground(PoseStack poseStack) {
        var x = Minecraft.getInstance().getWindow().getWidth();
        var y = Minecraft.getInstance().getWindow().getHeight();

        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        fillGradient(poseStack.last().pose(), bufferBuilder, 0, 0, x, y, 0, -1072689136, -804253680);
        tesselator.end();
        RenderSystem.disableBlend();
    }

    private static void fillGradient(Matrix4f matrix, BufferBuilder builder, int x1, int y1, int x2, int y2, int blitOffset, int colorA, int colorB) {
        float f = (float) FastColor.ARGB32.alpha(colorA) / 255.0F;
        float g = (float) FastColor.ARGB32.red(colorA) / 255.0F;
        float h = (float) FastColor.ARGB32.green(colorA) / 255.0F;
        float i = (float) FastColor.ARGB32.blue(colorA) / 255.0F;
        float j = (float) FastColor.ARGB32.alpha(colorB) / 255.0F;
        float k = (float) FastColor.ARGB32.red(colorB) / 255.0F;
        float l = (float) FastColor.ARGB32.green(colorB) / 255.0F;
        float m = (float) FastColor.ARGB32.blue(colorB) / 255.0F;
        builder.vertex(matrix, (float)x1, (float)y1, (float)blitOffset).color(g, h, i, f).endVertex();
        builder.vertex(matrix, (float)x1, (float)y2, (float)blitOffset).color(k, l, m, j).endVertex();
        builder.vertex(matrix, (float)x2, (float)y2, (float)blitOffset).color(k, l, m, j).endVertex();
        builder.vertex(matrix, (float)x2, (float)y1, (float)blitOffset).color(g, h, i, f).endVertex();
    }
}
