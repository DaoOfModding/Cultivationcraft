package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.FoundationEstablishmentCultivation;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

public class ExternalCultivationHandler
{
    public static void init()
    {
        addCultivation(FoundationEstablishmentCultivation.class);
    }

    // List of all techniques available in the game
    protected static ArrayList<Class<? extends CultivationType>> cultivationTypes = new ArrayList<Class<? extends CultivationType>>();

    public static void addCultivation(Class<? extends CultivationType> cultivation)
    {
        cultivationTypes.add(cultivation);
    }

    public static CultivationType getCultivation(ResourceLocation ID)
    {
        try
        {
            for (Class<? extends CultivationType> cultivation : cultivationTypes)
            {
                if (cultivation.newInstance().ID == ID)
                    return cultivation.newInstance();
            }
        }
        catch (IllegalAccessException | InstantiationException e)
        {
            Cultivationcraft.LOGGER.error("Error " + e + " whilst getting player cultivation of ID " + ID);
        }

        return new FoundationEstablishmentCultivation();
    }
}
