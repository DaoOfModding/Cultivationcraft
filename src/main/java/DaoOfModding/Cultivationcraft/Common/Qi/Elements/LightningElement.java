package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Reflection;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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

    public void applyStatusEffect(QiDamageSource source, Entity target, float damageAmount)
    {
        if (!(target.level instanceof ServerLevel))
            return;

        int bolts = (int)(damageAmount / 5.0) + 1;

        BlockPos pos = target.blockPosition();

        for (int i = 0; i < bolts; i++)
        {
            BlockPos blockpos = Reflection.findLightningTargetAround((ServerLevel) target.level, target.level.getBlockRandomPos(pos.getX(), pos.getY(), pos.getZ(), 10));
            effectBlock(target.level, blockpos);
        }
    }

    @Override
    public void effectBlock(Level level, BlockPos pos)
    {
        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
        lightningbolt.moveTo(Vec3.atBottomCenterOf(pos));
        level.addFreshEntity(lightningbolt);
    }
}
