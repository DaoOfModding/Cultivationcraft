package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.PlayerPose;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.PoseHandler;
import DaoOfModding.Cultivationcraft.Client.KeybindingControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RayTraceResult;

import java.util.ArrayList;

public class AttackTechnique extends Technique
{
    protected  PlayerPose attack = new PlayerPose();

    public AttackTechnique()
    {
        super();

        type = useType.Toggle;
        multiple = false;
        cooldown = 10;
    }

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
    public void attack(PlayerEntity player)
    {
        Cultivationcraft.LOGGER.info("Trying attack...");

        // Do nothing if on cooldown
        if (cooldownCount > 0)
            return;

        // Set the cooldown
        cooldownCount = cooldown;

        Cultivationcraft.LOGGER.info("Not on cooldown...");

        // Add the attacking pose to the PoseHandler
        PoseHandler.addPose(player.getUniqueID(), attack);

        // Default attack range
        double range = 1;

        // Add any range modifiers onto the attack range
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        for (String modifier : getModifiers())
            range += stats.getModifier(modifier).getAttackRange();

        // If the mouse is over an entity in range, attack that entity
        if (KeybindingControl.getMouseOver(range).getType() == RayTraceResult.Type.ENTITY)
        {
            Cultivationcraft.LOGGER.info("In range...");

            // TODO: Send attack packet to server
        }
    }

    // Attack specified entity with specified player, server only
    public void attackEntity(PlayerEntity player, Entity toAttack)
    {

    }
}
