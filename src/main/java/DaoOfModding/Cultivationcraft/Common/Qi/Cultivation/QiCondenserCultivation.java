package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens.CultivationTypeScreen;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.CultivationPassives.FoundationPassive;
import net.minecraft.world.entity.player.Player;

public class QiCondenserCultivation extends CultivationType
{
    public QiCondenserCultivation()
    {
        this(1);
    }

    public QiCondenserCultivation(int cultivationStage)
    {
        passive = new FoundationPassive();
        techLevel = 0;
        maxedTechsToBreakthrough = 3;
        maxStage = 5;
        stage = cultivationStage;
        screen = new CultivationTypeScreen();
        tribulation = new Tribulation(maxStage, 50, 0.4f);

        ID = "cultivationcraft.cultivation.qicondensation";
    }

    @Override
    public boolean canBreakthrough(Player player)
    {
        return false;
    }

    @Override
    public void breakthrough(Player player)
    {
        if (stage < maxStage)
        {
            QiCondenserCultivation newCultivation = new QiCondenserCultivation(stage+1);
            newCultivation.setPreviousCultivation(this);

            CultivatorStats.getCultivatorStats(player).setCultivation(newCultivation);
        }
    }
}
