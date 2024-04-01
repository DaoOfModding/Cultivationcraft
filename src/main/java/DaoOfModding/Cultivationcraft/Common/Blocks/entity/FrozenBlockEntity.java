package DaoOfModding.Cultivationcraft.Common.Blocks.entity;

import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import DaoOfModding.Cultivationcraft.Common.Blocks.custom.FrozenBlock;
import DaoOfModding.Cultivationcraft.Common.Blocks.util.TickableBlockEntity;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FrozenBlockEntity extends BlockEntity implements TickableBlockEntity {
    private ItemStack itemStack;
    protected int FREEZE_PROGRESS_TICKS = 0;
    protected static int FREEZE_DURATION_TICKS = 50;
    protected BlockPos frozenBlockPos;
    protected BlockState frozenBlock;
    protected BlockEntity frozenEntity;

    protected CompoundTag frozenEntityData;

    public FrozenBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockRegister.FROZEN_BLOCK_ENTITY.get(), blockPos, blockState);
        this.frozenBlockPos = blockPos;
        this.frozenBlock = ((FrozenBlock) blockState.getBlock()).getOldBlockState() != null ? ((FrozenBlock) blockState.getBlock()).getOldBlockState() : Blocks.AIR.defaultBlockState();
        this.frozenEntity = ((FrozenBlock) blockState.getBlock()).getOldBlockEntity();
        this.frozenEntityData = ((FrozenBlock) blockState.getBlock()).getOldBlockEntityData();
        this.itemStack = itemStack.getItem().getDefaultInstance();
    }

    public FrozenBlockEntity(BlockPos blockPos, BlockState blockState, BlockState oldBlockState, @Nullable BlockEntity frozenBlockEntity, @Nullable CompoundTag frozenEntityData) {
        super(BlockRegister.FROZEN_BLOCK_ENTITY.get(), blockPos, blockState);
        this.frozenBlockPos = blockPos;
        this.frozenBlock = oldBlockState != null ? oldBlockState : Blocks.AIR.defaultBlockState();
        this.frozenEntity = frozenBlockEntity;
        this.frozenEntityData = frozenEntityData;
        this.itemStack = new ItemStack(this.frozenBlock.getBlock().asItem());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        CompoundTag cultivationCraftData = tag.getCompound(Cultivationcraft.MODID);
        FREEZE_PROGRESS_TICKS = cultivationCraftData.getInt("unfreezeTicks");

        this.frozenBlock = NbtUtils.readBlockState(cultivationCraftData.getCompound("FrozenBlock"));
        this.frozenBlockPos = NbtUtils.readBlockPos(cultivationCraftData.getCompound("FrozenBlockPos"));

        if (cultivationCraftData.hasUUID("FrozenEntity")) {
            this.frozenEntity = loadStatic(this.frozenBlockPos, this.frozenBlock, cultivationCraftData);
            this.frozenEntityData = cultivationCraftData.getCompound("FrozenEntityData");
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        var cultivationCraftData = new CompoundTag();
        cultivationCraftData.putInt("unfreezeTicks", FREEZE_PROGRESS_TICKS);

        cultivationCraftData.put("FrozenBlock", NbtUtils.writeBlockState(frozenBlock));
        cultivationCraftData.put("FrozenBlockPos", NbtUtils.writeBlockPos(frozenBlockPos));

        if (frozenEntity != null) {
            cultivationCraftData.put("FrozenEntity", frozenEntity.getPersistentData());
            cultivationCraftData.put("FrozenEntityData", frozenEntityData);
        }
        tag.put(Cultivationcraft.MODID, cultivationCraftData);
    }

    public ItemStack getRenderStack() {
        ItemStack stack;

        if (frozenBlock != null) {
            stack = itemStack.getItem().getDefaultInstance();
        } else {
            stack = new ItemStack(Blocks.AIR.asItem());
        }

        return stack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return super.getUpdatePacket();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        load(Objects.requireNonNull(pkt.getTag()));
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
    }

    public void thawBlock() {
        Level world = this.getLevel();
        if (world != null && this.frozenBlock != null && this.frozenBlockPos != null) {
            // Revert the block to its previous state
            world.setBlockAndUpdate(this.frozenBlockPos, this.frozenBlock);
            if (frozenEntity != null) {
                BlockEntity unFrozenEntity = this.frozenEntity;
                unFrozenEntity.deserializeNBT(frozenEntityData);
                world.setBlockEntity(unFrozenEntity);
            }
        }
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }

        FrozenBlockEntity frozenBlockEntity = (FrozenBlockEntity) blockEntity;
        frozenBlockEntity.FREEZE_PROGRESS_TICKS++;

        if (frozenBlockEntity.FREEZE_PROGRESS_TICKS >= FREEZE_DURATION_TICKS) {
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
