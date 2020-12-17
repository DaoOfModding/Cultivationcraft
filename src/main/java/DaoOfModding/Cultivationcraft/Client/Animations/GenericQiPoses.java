package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.GenericPoses;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.PlayerPose;
import net.minecraft.util.math.vector.Vector3d;

public class GenericQiPoses
{
    public static PlayerPose HandsBehind = new PlayerPose();
    public static PlayerPose CrossLegs = new PlayerPose();

    // Rotates around Z, then Y, then X

    public static void init()
    {
        setupHandsBehind();
        setupCrossLegs();
    }

    // TODO: Add walking, swimming, sleeping, sitting, etc poses

    private static void setupHandsBehind()
    {
        HandsBehind.addAngle("LEFT_ARM", new Vector3d(Math.toRadians(30), 0, Math.toRadians(15)), GenericPoses.walkArmPriority + 2);
        HandsBehind.addAngle("RIGHT_ARM", new Vector3d(Math.toRadians(30), 0, Math.toRadians(-15)), GenericPoses.walkArmPriority + 2);

        HandsBehind.addAngle("LOWER_LEFT_ARM", new Vector3d(0, 0, Math.toRadians(30)), GenericPoses.walkArmPriority + 2);
        HandsBehind.addAngle("LOWER_RIGHT_ARM", new Vector3d(0, 0, Math.toRadians(-30)), GenericPoses.walkArmPriority + 2);
    }

    private static void setupCrossLegs()
    {
        CrossLegs.addAngle("LEFT_ARM", new Vector3d(Math.toRadians(30), Math.toRadians(-180), 0), GenericPoses.walkArmPriority + 4);
        CrossLegs.addAngle("RIGHT_ARM", new Vector3d(Math.toRadians(30), Math.toRadians(180), 0), GenericPoses.walkArmPriority + 4);

        CrossLegs.addAngle("LOWER_LEFT_ARM", new Vector3d(0, 0, Math.toRadians(30)), GenericPoses.walkArmPriority + 4);
        CrossLegs.addAngle("LOWER_RIGHT_ARM", new Vector3d(0, 0, Math.toRadians(-30)), GenericPoses.walkArmPriority + 4);

        CrossLegs.addAngle("LEFT_LEG", new Vector3d(Math.toRadians(45), Math.toRadians(-180), Math.toRadians(-90)), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle("RIGHT_LEG", new Vector3d(Math.toRadians(45), Math.toRadians(180), Math.toRadians(90)), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle("LOWER_LEFT_LEG", new Vector3d(Math.toRadians(90), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle("LOWER_RIGHT_LEG", new Vector3d(Math.toRadians(90), 0, 0), GenericPoses.walkLegPriority + 4);
    }
}
