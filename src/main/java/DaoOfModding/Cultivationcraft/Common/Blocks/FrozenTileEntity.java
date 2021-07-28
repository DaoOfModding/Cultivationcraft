package DaoOfModding.Cultivationcraft.Common.Blocks;

import DaoOfModding.Cultivationcraft.Common.BlockRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

public class FrozenTileEntity extends TileEntity implements ITickableTileEntity
{
    public static final ModelProperty<BlockState> FROZEN_BLOCK = new ModelProperty<>();
    public static final ModelProperty<Integer> RAMP_BLOCK = new ModelProperty<>();

    private int unfreezeTicks = -1;
    private BlockState frozenBlock = Blocks.AIR.defaultBlockState();
    private TileEntity frozenEntity = null;
    private Direction ramp = Direction.DOWN;

    private Boolean isClient = false;

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

        setChanged();
    }

    public void setIsClient()
    {
        isClient = true;
    }

    public BlockState getFrozenBlock()
    {
        return frozenBlock;
    }

    @Override
    public void tick()
    {
        if (!this.hasLevel())
            return;

        // Do nothing on client unless this is a client instance
        if (this.level.isClientSide && !isClient)
            return;

        // Do nothing if tile entity has infinite freeze duration
        if (unfreezeTicks == -1)
            return;

        unfreezeTicks -= 1;

        // Do nothing if there is still time left on the freeze
        if (unfreezeTicks > 0)
            return;

        // Replace this block with its unfrozen version (Don't update neighbouring blocks so grass and things don't break)
        level.setBlock(worldPosition, frozenBlock, 1 + 2 + 16 + 32);

        if (frozenEntity != null)
            level.setBlockEntity(worldPosition, frozenEntity);
    }

    public void setRamp(Direction dir)
    {
        ramp = dir;
    }

    @Override
    public IModelData getModelData()
    {
        return new ModelDataMap.Builder().withInitial(FROZEN_BLOCK, frozenBlock).withInitial(RAMP_BLOCK, ramp.get3DDataValue()).build();
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        save(nbtTagCompound);

        return new SUpdateTileEntityPacket(worldPosition, 76, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        load(getBlockState(), pkt.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        save(nbtTagCompound);
        return nbtTagCompound;
    }

    public Direction getRamp()
    {
        return ramp;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag)
    {
        load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT NBT)
    {
        super.save(NBT);
        NBT.putInt("unfreezeTicks", unfreezeTicks);
        NBT.putInt("ramp", ramp.get3DDataValue());
        NBT.put("FrozenBlock", NBTUtil.writeBlockState(frozenBlock));

        if (frozenEntity != null)
            NBT.put("FrozenEntity", frozenEntity.getTileData());

        return NBT;
    }

    @Override
    public void load(BlockState state, CompoundNBT NBT)
    {
        super.load(state, NBT);
        unfreezeTicks = NBT.getInt("unfreezeTicks");

        Direction oldRamp = ramp;
        ramp = Direction.from3DDataValue(NBT.getInt("ramp"));

        Block oldFreeze = frozenBlock.getBlock();
        frozenBlock = NBTUtil.readBlockState(NBT.getCompound("FrozenBlock"));

        if (NBT.hasUUID("FrozenEntity"))
            frozenEntity = loadStatic(frozenBlock, NBT);

        // If the frozen block or ramp status has changed, refresh the model data
        if (this.level != null && this.level.isClientSide())
            if (oldFreeze != frozenBlock.getBlock() || oldRamp != ramp)
                net.minecraftforge.client.model.ModelDataManager.requestModelDataRefresh(this);

        // As soon as data is read in for this block, mark it as not being a client block
        isClient = false;
    }
}
