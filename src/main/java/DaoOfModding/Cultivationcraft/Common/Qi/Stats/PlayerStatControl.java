package DaoOfModding.Cultivationcraft.Common.Qi.Stats;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.UUID;

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
        stats = new PlayerStatModifications();
        stats.setStat(StatIDs.weight, StatIDs.defaultWeight);
        stats.setStat(StatIDs.maxHP, StatIDs.defaultMaxHP);
        stats.setStat(StatIDs.maxStamina, StatIDs.defaultMaxStamina);
        stats.setStat(StatIDs.qiAbsorbRange, StatIDs.defaultQiAbsorbRange);
        stats.setStat(StatIDs.staminaUse, StatIDs.defaultStaminaUse);
        stats.setStat(StatIDs.healthRegen, StatIDs.defaultHealthRegen);
        stats.setStat(StatIDs.healthStaminaConversion, StatIDs.defaulthealthStaminaConversion);
        stats.setStat(StatIDs.movementSpeed, StatIDs.defaultMovementSpeed);
        stats.setStat(StatIDs.attackRange, StatIDs.defaultAttackRange);
        stats.setStat(StatIDs.jumpHeight, StatIDs.defaultJumpHeight);
        stats.setStat(StatIDs.fallHeight, StatIDs.defaultFallHeight);
        stats.setStat(StatIDs.legSupport, StatIDs.defaultLegSupport);
    }

    public PlayerStatModifications getStats()
    {
        return stats;
    }

    public void updateStats(Player player)
    {
        // Clear the existing stats
        setupStats();

        stats.setStat(StatIDs.qiAbsorb, BodyPartQiCostController.calculateQiAbsorb(player));
        stats.setStat(StatIDs.qiCost, BodyPartQiCostController.calculateQiCost(player));

        // Add all existing body part stats to the players stats
        for (BodyPart part : BodyModifications.getBodyModifications(player).getModifications().values())
        {
            if (player.level.isClientSide())
                part.onLoad(player.getUUID());

            BodyPartStatControl.addStats(player.getUUID(), part.getStatChanges());
        }

        // Add all existing body part option stats to the players stats
        for (HashMap<String, BodyPartOption> options : BodyModifications.getBodyModifications(player).getModificationOptions().values())
            for (BodyPartOption part : options.values())
            {
                if (player.level.isClientSide())
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

        // Only apply player attribute modifiers on the server
        if (!player.level.isClientSide)
            applyStats(player);

        PlayerHealthManager.updateFoodStats(player);
    }

    protected void applyStats(Player player)
    {
        if (healthModifier != null)
            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthModifier);

        if (movementModifier != null)
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(movementModifier);

        healthModifier = new AttributeModifier("BodyForgeHealth", stats.getStat(StatIDs.maxHP) - StatIDs.defaultMaxHP, AttributeModifier.Operation.ADDITION);
        movementModifier = new AttributeModifier("BodyForgeMove", stats.getStat(StatIDs.movementSpeed) * getLegWeightModifier() - StatIDs.defaultMovementSpeed, AttributeModifier.Operation.ADDITION);

        player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(healthModifier);
        player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(movementModifier);
    }

    // Returns a modifier based on how overweight the player is
    public float getFlightWeightModifier()
    {
        float support = stats.getStat(StatIDs.wingSupport);
        float weight = stats.getStat(StatIDs.weight);

        // No modifier if the players legSupport is not less than the players weight
        if (support >= weight)
            return 1;

        // Return 0.1 if weight is (just under) double or more than the leg support
        return Math.max(0.1f, 2 - weight / support);
    }

    // Returns a modifier based on how overweight the player is
    public float getLegWeightModifier()
    {
        float support = stats.getStat(StatIDs.legSupport);
        float weight = stats.getStat(StatIDs.weight);

        // No modifier if the players legSupport is not less than the players weight
        if (support >= weight)
            return 1;

        // Return 0.1 if weight is (just under) double or more than the leg support
        return Math.max(0.1f, 2 - weight / support);
    }
}
