package DaoOfModding.Cultivationcraft.Client.Renderers.BlockEntityRenderers;

import DaoOfModding.Cultivationcraft.Common.Blocks.custom.FrozenBlock;
import DaoOfModding.Cultivationcraft.Common.Blocks.entity.FrozenBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

public class FrozenBlockEntityRenderer implements BlockEntityRenderer<FrozenBlockEntity> {
    public FrozenBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(FrozenBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BlockState blockState = ((FrozenBlockEntity) pBlockEntity).getFrozenBlock();
        String name = blockState.getBlock().getName().getString();

        pPoseStack.pushPose();
        pPoseStack.translate(0.0005D, 0.0005D, 0.0005D);
        pPoseStack.scale(0.999F, 0.999F, 0.999F);

        if (name.contains("Chest")) renderChest(pBlockEntity, pPoseStack);
        if (!((FrozenBlock) pBlockEntity.getBlockState().getBlock()).getIsSecondBlock()) {
            blockRenderer.renderSingleBlock(blockState, pPoseStack, pBufferSource, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()), pPackedOverlay);
        }
        pPoseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }

    private void renderChest(FrozenBlockEntity pBlockEntity, PoseStack pPoseStack) {
        float f = pBlockEntity.getBlockState().getValue(FrozenBlock.FACING).toYRot();
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(-f));

        switch (pBlockEntity.getBlockState().getValue(FrozenBlock.FACING)) {
            case NORTH -> pPoseStack.translate(-1D, 0D, -1D);
            case SOUTH -> pPoseStack.translate(0D, 0D, 0D);
            case WEST -> pPoseStack.translate(0D, 0D, -1D);
            case EAST -> pPoseStack.translate(-1D, 0D, 0D);
        }
    }
}
