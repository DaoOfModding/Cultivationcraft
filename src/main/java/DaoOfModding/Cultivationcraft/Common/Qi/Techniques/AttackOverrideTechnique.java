package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.StaminaHandler;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPoseHandler;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;

public class AttackOverrideTechnique extends AttackTechnique
{
    public AttackOverrideTechnique()
    {
        super();

        type = useType.Toggle;
        multiple = false;
        cooldown = 10;
    }

    @Override
    public void attack(Player player, int slot)
    {
        // Do nothing if on cooldown
        if (cooldownCount > 0)
            return;

        // Do nothing if player does not have enough stamina
        if (hasTechniqueStat(DefaultTechniqueStatIDs.staminaCost) && !StaminaHandler.consumeStamina(player, (float)getTechniqueStat(DefaultTechniqueStatIDs.staminaCost)))
            return;

        if (player.level.isClientSide)
            PoseHandler.getPlayerPoseHandler(player.getUUID()).cancelNextAttackAnimation();

        super.attack(player, slot);

        // Set the cooldown
        cooldownCount = cooldown;
    }

    @Override
    public void attackEntity(Player player, Entity toAttack)
    {
        // Do nothing if on cooldown
        if (cooldownCount > 0)
            return;

        // Do nothing if player does not have enough stamina
        if (hasTechniqueStat(DefaultTechniqueStatIDs.staminaCost) && !StaminaHandler.consumeStamina(player, (float)getTechniqueStat(DefaultTechniqueStatIDs.staminaCost)))
            return;

        if (player.level.isClientSide)
            PoseHandler.getPlayerPoseHandler(player.getUUID()).cancelNextAttackAnimation();

        super.attackEntity(player, toAttack);

        // Set the cooldown
        cooldownCount = cooldown;
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        if (cooldownCount > 0)
            cooldownCount = cooldownCount - 1;

        // Only add the pose if players main hand is empty
        if (event.player.getMainHandItem().isEmpty())
            PoseHandler.addPose(event.player.getUUID(), pose);
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        super.tickServer(event);

        if (cooldownCount > 0)
            cooldownCount = cooldownCount - 1;
    }
}
