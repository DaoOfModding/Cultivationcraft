package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class Breath
{
    public static final Breath NONE = new Breath(new Color(0, 0, 0, 0), Elements.noElement);
    public static final Breath AIR = new Breath(new Color(255, 255, 255, 255), Elements.noElement);
    public static final Breath WATER = new WaterBreath(new Color(127, 200, 255, 255), Elements.waterElement, Fluids.WATER, 8f, 5f);
    public static final Breath FIRE = new Breath(new Color(255, 0, 0, 255), Elements.fireElement, Fluids.LAVA);

    Color color;
    FlowingFluid fluid;
    Float digPower;
    Float damage;
    ResourceLocation element;
    Boolean canExpell = false;

    public Breath(Color col, ResourceLocation newElement)
    {
        this(col, newElement, null, 0f, 0f);
    }

    public Breath(Color col, ResourceLocation newElement, FlowingFluid flu)
    {
        this(col, newElement, flu,  0f, 0f);
    }

    public Breath(Color col, ResourceLocation newElement, FlowingFluid flu, float diggingPower, float damagePower)
    {
        color = col;
        fluid = flu;
        digPower = diggingPower;
        damage = damagePower;
        element = newElement;
    }

    public ResourceLocation getElement()
    {
        return element;
    }

    public boolean canExpell()
    {
        return canExpell;
    }

    public Color getColor()
    {
        return color;
    }

    public FlowingFluid getFluid()
    {
        return fluid;
    }

    public ParticleOptions getParticle(Vec3 endTarget, Entity targetEntity)
    {
        return null;
    }

    public void onBlockDestroy(Level level, BlockPos pos)
    {
    }

    public float getDigPower(BlockGetter p_60801_, BlockPos p_60802_)
    {
        return digPower;
    }

    public float getDamage()
    {
        return damage;
    }
}
