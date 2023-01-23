package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class LightningElement extends ElementVariant
{
    public LightningElement(ResourceLocation Element, Color elementColor, double newDensity, double chance)
    {
        super(Element, elementColor, newDensity, chance);

        effectTickChance = 1.0 / 200.0;
    }

    @Override
    public void effectBlock(Level level, BlockPos pos)
    {
        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
        lightningbolt.moveTo(Vec3.atBottomCenterOf(pos));
        level.addFreshEntity(lightningbolt);
    }
}
