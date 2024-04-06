package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.*;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreForming.FireFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.FireModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.TechniqueModifier;
import DaoOfModding.Cultivationcraft.Cultivationcraft;

import java.util.ArrayList;

public class ExternalCultivationHandler
{
    public static void init()
    {
        addCultivation(NoCultivation.class);
        addCultivation(FoundationEstablishmentCultivation.class);
        addCultivation(QiCondenserCultivation.class);
        addCultivation(CoreFormingCultivation.class);
        addCultivation(FireFormingCultivation.class);

        addModifier(TechniqueModifier.class);
        addModifier(FireModifier.class);
    }

    // List of all techniques available in the game
    protected static ArrayList<Class<? extends CultivationType>> cultivationTypes = new ArrayList<Class<? extends CultivationType>>();
    protected static ArrayList<Class<? extends TechniqueModifier>> cultivationModifiers = new ArrayList<Class<? extends TechniqueModifier>>();

    public static void addCultivation(Class<? extends CultivationType> cultivation)
    {
        cultivationTypes.add(cultivation);
    }

    public static void addModifier(Class<? extends TechniqueModifier> modifier)
    {
        cultivationModifiers.add(modifier);
    }

    public static CultivationType getCultivation(String className)
    {
        try
        {
            for (Class<? extends CultivationType> cultivation : cultivationTypes)
            {
                if (cultivation.toString().compareTo(className) == 0)
                    return cultivation.newInstance();
            }
        }
        catch (IllegalAccessException | InstantiationException e)
        {
            Cultivationcraft.LOGGER.error("Error " + e + " whilst getting player cultivation of ID " + className);
        }

        return new NoCultivation();
    }

    public static TechniqueModifier getModifier(String className)
    {
        try
        {
            for (Class<? extends TechniqueModifier> modifier : cultivationModifiers)
            {
                if (modifier.toString().compareTo(className) == 0)
                    return modifier.newInstance();
            }
        }
        catch (IllegalAccessException | InstantiationException e)
        {
            Cultivationcraft.LOGGER.error("Error " + e + " whilst getting technique modifier of ID " + className);
        }

        return new TechniqueModifier();
    }
}
