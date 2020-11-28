package DaoOfModding.Cultivationcraft.Common;

import net.minecraft.util.math.vector.Vector3d;

public class PlayerPose
{
    public enum Limb {LEFTARM, RIGHTARM, LEFTLEG, RIGHTLEG}

    private int[] priorities = new int[4];
    private Vector3d[] angles = new Vector3d[4];

    public PlayerPose() {}

    // Sets angle for specified limb with the specified priority level
    public void setAngle(Limb limb, Vector3d angle, int priority)
    {
        angles[limb.ordinal()] = angle;
        priorities[limb.ordinal()] = priority;
    }

    // Check if an angle for this limb exists
    public boolean hasAngle(Limb limb)
    {
        return (angles[limb.ordinal()] != null);
    }

    // Get the angle for the specified limb
    public Vector3d getAngle(Limb limb)
    {
        return angles[limb.ordinal()];
    }

    public int getPriority(Limb limb)
    {
        return priorities[limb.ordinal()];
    }

    public PlayerPose copy()
    {
        PlayerPose copy = new PlayerPose();

        for (Limb limb : Limb.values())
            if (hasAngle(limb))
                copy.setAngle(limb, getAngle(limb), getPriority(limb));

        return copy;
    }
}
