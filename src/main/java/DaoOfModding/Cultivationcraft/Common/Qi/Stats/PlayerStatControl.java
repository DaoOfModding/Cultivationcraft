package DaoOfModding.Cultivationcraft.Common.Qi.Stats;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.PassiveTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;

import java.util.HashMap;

public class PlayerStatControl
{
    PlayerStatModifications stats = new PlayerStatModifications();

    AttributeModifier healthModifier;
    AttributeModifier movementModifier;
    AttributeModifier armorModifier;
    AttributeModifier armorToughnessModifier;
    AttributeModifier swimModifier;
    AttributeModifier attackModifier;
    AttributeModifier rangeModifier;
    AttributeModifier stepHeightModifier;

    // Setup players with the default player stats
    public PlayerStatControl()
    {
        setupStats(stats);
    }

    public void setupStats(PlayerStatModifications newStats)
    {
        newStats.setStat(StatIDs.weight, StatIDs.defaultWeight);
        newStats.setStat(StatIDs.maxHP, StatIDs.defaultMaxHP);
        newStats.setStat(StatIDs.maxStamina, StatIDs.defaultMaxStamina);
        newStats.setStat(StatIDs.lungCapacity, StatIDs.defaultLungCapacity);
        newStats.setStat(StatIDs.qiAbsorbRange, StatIDs.defaultQiAbsorbRange);
        newStats.setStat(StatIDs.staminaUse, StatIDs.defaultStaminaUse);
        newStats.setStat(StatIDs.healthRegen, StatIDs.defaultHealthRegen);
        newStats.setStat(StatIDs.healthStaminaConversion, StatIDs.defaulthealthStaminaConversion);
        newStats.setStat(StatIDs.movementSpeed, StatIDs.defaultMovementSpeed);
        newStats.setStat(StatIDs.swimSpeed, StatIDs.defaultSwimSpeed);
        newStats.setStat(StatIDs.attackRange, StatIDs.defaultAttackRange);
        newStats.setStat(StatIDs.jumpHeight, StatIDs.defaultJumpHeight);
        newStats.setStat(StatIDs.fallHeight, StatIDs.defaultFallHeight);
        newStats.setStat(StatIDs.legSupport, StatIDs.defaultLegSupport);
        newStats.setStat(StatIDs.armAttackModifier, StatIDs.defaultAttackModifier);
        newStats.setStat(StatIDs.attackRange, StatIDs.defaultAttackRange);
        newStats.setElementalStat(StatIDs.resistanceModifier, Elements.lightningElement, StatIDs.defaultLightningResist);
    }

    public PlayerStatModifications getStats()
    {
        return stats;
    }

    public void updateStats(Player player)
    {
        PlayerStatModifications newStats = new PlayerStatModifications();

        // Clear the existing stats
        setupStats(newStats);

        newStats.setStat(StatIDs.qiAbsorb, BodyPartQiCostController.calculateQiAbsorb(player));
        newStats.setStat(StatIDs.qiCost, BodyPartQiCostController.calculateQiCost(player));

        // Add all existing body part stats to the players stats
        for (BodyPart part : BodyModifications.getBodyModifications(player).getModifications().values())
        {
            if (player.level.isClientSide())
                part.onLoad(player.getUUID());

            newStats.combine(part.getStatChanges());
        }

        // Add all existing body part option stats to the players stats
        for (HashMap<String, BodyPartOption> options : BodyModifications.getBodyModifications(player).getModificationOptions().values())
            for (BodyPartOption part : options.values())
            {
                if (player.level.isClientSide())
                    part.onLoad(player.getUUID());

                newStats.combine(part.getStatChanges());
            }

        // Add all existing stat modifiers on active techniques to the player stats
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(player);

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
        {
            Technique tech = techs.getTechnique(i);
            if (tech != null && tech.isActive() && tech.getStats() != null)
                newStats.combine(tech.getStats());
        }

        for (PassiveTechnique passive : techs.getPassives())
            newStats.combine(passive.getStats());

        calculateVariantResistances(player, newStats);
        calculateSizeStatChanges(newStats);

        // Apply resistance caps
        BodyPartStatControl.applyCaps(player, newStats);

        stats = newStats;

        // Only apply player attribute modifiers on the server
        if (!player.level.isClientSide)
            applyStats(player);

        PlayerHealthManager.updateFoodStats(player);
        PlayerHealthManager.updateLungs(player);
    }

