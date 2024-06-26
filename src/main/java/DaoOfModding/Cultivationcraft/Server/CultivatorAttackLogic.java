package DaoOfModding.Cultivationcraft.Server;

import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.QuestHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class CultivatorAttackLogic
{
    public static boolean canAttack(Player player, Entity toAttack, double range)
    {
        if (!toAttack.isAttackable())
            return false;

        if (toAttack.skipAttackInteraction(player))
            return false;

        if (range > -1)
            if (toAttack.position().subtract(player.position()).length() > range)
                return false;

        return true;
    }

    // Attack specified entity with specified player, server only
    public static boolean attackEntity(Player player, Entity toAttack, double range, float damage, SoundEvent attackSound, ResourceLocation element, String source)
    {
        if (!canAttack(player, toAttack, range))
            return false;

        // TODO: Add knockback as an attackModifier
        float knockback = 1;

        Vec3 entityMotion = toAttack.getDeltaMovement();
        float entityHealth = 0;

        if (toAttack instanceof LivingEntity)
            entityHealth = ((LivingEntity) toAttack).getHealth();

        // If player does no damage (?) then play a corresponding sound and do nothing
        if (!toAttack.hurt(QiDamageSource.playerAttack(player, element, source, true), damage))
        {
            player.level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, player.getSoundSource(), 1.0F, 1.0F);
            return false;
        }

        // Play attack sound
        if (attackSound != null)
            player.level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), attackSound, player.getSoundSource(), 1.0F, 1.0F);

        // Apply knockback to attacked entity
        if (knockback > 0) {
            if (toAttack instanceof LivingEntity)
                ((LivingEntity) toAttack).knockback(knockback, Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), (double) (-Mth.cos(player.getYRot() * ((float) Math.PI / 180F))));
            else
                toAttack.push((-Mth.sin(player.getYRot() * ((float) Math.PI / 180F)) * knockback), 0.1D, (double) (Mth.cos(player.getYRot() * ((float) Math.PI / 180F)) * knockback));

            player.setDeltaMovement(player.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            player.setSprinting(false);
        }

        // Usefull code for AOE attacks
        /*
                        if (flag3) {
                            float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(this) * attack;

                            for(LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class, targetEntity.getBoundingBox().grow(1.0D, 0.25D, 1.0D))) {
                                if (livingentity != this && livingentity != targetEntity && !this.isOnSameTeam(livingentity) && (!(livingentity instanceof ArmorStandEntity) || !((ArmorStandEntity)livingentity).hasMarker()) && this.getDistanceSq(livingentity) < 9.0D) {
                                    livingentity.applyKnockback(0.4F, (double)Mth.sin(this.rotationYaw * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.rotationYaw * ((float)Math.PI / 180F))));
                                    livingentity.attackEntityFrom(DamageSource.causePlayerDamage(this), f3);
                                }
                            }

                            this.world.playSound((Player)null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
                            this.spawnSweepParticles();
                        }*/

        // If attacking a player and they were knockedback, send a packet to them telling them so
        if (toAttack instanceof ServerPlayer && toAttack.hurtMarked) {
            ((ServerPlayer) toAttack).connection.send(new ClientboundSetEntityMotionPacket(toAttack));
            toAttack.hurtMarked = false;
            toAttack.setDeltaMovement(entityMotion);
        }

        // Set the attacked entity as the last entity the player attacked
        player.setLastHurtMob(toAttack);

        // Apply any relevant thorn enchantments from the attacked entity
        if (toAttack instanceof LivingEntity)
            EnchantmentHelper.doPostHurtEffects((LivingEntity) toAttack, player);


        // If attacked entity took enough damage, spawn a damage indicator
        if (toAttack instanceof LivingEntity)
        {
            float rawDamage = entityHealth - ((LivingEntity) toAttack).getHealth();
            player.awardStat(Stats.DAMAGE_DEALT, Math.round(rawDamage * 10.0F));

            if (player.level instanceof ServerLevel && rawDamage > 2.0F) {
                int k = (int) ((double) rawDamage * 0.5D);
                ((ServerLevel) player.level).sendParticles(ParticleTypes.DAMAGE_INDICATOR, toAttack.getX(), toAttack.getY(0.5D), toAttack.getZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
            }
        }

        return true;
    }
}
