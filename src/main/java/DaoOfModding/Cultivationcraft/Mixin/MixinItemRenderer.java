package DaoOfModding.Cultivationcraft.Mixin;

import DaoOfModding.Cultivationcraft.Common.FlyingSwordController;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ItemRenderer.class)

// Yes, yes, Mixins are pure evil and all that. I tried really hard to work out another way of doing this, and couldn't.
// I feel I should be able to, it seems simple enough, but I couldn't work it out. If someone wants to tell me how to do
// this without resorting to Mixins, please, let me know

@Implements(@Interface(iface = IResourceManagerReloadListener.class, prefix = "ItemRenderer$"))
public abstract class MixinItemRenderer
{
    private static final ResourceLocation FLYINGSWORDICON = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/flyingswordicon.png");

    @Shadow
    public float zLevel;

    @Shadow
    @Final
    private TextureManager textureManager;

    // Base texture mapping coordinates
    private final float minU = 0;
    private final float maxU = 1;
    private final float minV = 0;
    private final float maxV = 1;

    @Inject(method = "renderItemOverlayIntoGUI", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getCount()I"))
    protected void onUpdate(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text, CallbackInfo ci)
    {
        // If the specified itemStack is a flying sword
        if (FlyingSwordController.isFlying(stack))
        {
            // Setup the MatrixStack we're using
            MatrixStack matrixstack = new MatrixStack();
            matrixstack.translate(0.0D, 0.0D, (double)(this.zLevel + 200.0F));

            // Draw the flying sword icon
            drawTexture(matrixstack, xPosition, yPosition, 16, 16, FLYINGSWORDICON);
        }
    }

    private void drawTexture(MatrixStack matrixstack, int x, int y, int width, int height, ResourceLocation texture)
    {
        float x1 = x;
        float x2 = x + width;
        float y1 = y;
        float y2 = y + height;

        // Bind the texture to output
        textureManager.bindTexture(texture);

        // Draw the texture
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(matrixstack.getLast().getMatrix(), (float)x1, (float)y2, (float)0).tex(minU, maxV).endVertex();
        bufferbuilder.pos(matrixstack.getLast().getMatrix(), (float)x2, (float)y2, (float)0).tex(maxU, maxV).endVertex();
        bufferbuilder.pos(matrixstack.getLast().getMatrix(), (float)x2, (float)y1, (float)0).tex(maxU, minV).endVertex();
        bufferbuilder.pos(matrixstack.getLast().getMatrix(), (float)x1, (float)y1, (float)0).tex(minU, minV).endVertex();
        bufferbuilder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(bufferbuilder);
    }

}
