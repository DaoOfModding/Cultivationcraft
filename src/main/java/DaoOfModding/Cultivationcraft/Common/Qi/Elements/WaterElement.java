package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.awt.*;

public class WaterElement extends Element
{
    public WaterElement(ResourceLocation resourcelocation, Color elementColor, double newDensity)
    {
        super(resourcelocation, elementColor, newDensity);
    }

    public void applyStatusEffect(Entity target, float damageAmount)
    {
        target.clearFire();
    }
}
