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

    public PlayerPose()
    {
    }

    // Set all angles on the specified limb to the specified values
    public void setAngles(String limb, ArrayList<Vector3d> newAngles, ArrayList<Double> newSpeeds, int priority)
    {
        angles.put(limb, newAngles);
        speed.put(limb, newSpeeds);
        priorities.put(limb, priority);
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
        addAngle(limb, angle, priority, PoseHandler.defaultAnimationSpeed);
    }

    // Adds angle to specified limb with the specified priority level and speed
    public void addAngle(String limb, Vector3d angle, int priority, double aSpeed)
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

    public PlayerPose clone()
    {
        PlayerPose copyPose = new PlayerPose();

        for (String limb : angles.keySet())
            copyPose.setAngles(limb, (ArrayList<Vector3d>)angles.get(limb).clone(), (ArrayList<Double>)speed.get(limb).clone(), priorities.get(limb));

        return copyPose;
    }
}
