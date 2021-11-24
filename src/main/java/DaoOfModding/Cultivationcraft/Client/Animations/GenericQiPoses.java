package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.mlmanimator.Client.AnimationFramework.AnimationBuilder;
import DaoOfModding.mlmanimator.Client.AnimationFramework.AnimationSpeedCalculator;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.mlmanimator.Client.Poses.GenericPoses;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import net.minecraft.util.math.vector.Vector3d;

public class GenericQiPoses
{
    public static final int attackPriority = 10;

    public static PlayerPose Idle = new PlayerPose();
    public static PlayerPose Walk = new PlayerPose();

    public static PlayerPose HandsBehind = new PlayerPose();
    public static PlayerPose CrossLegs = new PlayerPose();

    public static void init()
    {
        setupHandsBehind();
        setupCrossLegs();

        setupIdle();
        setupWalking();
        setupJumping();
        setupSwimming();
    }

    private static void setupIdle()
    {
        Idle.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vector3d(Math.toRadians(30), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vector3d(Math.toRadians(-60), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.reverseJointLeftFootModel, new Vector3d(Math.toRadians(30), 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vector3d(Math.toRadians(30), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vector3d(Math.toRadians(-60), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.reverseJointRightFootModel, new Vector3d(Math.toRadians(30), 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vector3d(Math.toRadians(-120), Math.toRadians(-160), 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLowerLeftLegModel, new Vector3d(Math.toRadians(110), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vector3d(Math.toRadians(-120), Math.toRadians(-90), 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLowerLeftLegModelTwo, new Vector3d(Math.toRadians(110), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vector3d(Math.toRadians(-120), Math.toRadians(-30), 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLowerLeftLegModelThree, new Vector3d(Math.toRadians(110), 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.hexaRightLegModel, new Vector3d(Math.toRadians(-120), Math.toRadians(160), 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLowerRightLegModel, new Vector3d(Math.toRadians(110), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vector3d(Math.toRadians(-120), Math.toRadians(90), 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLowerRightLegModelTwo, new Vector3d(Math.toRadians(110), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vector3d(Math.toRadians(-120), Math.toRadians(30), 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLowerRightLegModelThree, new Vector3d(Math.toRadians(110), 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.singleLegModel, new Vector3d(Math.toRadians(-30), 0, 0), 0, 5f, -1);
        Idle.addAngle(BodyPartModelNames.singleLegLowerModel, new Vector3d(Math.toRadians(60), 0, 0), 0, 5f, -1);

        // Mouth closed
        Idle.addAngle(BodyPartModelNames.FPjawModel, new Vector3d(Math.toRadians(-80), 0, 0), 1);
        Idle.addAngle(BodyPartModelNames.FPjawModelLower, new Vector3d(Math.toRadians(160), 0, 0), 1);

        // Wings folded
        Idle.addAngle(BodyPartModelNames.rwingUpperArmModel, new Vector3d(Math.toRadians(35), Math.toRadians(-15), Math.toRadians(-70)), 0);
        Idle.addAngle(BodyPartModelNames.lwingUpperArmModel, new Vector3d(Math.toRadians(35), Math.toRadians(15), Math.toRadians(70)), 0);

        Idle.addAngle(BodyPartModelNames.rwingStrand1Model, new Vector3d(0, 0, Math.toRadians(50)), 0);
        Idle.addAngle(BodyPartModelNames.rwingStrand2Model, new Vector3d(0, 0, Math.toRadians(60)), 0);
        Idle.addAngle(BodyPartModelNames.rwingStrand3Model, new Vector3d(0, 0, Math.toRadians(70)), 0);
        Idle.addAngle(BodyPartModelNames.rwingStrand4Model, new Vector3d(0, 0, Math.toRadians(80)), 0);

        Idle.addAngle(BodyPartModelNames.lwingStrand1Model, new Vector3d(0, 0, Math.toRadians(-50)), 0);
        Idle.addAngle(BodyPartModelNames.lwingStrand2Model, new Vector3d(0, 0, Math.toRadians(-60)), 0);
        Idle.addAngle(BodyPartModelNames.lwingStrand3Model, new Vector3d(0, 0, Math.toRadians(-70)), 0);
        Idle.addAngle(BodyPartModelNames.lwingStrand4Model, new Vector3d(0, 0, Math.toRadians(-80)), 0);


        Idle.addAngle(BodyPartModelNames.linsectWing, new Vector3d(0, Math.toRadians(0), Math.toRadians(-60)), 0);
        Idle.addAngle(BodyPartModelNames.rinsectWing, new Vector3d(0, Math.toRadians(0), Math.toRadians(60)), 0);

        Idle.addAngle(BodyPartModelNames.linsectWingInner, new Vector3d(0, Math.toRadians(15), Math.toRadians(-30)), 0);
        Idle.addAngle(BodyPartModelNames.rinsectWingInner, new Vector3d(0, Math.toRadians(-15), Math.toRadians(30)), 0);

        GenericPoses.addToIdle(Idle);
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


        Walk.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vector3d(Math.toRadians(-120), Math.toRadians(-160), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vector3d(Math.toRadians(-160), Math.toRadians(-110), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vector3d(Math.toRadians(-120), Math.toRadians(-110), 0), GenericPoses.walkLegPriority, 5f, 1);

        Walk.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vector3d(Math.toRadians(-160), Math.toRadians(-50), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vector3d(Math.toRadians(-120), Math.toRadians(-50), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vector3d(Math.toRadians(-120), Math.toRadians(-90), 0), GenericPoses.walkLegPriority, 5f, 1);

        Walk.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vector3d(Math.toRadians(-120), Math.toRadians(0), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vector3d(Math.toRadians(-120), Math.toRadians(-30), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vector3d(Math.toRadians(-160), Math.toRadians(0), 0), GenericPoses.walkLegPriority, 5f, 1);

        Walk.addAngle(BodyPartModelNames.hexaRightLegModel, new Vector3d(Math.toRadians(-160), Math.toRadians(110), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaRightLegModel, new Vector3d(Math.toRadians(-120), Math.toRadians(110), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaRightLegModel, new Vector3d(Math.toRadians(-120), Math.toRadians(160), 0), GenericPoses.walkLegPriority, 5f, 1);

        Walk.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vector3d(Math.toRadians(-120), Math.toRadians(50), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vector3d(Math.toRadians(-120), Math.toRadians(90), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vector3d(Math.toRadians(-160), Math.toRadians(50), 0), GenericPoses.walkLegPriority, 5f, 1);

        Walk.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vector3d(Math.toRadians(-120), Math.toRadians(30), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vector3d(Math.toRadians(-160), Math.toRadians(0), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vector3d(Math.toRadians(-120), Math.toRadians(0), 0), GenericPoses.walkLegPriority, 5f, 1);

        // Add the new walking poses to the generic walk pose*/
        GenericPoses.addToWalking(Walk);
    }

    private static void setupJumping()
    {
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointLeftFootModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointRightFootModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);

        GenericPoses.Jumping.addAngle(BodyPartModelNames.singleLegModel, new Vector3d(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.singleLegLowerModel, new Vector3d(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);


        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vector3d(Math.toRadians(-30), Math.toRadians(-160), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLowerLeftLegModel, new Vector3d(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vector3d(Math.toRadians(-30), Math.toRadians(-90), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLowerLeftLegModelTwo, new Vector3d(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vector3d(Math.toRadians(-30), Math.toRadians(-30), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLowerLeftLegModelThree, new Vector3d(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);

        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaRightLegModel, new Vector3d(Math.toRadians(-30), Math.toRadians(160), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLowerRightLegModel, new Vector3d(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vector3d(Math.toRadians(-30), Math.toRadians(90), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLowerRightLegModelTwo, new Vector3d(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vector3d(Math.toRadians(-30), Math.toRadians(30), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLowerRightLegModelThree, new Vector3d(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);
    }

    private static void setupSwimming()
    {
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.singleLegModel, new Vector3d(Math.toRadians(-30), 0, 0), GenericPoses.swimLegPriority, 20f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.singleLegLowerModel, new Vector3d(Math.toRadians(60), 0, 0), GenericPoses.swimLegPriority, 20f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.singleLegModel, new Vector3d(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.singleLegLowerModel, new Vector3d(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);


        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vector3d(Math.toRadians(-120), Math.toRadians(-160), 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerLeftLegModel, new Vector3d(Math.toRadians(110), 0, 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vector3d(Math.toRadians(-120), Math.toRadians(-90), 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerLeftLegModelTwo, new Vector3d(Math.toRadians(110), 0, 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vector3d(Math.toRadians(-120), Math.toRadians(-30), 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerLeftLegModelThree, new Vector3d(Math.toRadians(110), 0, 0), GenericPoses.swimLegPriority, 40f, 2);

        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vector3d(Math.toRadians(-30), Math.toRadians(-160), 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerLeftLegModel, new Vector3d(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vector3d(Math.toRadians(-30), Math.toRadians(-90), 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerLeftLegModelTwo, new Vector3d(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vector3d(Math.toRadians(-30), Math.toRadians(-30), 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerLeftLegModelThree, new Vector3d(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);


        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaRightLegModel, new Vector3d(Math.toRadians(-120), Math.toRadians(160), 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerRightLegModel, new Vector3d(Math.toRadians(110), 0, 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vector3d(Math.toRadians(-120), Math.toRadians(90), 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerRightLegModelTwo, new Vector3d(Math.toRadians(110), 0, 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vector3d(Math.toRadians(-120), Math.toRadians(30), 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerRightLegModelThree, new Vector3d(Math.toRadians(110), 0, 0), GenericPoses.swimLegPriority, 40f, 2);

        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaRightLegModel, new Vector3d(Math.toRadians(-30), Math.toRadians(160), 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerRightLegModel, new Vector3d(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vector3d(Math.toRadians(-30), Math.toRadians(90), 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerRightLegModelTwo, new Vector3d(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vector3d(Math.toRadians(-30), Math.toRadians(30), 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerRightLegModelThree, new Vector3d(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);
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
        CrossLegs.addAngle(GenericLimbNames.leftArm, new Vector3d(Math.toRadians(0), Math.toRadians(-30), 0), GenericPoses.walkArmPriority + 4);
        CrossLegs.addAngle(GenericLimbNames.rightArm, new Vector3d(Math.toRadians(0), Math.toRadians(30), 0), GenericPoses.walkArmPriority + 4);

        CrossLegs.addAngle(GenericLimbNames.lowerLeftArm, new Vector3d(Math.toRadians(-30), 0, 0), GenericPoses.walkArmPriority + 4);
        CrossLegs.addAngle(GenericLimbNames.lowerRightArm, new Vector3d(Math.toRadians(-30), 0, 0), GenericPoses.walkArmPriority + 4);

        CrossLegs.addAngle(GenericLimbNames.leftLeg, new Vector3d(Math.toRadians(45), Math.toRadians(-180), Math.toRadians(-90)), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(GenericLimbNames.rightLeg, new Vector3d(Math.toRadians(45), Math.toRadians(180), Math.toRadians(90)), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(GenericLimbNames.lowerLeftLeg, new Vector3d(Math.toRadians(90), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(GenericLimbNames.lowerRightLeg, new Vector3d(Math.toRadians(90), 0, 0), GenericPoses.walkLegPriority + 4);


        CrossLegs.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vector3d(Math.toRadians(45), Math.toRadians(-180), Math.toRadians(-90)), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vector3d(Math.toRadians(45), Math.toRadians(180), Math.toRadians(90)), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vector3d(Math.toRadians(-60), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vector3d(Math.toRadians(-60), 0, 0), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.singleLegModel, new Vector3d(Math.toRadians(-90), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.singleLegLowerModel, new Vector3d(Math.toRadians(0), 0, 0), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vector3d(Math.toRadians(-150), Math.toRadians(-160), 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLowerLeftLegModel, new Vector3d(Math.toRadians(150), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vector3d(Math.toRadians(-150), Math.toRadians(-90), 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLowerLeftLegModelTwo, new Vector3d(Math.toRadians(150), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vector3d(Math.toRadians(-150), Math.toRadians(-30), 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLowerLeftLegModelThree, new Vector3d(Math.toRadians(150), 0, 0), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.hexaRightLegModel, new Vector3d(Math.toRadians(-150), Math.toRadians(160), 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLowerRightLegModel, new Vector3d(Math.toRadians(150), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vector3d(Math.toRadians(-150), Math.toRadians(90), 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLowerRightLegModelTwo, new Vector3d(Math.toRadians(150), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vector3d(Math.toRadians(-150), Math.toRadians(30), 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLowerRightLegModelThree, new Vector3d(Math.toRadians(150), 0, 0), GenericPoses.walkLegPriority + 4);
    }
}
