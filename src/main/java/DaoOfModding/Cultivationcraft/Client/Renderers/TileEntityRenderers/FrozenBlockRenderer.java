package DaoOfModding.Cultivationcraft.Client.Renderers.TileEntityRenderers;

import DaoOfModding.Cultivationcraft.Common.Blocks.FrozenTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class FrozenBlockRenderer extends TileEntityRenderer<FrozenTileEntity>
{
    public FrozenBlockRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    public void render(FrozenTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        BlockState blockstate = tileEntityIn.getFrozenBlock();
        Block block = blockstate.getBlock();

        matrixStackIn.pushPose();

        matrixStackIn.popPose();
    }
}
