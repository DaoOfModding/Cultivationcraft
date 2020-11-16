package DaoOfModding.Cultivationcraft.Common.Blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class FrozenBlock extends Block
{
    public FrozenBlock()
    {
        super(AbstractBlock.Properties.create(Material.ICE).hardnessAndResistance(-1.0F, 3600000.0F).slipperiness(0.99f).noDrops());
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
}
