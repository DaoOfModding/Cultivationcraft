package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath;

import DaoOfModding.Cultivationcraft.Client.Particles.WindParticle.WindParticleData;
import DaoOfModding.Cultivationcraft.Common.Qi.Effects.Wind;
import DaoOfModding.Cultivationcraft.Common.Qi.Effects.WindInstance;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class WindBreath extends Breath
{
    protected int tickSinceWind = 0;

    public WindBreath(Color col, ResourceLocation newElement, FlowingFluid flu, float diggingPower, float damagePower)
    {
        super(col, newElement, flu, diggingPower, damagePower);

        canExpell = true;
    }

    public void tick(Player player)
    {
        if (tickSinceWind == 10)
        {
            Wind.addWindEffect(player, new WindInstance(player.getLookAngle().scale(-1), 0.15f, 0.6f));
            tickSinceWind = 0;
        }
        else
            tickSinceWind++;
    }

    public ParticleOptions getParticle(Vec3 endTarget, Entity targetEntity)
    {
        return new WindParticleData(endTarget, targetEntity);
    }

    // Returns whether the specified block is mineable
    public boolean canBeMined(Player player, BlockPos pos, Direction direction, Technique source)
    {
        if (player.level.getBlockState(pos).getBlock() instanceof LeavesBlock)
            return true;

        return false;
    }

    // Returns whether this block should be destroyed on hit or not
    public boolean doBlockAttack(Player player, BlockPos pos, Direction direction)
    {
        if (player.level.getBlockState(pos).getBlock() instanceof LeavesBlock)
            return true;

        return false;
    }

    // Called to see if this breath can damage the specified entity (on server)
    public boolean tryAttack(Player player, Entity toAttack)
    {
        Wind.addWindEffect(toAttack, new WindInstance(player.getLookAngle(), 0.25f, 6));

        return false;
    }
}
