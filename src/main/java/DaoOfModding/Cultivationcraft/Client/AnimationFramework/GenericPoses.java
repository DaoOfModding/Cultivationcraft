package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.util.math.vector.Vector3d;

public class GenericPoses
{
    public static final int walkLegPriority = 10;
    public static final int walkArmPriority = 4;

    public static PlayerPose Walking = new PlayerPose();

    public static void init()
    {
        setupWalking();
    }

    public static void setupWalking()
    {
        Vector3d[] walkAngle = new Vector3d[7];
        walkAngle[0] = new Vector3d(Math.toRadians(-75), Math.toRadians(0), Math.toRadians(0));
        walkAngle[1] = new Vector3d(Math.toRadians(-45), Math.toRadians(0), Math.toRadians(0));
        walkAngle[2] = new Vector3d(Math.toRadians(-15), Math.toRadians(0), Math.toRadians(0));
        walkAngle[3] = new Vector3d(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));
        walkAngle[4] = new Vector3d(Math.toRadians(15), Math.toRadians(0), Math.toRadians(0));
        walkAngle[5] = new Vector3d(Math.toRadians(30), Math.toRadians(0), Math.toRadians(0));
        walkAngle[6] = new Vector3d(Math.toRadians(45), Math.toRadians(0), Math.toRadians(0));

        Walking = AnimationBuilder.generateRepeatingMirroredLimbs("LEFT_LEG", "RIGHT_LEG", walkAngle, walkLegPriority, 3, 2);


        Vector3d[] lowerWalkAngle = new Vector3d[7];
        lowerWalkAngle[0] = new Vector3d(Math.toRadians(75), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[1] = new Vector3d(Math.toRadians(45), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[2] = new Vector3d(Math.toRadians(15), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[3] = new Vector3d(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[4] = new Vector3d(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[5] = new Vector3d(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[6] = new Vector3d(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));

        Walking = Walking.combine(AnimationBuilder.generateRepeatingMirroredLimbs("LOWER_LEFT_LEG", "LOWER_RIGHT_LEG", lowerWalkAngle, walkLegPriority, 3, 2));


        Walking.addAngle("LEFT_ARM", new Vector3d(Math.toRadians(45), Math.toRadians(0), 0), walkArmPriority);
        Walking.addAngle("LEFT_ARM", new Vector3d(Math.toRadians(-45), Math.toRadians(0), 0), walkArmPriority);

        Walking.addAngle("RIGHT_ARM", new Vector3d(Math.toRadians(-45), Math.toRadians(0), 0), walkArmPriority);
        Walking.addAngle("RIGHT_ARM", new Vector3d(Math.toRadians(45), Math.toRadians(0), 0), walkArmPriority);

        //Walking.addAngle("LOWERLEFTARM", new Vector3d(0, 0, Math.toRadians(30)), 4);
        //Walking.addAngle("LOWERRIGHTARM", new Vector3d(0, 0, Math.toRadians(-30)), 4);
    }
}
