package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Client.CultivatorAttackLogicClient;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Server.CultivatorAttackLogic;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.UUID;

public class AttackTechnique extends Technique
{
    protected SoundEvent attackSound = SoundEvents.PLAYER_ATTACK_STRONG;
    protected SoundEvent missSound = SoundEvents.PLAYER_ATTACK_WEAK;

    protected  PlayerPose attack = new PlayerPose();

    protected int minePower = 0;

    public AttackTechnique()
    {
        super();

        addTechniqueStat(DefaultTechniqueStatIDs.range, 5);
        addTechniqueStat(DefaultTechniqueStatIDs.damage, 1);
    }

    // Try to attack with specified player, client only
    public void attack(Player player, int slot)
    {
        HitResult.Type result = HitResult.Type.MISS;
        UUID attackUUID = null;
        Entity attackEntity = CultivatorAttackLogicClient.tryAttackEntity(getRange(player));
        Vec3 location = new Vec3(0, 0, 0);
        Direction direction = null;

        if (attackEntity != null)
        {
            attackUUID = attackEntity.getUUID();
            location = attackEntity.position();
            result = HitResult.Type.ENTITY;
        }
        else
        {
            BlockHitResult blockpos = CultivatorAttackLogicClient.tryAttackBlock(getRange(player));

            if (blockpos != null)
            {
                location = new Vec3(blockpos.getBlockPos().getX(), blockpos.getBlockPos().getY(), blockpos.getBlockPos().getZ());
                result = HitResult.Type.BLOCK;
                direction = blockpos.getDirection();
            }
        }

        attackAnimation(player, attackEntity);

        ClientPacketHandler.sendAttackToServer(player.getUUID(), result, location, attackUUID, direction, slot);
    }

    public void attackAnimation(Player player, Entity attackTarget)
    {
        // Add the attacking pose to the PoseHandler
        PoseHandler.addPose(player.getUUID(), attack);
    }

    public double getRange(Player player)
    {
        // Default attack range
        return getTechniqueStat(DefaultTechniqueStatIDs.range, player);
    }

    public float getAttack(Player player)
    {
        // Default attack damage
        return (float)getTechniqueStat(DefaultTechniqueStatIDs.damage, player);
    }

    protected float getMinePower(BlockGetter p_60801_, BlockPos p_60802_)
    {
        return minePower;
    }

    public void attackNothing(Player player)
    {
        player.level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), missSound, player.getSoundSource(), 1.0F, 1.0F);
    }

    public void attackBlock(Player player, BlockState block, BlockPos pos, Direction direction)
    {
        player.level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), attackSound, player.getSoundSource(), 1.0F, 1.0F);
    }

    // Attack specified entity with specified player, server only
    public void attackEntity(Player player, Entity toAttack)
    {
        if (getAttack(player) == 0)
            return;

        if (!CultivatorAttackLogic.attackEntity(player, toAttack, getRange(player), getAttack(player), attackSound, getElement(), langLocation))
            return;

        // If the entity is dead then call onKill
        if (toAttack instanceof LivingEntity && !((LivingEntity)toAttack).isAlive())
            onKill(player, (LivingEntity)toAttack);
    }

    // What to do when any entity has been killed by this technique
    protected void onKill(Player player, LivingEntity entity)
    {
    }
}
