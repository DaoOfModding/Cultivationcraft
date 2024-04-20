package DaoOfModding.Cultivationcraft.Common.Items;

import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import DaoOfModding.Cultivationcraft.Common.Blocks.custom.FrozenBlock;
import DaoOfModding.Cultivationcraft.Common.Blocks.entity.FrozenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Objects;

public class FreezeControl
{
    public static void Freeze(Level level, BlockPos pos, int duration)
    {
        BlockState oldState1 = getCurrentState(level, pos);
        BlockEntity oldEntity1 = getCurrentEntity(level, pos);

        BlockPos secondBlockPos = getMultiblockPos(pos, level.getBlockState(pos));

        // Just freeze this block if it is not part of a Multiblock
        if (secondBlockPos == null)
        {
            createFrozenBlock(level, pos, oldState1, oldEntity1, duration);
            return;
        }

        BlockState oldState2 = getCurrentState(level, secondBlockPos);
        BlockEntity oldEntity2 = getCurrentEntity(level, secondBlockPos);

        FrozenBlockEntity freeze = null;
        FrozenBlockEntity second = null;

        boolean isSecond = handleIsBedSecondBlock(level.getBlockState(pos));

        // Make sure you freeze the blocks in the correct order, otherwise an additional item will be spawned
        if (!isSecond)
            freeze = createFrozenBlock(level, pos, oldState1, oldEntity1, duration);

        second = createFrozenBlock(level, secondBlockPos, oldState2, oldEntity2, duration);

        if (isSecond)
            freeze = createFrozenBlock(level, pos, oldState1, oldEntity1, duration);

        if (freeze != null)
            freeze.setConnected(second);
    }

    public static BlockState getCurrentState(Level level, BlockPos pos)
    {
        return level.getBlockState(pos);
    }

    public static BlockEntity getCurrentEntity(Level level, BlockPos pos)
    {
        return level.getBlockEntity(pos);
    }

    public static FrozenBlockEntity createFrozenBlock(Level world, BlockPos blockPos, BlockState oldState, BlockEntity oldBlockEntity, int duration)
    {
        FrozenBlockEntity frozen = null;

        if (!world.isClientSide()) {
            BlockState FrozenBlock = setFrozenBlock(oldState, handleIsBedSecondBlock(oldState), duration);
            world.setBlockAndUpdate(blockPos, FrozenBlock);

            frozen = ((FrozenBlockEntity) Objects.requireNonNull(world.getBlockEntity(blockPos)));

            CompoundTag oldBlockEntityData = null;
            if (oldBlockEntity != null)
                oldBlockEntityData = oldBlockEntity.serializeNBT();

            frozen.setOldBlockFields(oldState, oldBlockEntity, oldBlockEntityData);
        }

        return frozen;
    }

    public static boolean handleIsBedSecondBlock(BlockState oldBlockState) {
        BlockState bed = oldBlockState.getBlock().getName().getString().contains("Bed") ? oldBlockState : null;
        boolean isBed = bed != null;
        boolean isSecondBlock = isBed && bed.getValue(Objects.requireNonNull(bed.getBlock().getStateDefinition().getProperty("part"))).toString().equals("foot");
        return isBed && isSecondBlock;
    }

    public static BlockPos getMultiblockPos(BlockPos blockPos, BlockState blockState) {
        String name = blockState.getBlock().getName().getString();

        if (blockState.getBlock() instanceof DoorBlock || blockState.getBlock() instanceof DoublePlantBlock) {
            Property<?> half = blockState.getBlock().getStateDefinition().getProperty("half");
            assert half != null;
            return blockState.getValue(half).toString().equals("upper") ? blockPos.below() : blockPos.above();
        } else if (name.contains("Piston")) {
            Property<?> facing = blockState.getBlock().getStateDefinition().getProperty("facing");
            if (name.equals("Piston Head")) {
                assert facing != null;
                return blockPos.relative(((Direction) blockState.getValue(facing)).getOpposite());
            } else {
                if (blockState.getValue((Property<Boolean>) blockState.getBlock().getStateDefinition().getProperty("extended"))) {
                    assert facing != null;
                    return blockPos.relative((Direction) blockState.getValue(facing));
                }
            }
        } else if (blockState.getBlock() instanceof BedBlock) {
            Property<?> facing = blockState.getBlock().getStateDefinition().getProperty("facing");
            Property<?> part = blockState.getBlock().getStateDefinition().getProperty("part");
            assert part != null;
            if (blockState.getValue(part).toString().equals("foot")) {
                assert facing != null;
                return blockPos.relative((Direction) blockState.getValue(facing));
            } else {
                assert facing != null;
                return blockPos.relative(((Direction) blockState.getValue(facing)).getOpposite());
            }
        }

        return null;
    }

    public static BlockState setFrozenBlock(BlockState oldBlockState, boolean isSecondBlock, int duration)
    {
        BlockState frozenBlock = BlockRegister.FROZEN_BLOCK.get().defaultBlockState();
        Property<Direction> facing = oldBlockState.getBlock().getStateDefinition().getProperty("facing") != null
                ? (Property<Direction>) oldBlockState.getBlock().getStateDefinition().getProperty("facing")
                : null;
        if (
                facing != null
                        && !oldBlockState.getValue(facing).equals(Direction.UP)
                        && !oldBlockState.getValue(facing).equals(Direction.DOWN)
        ) {
            frozenBlock = frozenBlock.setValue(FrozenBlock.FACING, oldBlockState.getValue(facing));
            frozenBlock = frozenBlock.setValue(FrozenBlock.IS_SECOND_BLOCK, isSecondBlock);
        }

        // TODO: Duration goes here

        return frozenBlock;
    }
}
