package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.FoundationEstablishmentCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.NoCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.QiCondenserCultivation;
import DaoOfModding.Cultivationcraft.Cultivationcraft;

import java.util.ArrayList;

public class ExternalCultivationHandler
{
    public static void init()
    {
        addCultivation(NoCultivation.class);
        addCultivation(FoundationEstablishmentCultivation.class);
        addCultivation(QiCondenserCultivation.class);
    }

    // List of all techniques available in the game
    protected static ArrayList<Class<? extends CultivationType>> cultivationTypes = new ArrayList<Class<? extends CultivationType>>();

    public static void addCultivation(Class<? extends CultivationType> cultivation)
    {
        cultivationTypes.add(cultivation);
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
}
