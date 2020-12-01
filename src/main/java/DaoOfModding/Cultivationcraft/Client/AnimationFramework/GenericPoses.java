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

    private static void setupHandsBehind()
    {
        HandsBehind.addAngle(PlayerPose.Limb.LEFTARM, new Vector3d(Math.toRadians(30), 0, Math.toRadians(30)), 2);
        HandsBehind.addAngle(PlayerPose.Limb.RIGHTARM, new Vector3d(Math.toRadians(30), 0, Math.toRadians(-30)), 2);
    }

    private static void setupCrossLegs()
    {
        CrossLegs.addAngle(PlayerPose.Limb.LEFTARM, new Vector3d(Math.toRadians(-30), 0, Math.toRadians(30)), 2);
        CrossLegs.addAngle(PlayerPose.Limb.RIGHTARM, new Vector3d(Math.toRadians(-30), 0, Math.toRadians(-30)), 2);

        CrossLegs.addAngle(PlayerPose.Limb.LEFTLEG, new Vector3d(Math.toRadians(-90), Math.toRadians(45), 0), 10);
        CrossLegs.addAngle(PlayerPose.Limb.RIGHTLEG, new Vector3d(Math.toRadians(-90), Math.toRadians(-45), 0), 10);
    }
}
