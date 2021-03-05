package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.PoseHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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
    public void attack(PlayerEntity player, int slot)
    {
        // Do nothing if on cooldown
        if (cooldownCount > 0)
            return;

        super.attack(player, slot);

        // Set the cooldown
        cooldownCount = cooldown;
    }

    @Override
    public void attackEntity(PlayerEntity player, Entity toAttack)
    {
        // Do nothing if on cooldown
        if (cooldownCount > 0)
            return;

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
        if (event.player.getHeldItemMainhand().isEmpty())
            PoseHandler.addPose(event.player.getUniqueID(), pose);
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        super.tickServer(event);

        if (cooldownCount > 0)
            cooldownCount = cooldownCount - 1;
    }
}