package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.Cultivationcraft.Client.KeybindingControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Misc;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.UUID;

public class AttackTechnique extends Technique
{
    protected  PlayerPose attack = new PlayerPose();

    ArrayList<String> modifiers = new ArrayList<String>();

    public void addModifier(String modifierID)
    {
        modifiers.add(modifierID);
    }

    public Boolean hasModifier(String modifierID)
    {
        return modifiers.contains(modifierID);
    }

    public ArrayList<String> getModifiers()
    {
        return modifiers;
    }

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
            targetID = Misc.getEntityAtLocation(result.getHitVec(), Minecraft.getInstance().world).getUniqueID();

        ClientPacketHandler.sendAttackToServer(player.getUniqueID(), result.getType(), result.getHitVec(), targetID, slot);
    }

    public void attackAnimation(PlayerEntity player)
    {
        // Add the attacking pose to the PoseHandler
        PoseHandler.addPose(player.getUniqueID(), attack);
    }

    public double getRange(PlayerEntity player)
    {
        // Default attack range
        double range = 9;

        // Add any range modifiers onto the attack range
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        for (String modifier : getModifiers())
            range += stats.getModifier(modifier).getAttackRange();

        return range;
    }

    public int getAttack(PlayerEntity player)
    {
        // Default attack damage
        int attack = 1;

        // Add any attack modifiers onto the attack damage
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        for (String modifier : getModifiers())
            attack += stats.getModifier(modifier).getAttack();

        return attack;
    }

    // Attack specified entity with specified player, server only
    public void attackEntity(PlayerEntity player, Entity toAttack)
    {
        Cultivationcraft.LOGGER.info("Trying to attack...");

        if (!toAttack.canBeAttackedWithItem())
            return;

        if (toAttack.hitByEntity(player))
            return;

        Cultivationcraft.LOGGER.info("Attack allowed...");

        // Get attack range
        double range = getRange(player);

        // Do nothing if entity is not in attack range
        if (toAttack.getPositionVec().subtract(player.getPositionVec()).length() > range)
            return;


        int attack = getAttack(player);
        // TODO: Add knockback as an attackModifier
        float knockback = 1;

        // TODO: Check if toAttack entity is a cultivator, apply damage resistances && any extra stuff if so

        Vector3d entityMotion = toAttack.getMotion();
        float entityHealth = 0;

        if (toAttack instanceof LivingEntity)
            entityHealth = ((LivingEntity) toAttack).getHealth();

        // If player does no damage (?) then play a coresponding sound and do nothing
        if (!toAttack.attackEntityFrom(DamageSource.causePlayerDamage(player), attack)) {
            player.world.playSound((PlayerEntity) null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundCategory(), 1.0F, 1.0F);
            return;
        }

        Cultivationcraft.LOGGER.info("Attack succeeded...");

        // Play attack sound
        player.world.playSound((PlayerEntity) null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);

        // Apply knockback to attacked entity
        if (knockback > 0) {
            if (toAttack instanceof LivingEntity)
                ((LivingEntity) toAttack).applyKnockback(knockback, (double) MathHelper.sin(player.rotationYaw * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(player.rotationYaw * ((float) Math.PI / 180F))));
            else
                toAttack.addVelocity((double) (-MathHelper.sin(player.rotationYaw * ((float) Math.PI / 180F)) * knockback), 0.1D, (double) (MathHelper.cos(player.rotationYaw * ((float) Math.PI / 180F)) * knockback));

            player.setMotion(player.getMotion().mul(0.6D, 1.0D, 0.6D));
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
        if (toAttack instanceof ServerPlayerEntity && toAttack.velocityChanged) {
            ((ServerPlayerEntity) toAttack).connection.sendPacket(new SEntityVelocityPacket(toAttack));
            toAttack.velocityChanged = false;
            toAttack.setMotion(entityMotion);
        }

        // Set the attacked entity as the last entity the player attacked
        player.setLastAttackedEntity(toAttack);

        // Apply any relevant thorn enchantments from the attacked entity
        if (toAttack instanceof LivingEntity)
            EnchantmentHelper.applyThornEnchantments((LivingEntity) toAttack, player);


        // If attacked entity took enough damage, spawn a damage indicator
        if (toAttack instanceof LivingEntity)
        {
            float rawDamage = entityHealth - ((LivingEntity) toAttack).getHealth();
            player.addStat(Stats.DAMAGE_DEALT, Math.round(rawDamage * 10.0F));

            if (player.world instanceof ServerWorld && rawDamage > 2.0F) {
                int k = (int) ((double) rawDamage * 0.5D);
                ((ServerWorld) player.world).spawnParticle(ParticleTypes.DAMAGE_INDICATOR, toAttack.getPosX(), toAttack.getPosYHeight(0.5D), toAttack.getPosZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
            }
        }
    }

}
