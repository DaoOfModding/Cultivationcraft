package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.awt.*;

public class WaterElement extends Element
{
    public WaterElement(ResourceLocation resourcelocation, Color elementColor, double newDensity)
    {
        super(resourcelocation, elementColor, newDensity);

        effectTickChance = 1;
    }

    public void applyStatusEffect(QiDamageSource source, Entity target, float damageAmount)
    {
        target.clearFire();

        super.applyStatusEffect(source, target, damageAmount);
    }
}
