package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class Breath
{
    public static final Breath NONE = new Breath(new Color(0, 0, 0, 0), Elements.noElement);
    public static final Breath AIR = new Breath(new Color(255, 255, 255, 255), Elements.noElement);
    public static final Breath WATER = new WaterBreath(new Color(127, 200, 255, 255), Elements.waterElement, Fluids.WATER, 8f, 5f);
    public static final Breath FIRE = new FireBreath(new Color(255, 0, 0, 255), Elements.fireElement, Fluids.LAVA, 0f, 20f);
    public static final Breath WIND = new WindBreath(new Color(200, 255, 225, 255), Elements.windElement, Fluids.LAVA, 99f, 0f);

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

    public void tick(Player player)
    {

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


    // Returns whether the specified block is mineable
    public boolean canBeMined(Player player, BlockPos pos, Direction direction, Technique source)
    {
        return true;
    }

    // Returns whether this block should be destroyed on hit or not
    public boolean doBlockAttack(Player player, BlockPos pos, Direction direction)
    {
        return true;
    }

    // Called after this breath destroys a block at the specified location
    public void onBlockDestroy(Level level, BlockPos pos)
    {
    }

    // Called to see if this breath can damage the specified entity (on server)
    public boolean tryAttack(Player player, Entity toAttack)
    {
        return true;
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
