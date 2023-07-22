package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood;

import DaoOfModding.Cultivationcraft.Client.Renderers.BloodRenderer;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

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

    // Called when player takes damage
    public void onHit(Player player, Vec3 source, double amount)
    {
        if (player.level.isClientSide)
            return;

        int waterLevel = (int)(amount / 5f);
        if (waterLevel > 7)
            waterLevel = 7;
        if (waterLevel < 2)
            waterLevel = 2;

        BlockPos blockpos = new BlockPos(player.position());

        placeWater(player.level, blockpos, waterLevel);


        Vec3 dir = BloodRenderer.getBloodDirection(player, source);

        int x = 0;
        int z = 0;

        if (dir.x >= 0.25)
            x = 1;
        else if (dir.x <= -0.25)
            x = -1;

        if (dir.z >= 0.25)
            z = 1;
        else if (dir.z <= -0.25)
            z = -1;

        BlockPos addWaterSpawn = new BlockPos(player.position().x + x, player.position().y, player.position().z + z);

        placeWater(player.level, addWaterSpawn, waterLevel-1);

        if (x != 0 && z != 0)
        {
            addWaterSpawn = new BlockPos(player.position().x, player.position().y, player.position().z + z);
            placeWater(player.level, addWaterSpawn, waterLevel-1);

            addWaterSpawn = new BlockPos(player.position().x + x, player.position().y, player.position().z);
            placeWater(player.level, addWaterSpawn, waterLevel-1);
        }
    }

    protected void placeWater(Level level, BlockPos blockpos, int waterLevel)
    {

        // If the player is standing in water, increase it's height by 1
        if (level.getBlockState(blockpos).is(Blocks.WATER))
        {
            int height = level.getBlockState(blockpos).getValue(LiquidBlock.LEVEL) + waterLevel;

            if (height > 15)
                height = 15;

            BlockState state = Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, height);
            level.setBlock(blockpos, state, 2);
            level.sendBlockUpdated(blockpos, state, state, 1);
        }
        // If the player is standing in air, add 1 height high water 1
        else if (level.getBlockState(blockpos).isAir())
        {
            BlockState state = Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, waterLevel);
            level.setBlock(blockpos, state, 2);
            level.sendBlockUpdated(blockpos, state, state, 1);
        }
    }
}
