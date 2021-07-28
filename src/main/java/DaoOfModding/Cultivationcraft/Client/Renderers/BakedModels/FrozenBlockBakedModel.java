package DaoOfModding.Cultivationcraft.Client.Renderers.BakedModels;

import DaoOfModding.Cultivationcraft.Common.Blocks.FrozenTileEntity;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.state.StateHolder;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FrozenBlockBakedModel implements IDynamicBakedModel
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("block/ice");

    private TextureAtlasSprite getTexture()
    {
        return Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(TEXTURE);
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData)
    {
        // TODO: Sometimes treats normal blocks as air

        BlockState frozenBlock = extraData.getData(FrozenTileEntity.FROZEN_BLOCK);

        IBakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(frozenBlock);

        // This... should not happen, but lets not let any infinite recursions occur just in case...
        if (model instanceof FrozenBlockBakedModel)
            return new ArrayList<>();

        List<BakedQuad> frozenQuads = new ArrayList<>();

        Direction dir = Direction.from3DDataValue(extraData.getData(FrozenTileEntity.RAMP_BLOCK));
        // If the frozen block is air, use the default ice model instead
        if (frozenBlock.getMaterial() == Material.AIR)
        {
            if (dir == Direction.DOWN)
                frozenQuads = Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.ICE.defaultBlockState()).getQuads(frozenBlock, side, rand, extraData);
            // If the frozen block is a ramp, use a retextured stair model instead
            else
            {
                BlockState StairState = Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairsBlock.FACING, dir).setValue(StairsBlock.HALF, Half.BOTTOM).setValue(StairsBlock.SHAPE, StairsShape.STRAIGHT).setValue(StairsBlock.WATERLOGGED, false);
                List<BakedQuad> quads = Minecraft.getInstance().getBlockRenderer().getBlockModel(StairState).getQuads(frozenBlock, side, rand, extraData);

                for (BakedQuad quad : quads)
                    frozenQuads.add(BakedModelUtils.retextureQuad(quad, getTexture()));
            }
        }
        // If the frozen block is a liquid, use the default ice model textured as that liquid, then frozen
        else if (frozenBlock.getBlock() instanceof FlowingFluidBlock)
        {
            List<BakedQuad> quads = Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.ICE.defaultBlockState()).getQuads(frozenBlock, side, rand, extraData);

            ResourceLocation liquidLocation = ((FlowingFluidBlock)frozenBlock.getBlock()).getFluid().getAttributes().getStillTexture();
            TextureAtlasSprite liquidTexture = Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(liquidLocation);

            for (BakedQuad quad : quads)
            {
                frozenQuads.add(BakedModelUtils.retextureQuad(quad, liquidTexture));
                frozenQuads.add(quad);
            }
        }
        else
        {
            List<BakedQuad> quads = model.getQuads(frozenBlock, side, rand, extraData);

            // Loop though each quad, first adding it to the list to be drawn
            // Then adding a copy of it textured in ice on top
            for (BakedQuad quad : quads)
            {
                frozenQuads.add(quad);
                frozenQuads.add(BakedModelUtils.retextureQuad(quad, getTexture()));
            }
        }

        return frozenQuads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight()
    {
        return false;
    }

    @Override
    public boolean isCustomRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon()
    {
        return getTexture();
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return ItemOverrideList.EMPTY;
    }

    @Override
    public ItemCameraTransforms getTransforms()
    {
        return ItemCameraTransforms.NO_TRANSFORMS;
    }
}
