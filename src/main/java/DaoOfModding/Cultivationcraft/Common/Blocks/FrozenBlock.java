package DaoOfModding.Cultivationcraft.Common.Blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class FrozenBlock extends Block
{
    public FrozenBlock()
    {
        super(AbstractBlock.Properties.create(Material.ICE).hardnessAndResistance(-1.0F, 3600000.0F).slipperiness(0.99f).noDrops().notSolid().variableOpacity());
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new FrozenTileEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState iBlockState) {
        return BlockRenderType.MODEL;
    }
}
