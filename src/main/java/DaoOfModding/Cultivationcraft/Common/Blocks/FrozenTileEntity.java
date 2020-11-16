package DaoOfModding.Cultivationcraft.Common.Blocks;

import DaoOfModding.Cultivationcraft.Common.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

public class FrozenTileEntity extends TileEntity implements ITickableTileEntity
{
    private int unfreezeTicks = -1;
    private BlockState frozenBlock = Blocks.AIR.getDefaultState();
    private TileEntity frozenEntity = null;

    public FrozenTileEntity()
    {
        super(BlockRegister.frozenTileEntityType);
    }

    public void setUnfreezeTicks(int ticks)
    {
        unfreezeTicks = ticks;
    }

    public void setFrozenBlock(BlockState state, TileEntity entity)
    {
        frozenBlock = state;
        frozenEntity = entity;
    }

    public BlockState getFrozenBlock()
    {
        return frozenBlock;
    }

    @Override
    public void tick()
    {
        if (!this.hasWorld())
            return;

        /*if (world.isRemote)
            return;*/

        // Do nothing if tile entity has infinite freeze duration
        if (unfreezeTicks == -1)
            return;

        unfreezeTicks -= 1;

        // Do nothing if there is still time left on the freeze
        if (unfreezeTicks > 0)
            return;

        // Replace this block with its unfrozen version
        world.setBlockState(pos, frozenBlock);

        if (frozenEntity != null)
            world.setTileEntity(pos, frozenEntity);
    }

    // TODO: Texture for frozen block to be added here
    /*
    @Override
    public IModelData getModelData()
    {

    }*/

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);

        return new SUpdateTileEntityPacket(this.pos, 76, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        read(getBlockState(), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag)
    {
        read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT NBT)
    {
        super.write(NBT);
        NBT.putInt("unfreezeTicks", unfreezeTicks);
        NBT.put("FrozenBlock", NBTUtil.writeBlockState(frozenBlock));

        if (frozenEntity != null)
            NBT.put("FrozenEntity", frozenEntity.getTileData());

        return NBT;
    }

    @Override
    public void read(BlockState state, CompoundNBT NBT)
    {
        super.read(state, NBT);
        unfreezeTicks = NBT.getInt("unfreezeTicks");
        frozenBlock = NBTUtil.readBlockState(NBT.getCompound("FrozenBlock"));

        if (NBT.hasUniqueId("FrozenEntity"))
            frozenEntity = readTileEntity(frozenBlock, NBT);
    }
}
