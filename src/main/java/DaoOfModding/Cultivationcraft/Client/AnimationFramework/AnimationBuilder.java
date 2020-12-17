package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.util.math.vector.Vector3d;

public class AnimationBuilder
{

    // Generate a pose for limbs based on the angles supplied
    // Inverts the angles for limb2
    public static PlayerPose generateRepeatingMirroredLimbs(String limb1, String limb2, Vector3d[] angles, int priority, int framesPerAngle)
    {
        return generateRepeatingMirroredLimbs(limb1, limb2, angles, priority, framesPerAngle, -1);
    }

    // Generate a pose for limbs based on the angles supplied
    // Plays the provided animation forwards then backwards
    // Starts the second limb going backwards
    public static PlayerPose generateRepeatingMirroredLimbs(String limb1, String limb2, Vector3d[] angles, int priority, int framesPerAngle, int animationLock)
    {
        PlayerPose LegPose = new PlayerPose();

        // Generate appropriate speeds for each frame
        double[] speeds = new double[angles.length];
        for (int i = 0; i < angles.length; i++)
        {
            int lastI = i - 1;

            if (lastI < 0)
                lastI = angles.length-1;

            speeds[i] = AnimationSpeedCalculator.ticksToSpeed(angles[lastI], angles[i], framesPerAngle);
        }

        // Generate each frame going forwards
        for (int i = 0; i < angles.length; i++)
        {
            LegPose.addAngle(limb1, angles[i], priority, speeds[i], animationLock);
            LegPose.addAngle(limb2, angles[angles.length - (i + 1)], priority, speeds[angles.length - (i + 1)], animationLock);
        }

        // Generate each frame going backwards
        for (int i = 1; i < angles.length; i++)
        {
            LegPose.addAngle(limb1, angles[angles.length - (i + 1)], priority, speeds[angles.length - (i + 1)], animationLock);
            LegPose.addAngle(limb2, angles[i], priority, speeds[i], animationLock);
        }

        return LegPose;
    }
}
