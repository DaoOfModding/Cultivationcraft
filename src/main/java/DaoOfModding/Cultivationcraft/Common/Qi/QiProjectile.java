package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Element;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques.QiEmission;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Server.CultivatorAttackLogic;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class QiProjectile extends AbstractHurtingProjectile
{
    ResourceLocation element = Elements.noElement;
    int damage = 0;
    float speed = 1;
    QiEmission tech;

    public QiProjectile(EntityType<? extends QiProjectile> entityType, Level level)
    {
        super(entityType, level);
    }

    public QiProjectile(Level level, LivingEntity owner, double x, double y, double z, ResourceLocation projectileElement, int damageOnHit, float movementSpeed, QiEmission source)
    {
        super(Register.QIPROJECTILE.get(), owner, x, y, z, level);

        this.setPos(x, y, z);

        element = projectileElement;
        damage = damageOnHit;
        speed = movementSpeed / 2f;

        tech = source;
    }

    public void setDirection(Vec3 dir)
    {
        dir = dir.scale(speed);

        setDeltaMovement(dir);

        dir = dir.scale(1/10f);
        xPower = dir.x;
        yPower = dir.y;
        zPower = dir.z;
    }

    protected void onHitEntity(EntityHitResult hit)
    {
        // Do nothing if this is hitting its owner
        if (this.ownedBy(hit.getEntity()))
            return;

        CultivatorAttackLogic.attackEntity((Player)getOwner(), hit.getEntity(), -1, damage, null, element, "QiProjectile");

        if (tech != null)
            tech.levelUp((Player)getOwner(), 10);

        this.discard();
    }

    protected void onHitBlock(BlockHitResult hit)
    {
        BlockState blockstate = this.level.getBlockState(hit.getBlockPos());
        blockstate.onProjectileHit(this.level, blockstate, hit, this);

        // Destroy this projectile if it hits a block
        this.discard();
    }

    public boolean hurt(DamageSource p_36910_, float p_36911_) {
        return false;
    }

    protected ParticleOptions getTrailParticle() {
        return Register.qiParticleType.get();
    }

    protected boolean shouldBurn()
    {
        return false;
    }
}
