package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens.FoundationEstablishmentScreen;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.CultivationPassives.FoundationPassive;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class FoundationEstablishmentCultivation extends CultivationType
{
    public FoundationEstablishmentCultivation()
    {
        this(1);
    }

    public FoundationEstablishmentCultivation(int cultivationStage)
    {
        passive = new FoundationPassive();
        techLevel = 100;
        maxedTechsToBreakthrough = 3;
        maxStage = 5;
        stage = cultivationStage;
        screen = new FoundationEstablishmentScreen();
        tribulation = new Tribulation(maxStage, 20, 0.2f);

            advancements.add(new QiCondenserCultivation(1));

        ID = "cultivationcraft.cultivation.foundation";
    }

    @Override
    public void breakthrough(Player player)
    {
        if (stage < maxStage)
        {
            FoundationEstablishmentCultivation newCultivation = new FoundationEstablishmentCultivation(stage+1);
            newCultivation.setPreviousCultivation(this);

            CultivatorStats.getCultivatorStats(player).setCultivation(newCultivation);
        }
    }
}
