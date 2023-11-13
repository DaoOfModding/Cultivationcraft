package DaoOfModding.Cultivationcraft.Client.GUI.ItemDecorators;

import DaoOfModding.Cultivationcraft.Common.FlyingSwordBind;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

public class FlyingSwordDecorator implements IItemDecorator
{
    public static final ResourceLocation icon = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/flyingswordicon.png");

    private static final int xSize = 16;
    private static final int ySize = 16;

    public boolean render(Font font, ItemStack stack, int xOffset, int yOffset, float blitOffset)
    {
        // Don't do anything if the item isn't bound
        if (!FlyingSwordBind.isBound(stack))
            return false;

        RenderSystem.setShaderTexture(0, icon);

        // Render as red if not the owning player
        if (FlyingSwordBind.getOwner(stack).compareTo(Minecraft.getInstance().player.getUUID()) == 0)
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        else
            RenderSystem.setShaderColor(0.8F, 0.0F, 0.0F, 1.0F);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(xOffset, yOffset + ySize, 300 + blitOffset).uv(0, 1).endVertex();
        bufferbuilder.vertex(xOffset + xSize, yOffset + ySize, 300 + blitOffset).uv(1, 1).endVertex();
        bufferbuilder.vertex(xOffset + xSize, yOffset, 300 + blitOffset).uv(1, 0).endVertex();
        bufferbuilder.vertex(xOffset, yOffset, 300 + blitOffset).uv(0, 0).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());

        return true;
    }
}
