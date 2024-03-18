package DaoOfModding.Cultivationcraft.Common.Blocks;

import DaoOfModding.Cultivationcraft.Common.Blocks.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FrozenBlockEntity extends BlockEntity implements TickableBlockEntity {
    private int FREEZE_PROGRESS_TICKS = 0;
    private static int FREEZE_DURATION_TICKS = 50;
    private BlockState previousBlockState;
    private BlockPos frozenBlockPos;

    public FrozenBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockRegister.FROZEN_BLOCK_ENTITY.get(), blockPos, blockState);
    }


    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        /*CompoundTag cultivationCraftModData = tag.getCompound(Cultivationcraft.MODID);
        this.counter = cultivationCraftModData.getInt("Counter");*/
    }

    /*@Override
    public void load(BlockState state, CompoundTag NBT) {
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
    }*/

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        /*var cultivationCraftModData = new CompoundTag();
        cultivationCraftModData.putInt("Counter", this.counter);
        tag.put(Cultivationcraft.MODID, cultivationCraftModData);*/
    }

   /* @Override
    public CompoundTag save(CompoundTag NBT) {
        super.save(NBT);
        NBT.putInt("unfreezeTicks", unfreezeTicks);
        NBT.putInt("ramp", ramp.get3DDataValue());
        NBT.put("FrozenBlock", NBTUtil.writeBlockState(frozenBlock));

        if (frozenEntity != null)
            NBT.put("FrozenEntity", frozenEntity.getTileData());

        return NBT;
    }*/

    @Override
    public void onLoad() {
        super.onLoad();
        System.out.println("onLoad");
        freezeBlock(this.getBlockPos());
    }

    public void freezeBlock(BlockPos blockPos) {
        Level world = this.getLevel();
        if (world != null) {
            this.frozenBlockPos = blockPos;
            this.previousBlockState = world.getBlockState(blockPos);
            System.out.println("previous block state: " + this.previousBlockState);
            System.out.println("frozen block pos: " + this.frozenBlockPos);

            // Replace the block with a "frozen" state
            world.setBlockAndUpdate(blockPos, BlockRegister.FROZEN_BLOCK.get().defaultBlockState());
            setChanged();
            // Schedule thawing after a set duration
            world.scheduleTick(blockPos, BlockRegister.FROZEN_BLOCK.get(), FREEZE_DURATION_TICKS);
            System.out.println("replaced block with frozen state");
        }
    }

    public void thawBlock() {
        System.out.println("inside thawBlock method");
        Level world = this.getLevel();
        System.out.println("world is not null: " + (world != null));
        System.out.println("previousBlockState is not null: " + (this.previousBlockState != null));
        System.out.println("frozenBlockPos is not null: " + (this.frozenBlockPos != null));
        if (world != null && this.previousBlockState != null && this.frozenBlockPos != null) {
            System.out.println("reverting block to previous state");
            // Revert the block to its previous state
            world.setBlockAndUpdate(this.frozenBlockPos, this.previousBlockState);
        }
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }

        FrozenBlockEntity frozenBlockEntity = (FrozenBlockEntity) blockEntity;
        // Do nothing if tile entity has infinite freeze duration
        if (frozenBlockEntity.FREEZE_PROGRESS_TICKS >= FREEZE_DURATION_TICKS) {
            return;
        }

        frozenBlockEntity.FREEZE_PROGRESS_TICKS++;
        setChanged(level, blockPos, blockState);
        System.out.println("freeze progress ticks: " + frozenBlockEntity.FREEZE_PROGRESS_TICKS);
        // Replace this block with its unfrozen version (Don't update neighbouring blocks so grass and things don't break)
        if (frozenBlockEntity.FREEZE_PROGRESS_TICKS == FREEZE_DURATION_TICKS) {
            frozenBlockEntity.thawBlock();
            frozenBlockEntity.FREEZE_PROGRESS_TICKS = 0;
            setChanged(level, blockPos, blockState);
        }
    }

}

/*
public class FrozenBlockEntity extends BlockEntity implements ITickableBlockEntity {
    public static final ModelProperty<BlockState> FROZEN_BLOCK = new ModelProperty<>();
    public static final ModelProperty<Integer> RAMP_BLOCK = new ModelProperty<>();

    protected int unfreezeTicks = -1;
    protected BlockState frozenBlock = Blocks.AIR.defaultBlockState();
    protected BlockEntity frozenEntity = null;
    protected Direction ramp = Direction.DOWN;

    protected Boolean isClient = false;

    public FrozenBlockEntity() {
        super(BlockRegister.frozenBlockEntityType);
    }

    public void setUnfreezeTicks(int ticks) {
        unfreezeTicks = ticks;
    }

    public void setFrozenBlock(BlockState state, BlockEntity entity) {
        frozenBlock = state;
        frozenEntity = entity;

        setChanged();
    }

    public void setIsClient() {
        isClient = true;
    }

    public BlockState getFrozenBlock() {
        return frozenBlock;
    }

    @Override
    public void tick() {
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

    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(FROZEN_BLOCK, frozenBlock).withInitial(RAMP_BLOCK, ramp.get3DDataValue()).build();
    }

    @Override
    public SUpdateBlockEntityPacket getUpdatePacket() {
        CompoundTag nbtTagCompound = new CompoundTag();
        save(nbtTagCompound);

        return new SUpdateBlockEntityPacket(worldPosition, 76, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateBlockEntityPacket pkt) {
        load(getBlockState(), pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbtTagCompound = new CompoundTag();
        save(nbtTagCompound);
        return nbtTagCompound;
    }

    public Direction getRamp() {
        return ramp;
    }

    public void setRamp(Direction dir) {
        ramp = dir;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundTag tag) {
        load(state, tag);
    }

    @Override
    public CompoundTag save(CompoundTag NBT) {
        super.save(NBT);
        NBT.putInt("unfreezeTicks", unfreezeTicks);
        NBT.putInt("ramp", ramp.get3DDataValue());
        NBT.put("FrozenBlock", NBTUtil.writeBlockState(frozenBlock));

        if (frozenEntity != null)
            NBT.put("FrozenEntity", frozenEntity.getTileData());

        return NBT;
    }

    @Override
    public void load(BlockState state, CompoundTag NBT) {
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
*/
