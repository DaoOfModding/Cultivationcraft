package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.util.math.vector.Vector3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PlayerPose
{
    private HashMap<String, Integer> priorities = new HashMap<String, Integer>();

    // X = Depth, positive goes backwards, negative goes forward
    // Y = Rotation
    // Z = Left and Right, Positive goes right, negative goes left
    private HashMap<String, ArrayList<Vector3d>> angles = new HashMap<String, ArrayList<Vector3d>>();
    private HashMap<String, ArrayList<Double>> speed = new HashMap<String, ArrayList<Double>>();


    private HashMap<String, Vector3d> offset = new HashMap<String, Vector3d>();
    private HashMap<String, Integer> aLock = new HashMap<String, Integer>();

    public PlayerPose()
    {
    }

    // Set all angles on the specified limb to the specified values
    public void setAngles(String limb, ArrayList<Vector3d> newAngles, ArrayList<Double> newSpeeds, int priority, Vector3d off, int animationLock)
    {
        angles.put(limb, newAngles);
        speed.put(limb, newSpeeds);
        priorities.put(limb, priority);

        offset.put(limb, off);
        aLock.put(limb, animationLock);
    }

    // Add the specified offset into the offset vector for the specified limb
    // The offset will be added to the angle at render time
    public void addOffset(String limb, Vector3d offsetVector)
    {
        if (offset.containsKey(limb))
            offsetVector = offset.get(limb).add(offsetVector);

        offset.put(limb, offsetVector);
    }

    public Vector3d getOffset(String limb)
    {
        if (offset.containsKey(limb))
            return offset.get(limb);

        return new Vector3d(0, 0, 0);
    }

    public int getAnimationLock(String limb)
    {
        if (aLock.containsKey(limb))
            return aLock.get(limb);

        return -1;
    }

    // Returns a list of all limbs used in this pose
    public Set<String> getLimbs()
    {
        return angles.keySet();
    }

    // Get all angle frames for the specified limb
    public ArrayList<Vector3d> getAngles(String limb)
    {
        return angles.get(limb);
    }

    // Get all angle frames for the specified limb
    public ArrayList<Double> getSpeeds(String limb)
    {
        return speed.get(limb);
    }

    // Adds angle to specified limb with the specified priority level
    public void addAngle(String limb, Vector3d angle, int priority)
    {
        addAngle(limb, angle, priority, AnimationSpeedCalculator.defaultSpeed, -1);
    }

    // Adds angle to specified limb with the specified priority level, speed and animation lock
    public void addAngle(String limb, Vector3d angle, int priority, double aSpeed, int animationLock)
    {
        // If the specified limb has not been initialised, initialise it
        if (!angles.containsKey(limb))
        {
            angles.put(limb, new ArrayList<Vector3d>());
            speed.put(limb, new ArrayList<Double>());
        }

        angles.get(limb).add(angle);
        speed.get(limb).add(aSpeed);

        priorities.put(limb, priority);
        aLock.put(limb, animationLock);
    }

    public double getAnimationSpeed(String limb, int frame)
    {
        return speed.get(limb).get(frame);
    }

    // Check if an angle for this limb exists
    public boolean hasAngle(String limb)
    {
        return (angles.containsKey(limb));
    }

    // Get the angle of the current frame for the specified limb
    public Vector3d getAngle(String limb, int frame)
    {
        return angles.get(limb).get(frame);
    }

    // Get the angle of the first frame for the specified limb
    public Vector3d getAngle(String limb)
    {
        return getAngle(limb, 0);
    }

    // Get the number of frames stored in the specified limb
    public int getFrames(String limb)
    {
        return angles.get(limb).size();
    }

    public int getPriority(String limb)
    {
        return priorities.get(limb);
    }

    // Combines two player poses
    public PlayerPose combine(PlayerPose secondPose)
    {
        PlayerPose copy = secondPose.clone();

        // Loop through every limb in the pose
        for (String limb : angles.keySet())
            // Add the limb to the copy if the copy does not contain that limb or the copy's limb has a lower priority
            if (!copy.hasAngle(limb) || copy.getPriority(limb) < getPriority(limb))
                copy.setAngles(limb, getAngles(limb), getSpeeds(limb), getPriority(limb), getOffset(limb), getAnimationLock(limb));

        return copy;
    }

    public PlayerPose clone()
    {
        PlayerPose copyPose = new PlayerPose();

        for (String limb : angles.keySet())
            copyPose.setAngles(limb, (ArrayList<Vector3d>)angles.get(limb).clone(), (ArrayList<Double>)speed.get(limb).clone(), priorities.get(limb), offset.get(limb), aLock.get(limb));

        return copyPose;
    }
}
