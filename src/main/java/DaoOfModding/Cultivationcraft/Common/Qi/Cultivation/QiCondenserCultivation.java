package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens.QiCondenserScreen;
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
        super(cultivationStage);
        passive = new FoundationPassive();
        maxedTechsToBreakthrough = 3;
        maxStage = 8;
        screen = new QiCondenserScreen();
        tribulation = new Tribulation(maxStage, 50, 0.4f);

        ID = "cultivationcraft.cultivation.qicondensation";
    }

    public void stageCalculations()
    {
        techLevel = 200 + (100 * stage);
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
