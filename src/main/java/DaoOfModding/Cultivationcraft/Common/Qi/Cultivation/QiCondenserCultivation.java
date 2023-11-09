package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import net.minecraft.world.entity.player.Player;

public class QiCondenserCultivation extends CultivationType
{
    public QiCondenserCultivation()
    {
        this(1);
    }

    public QiCondenserCultivation(int cultivationStage)
    {
        // TODO
        /*
        passive = new FoundationPassive();
        techLevel = 100;
        maxedTechsToBreakthrough = 3;
        maxStage = 5;
        stage = cultivationStage;
        screen = new FoundationEstablishmentScreen();
        tribulation = new Tribulation(maxStage, 10, 0.2f);

        ID = "cultivationcraft.cultivation.foundation";*/
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
