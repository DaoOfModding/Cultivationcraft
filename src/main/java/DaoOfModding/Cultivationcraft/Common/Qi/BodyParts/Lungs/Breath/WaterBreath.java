package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath;

import DaoOfModding.Cultivationcraft.Client.Particles.WaterParticle.WaterParticleData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class WaterBreath extends Breath
{
    public WaterBreath(Color col, ResourceLocation newElement, FlowingFluid flu, float diggingPower, float damagePower)
    {
        super(col, newElement, flu, diggingPower, damagePower);

        canExpell = true;
    }

    public ParticleOptions getParticle(Vec3 endTarget, Entity targetEntity)
    {
        return new WaterParticleData(endTarget, targetEntity);
    }

    @Override
    public void onBlockDestroy(Level level, BlockPos pos)
    {
        level.setBlock(pos, fluid.getSource().defaultFluidState().createLegacyBlock(), 2);
    }
}
