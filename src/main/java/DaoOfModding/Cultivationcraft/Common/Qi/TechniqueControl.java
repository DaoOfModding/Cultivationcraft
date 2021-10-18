package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.*;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques.*;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

public class TechniqueControl
{
    // Initialises all techniques
    public static void init()
    {
        TechniqueControl.addTechnique(DivineSenseTechnique.class);
        TechniqueControl.addTechnique(IceWalkTechnique.class);
        TechniqueControl.addTechnique(IceAuraTechnique.class);
        TechniqueControl.addTechnique(MeditateTechnique.class);
        TechniqueControl.addTechnique(LeapTechnique.class);
        TechniqueControl.addTechnique(BiteTechnique.class);
        TechniqueControl.addTechnique(RollTechnique.class);
        TechniqueControl.addTechnique(SpreadTechnique.class);
        TechniqueControl.addTechnique(FloatTechnique.class);
        TechniqueControl.addTechnique(BounceTechnique.class);
    }

    // List of all techniques available in the game
    private static ArrayList<Class> techniques = new ArrayList<Class>();

    public static void addTechnique(Class technique)
    {
        techniques.add(technique);
    }

    // Returns a list of all the techniques that can be used by the specified player
    public static ArrayList<Class> getAvailableTechnics(PlayerEntity player)
    {
        ArrayList<Class> available = new ArrayList<Class>();

        for (Class technique : techniques)
            if (isAvailable(technique, player))
                available.add(technique);

        return available;
    }

    public static boolean isAvailable(Class technique, PlayerEntity player)
    {
        try
        {
            Technique test = (Technique)technique.newInstance();

            return test.isValid(player);
        }
        catch (Exception e)
        {
            Cultivationcraft.LOGGER.error("Error check technique validity: " + e);

            return false;
        }
    }
}
