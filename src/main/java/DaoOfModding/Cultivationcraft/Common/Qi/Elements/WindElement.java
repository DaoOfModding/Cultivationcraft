package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Effects.Wind;
import DaoOfModding.Cultivationcraft.Common.Qi.Effects.WindInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class WindElement extends Element
{
    public WindElement(ResourceLocation resourcelocation, Color elementColor, double newDensity)
    {
        super(resourcelocation, elementColor, newDensity);

        effectTickChance = 1;
    }

    public void applyStatusEffect(QiDamageSource source, Entity target, float damageAmount)
    {
        if (target.level.isClientSide)
            return;

        Vec3 direction = target.position().subtract(source.getSourcePosition()).normalize();
        Wind.addWindEffect(target, new WindInstance(direction, damageAmount * 0.025f, damageAmount/5f));

        super.applyStatusEffect(source, target, damageAmount);
    }
}
