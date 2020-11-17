package DaoOfModding.Cultivationcraft.Client.Renderers.BakedModels;

import DaoOfModding.Cultivationcraft.Common.Blocks.FrozenTileEntity;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData)
    {
        BlockState frozenBlock = extraData.getData(FrozenTileEntity.FROZEN_BLOCK);

        IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(frozenBlock);

        // This... should not happen, but lets not let any infinite recursions occur just in case...
        if (model instanceof FrozenBlockBakedModel)
            return new ArrayList<>();

        List<BakedQuad> quads = model.getQuads(frozenBlock, side, rand, extraData);
        List<BakedQuad> frozenQuads = new ArrayList<>();

        // If there are no quads for this model, use the default ice model instead
        if (quads.size() == 0)
            frozenQuads = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(Blocks.ICE.getDefaultState()).getQuads(frozenBlock, side, rand, extraData);
        else
        {
            // Loop though each quad, first adding it to the list to be drawn
            // Then adding a copy of it textured in ice on top
            for (BakedQuad quad : quads) {
                frozenQuads.add(quad);
                frozenQuads.add(BakedModelUtils.retextureQuad(quad, getTexture()));
            }
        }

        return frozenQuads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    // WHAT IS THIS!? STUPID OBFUSCATED CODE >:(
    @Override
    public boolean func_230044_c_()
    {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return getTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }
}
