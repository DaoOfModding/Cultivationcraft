package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.*;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques.*;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.*;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques.JetTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques.*;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public class TechniqueControl
{
    // Initialises all techniques
    public static void init()
    {
        TechniqueControl.addTechnique(MeditateTechnique.class);
        TechniqueControl.addTechnique(DivineSenseTechnique.class);
        TechniqueControl.addTechnique(QiBarrierTechnique.class);
        TechniqueControl.addTechnique(ItemEnhanceTechnique.class);
        TechniqueControl.addTechnique(FlightTechnique.class);
        TechniqueControl.addTechnique(QiEmission.class);
        TechniqueControl.addTechnique(FlyingSwordFormationTechnique.class);
        //TechniqueControl.addTechnique(IceWalkTechnique.class);
        //TechniqueControl.addTechnique(IceAuraTechnique.class);
        TechniqueControl.addTechnique(LeapTechnique.class);
        TechniqueControl.addTechnique(BiteTechnique.class);
        TechniqueControl.addTechnique(RollTechnique.class);
        TechniqueControl.addTechnique(SpreadTechnique.class);
        TechniqueControl.addTechnique(FloatTechnique.class);
        TechniqueControl.addTechnique(DartTechnique.class);
        TechniqueControl.addTechnique(BounceTechnique.class);
        TechniqueControl.addTechnique(JetLegTechnique.class);
        TechniqueControl.addTechnique(JetTechnique.class);
        TechniqueControl.addTechnique(SpitTechnique.class);
        TechniqueControl.addTechnique(BeamTechnique.class);
        TechniqueControl.addTechnique(SpinPetalTechnique.class);
        TechniqueControl.addPassiveTechnique(ExpandingStomachTechnique.class);
        TechniqueControl.addPassiveTechnique(GlideTechnique.class);
    }

    // List of all techniques available in the game
    protected static ArrayList<Class> techniques = new ArrayList<Class>();

    // List of all passiveTechniques available in the game
    protected static ArrayList<Class> passiveTechniques = new ArrayList<Class>();

    public static void addTechnique(Class technique)
    {
        techniques.add(technique);
    }

    public static void addPassiveTechnique(Class technique)
    {
        passiveTechniques.add(technique);
    }

    public static ArrayList<Class> getPassiveTechniques(Player player)
    {
        ArrayList<Class> available = new ArrayList<Class>();

        for (Class technique : passiveTechniques)
            if (isAvailable(technique, player))
                available.add(technique);

        return available;
    }

    // Returns a list of all the techniques that can be used by the specified player
    public static ArrayList<Class> getAvailableTechnics(Player player)
    {
        ArrayList<Class> available = new ArrayList<Class>();

        for (Class technique : techniques)
            if (isAvailable(technique, player))
                available.add(technique);

        return available;
    }

    public static boolean isAvailable(Class technique, Player player)
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
