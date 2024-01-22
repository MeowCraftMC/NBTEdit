package cx.rain.mc.nbtedit.utility;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public static Button.OnTooltip getTooltip(Component component) {
        return getTooltip(component, component);
    }

    public static Button.OnTooltip getTooltip(Component component, Component narration) {
        return new Button.OnTooltip() {
            @Override
            public void onTooltip(Button button, PoseStack poseStack, int mouseX, int mouseY) {
                RenderHelper.renderTooltip(poseStack, component, mouseX, mouseY);
            }

            @Override
            public void narrateTooltip(Consumer<Component> contents) {
                contents.accept(narration);
            }
        };
    }

    public static void renderTooltip(PoseStack poseStack, Component tooltip, int mouseX, int mouseY) {
        var width = Minecraft.getInstance().getWindow().getWidth();
        var height = Minecraft.getInstance().getWindow().getHeight();

        if (tooltip.getString().isBlank()) {
            return;
        }

        var tooltips = Minecraft.getInstance().font.split(tooltip, width / 6).stream().map(ClientTooltipComponent::create).toList();

        int i = 0;
        int j = tooltips.size() == 1 ? -2 : 0;

        ClientTooltipComponent clientTooltipComponent;
        for(Iterator<ClientTooltipComponent> it = tooltips.iterator(); it.hasNext(); j += clientTooltipComponent.getHeight()) {
            clientTooltipComponent = it.next();
            int k = clientTooltipComponent.getWidth(Minecraft.getInstance().font);
            if (k > i) {
                i = k;
            }
        }

        int l = mouseX + 12;
        int m = mouseY - 12;
        if (l + i > width) {
            l -= 28 + i;
        }

        if (m + j + 6 > height) {
            m = height - j - 6;
        }

        poseStack.pushPose();
        var o = -267386864;
        var p = 1347420415;
        var q = 1344798847;
        float f = Minecraft.getInstance().getItemRenderer().blitOffset;
        Minecraft.getInstance().getItemRenderer().blitOffset = 400.0F;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix4f = poseStack.last().pose();
        fillGradient(matrix4f, bufferBuilder, l - 3, m - 4, l + i + 3, m - 3, 400, o, o);
        fillGradient(matrix4f, bufferBuilder, l - 3, m + j + 3, l + i + 3, m + j + 4, 400, o, o);
        fillGradient(matrix4f, bufferBuilder, l - 3, m - 3, l + i + 3, m + j + 3, 400, o, o);
        fillGradient(matrix4f, bufferBuilder, l - 4, m - 3, l - 3, m + j + 3, 400, o, o);
        fillGradient(matrix4f, bufferBuilder, l + i + 3, m - 3, l + i + 4, m + j + 3, 400, o, o);
        fillGradient(matrix4f, bufferBuilder, l - 3, m - 3 + 1, l - 3 + 1, m + j + 3 - 1, 400, p, q);
        fillGradient(matrix4f, bufferBuilder, l + i + 2, m - 3 + 1, l + i + 3, m + j + 3 - 1, 400, p, q);
        fillGradient(matrix4f, bufferBuilder, l - 3, m - 3, l + i + 3, m - 3 + 1, 400, p, p);
        fillGradient(matrix4f, bufferBuilder, l - 3, m + j + 2, l + i + 3, m + j + 3, 400, q, q);
        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BufferUploader.drawWithShader(bufferBuilder.end());
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        poseStack.translate(0.0, 0.0, 400.0);
        int s = m;

        int t;
        ClientTooltipComponent clientTooltipComponent2;
        for(t = 0; t < tooltips.size(); ++t) {
            clientTooltipComponent2 = tooltips.get(t);
            clientTooltipComponent2.renderText(Minecraft.getInstance().font, l, s, matrix4f, bufferSource);
            s += clientTooltipComponent2.getHeight() + (t == 0 ? 2 : 0);
        }

        bufferSource.endBatch();
        poseStack.popPose();
        s = m;

        for(t = 0; t < tooltips.size(); ++t) {
            clientTooltipComponent2 = tooltips.get(t);
            clientTooltipComponent2.renderImage(Minecraft.getInstance().font, l, s, poseStack, Minecraft.getInstance().getItemRenderer(), 400);
            s += clientTooltipComponent2.getHeight() + (t == 0 ? 2 : 0);
        }

        Minecraft.getInstance().getItemRenderer().blitOffset = f;
    }
}
