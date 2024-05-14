package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens.CoreFormingScreen;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.CultivationPassives.QiCondenserPassive;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.TechniqueModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class CoreFormingCultivation extends CultivationType
{
    ResourceLocation myElement = Elements.noElement;

    public CoreFormingCultivation() {
        this(1);
    }

    public CoreFormingCultivation(int cultivationStage)
    {
        super(cultivationStage);
        maxedTechsToBreakthrough = 0;
        maxStage = 9;
        // TEMP
        passive = new QiCondenserPassive();
        screen = new CoreFormingScreen();
        tribulation = new Tribulation(maxStage, 75, 0.6f);
        statLeveling = false;

        ID = "cultivationcraft.cultivation.coreforming";
    }

    public void setCore(TechniqueModifier mod)
    {
        modifiers.add(mod);
        setQuest(mod.getStabaliseQuest());
    }

    public void setElement(ResourceLocation element) {
        myElement = element;
    }

    public ResourceLocation getElement() {
        return myElement;
    }

    public TechniqueModifier getFocus()
    {
        return modifiers.get(0);
    }

    @Override
    public boolean canCultivate(ResourceLocation element) {
        if (getElement().compareTo(element) == 0 || getElement().compareTo(Elements.noElement) == 0 || getElement().compareTo(Elements.anyElement) == 0)
            return true;

        return false;
    }

    @Override
    public boolean canCultivate(Player player) {
        if (getElement() == Elements.noElement)
            return true;

        if (CultivatorStats.getCultivatorStats(player).getCultivation().getClass() == QiCondenserCultivation.class &&
                ((((QiCondenserCultivation) CultivatorStats.getCultivatorStats(player).getCultivation()).getCurrentElementFocus().compareTo(getElement()) == 0) ||
                        ((QiCondenserCultivation) CultivatorStats.getCultivatorStats(player).getCultivation()).getCurrentElementFocus().compareTo(Elements.anyElement) == 0))
            return true;

        if (CultivatorStats.getCultivatorStats(player).getCultivation().getClass() == CoreFormingCultivation.class &&
                ((CoreFormingCultivation) CultivatorStats.getCultivatorStats(player).getCultivation()).getElement().compareTo(getElement()) == 0)
            return true;

        return false;
    }

    public void stageCalculations()
    {
        techLevel = 1000;
    }

    // TEMP whilst no further cultivation
    public boolean canBreakthrough(Player player) {
        if (stage == maxStage)
            return false;

        return super.canBreakthrough(player);
    }

    @Override
    public void breakthrough(Player player) {
        if (stage < maxStage) {
            CoreFormingCultivation newCultivation = new CoreFormingCultivation(stage + 1);
            newCultivation.setPreviousCultivation(this);

            CultivatorStats.getCultivatorStats(player).setCultivation(newCultivation);
        }
    }
}
