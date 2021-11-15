package DaoOfModding.Cultivationcraft.Common.Qi.Stats;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;

public class PlayerStatControl
{
    PlayerStatModifications stats = new PlayerStatModifications();

    AttributeModifier healthModifier;
    AttributeModifier movementModifier;

    // Setup players with the default player stats
    public PlayerStatControl()
    {
        setupStats();
    }

    public void setupStats()
    {
        stats.setStat(StatIDs.weight, StatIDs.defaultWeight);
        stats.setStat(StatIDs.maxHP, StatIDs.defaultMaxHP);
        stats.setStat(StatIDs.maxStamina, StatIDs.defaultMaxStamina);
        stats.setStat(StatIDs.staminaUse, StatIDs.defaultStaminaUse);
        stats.setStat(StatIDs.movementSpeed, StatIDs.defaultMovementSpeed);
        stats.setStat(StatIDs.jumpHeight, 1);
    }

    public PlayerStatModifications getStats()
    {
        return stats;
    }

    public void updateStats(PlayerEntity player)
    {
        // Clear the existing stats
        setupStats();

        // Add all existing body part stats to the players stats
        for (BodyPart part : BodyModifications.getBodyModifications(player).getModifications().values())
        {
            part.onLoad(player.getUUID());
            BodyPartStatControl.addStats(player.getUUID(), part.getStatChanges());
        }

        // Add all existing body part option stats to the players stats
        for (HashMap<String, BodyPartOption> options : BodyModifications.getBodyModifications(player).getModificationOptions().values())
            for (BodyPartOption part : options.values())
            {
                // Only do bodyPart onLoads on client
                if (player.isLocalPlayer())
                    part.onLoad(player.getUUID());

                BodyPartStatControl.addStats(player.getUUID(), part.getStatChanges());
            }

        // Add all existing stat modifiers on active techniques to the player stats
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(player);

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
        {
            Technique tech = techs.getTechnique(i);
            if (tech != null && tech.isActive() && tech.getStats() != null)
                BodyPartStatControl.addStats(player.getUUID(), tech.getStats());
        }

        applyStats(player);
    }

    private void applyStats(PlayerEntity player)
    {
        if (healthModifier != null)
            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthModifier);

        if (movementModifier != null)
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(movementModifier);

        healthModifier = new AttributeModifier("BodyForgeHealth", stats.getStat(StatIDs.maxHP) - StatIDs.defaultMaxHP, AttributeModifier.Operation.ADDITION);
        movementModifier = new AttributeModifier("BodyForgeMove", stats.getStat(StatIDs.movementSpeed) - StatIDs.defaultMovementSpeed, AttributeModifier.Operation.ADDITION);

        player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(healthModifier);
        player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(movementModifier);

        PlayerHealthManager.updateFoodStats(player);
    }
}
