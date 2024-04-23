package DaoOfModding.Cultivationcraft.Common.Blocks.entity;

import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import DaoOfModding.Cultivationcraft.Common.Blocks.custom.FrozenBlock;
import DaoOfModding.Cultivationcraft.Common.Blocks.util.TickableBlockEntity;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

public class FrozenBlockEntity extends BlockEntity implements TickableBlockEntity {
    protected int FREEZE_PROGRESS_TICKS = 0;
    protected int FREEZE_DURATION_TICKS;
    protected ItemStack itemStack;
    protected BlockPos blockPos;
    protected BlockState oldBlockState;
    protected BlockEntity oldBlockEntity;
    protected CompoundTag oldBlockEntityData;
    protected boolean isSecondBlock;

    protected FrozenBlockEntity connectedEntity;

    public FrozenBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockRegister.FROZEN_BLOCK_ENTITY.get(), blockPos, blockState);
        this.blockPos = blockPos;
        this.oldBlockState = Blocks.AIR.defaultBlockState();
        this.oldBlockEntity = null;
        this.oldBlockEntityData = null;
        this.itemStack = new ItemStack(this.oldBlockState.getBlock().asItem());

        Property<Boolean> isSecondBlock = (Property<Boolean>) ((FrozenBlock) blockState.getBlock()).getStateDefinition().getProperty("is_second_block");
        assert isSecondBlock != null;
        this.isSecondBlock = blockState.getValue(isSecondBlock);

        Property<Integer> freezeDurationTicks = (Property<Integer>) ((FrozenBlock) blockState.getBlock()).getStateDefinition().getProperty("freeze_duration_ticks");
        assert freezeDurationTicks != null;
        this.FREEZE_DURATION_TICKS = blockState.getValue(freezeDurationTicks);
    }

    public FrozenBlockEntity(BlockPos blockPos, BlockState blockState, BlockState oldBlockState, BlockEntity oldBlockEntity, CompoundTag oldBlockEntityData, boolean isSecondBlock) {
        super(BlockRegister.FROZEN_BLOCK_ENTITY.get(), blockPos, blockState);
        this.blockPos = blockPos;
        this.oldBlockState = oldBlockState;
        this.oldBlockEntity = oldBlockEntity;
        this.oldBlockEntityData = oldBlockEntityData;
        this.itemStack = new ItemStack(this.oldBlockState.getBlock().asItem());
        this.isSecondBlock = isSecondBlock;
    }

    public void setConnected(FrozenBlockEntity second)
    {
        connectedEntity = second;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);

        CompoundTag cultivationCraftData = nbt.getCompound(Cultivationcraft.MODID);
        FREEZE_PROGRESS_TICKS = cultivationCraftData.getInt("unfreezeTicks");

        this.oldBlockState = NbtUtils.readBlockState(cultivationCraftData.getCompound("FrozenBlockState"));
        this.blockPos = NbtUtils.readBlockPos(cultivationCraftData.getCompound("FrozenBlockPos"));
        this.itemStack = new ItemStack(this.oldBlockState.getBlock().asItem());
        this.isSecondBlock = cultivationCraftData.getBoolean("isSecondBlock");

        if (cultivationCraftData.hasUUID("FrozenBlockEntity")) {
            this.oldBlockEntity = loadStatic(this.blockPos, this.oldBlockState, cultivationCraftData);
            this.oldBlockEntityData = cultivationCraftData.getCompound("FrozenBlockEntityData");
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);

        var cultivationCraftData = new CompoundTag();
        cultivationCraftData.putInt("unfreezeTicks", FREEZE_PROGRESS_TICKS);

        cultivationCraftData.put("FrozenBlockState", NbtUtils.writeBlockState(oldBlockState));
        cultivationCraftData.put("FrozenBlockPos", NbtUtils.writeBlockPos(blockPos));
        cultivationCraftData.putBoolean("isSecondBlock", isSecondBlock);

        if (oldBlockEntity != null) {
            cultivationCraftData.put("FrozenBlockEntity", oldBlockEntity.getPersistentData());
            cultivationCraftData.put("FrozenBlockEntityData", oldBlockEntityData);
        }
        nbt.put(Cultivationcraft.MODID, cultivationCraftData);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void setOldBlockFields(BlockState oldBlockState, BlockEntity oldBlockEntity, CompoundTag oldBlockEntityData) {
        this.oldBlockState = oldBlockState;
        this.oldBlockEntity = oldBlockEntity;
        this.oldBlockEntityData = oldBlockEntityData;
        setChanged();
        int rotation = 0;
    }

    public boolean isSecondBlock() {
        return isSecondBlock;
    }

    public BlockState getOldBlockState() {
        return oldBlockState;
    }

    public ItemStack getRenderStack() {
        ItemStack stack;
        if (oldBlockState != null && !isSecondBlock) {
            stack = itemStack.getItem().getDefaultInstance();
        } else {
            stack = new ItemStack(Blocks.AIR.asItem());
        }
        return stack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void thawBlock() {
        Level world = this.getLevel();
        if (world != null && this.oldBlockState != null && this.blockPos != null)
        {
            // Revert the block to its previous state
            world.setBlockAndUpdate(this.blockPos, this.oldBlockState);
            if (oldBlockEntity != null) {
                BlockEntity unFrozenEntity = this.oldBlockEntity;
                unFrozenEntity.deserializeNBT(oldBlockEntityData);
                world.setBlockEntity(unFrozenEntity);
            }

            if (connectedEntity != null)
                connectedEntity.thawBlock();
        }
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }
        this.FREEZE_PROGRESS_TICKS++;
        if (this.FREEZE_PROGRESS_TICKS >= FREEZE_DURATION_TICKS)
        {
            this.thawBlock();
            this.FREEZE_PROGRESS_TICKS = 0;
            setChanged(level, blockPos, blockState);
        }
    }


}