package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.util.math.vector.Vector3d;

public class GenericPoses
{
    public static final int walkLegPriority = 10;
    public static final int walkArmPriority = 4;

    public static PlayerPose Walking = new PlayerPose();
    public static PlayerPose Jumping = new PlayerPose();

    public static void init()
    {
        setupWalking();
        setupJumping();
    }

    // TODO: Add swimming, sleeping, sitting, etc poses

    public static void setupWalking()
    {
        Vector3d[] walkAngle = new Vector3d[5];

        walkAngle[0] = new Vector3d(Math.toRadians(-45), Math.toRadians(0), Math.toRadians(0));
        walkAngle[1] = new Vector3d(Math.toRadians(-45), Math.toRadians(0), Math.toRadians(0));
        walkAngle[2] = new Vector3d(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));
        walkAngle[3] = new Vector3d(Math.toRadians(30), Math.toRadians(0), Math.toRadians(0));
        walkAngle[4] = new Vector3d(Math.toRadians(30), Math.toRadians(0), Math.toRadians(0));

        Walking = AnimationBuilder.generateRepeatingMirroredLimbs(GenericLimbNames.leftLeg, GenericLimbNames.rightLeg, walkAngle, walkLegPriority, 3, 1);


        Vector3d[] lowerWalkAngle = new Vector3d[5];
        lowerWalkAngle[0] = new Vector3d(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[1] = new Vector3d(Math.toRadians(45), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[2] = new Vector3d(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[3] = new Vector3d(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[4] = new Vector3d(Math.toRadians(15), Math.toRadians(0), Math.toRadians(0));

        Walking = Walking.combine(AnimationBuilder.generateRepeatingMirroredLimbs(GenericLimbNames.lowerLeftLeg, GenericLimbNames.lowerRightLeg, lowerWalkAngle, walkLegPriority, 3, 1));


        Walking.addAngle(GenericLimbNames.leftArm, new Vector3d(Math.toRadians(45), Math.toRadians(0), 0), walkArmPriority);
        Walking.addAngle(GenericLimbNames.leftArm, new Vector3d(Math.toRadians(-45), Math.toRadians(0), 0), walkArmPriority);

        Walking.addAngle(GenericLimbNames.rightArm, new Vector3d(Math.toRadians(-45), Math.toRadians(0), 0), walkArmPriority);
        Walking.addAngle(GenericLimbNames.rightArm, new Vector3d(Math.toRadians(45), Math.toRadians(0), 0), walkArmPriority);
    }

    public static void setupJumping()
    {
        Jumping.addAngle(GenericLimbNames.leftArm, new Vector3d(Math.toRadians(-180), Math.toRadians(0), Math.toRadians(30)), walkArmPriority+1);
        Jumping.addAngle(GenericLimbNames.rightArm, new Vector3d(Math.toRadians(-180), Math.toRadians(0), Math.toRadians(-30)), walkArmPriority+1);

        Jumping.addAngle(GenericLimbNames.leftLeg, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), walkLegPriority+1, 1f, -1);
        Jumping.addAngle(GenericLimbNames.rightLeg, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), walkLegPriority+1, 1f, -1);
        Jumping.addAngle(GenericLimbNames.lowerLeftLeg, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), walkLegPriority+1, 1f, -1);
        Jumping.addAngle(GenericLimbNames.lowerRightLeg, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), walkLegPriority+1, 1f, -1);
    }
}
