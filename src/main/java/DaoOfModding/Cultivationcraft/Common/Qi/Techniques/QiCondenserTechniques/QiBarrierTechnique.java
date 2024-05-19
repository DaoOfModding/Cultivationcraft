package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques;

import DaoOfModding.Cultivationcraft.Client.Renderers.QiGlowRenderer;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.TechniqueModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.TechniqueStatModification;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;

public class QiBarrierTechnique extends Technique
{
    public static final ResourceLocation qiToHealthRatio = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.tstat.qihealthratio");
    public static final ResourceLocation statusResist = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.tstat.statusresist");

    public QiBarrierTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.barrier";
        Element = Elements.noElement;

        type = useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/barrier.png");

        canLevel = true;

        addMinTechniqueStat(DefaultTechniqueStatIDs.qiCost, 0.1);
        addMaxTechniqueStat(statusResist, 1);

        TechniqueStatModification qiCostModification = new TechniqueStatModification(DefaultTechniqueStatIDs.qiCost);
        TechniqueStatModification qiToHealthModification = new TechniqueStatModification(qiToHealthRatio);
        TechniqueStatModification statusResistModification = new TechniqueStatModification(statusResist);

        qiCostModification.addStatChange(DefaultTechniqueStatIDs.qiCost, -0.0005);

        qiToHealthModification.addStatChange(DefaultTechniqueStatIDs.qiCost, 0.001);
        qiToHealthModification.addStatChange(qiToHealthRatio, 0.0001);

        statusResistModification.addStatChange(statusResist, 0.0001);
        statusResistModification.addStatChange(qiToHealthRatio, -0.0001);


        addTechniqueStat(DefaultTechniqueStatIDs.qiCost, 1, qiCostModification);
        addTechniqueStat(qiToHealthRatio, 0.1, qiToHealthModification);
        addTechniqueStat(statusResist, 0, statusResistModification);
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
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        if (!CultivatorStats.getCultivatorStats(event.player).getCultivation().consumeQi(event.player, getTechniqueStat(DefaultTechniqueStatIDs.qiCost, event.player) / 20f))
        {
            deactivate(event.player);
            return;
        }

        QiGlowRenderer.setQiVisible(event.player, Elements.getElement(getElement()));

        super.tickClient(event);
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        if (!CultivatorStats.getCultivatorStats(event.player).getCultivation().consumeQi(event.player, getTechniqueStat(DefaultTechniqueStatIDs.qiCost, event.player) / 20f))
        {
            deactivate(event.player);
            return;
        }

        tickTechniqueModifiers(event.player, event.player.position(), getElement());

        super.tickServer(event);
    }

    @Override
    public float onDamage(QiDamageSource source, float amount, Player player)
    {
        float resist = 1 - BodyPartStatControl.getStats(player).getElementalStat(StatIDs.resistanceModifier, source.damageElement) / 100f;

        resist *= amount;

        if (CultivatorStats.getCultivatorStats(player).getCultivation().consumeQi(player, resist / getTechniqueStat(qiToHealthRatio, player)))
        {
            levelUp(player, amount);

            for (TechniqueModifier mod : CultivatorStats.getCultivatorStats(player).getCultivation().getModifiers())
                mod.onHitTaken(player, resist, getElement());

            if (source.doStatusEffect())
                Elements.getElement(source.getElement()).applyStatusEffect(source, player, (float)(resist - getTechniqueStat(statusResist, player) * resist));

            return 0;
        }

        return amount;
    }
}
