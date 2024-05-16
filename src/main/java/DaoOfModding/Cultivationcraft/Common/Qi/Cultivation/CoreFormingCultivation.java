package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.ConceptScreen;
import DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens.CoreFormingScreen;
import DaoOfModding.Cultivationcraft.Client.GUI.Screens.GenericTabScreen;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.ExternalCultivationHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.CultivationPassives.QiCondenserPassive;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.*;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
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

        ID = new ResourceLocation(Cultivationcraft.MODID, "cultivation.coreforming");
    }

    public void setCore(TechniqueModifier mod)
    {
        modifiers.add(mod);
        setQuest(mod.getStabaliseQuest());
    }

    public TechniqueModifier getFocus()
    {
        return modifiers.get(0);
    }

    @Override
    public boolean hasAdvancementOptions()
    {
        return true;
    }

    @Override
    public void doPreAdvancementActions(CultivationType advancingFrom, Player player)
    {
        // Learn basic concepts without requiring their quests
        new QiModifier().learn(player);

        ResourceLocation currentElementFocus = ((QiCondenserCultivation) advancingFrom).getCurrentElementFocus();

        if (currentElementFocus.compareTo(Elements.earthElement) == 0 || currentElementFocus.compareTo(Elements.anyElement) == 0)
            new EarthModifier().learn(player);

        if (currentElementFocus.compareTo(Elements.fireElement) == 0 || currentElementFocus.compareTo(Elements.anyElement) == 0)
            new FireModifier().learn(player);

        if (currentElementFocus.compareTo(Elements.iceElement) == 0 || currentElementFocus.compareTo(Elements.anyElement) == 0)
            new IceModifier().learn(player);

        if (currentElementFocus.compareTo(Elements.lightningElement) == 0 || currentElementFocus.compareTo(Elements.anyElement) == 0)
            new LightningModifier().learn(player);

        if (currentElementFocus.compareTo(Elements.waterElement) == 0 || currentElementFocus.compareTo(Elements.anyElement) == 0)
            new WaterModifier().learn(player);

        if (currentElementFocus.compareTo(Elements.windElement) == 0 || currentElementFocus.compareTo(Elements.anyElement) == 0)
            new WindModifier().learn(player);

        if (currentElementFocus.compareTo(Elements.woodElement) == 0 || currentElementFocus.compareTo(Elements.anyElement) == 0)
            new WoodModifier().learn(player);
    }

    @Override
    public boolean doPostAdvancementActions(Player player, String extra)
    {
        TechniqueModifier mod = ExternalCultivationHandler.getModifier(new ResourceLocation(extra));

        if (mod.canUse(player) && mod.hasLearnt(player))
            setCore(mod);
        else
            return  false;

        return true;
    }

    @Override
    public GenericTabScreen getAdvancmentOptionScreen()
    {
        return new ConceptScreen(true);
    }

    @Override
    public boolean canCultivate(ResourceLocation element)
    {
        for (ResourceLocation testElement : getElements())
            if (testElement.compareTo(element) == 0 || testElement.compareTo(Elements.noElement) == 0 || testElement.compareTo(Elements.anyElement) == 0)
                return true;

        return false;
    }

    @Override
    public boolean canCultivate(Player player)
    {
        if (CultivatorStats.getCultivatorStats(player).getCultivation().getClass() == QiCondenserCultivation.class)
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
