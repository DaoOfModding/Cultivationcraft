package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.util.math.vector.Vector3d;

import java.util.ArrayList;

public class PlayerPose
{
    public enum Limb {LEFTARM, RIGHTARM, LEFTLEG, RIGHTLEG}

    private int[] priorities = new int[4];

    // X = Depth, positive goes backwards, negative goes forward
    // Y = Rotation
    // Z = Left and Right, Positive goes right, negative goes left
    private ArrayList<Vector3d>[] angles = new ArrayList[4];
    private ArrayList<Double>[] speed = new ArrayList[4];

    public PlayerPose()
    {
        for (Limb limb : Limb.values())
        {
            angles[limb.ordinal()] = new ArrayList<Vector3d>();
            speed[limb.ordinal()] = new ArrayList<Double>();
        }
    }

    // Set all angles on the specified limb to the specified values
    public void setAngles(Limb limb, ArrayList<Vector3d> newAngles, ArrayList<Double> newSpeeds, int priority)
    {
        angles[limb.ordinal()] = newAngles;
        speed[limb.ordinal()] = newSpeeds;
        priorities[limb.ordinal()] = priority;
    }

    // Get all angle frames for the specified limb
    public ArrayList<Vector3d> getAngles(Limb limb)
    {
        return angles[limb.ordinal()];
    }

    // Get all angle frames for the specified limb
    public ArrayList<Double> getSpeeds(Limb limb)
    {
        return speed[limb.ordinal()];
    }

    // Adds angle to specified limb with the specified priority level
    public void addAngle(Limb limb, Vector3d angle, int priority)
    {
        addAngle(limb, angle, priority, PoseHandler.defaultAnimationSpeed);
    }

    // Adds angle to specified limb with the specified priority level and speed
    public void addAngle(Limb limb, Vector3d angle, int priority, double aSpeed)
    {
        angles[limb.ordinal()].add(angle);
        speed[limb.ordinal()].add(aSpeed);

        priorities[limb.ordinal()] = priority;
    }

    public double getAnimationSpeed(Limb limb, int frame)
    {
        return speed[limb.ordinal()].get(frame);
    }

    // Check if an angle for this limb exists
    public boolean hasAngle(Limb limb)
    {
        return (angles[limb.ordinal()].size() > 0);
    }

    // Get the angle of the current frame for the specified limb
    public Vector3d getAngle(Limb limb, int frame)
    {
        return angles[limb.ordinal()].get(frame);
    }

    // Get the angle of the first frame for the specified limb
    public Vector3d getAngle(Limb limb)
    {
        return getAngle(limb, 0);
    }

    // Get the number of frames stored in the specified limb
    public int getFrames(Limb limb)
    {
        return angles[limb.ordinal()].size();
    }

    public int getPriority(Limb limb)
    {
        return priorities[limb.ordinal()];
    }

    public PlayerPose clone()
    {
        PlayerPose copyPose = new PlayerPose();

        for (Limb limb : Limb.values())
            copyPose.setAngles(limb, (ArrayList<Vector3d>)angles[limb.ordinal()].clone(), (ArrayList<Double>)speed[limb.ordinal()].clone(), priorities[limb.ordinal()]);

        return copyPose;
    }
}
