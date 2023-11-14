package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques;

import DaoOfModding.Cultivationcraft.Client.Renderers.QiGlowRenderer;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.TechniqueStatModification;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;

public class QiBarrierTechnique extends Technique
{
    public static final ResourceLocation qiToHealthRatio = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.tstat.qihealthratio");

    public QiBarrierTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.barrier";
        Element = Elements.noElement;

        type = useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/barrier.png");

        canLevel = true;

        TechniqueStatModification qiCostModification = new TechniqueStatModification(DefaultTechniqueStatIDs.qiCost);
        TechniqueStatModification qiToHealthModification = new TechniqueStatModification(qiToHealthRatio);

        qiCostModification.addStatChange(DefaultTechniqueStatIDs.qiCost, -0.0001);
        qiToHealthModification.addStatChange(DefaultTechniqueStatIDs.qiCost, 0.001);
        qiToHealthModification.addStatChange(qiToHealthRatio, 0.0001);


        addTechniqueStat(DefaultTechniqueStatIDs.qiCost, 1, qiCostModification);
        addTechniqueStat(qiToHealthRatio, 0.1, qiToHealthModification);
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

        super.tickServer(event);
    }

    @Override
    public float onDamage(QiDamageSource source, float amount, Player player)
    {
        if (CultivatorStats.getCultivatorStats(player).getCultivation().consumeQi(player, amount / getTechniqueStat(qiToHealthRatio, player)))
        {
            levelUp(player, amount);
            return 0;
        }

        return amount;
    }
}