    // Applies additional stat changes based on player size
    protected void calculateSizeStatChanges(PlayerStatModifications newStats)
    {
        if (newStats.getStats().get(StatIDs.size) == null)
            return;

        PlayerStatModifications sizeChanges = new PlayerStatModifications();

        float sizeAdjustment = getSizeAdjustment(newStats);

        sizeChanges.setStat(StatIDs.attackRange, sizeAdjustment * StatIDs.defaultAttackRange);

        if (sizeAdjustment > 0)
            sizeChanges.setStat(StatIDs.stepHeight, sizeAdjustment * 0.5f);

        newStats.combine(sizeChanges);
    }

    public float getSizeAdjustment()
    {
        return getSizeAdjustment(stats);
    }

    public float getSizeAdjustment(PlayerStatModifications newStats)
    {
        if (!newStats.getStats().containsKey(StatIDs.size))
            return 0;

        return newStats.getStats().get(StatIDs.size);
    }

    // Applies additional resistances for variant elements
    protected void calculateVariantResistances(Player player, PlayerStatModifications newStats)
    {
        PlayerStatModifications resistances = new PlayerStatModifications();

        for (ResourceLocation element : Elements.getElements())
        {
            float resist = Elements.getElement(element).resistanceModifier(player);

            resistances.setElementalStat(StatIDs.resistanceModifier, element, resist);
        }

        newStats.combine(resistances);
    }

    protected void applyStats(Player player)
    {
        if (healthModifier != null)
            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthModifier);

        if (movementModifier != null)
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(movementModifier);

        if (armorModifier != null)
            player.getAttribute(Attributes.ARMOR).removeModifier(armorModifier);

        if (armorToughnessModifier != null)
            player.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(armorToughnessModifier);

        if (swimModifier != null)
            player.getAttribute(ForgeMod.SWIM_SPEED.get()).removeModifier(swimModifier);

        if (rangeModifier != null)
            player.getAttribute(ForgeMod.REACH_DISTANCE.get()).removeModifier(rangeModifier);

        if (attackModifier != null)
            player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(attackModifier);

        if (stepHeightModifier != null)
            player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).removeModifier(stepHeightModifier);

        healthModifier = new AttributeModifier("BodyForgeHealth", stats.getStat(StatIDs.maxHP) - StatIDs.defaultMaxHP, AttributeModifier.Operation.ADDITION);
        movementModifier = new AttributeModifier("BodyForgeMove", stats.getStat(StatIDs.movementSpeed) * getLegWeightModifier() - StatIDs.defaultMovementSpeed, AttributeModifier.Operation.ADDITION);
        armorModifier = new AttributeModifier("BodyForgeArmor", stats.getStat(StatIDs.armor), AttributeModifier.Operation.ADDITION);
        armorToughnessModifier = new AttributeModifier("BodyForgeArmorToughness", stats.getStat(StatIDs.armorToughness), AttributeModifier.Operation.ADDITION);
        swimModifier = new AttributeModifier("BodyForgeSwimSpeed", stats.getStat(StatIDs.swimSpeed) - StatIDs.defaultSwimSpeed, AttributeModifier.Operation.ADDITION);
        rangeModifier = new AttributeModifier("BodyForgeReach", stats.getStat(StatIDs.attackRange) - StatIDs.defaultAttackRange, AttributeModifier.Operation.ADDITION);
        attackModifier = new AttributeModifier("BodyForgeAttack", stats.getStat(StatIDs.armAttackModifier)  - StatIDs.defaultAttackModifier, AttributeModifier.Operation.MULTIPLY_TOTAL);
        stepHeightModifier = new AttributeModifier("BodyForgeStepHeight", stats.getStat(StatIDs.stepHeight) , AttributeModifier.Operation.ADDITION);

        player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(healthModifier);
        player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(movementModifier);
        player.getAttribute(Attributes.ARMOR).addTransientModifier(armorModifier);
        player.getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(armorToughnessModifier);
        player.getAttribute(ForgeMod.SWIM_SPEED.get()).addTransientModifier(swimModifier);
        player.getAttribute(ForgeMod.REACH_DISTANCE.get()).addTransientModifier(rangeModifier);
        player.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(attackModifier);
        player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).addTransientModifier(stepHeightModifier);
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
