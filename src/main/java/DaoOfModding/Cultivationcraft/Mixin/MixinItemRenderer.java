package DaoOfModding.Cultivationcraft.Mixin;

import DaoOfModding.Cultivationcraft.Common.FlyingSwordController;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManagerReloadListener;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
    @Shadow
    public float zLevel;

    @Inject(method = "renderItemOverlayIntoGUI", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getCount()I"))
    protected void onUpdate(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text, CallbackInfo ci)
    {
        // If the specified itemStack is a flying sword
        if (FlyingSwordController.isFlying(stack))
        {
            // TODO: Something better than this, an Icon preferably
            MatrixStack matrixstack = new MatrixStack();
            String s = "F";
            matrixstack.translate(0.0D, 0.0D, (double)(this.zLevel + 200.0F));
            IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
            fr.renderString(s, (float)(xPosition + 19 - 2 - fr.getStringWidth(s)), (float)(yPosition + 6 + 3), 16777215, true, matrixstack.getLast().getMatrix(), irendertypebuffer$impl, false, 0, 15728880);
            irendertypebuffer$impl.finish();
        }
    }

}
