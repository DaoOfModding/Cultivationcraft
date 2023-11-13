package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.AttackTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.TechniqueStatModification;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class ItemEnhanceTechnique extends AttackTechnique
{
    protected boolean attack = false;

    public ItemEnhanceTechnique()
    {
        super();

        removeTechniqueStat(DefaultTechniqueStatIDs.range);

        langLocation = "cultivationcraft.technique.itemenhance";
        Element = Elements.noElement;

        type = Technique.useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/itemenhance.png");

        canLevel = true;

        TechniqueStatModification qiCostModification = new TechniqueStatModification(DefaultTechniqueStatIDs.qiCost);
        TechniqueStatModification damageModification = new TechniqueStatModification(DefaultTechniqueStatIDs.damage);

        qiCostModification.addStatChange(DefaultTechniqueStatIDs.qiCost, -0.01);
        damageModification.addStatChange(DefaultTechniqueStatIDs.qiCost, 0.05);
        damageModification.addStatChange(DefaultTechniqueStatIDs.damage, 0.01);

        addTechniqueStat(DefaultTechniqueStatIDs.qiCost, 10, qiCostModification);
        addTechniqueStat(DefaultTechniqueStatIDs.damage, 1, damageModification);
    }

    @Override
    public boolean isValid(Player player)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() == CultivationTypes.QI_CONDENSER)
            return true;

        return false;
    }

    @Override
    public void onPlayerAttack(AttackEntityEvent event)
    {
        attack = true;
    }

    @Override
    public boolean cancelAttack(LivingAttackEvent event)
    {
        if (!attack)
            return false;

        attack = false;

        Player attackingPlayer = (Player)event.getSource().getEntity();

        // Do nothing if player does not have enough Qi to use this technique
        if (!CultivatorStats.getCultivatorStats(attackingPlayer).getCultivation().consumeQi(attackingPlayer, getTechniqueStat(DefaultTechniqueStatIDs.qiCost, attackingPlayer)))
        {
            deactivate(attackingPlayer);
            return false;
        }

        levelUp(attackingPlayer, 2);

        // Make a new QiDamageSource of this techniques element
        QiDamageSource newSource = new QiDamageSource(event.getSource().getMsgId(), event.getSource().getEntity(), getElement(), true);

        // Hurt the target entity with the new damage source and increased damage
        event.getEntity().hurt(newSource, event.getAmount() + (float)getTechniqueStat(DefaultTechniqueStatIDs.damage, attackingPlayer));

        // Cancel the initial attack
        return true;
    }
}
