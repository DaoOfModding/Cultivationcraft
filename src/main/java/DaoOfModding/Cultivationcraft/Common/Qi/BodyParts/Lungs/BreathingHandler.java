package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidType;

import java.util.HashMap;

public class BreathingHandler
{
    protected static HashMap<FluidType, Breath> fluidBreaths = new HashMap<FluidType, Breath>();
    protected static HashMap<BlockState, Breath> blockBreaths = new HashMap<BlockState, Breath>();

    public static void init()
    {
        addBlockBreaths(Fluids.WATER.getFluidType(), Breath.WATER);
        addBlockBreaths(Fluids.LAVA.getFluidType(), Breath.FIRE);
        addBlockBreaths(Blocks.FIRE.defaultBlockState(), Breath.FIRE);
        addBlockBreaths(Blocks.SOUL_FIRE.defaultBlockState(), Breath.FIRE);
    }

    public static void addBlockBreaths(FluidType fluid, Breath breath)
    {
        fluidBreaths.put(fluid, breath);
    }

    public static void addBlockBreaths(BlockState block, Breath breath)
    {
        blockBreaths.put(block, breath);
    }

    public static Breath getBreath(Player player)
    {
        if (fluidBreaths.containsKey(player.getEyeInFluidType()))
            return fluidBreaths.get(player.getEyeInFluidType());

        if (blockBreaths.containsKey(player.level.getBlockState(player.blockPosition())))
            return blockBreaths.get(player.level.getBlockState(player.blockPosition()));

        if (player.isOnFire())
            return Breath.FIRE;

        if (player.getEyeInFluidType().isAir() && !player.level.getBlockState(new BlockPos(player.getX(), player.getEyeY(), player.getZ())).is(Blocks.BUBBLE_COLUMN))
            return Breath.AIR;

        return Breath.NONE;
    }
}
