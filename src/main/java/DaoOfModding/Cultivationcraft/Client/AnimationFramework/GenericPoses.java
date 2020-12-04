package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.util.math.vector.Vector3d;

public class GenericPoses
{
    public static PlayerPose HandsBehind = new PlayerPose();
    public static PlayerPose CrossLegs = new PlayerPose();

    public static void init()
    {
        setupHandsBehind();
        setupCrossLegs();
    }

    // TODO: Add walking, swimming, sleeping, sitting, etc poses

    private static void setupHandsBehind()
    {
        HandsBehind.addAngle("LEFTARM", new Vector3d(Math.toRadians(30), 0, Math.toRadians(15)), 2);
        HandsBehind.addAngle("RIGHTARM", new Vector3d(Math.toRadians(30), 0, Math.toRadians(-15)), 2);

        HandsBehind.addAngle("LOWERLEFTARM", new Vector3d(0, 0, Math.toRadians(30)), 2);
        HandsBehind.addAngle("LOWERRIGHTARM", new Vector3d(0, 0, Math.toRadians(-30)), 2);
    }

    private static void setupCrossLegs()
    {
        CrossLegs.addAngle("LEFTLEG", new Vector3d(Math.toRadians(-90), Math.toRadians(-45), Math.toRadians(0)), 10);
        CrossLegs.addAngle("RIGHTLEG", new Vector3d(Math.toRadians(-90), Math.toRadians(45), Math.toRadians(0)), 10);

        /*CrossLegs.addAngle("LOWERLEFTLEG", new Vector3d(0, 0, Math.toRadians(90)), 10);
        CrossLegs.addAngle("LOWERRIGHTLEG", new Vector3d(0, 0, Math.toRadians(-90)), 10);*/
    }
}
