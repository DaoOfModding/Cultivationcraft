package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Client.Physics;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BounceModifier extends TechniqueModifier
{
    public BounceModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.bounce");
        CATEGORY = MODIFIER_CATEGORY;

        unlockQuest = new Quest(Quest.BOUNCE, 1);
        stabiliseQuest = new Quest(Quest.BOUNCE, 1000);

        allowSameCategory = true;
        qiMult = 1.5f;
    }

    // TODO: Qi consumption reduced on damage taken

    public boolean onHitBlock(Player owner, BlockHitResult hit, float damage, ResourceLocation element, Entity projectile)
    {
        super.onHitBlock(owner, hit, damage, element, projectile);

        Vec3 bounceDirection = bounce(projectile.getDeltaMovement().normalize(), new Vec3(hit.getDirection().getNormal().getX(), hit.getDirection().getNormal().getY(), hit.getDirection().getNormal().getZ()));

        // Set the new delta movement of the projectile to be the bounce direction multiplied by the previous speed
        projectile.setDeltaMovement(bounceDirection.scale(projectile.getDeltaMovement().length()));

        return true;
    }

    protected Vec3 bounce(Vec3 direction, Vec3 normal)
    {
        Vec3 bounce = normal.scale(normal.dot(direction) * 2);
        bounce = bounce.subtract(direction);
        bounce = bounce.scale(-1);

        return bounce;
    }

    public void onHitTaken(Player owner, float damage, ResourceLocation defensiveElement, QiDamageSource source)
    {
        if (source.isInternal() || damage <= 0)
            return;

        // Do nothing if the player does not have enough Qi
        if (!CultivatorStats.getCultivatorStats(owner).getCultivation().consumeQi(owner, damage))
            return;

        if (source.msgId.compareTo(DamageSource.FALL.getMsgId()) == 0)
            Physics.Bounce(owner, 0.75f);
    }
}
