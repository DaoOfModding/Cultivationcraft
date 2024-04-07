package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens.QiCondenserScreen;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreForming.FireFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreForming.IceFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.CultivationPassives.QiCondenserPassive;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultCultivationStatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class QiCondenserCultivation extends CultivationType
{
    protected HashMap<ResourceLocation, Float> elementProgression = new HashMap();

    public QiCondenserCultivation()
    {
        this(1);
    }

    public QiCondenserCultivation(int cultivationStage)
    {
        super(cultivationStage);
        passive = new QiCondenserPassive();
        maxedTechsToBreakthrough = 3;
        maxStage = 8;
        screen = new QiCondenserScreen();
        tribulation = new Tribulation(maxStage, 50, 0.4f);

        advancements.add(new CoreFormingCultivation());
        advancements.add(new FireFormingCultivation());
        advancements.add(new IceFormingCultivation());

        ID = "cultivationcraft.cultivation.qicondensation";
    }

    public void stageCalculations()
    {
        techLevel = 200 + (100 * stage);
    }

    public boolean canBreakthrough(Player player)
    {
        return super.canBreakthrough(player);
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
        if (Qi == 0)
            return 0;

        float changedQi = super.progressCultivation(player, Qi, element);

        float usedQi = Qi - changedQi;

        if (!elementProgression.containsKey(element))
            elementProgression.put(element, usedQi);
        else
            elementProgression.put(element, elementProgression.get(element) + usedQi);

        return changedQi;
    }

    @Override
    public void reset(Player player)
    {
        super.reset(player);

        elementProgression = new HashMap<>();
    }

    // Returns the % of current cultivation that is of the selected element for this stage of cultivation
    public float getElementFocusAmountRaw(ResourceLocation element)
    {
        float progress = getTechLevelProgressWithoutPrevious(passive.getClass().toString());
        float amount = 1;

        // Only try and calculate the % if any Qi has been cultivated so far
        if (progress > 0)
        {
            amount = 0;

            if (elementProgression.containsKey(element))
                amount = elementProgression.get(element);

            // If this is not the anyElement, add values from the any element to this one
            if (element.compareTo(Elements.anyElement) != 0)
                if (elementProgression.containsKey(Elements.anyElement))
                    amount += elementProgression.get(Elements.anyElement);

            if (amount != 0)
                amount /= progress;
        }

        if (amount > 1)
            amount = 1;

        // ensuring the % isn't as high if this cultivation level is not yet finished
        amount *= (float)getTechLevelProgressWithoutPrevious(passive.getClass().toString()) / (float)getMaxTechLevelWithoutPrevious();

        if (getPreviousCultivation() instanceof QiCondenserCultivation)
            amount += ((QiCondenserCultivation)getPreviousCultivation()).getElementFocusAmountRaw(element);

        return amount;
    }

    // Returns the % of current cultivation that is of the selected element
    public float getElementFocusAmount(ResourceLocation element)
    {
        float amount = getElementFocusAmountRaw(element);

        amount /= (stage - (1 - (float)getTechLevelProgressWithoutPrevious(passive.getClass().toString()) / (float)getMaxTechLevelWithoutPrevious()));

        if (amount > 1)
            return 1;

        return amount;
    }

    public ResourceLocation getCurrentElementFocus()
    {
        float amount = 0;
        ResourceLocation currentElement = Elements.noElement;

        HashMap<ResourceLocation, Float> combinedElements = getCombinedElements();

        for (ResourceLocation element : combinedElements.keySet())
        {
            if (element.compareTo(Elements.anyElement) != 0)
                if (combinedElements.get(element) > amount)
                {
                    amount = combinedElements.get(element);
                    currentElement = element;
                }
        }

        return currentElement;
    }

    protected HashMap<ResourceLocation, Float> getCombinedElements()
    {
        if (getPreviousCultivation() instanceof QiCondenserCultivation)
            return combine(elementProgression, ((QiCondenserCultivation)getPreviousCultivation()).getCombinedElements());

        return elementProgression;
    }

    protected HashMap<ResourceLocation, Float> combine(HashMap<ResourceLocation, Float> set1, HashMap<ResourceLocation, Float> set2)
    {
        HashMap<ResourceLocation, Float> output = new HashMap<ResourceLocation, Float>();

        for (ResourceLocation element : set1.keySet())
            output.put(element, set1.get(element));

        for (ResourceLocation element : set2.keySet())
        {
            float value = 0;
            if (output.containsKey(element))
                value = output.get(element);

            output.put(element, value + set2.get(element));
        }

        return output;
    }

    @Override
    public CompoundTag writeNBT()
    {
        CompoundTag nbt = super.writeNBT();

        int i = 0;
        for (Map.Entry<ResourceLocation, Float> elementEntry : elementProgression.entrySet())
        {
            nbt.putString("ELEMENT"+i+"NAME", elementEntry.getKey().toString());
            nbt.putFloat("ELEMENT"+i+"VALUE", elementEntry.getValue());
            i++;
        }

        return nbt;
    }

    @Override
    public void readNBT(CompoundTag nbt)
    {
        super.readNBT(nbt);

        int i = 0;
        HashMap<ResourceLocation, Float> values = new HashMap<>();

        while (nbt.contains("ELEMENT"+i+"NAME"))
        {
            values.put(new ResourceLocation(nbt.getString("ELEMENT"+i+"NAME")), nbt.getFloat("ELEMENT"+i+"VALUE"));

            i++;
        }

        elementProgression = values;
    }
}
