package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Client.ClientListeners;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class animatedTexture
{
    protected ResourceLocation texture;
    protected int frames = 1;

    public animatedTexture(ResourceLocation textureLocation)
    {
        texture = textureLocation;
    }

    public animatedTexture(ResourceLocation textureLocation, int numberOfFrames)
    {
        texture = textureLocation;
        frames = numberOfFrames;
    }

    public void render(float x, float y, int width, int height)
    {
        render(x, y, width, height,false);
    }

    public void render(PoseStack pose, float x, float y, int width, int height, float blit)
    {
        render(pose.last().pose(), x, y, width, height,1, false, blit);
    }

    public void render(float x, float y, int width, int height, boolean mirror)
    {
        render(new PoseStack().last().pose(), x, y, width, height, 1, mirror, -90);
    }

    // texHeight is a number between 0 (don't render at all) and 1 (render the full texture)
    public void render(float x, float y, int width, int height, float texHeight)
    {
        render(new PoseStack().last().pose(), x, y, width, height, texHeight, false, -90);
    }

    public void render(float x, float y, int width, int height, float texHeight, boolean mirror)
    {
        render(new PoseStack().last().pose(), x, y, width, height, texHeight, mirror, -90);
    }

    public void render(Matrix4f matrix, float x, float y, int width, int height, float texHeight, boolean mirror, float blit)
    {
        int u = 0;
        int u2 = 1;

        if (mirror)
        {
            u = 1;
            u2 = 0;
        }

        int tick = ClientListeners.tick % frames;

        float yPos = (1f / frames) * tick;
        texHeight = texHeight / frames;

        RenderSystem.setShaderTexture(0, texture);
        GlStateManager._enableBlend();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix, x, height + y, blit).uv(u, yPos).endVertex();
        bufferbuilder.vertex(matrix, x + width, height + y, blit).uv(u2, yPos).endVertex();
        bufferbuilder.vertex(matrix, x + width, y, blit).uv(u2, yPos - texHeight).endVertex();
        bufferbuilder.vertex(matrix, x, y, blit).uv(u, yPos - texHeight).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }
}
