package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens.QiCondenserScreen;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.CultivationPassives.FoundationPassive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

public class QiCondenserCultivation extends CultivationType
{
    protected HashMap<ResourceLocation, float> elementProgression = new HashMap();

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

    @Override
    public float progressCultivation(Player player, float Qi, ResourceLocation element)
    {
        float changedQi = super.progressCultivation(player, Qi, element);

        float usedQi = Qi - changedQi;

        if (!elementProgression.containsKey(element))
            elementProgression.put(element, 0);

        elementProgression.put(element, elementProgression.get(element) + usedQi);

        return changedQi;
    }

    public ResourceLocation getCurrentElementFocus()
    {
        float amount = 0;
        ResourceLocation currentElement = Elements.noElement;

        for (ResourceLocation element : elementProgression.keySet())
        {
            if (elementProgression.get(element) > amount)
            {
                amount = elementProgression.get(element);
                currentElement = element;
            }
        }

        return currentElement;
    }
}
