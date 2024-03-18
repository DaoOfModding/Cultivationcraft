package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Client.ClientListeners;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
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

    public void render(int x, int y, int width, int height)
    {
        render(x, y, width, height,false);
    }

    public void render(int x, int y, int width, int height, boolean mirror)
    {
        render(x, y, width, height,1, mirror);
    }

    // texHeight is a number between 0 (don't render at all) and 1 (render the full texture)
    public void render(int x, int y, int width, int height, float texHeight)
    {
        render(x, y, width, height, texHeight, false);
    }

    public void render(int x, int y, int width, int height, float texHeight, boolean mirror)
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

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(x, height + y, -90.0D).uv(u, yPos).endVertex();
        bufferbuilder.vertex(x + width, height + y, -90.0D).uv(u2, yPos).endVertex();
        bufferbuilder.vertex(x + width, y, -90.0D).uv(u2, yPos - texHeight).endVertex();
        bufferbuilder.vertex(x, y, -90.0D).uv(u, yPos - texHeight).endVertex();
        tesselator.end();
    }
}
