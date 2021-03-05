package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.AnimationBuilder;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.GenericLimbNames;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.GenericPoses;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.PlayerPose;
import net.minecraft.util.math.vector.Vector3d;

public class GenericQiPoses
{
    public static final int attackPriority = 10;

    public static PlayerPose Idle = new PlayerPose();
    public static PlayerPose Walk = new PlayerPose();

    public static PlayerPose HandsBehind = new PlayerPose();
    public static PlayerPose CrossLegs = new PlayerPose();

    public static PlayerPose extendedLegs = new PlayerPose();

    public static void init()
    {
        setupHandsBehind();
        setupCrossLegs();
        setupExtendedLegs();

        setupIdle();
        setupWalking();
        setupJumping();
    }

    private static void setupIdle()
    {
        Idle.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vector3d(Math.toRadians(30), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vector3d(Math.toRadians(-60), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.reverseJointLeftFootModel, new Vector3d(Math.toRadians(30), 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vector3d(Math.toRadians(30), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vector3d(Math.toRadians(-60), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.reverseJointRightFootModel, new Vector3d(Math.toRadians(30), 0, 0), 0);


        Idle.addAngle(BodyPartModelNames.FPjawModel, new Vector3d(Math.toRadians(-80), 0, 0), 1);
        Idle.addAngle(BodyPartModelNames.FPjawModelLower, new Vector3d(Math.toRadians(160), 0, 0), 1);
    }

    private static void setupWalking()
    {
        Vector3d[] walkAngle = new Vector3d[2];

        walkAngle[0] = new Vector3d(Math.toRadians(30), Math.toRadians(0), Math.toRadians(0));
        walkAngle[1] = new Vector3d(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));

        Walk = AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.reverseJointLeftLegModel, BodyPartModelNames.reverseJointRightLegModel, walkAngle, GenericPoses.walkLegPriority, 10, 1);

        Vector3d[] lowerWalkAngle = new Vector3d[2];
        lowerWalkAngle[0] = new Vector3d(Math.toRadians(-60), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[1] = new Vector3d(Math.toRadians(-90), Math.toRadians(0), Math.toRadians(0));

        Walk = Walk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.reverseJointLeftLegLowerModel, BodyPartModelNames.reverseJointRightLegLowerModel, lowerWalkAngle, GenericPoses.walkLegPriority, 10, 1));
    }

    private static void setupExtendedLegs()
    {
        extendedLegs.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.walkLegPriority, 1f, -1);
        extendedLegs.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.walkLegPriority, 1f, -1);
        extendedLegs.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.walkLegPriority, 1f, -1);
        extendedLegs.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.walkLegPriority, 1f, -1);
        extendedLegs.addAngle(BodyPartModelNames.reverseJointLeftFootModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.walkLegPriority, 1f, -1);
        extendedLegs.addAngle(BodyPartModelNames.reverseJointRightFootModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.walkLegPriority, 1f, -1);
    }

    private static void setupJumping()
    {
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointLeftFootModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointRightFootModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
    }

    private static void setupHandsBehind()
    {
        HandsBehind.addAngle(GenericLimbNames.leftArm, new Vector3d(Math.toRadians(30), 0, Math.toRadians(15)), GenericPoses.walkArmPriority + 2);
        HandsBehind.addAngle(GenericLimbNames.rightArm, new Vector3d(Math.toRadians(30), 0, Math.toRadians(-15)), GenericPoses.walkArmPriority + 2);

        HandsBehind.addAngle(GenericLimbNames.lowerLeftArm, new Vector3d(0, 0, Math.toRadians(30)), GenericPoses.walkArmPriority + 2);
        HandsBehind.addAngle(GenericLimbNames.lowerRightArm, new Vector3d(0, 0, Math.toRadians(-30)), GenericPoses.walkArmPriority + 2);
    }

    private static void setupCrossLegs()
    {
        CrossLegs.addAngle(GenericLimbNames.leftArm, new Vector3d(Math.toRadians(30), Math.toRadians(180), 0), GenericPoses.walkArmPriority + 4);
        CrossLegs.addAngle(GenericLimbNames.rightArm, new Vector3d(Math.toRadians(30), Math.toRadians(-180), 0), GenericPoses.walkArmPriority + 4);

        CrossLegs.addAngle(GenericLimbNames.lowerLeftArm, new Vector3d(0, 0, Math.toRadians(30)), GenericPoses.walkArmPriority + 4);
        CrossLegs.addAngle(GenericLimbNames.lowerRightArm, new Vector3d(0, 0, Math.toRadians(-30)), GenericPoses.walkArmPriority + 4);

        CrossLegs.addAngle(GenericLimbNames.leftLeg, new Vector3d(Math.toRadians(45), Math.toRadians(-180), Math.toRadians(-90)), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(GenericLimbNames.rightLeg, new Vector3d(Math.toRadians(45), Math.toRadians(180), Math.toRadians(90)), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(GenericLimbNames.lowerLeftLeg, new Vector3d(Math.toRadians(90), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(GenericLimbNames.lowerRightLeg, new Vector3d(Math.toRadians(90), 0, 0), GenericPoses.walkLegPriority + 4);


        CrossLegs.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vector3d(Math.toRadians(45), Math.toRadians(-180), Math.toRadians(-90)), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vector3d(Math.toRadians(45), Math.toRadians(180), Math.toRadians(90)), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vector3d(Math.toRadians(-60), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vector3d(Math.toRadians(-60), 0, 0), GenericPoses.walkLegPriority + 4);
    }
}
