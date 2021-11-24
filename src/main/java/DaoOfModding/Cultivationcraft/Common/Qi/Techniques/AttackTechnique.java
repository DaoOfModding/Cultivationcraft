package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.Cultivationcraft.Client.KeybindingControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Misc;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.UUID;

public class AttackTechnique extends Technique
{
    protected SoundEvent attackSound = SoundEvents.PLAYER_ATTACK_STRONG;
    protected SoundEvent missSound = SoundEvents.PLAYER_ATTACK_WEAK;

    protected  PlayerPose attack = new PlayerPose();

    protected double range = 5;
    protected float damage = 1;
    protected int minePower = 0;

    // Try to attack with specified player, client only
    public void attack(PlayerEntity player, int slot)
    {
        attackAnimation(player);

        // Get attack range
        double range = getRange(player);

        // If the mouse is over an entity in range, attack that entity
        RayTraceResult result = KeybindingControl.getMouseOver(range);

        UUID targetID = null;

        if (result.getType() == RayTraceResult.Type.ENTITY)
            targetID = Misc.getEntityAtLocation(result.getLocation(), Minecraft.getInstance().level).getUUID();

        Vector3d Location = result.getLocation();

        if (result.getType() == RayTraceResult.Type.BLOCK)
        {
            BlockPos blockpos = ((BlockRayTraceResult) result).getBlockPos();
            Location = new Vector3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        }

        ClientPacketHandler.sendAttackToServer(player.getUUID(), result.getType(), Location, targetID, slot);
    }

    public void attackAnimation(PlayerEntity player)
    {
        // Add the attacking pose to the PoseHandler
        PoseHandler.addPose(player.getUUID(), attack);
    }

    public double getRange(PlayerEntity player)
    {
        // Default attack range
        return range;
    }

    public float getAttack(PlayerEntity player)
    {
        // Default attack damage
        return damage;
    }

    public void attackNothing(PlayerEntity player)
    {
        player.level.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), missSound, player.getSoundSource(), 1.0F, 1.0F);
    }

    public void attackBlock(PlayerEntity player, BlockState block, BlockPos pos)
    {
        player.level.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), attackSound, player.getSoundSource(), 1.0F, 1.0F);

        // TODO: Mine block here
    }

    // Attack specified entity with specified player, server only
    public void attackEntity(PlayerEntity player, Entity toAttack)
    {
        if (!toAttack.isAttackable())
            return;

        if (toAttack.skipAttackInteraction(player))
            return;

        // Get attack range
        double range = getRange(player);

        // Do nothing if entity is not in attack range
        if (toAttack.position().subtract(player.position()).length() > range)
            return;


        float attack = getAttack(player);
        // TODO: Add knockback as an attackModifier
        float knockback = 1;

        // TODO: Check if toAttack entity is a cultivator, apply damage resistances && any extra stuff if so

        Vector3d entityMotion = toAttack.getDeltaMovement();
        float entityHealth = 0;

        if (toAttack instanceof LivingEntity)
            entityHealth = ((LivingEntity) toAttack).getHealth();

        // If player does no damage (?) then play a corresponding sound and do nothing
        if (!toAttack.hurt(DamageSource.playerAttack(player), attack)) {
            player.level.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, player.getSoundSource(), 1.0F, 1.0F);
            return;
        }

        // Play attack sound
        player.level.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), attackSound, player.getSoundSource(), 1.0F, 1.0F);

        // Apply knockback to attacked entity
        if (knockback > 0) {
            if (toAttack instanceof LivingEntity)
                ((LivingEntity) toAttack).knockback(knockback, (double) MathHelper.sin(player.yRot * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(player.yRot * ((float) Math.PI / 180F))));
            else
                toAttack.push((double) (-MathHelper.sin(player.yRot * ((float) Math.PI / 180F)) * knockback), 0.1D, (double) (MathHelper.cos(player.yRot * ((float) Math.PI / 180F)) * knockback));

            player.setDeltaMovement(player.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            player.setSprinting(false);
        }

        // Usefull code for AOE attacks
        /*
                        if (flag3) {
                            float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(this) * attack;

                            for(LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class, targetEntity.getBoundingBox().grow(1.0D, 0.25D, 1.0D))) {
                                if (livingentity != this && livingentity != targetEntity && !this.isOnSameTeam(livingentity) && (!(livingentity instanceof ArmorStandEntity) || !((ArmorStandEntity)livingentity).hasMarker()) && this.getDistanceSq(livingentity) < 9.0D) {
                                    livingentity.applyKnockback(0.4F, (double)MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F))));
                                    livingentity.attackEntityFrom(DamageSource.causePlayerDamage(this), f3);
                                }
                            }

                            this.world.playSound((PlayerEntity)null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
                            this.spawnSweepParticles();
                        }*/

        // If attacking a player and they were knockedback, send a packed to them telling them so
        if (toAttack instanceof ServerPlayerEntity && toAttack.hurtMarked) {
            ((ServerPlayerEntity) toAttack).connection.send(new SEntityVelocityPacket(toAttack));
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

            if (player.level instanceof ServerWorld && rawDamage > 2.0F) {
                int k = (int) ((double) rawDamage * 0.5D);
                ((ServerWorld) player.level).sendParticles(ParticleTypes.DAMAGE_INDICATOR, toAttack.getX(), toAttack.getY(0.5D), toAttack.getZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
            }
        }

        // If the entity is dead then call onKill
        if (toAttack instanceof LivingEntity && !((LivingEntity)toAttack).isAlive())
            onKill(player, (LivingEntity)toAttack);
    }

    // What to do when any entity has been killed by this technique
    protected void onKill(PlayerEntity player, LivingEntity entity)
    {
    }
}
