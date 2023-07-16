package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class WaterBlood extends CultivatorBlood
{
    public WaterBlood()
    {
        colour = new Vector3f(0f, 0.3f ,0.8f);
        life = 20*10;

        staminaHealingModifier = 3;
    }

    @Override
    public void regen(Player player)
    {
        BlockPos blockpos = new BlockPos(player.position());

        // Only heal if in water
        if (player.level.getBlockState(blockpos).is(Blocks.WATER))
            if (!staminaHealing(player))
                naturalHealing(player);
    }

    @Override
    public boolean canHeal(ResourceLocation element, @Nullable Player player)
    {
        if (element == Elements.waterElement && (player == null || player.hurtTime == 0))
            return true;

        return false;
    }

    @Override
    public boolean externalTick(Level level, double x, double y, double z, boolean onGround)
    {
        if (!onGround)
            return false;

        if (level.isClientSide)
        {
            ClientPacketHandler.sendExternalBloodTick(x, y, z, onGround);
            return true;
        }

        BlockPos blockpos = new BlockPos((float)x, (float)y, (float)z);

        // If the player is standing in water, increase it's height by 1
        if (level.getBlockState(blockpos).is(Blocks.WATER))
        {
            int height = level.getBlockState(blockpos).getValue(LiquidBlock.LEVEL) + 1;

            if (height > 15)
                height = 15;

            BlockState state = Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, height);
            level.setBlock(blockpos, state, 2);
            level.sendBlockUpdated(blockpos, state, state, 1);
        }
        // If the player is standing in air, add 1 height high water 1
        else if (level.getBlockState(blockpos).isAir())
        {
            BlockState state = Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, 1);
            level.setBlock(blockpos, state, 2);
            level.sendBlockUpdated(blockpos, state, state, 1);
        }

        return true;
    }
}
