package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Element;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques.QiEmission;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Server.CultivatorAttackLogic;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class QiProjectile extends AbstractHurtingProjectile
{
    private static final EntityDataAccessor<Float> ID_SPEED = SynchedEntityData.defineId(QiProjectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ID_LIFE = SynchedEntityData.defineId(QiProjectile.class, EntityDataSerializers.FLOAT);

    ResourceLocation element = Elements.noElement;
    int damage = 0;
    QiEmission tech;

    public QiProjectile(EntityType<? extends QiProjectile> entityType, Level level)
    {
        super(entityType, level);
    }

    public QiProjectile(Level level, LivingEntity owner, double x, double y, double z, ResourceLocation projectileElement, int damageOnHit, float movementSpeed, QiEmission source, float lifetime)
    {
        super(Register.QIPROJECTILE.get(), owner, x, y, z, level);

        this.setPos(x, y, z);

        element = projectileElement;
        damage = damageOnHit;

        entityData.set(ID_SPEED, movementSpeed / 2f);
        entityData.set(ID_LIFE, lifetime * 20);

        tech = source;
    }

    protected void defineSynchedData()
    {
        super.defineSynchedData();
        entityData.define(ID_SPEED, 0f);
        entityData.define(ID_LIFE, 0f);
    }


    public float getAlpha()
    {
        if (entityData.get(ID_LIFE) > 20)
            return 1;

        return entityData.get(ID_LIFE) / 20f;
    }

    public void tick()
    {
        super.tick();

        if (!level.isClientSide)
            entityData.set(ID_LIFE, entityData.get(ID_LIFE) - 1);

        if (entityData.get(ID_LIFE) == 0)
            this.discard();
    }

    public void setDirection(Vec3 dir)
    {
        dir = dir.scale(entityData.get(ID_SPEED));

        setDeltaMovement(dir);

        dir = dir.scale(1/10f);
        xPower = dir.x;
        yPower = dir.y;
        zPower = dir.z;
    }

    protected void onHitEntity(EntityHitResult hit)
    {
        // Do nothing if this is hitting its owner or another projectile
        if (this.ownedBy(hit.getEntity()) || hit.getEntity() instanceof QiProjectile)
            return;

        if (getOwner() == null)
        {
            this.discard();
            return;
        }

        CultivatorAttackLogic.attackEntity((Player)getOwner(), hit.getEntity(), -1, damage, null, element, "QiProjectile");

        if (tech != null)
            tech.levelUp((Player)getOwner(), damage);

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
