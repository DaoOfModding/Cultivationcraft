package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class Tribulation
{
    protected int tick = 0;
    protected int lightningStrikes = 0;
    protected int lightningPower = 0;
    protected float lightningPowerModifier = 0;

    protected final int targetStrikes;

    public Tribulation(int numberOfLightningStrikes, int lightningStrength, float lightningStrengthGrowth)
    {
        targetStrikes = numberOfLightningStrikes;
        lightningPower = lightningStrength;
        lightningPowerModifier = lightningStrengthGrowth;
    }

    public void reset()
    {
        tick = 0;
        lightningStrikes = 0;
    }

    public boolean tick(Player player)
    {
        // Do not do tribulation unless in a dimension with a sky
        if (player.level.dimensionType().hasCeiling() || !player.level.dimensionType().hasSkyLight())
        {
            reset();
            return false;
        }

        // Return true if the player has survived the target number of lightning strikes
        if (targetStrikes == lightningStrikes)
            return true;

        tick++;

        player.level.setThunderLevel(1);

        if (player.level.isClientSide)
            return false;

        // Spawn one lightning strike every 10 seconds
        if (tick % 200 == 0)
        {
            int strength = (int)(lightningPower * (1 + lightningStrikes * lightningPowerModifier));

            lightning(player.blockPosition(), strength, (ServerLevel) player.level);
        }

        return false;
    }

    public void lightning(BlockPos targetPos, float power, ServerLevel level)
    {
        if (!level.canSeeSky(targetPos))
        {
            int y = level.getMaxBuildHeight();

            while (power > 0 && y > targetPos.getY()) {
                BlockPos blockpos = new BlockPos(targetPos.getX(), y, targetPos.getZ());
                BlockState block = level.getBlockState(blockpos);

                if (!block.isAir() && !block.getMaterial().isLiquid()) {
                    float strength = Math.min(block.getDestroySpeed(level, blockpos), 2) / 0.5f;

                    // If the lightning does not have enough enough power left to break through the block, spawn the lightning on the block and return
                    if (strength > power)
                    {
                        spawnLightning(level, blockpos, power);
                        return;
                    }

                    power -= strength;
                    level.destroyBlock(blockpos, true);
                }

                y--;
            }
        }

        spawnLightning(level, targetPos, power);
    }

    public void spawnLightning(ServerLevel level, BlockPos pos, float power)
    {
        lightningStrikes++;

        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
        lightningbolt.moveTo(Vec3.atBottomCenterOf(pos));
        lightningbolt.setDamage(power);
        level.addFreshEntity(lightningbolt);
    }
}
