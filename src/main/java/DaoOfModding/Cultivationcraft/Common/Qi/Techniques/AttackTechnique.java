package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Server.CultivatorAttackLogic;
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

        RayTraceResult.Type result = RayTraceResult.Type.MISS;
        UUID attackUUID = null;
        Entity attackEntity = CultivatorAttackLogic.tryAttackEntity(getRange(player));
        Vector3d location = new Vector3d(0, 0, 0);

        if (attackEntity != null)
        {
            attackUUID = attackEntity.getUUID();
            location = attackEntity.position();
            result = RayTraceResult.Type.ENTITY;
        }
        else
        {
            BlockPos blockpos = CultivatorAttackLogic.tryAttackBlock(getRange(player));

            if (blockpos != null)
            {
                location = new Vector3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                result = RayTraceResult.Type.BLOCK;
            }
        }

        ClientPacketHandler.sendAttackToServer(player.getUUID(), result, location, attackUUID, slot);
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
        if (!CultivatorAttackLogic.attackEntity(player, toAttack, getRange(player), getAttack(player), attackSound))
            return;

        // If the entity is dead then call onKill
        if (toAttack instanceof LivingEntity && !((LivingEntity)toAttack).isAlive())
            onKill(player, (LivingEntity)toAttack);
    }

    // What to do when any entity has been killed by this technique
    protected void onKill(PlayerEntity player, LivingEntity entity)
    {
    }
}
