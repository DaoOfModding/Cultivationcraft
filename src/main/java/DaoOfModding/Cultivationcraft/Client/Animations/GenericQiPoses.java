package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.mlmanimator.Client.AnimationFramework.AnimationBuilder;
import DaoOfModding.mlmanimator.Client.AnimationFramework.AnimationSpeedCalculator;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.mlmanimator.Client.Poses.GenericPoses;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import net.minecraft.world.phys.Vec3;

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
        setupCrawling();
        setupCrouching();
        setupCrouchingWalk();

        addLegs();
    }

    protected static void addLegs()
    {
        GenericPoses.addLeg(BodyPartModelNames.largeLegLeftLowerModel);
        GenericPoses.addLeg(BodyPartModelNames.largeLegLeftModel);
        GenericPoses.addLeg(BodyPartModelNames.largeLegRightLowerModel);
        GenericPoses.addLeg(BodyPartModelNames.largeLegRightModel);

        GenericPoses.addLeg(BodyPartModelNames.footLeftModel);
        GenericPoses.addLeg(BodyPartModelNames.footRightModel);

        GenericPoses.addLeg(BodyPartModelNames.reverseJointLeftLegModel);
        GenericPoses.addLeg(BodyPartModelNames.reverseJointLeftLegLowerModel);
        GenericPoses.addLeg(BodyPartModelNames.reverseJointLeftFootModel);
        GenericPoses.addLeg(BodyPartModelNames.reverseJointRightLegModel);
        GenericPoses.addLeg(BodyPartModelNames.reverseJointRightLegLowerModel);
        GenericPoses.addLeg(BodyPartModelNames.reverseJointRightFootModel);

        GenericPoses.addLeg(BodyPartModelNames.hexaLeftLegModel);
        GenericPoses.addLeg(BodyPartModelNames.hexaLowerLeftLegModel);
        GenericPoses.addLeg(BodyPartModelNames.hexaLeftLegModelTwo);
        GenericPoses.addLeg(BodyPartModelNames.hexaLowerLeftLegModelTwo);
        GenericPoses.addLeg(BodyPartModelNames.hexaLeftLegModelThree);
        GenericPoses.addLeg(BodyPartModelNames.hexaLowerLeftLegModelThree);
        GenericPoses.addLeg(BodyPartModelNames.hexaRightLegModel);
        GenericPoses.addLeg(BodyPartModelNames.hexaLowerRightLegModel);
        GenericPoses.addLeg(BodyPartModelNames.hexaRightLegModelTwo);
        GenericPoses.addLeg(BodyPartModelNames.hexaLowerRightLegModelTwo);
        GenericPoses.addLeg(BodyPartModelNames.hexaRightLegModelThree);
        GenericPoses.addLeg(BodyPartModelNames.hexaLowerRightLegModelThree);

        GenericPoses.addLeg(BodyPartModelNames.singleLegModel);
        GenericPoses.addLeg(BodyPartModelNames.singleLegLowerModel);

        GenericPoses.addLeg(BodyPartModelNames.jetLegLeftModel);
        GenericPoses.addLeg(BodyPartModelNames.jetLegLeftLowerModel);
        GenericPoses.addLeg(BodyPartModelNames.jetLegLeftEmitter);
        GenericPoses.addLeg(BodyPartModelNames.jetLegRightModel);
        GenericPoses.addLeg(BodyPartModelNames.jetLegRightLowerModel);
        GenericPoses.addLeg(BodyPartModelNames.jetLegRightEmitter);

        GenericPoses.addLeg(BodyPartModelNames.longLegLeftLowerModel);
        GenericPoses.addLeg(BodyPartModelNames.longLegLeftModel);
        GenericPoses.addLeg(BodyPartModelNames.longLegRightLowerModel);
        GenericPoses.addLeg(BodyPartModelNames.longLegRightModel);
    }

    protected static void setupIdle()
    {
        Idle.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vec3(Math.toRadians(30), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vec3(Math.toRadians(-60), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.reverseJointLeftFootModel, new Vec3(Math.toRadians(30), 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vec3(Math.toRadians(30), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vec3(Math.toRadians(-60), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.reverseJointRightFootModel, new Vec3(Math.toRadians(30), 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vec3(Math.toRadians(-120), Math.toRadians(-160), 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLowerLeftLegModel, new Vec3(Math.toRadians(110), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vec3(Math.toRadians(-120), Math.toRadians(-90), 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLowerLeftLegModelTwo, new Vec3(Math.toRadians(110), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vec3(Math.toRadians(-120), Math.toRadians(-30), 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLowerLeftLegModelThree, new Vec3(Math.toRadians(110), 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.hexaRightLegModel, new Vec3(Math.toRadians(-120), Math.toRadians(160), 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLowerRightLegModel, new Vec3(Math.toRadians(110), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vec3(Math.toRadians(-120), Math.toRadians(90), 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLowerRightLegModelTwo, new Vec3(Math.toRadians(110), 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vec3(Math.toRadians(-120), Math.toRadians(30), 0), 0);
        Idle.addAngle(BodyPartModelNames.hexaLowerRightLegModelThree, new Vec3(Math.toRadians(110), 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.singleLegModel, new Vec3(Math.toRadians(-30), 0, 0), 0, 5f, -1);
        Idle.addAngle(BodyPartModelNames.singleLegLowerModel, new Vec3(Math.toRadians(60), 0, 0), 0, 5f, -1);

        Idle.addAngle(BodyPartModelNames.largeLegLeftModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.largeLegLeftLowerModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.largeLegRightModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.largeLegRightLowerModel, new Vec3(0, 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.jetLegLeftModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.jetLegLeftLowerModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.jetLegRightModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.jetLegRightLowerModel, new Vec3(0, 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.longLegLeftModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.longLegLeftLowerModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.longLegRightModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.longLegRightLowerModel, new Vec3(0, 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.footRightModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.footLeftModel, new Vec3(0, 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.shortArmLeftModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.shortArmRightModel, new Vec3(0, 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.flipperLeftModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.flipperRightModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.flipperLowerLeftModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.flipperLowerRightModel, new Vec3(0, 0, 0), 0);

        Idle.addAngle(BodyPartModelNames.longArmLeftModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.longArmRightModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.longArmLowerLeftModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.longArmLowerRightModel, new Vec3(0, 0, 0), 0);

        // Mouth closed
        Idle.addAngle(BodyPartModelNames.FPjawModel, new Vec3(Math.toRadians(-80), 0, 0), 1);
        Idle.addAngle(BodyPartModelNames.FPjawModelLower, new Vec3(Math.toRadians(160), 0, 0), 1);
        Idle.addAngle(BodyPartModelNames.jawModel, new Vec3(0, 0, 0), 0);
        Idle.addAngle(BodyPartModelNames.jawModelLower, new Vec3(0, 0, 0), 0);

        // Wings folded
        Idle.addAngle(BodyPartModelNames.rwingUpperArmModel, new Vec3(Math.toRadians(35), Math.toRadians(-15), Math.toRadians(-70)), 0);
        Idle.addAngle(BodyPartModelNames.lwingUpperArmModel, new Vec3(Math.toRadians(35), Math.toRadians(15), Math.toRadians(70)), 0);

        Idle.addAngle(BodyPartModelNames.rwingStrand1Model, new Vec3(0, 0, Math.toRadians(50)), 0);
        Idle.addAngle(BodyPartModelNames.rwingStrand2Model, new Vec3(0, 0, Math.toRadians(60)), 0);
        Idle.addAngle(BodyPartModelNames.rwingStrand3Model, new Vec3(0, 0, Math.toRadians(70)), 0);
        Idle.addAngle(BodyPartModelNames.rwingStrand4Model, new Vec3(0, 0, Math.toRadians(80)), 0);

        Idle.addAngle(BodyPartModelNames.lwingStrand1Model, new Vec3(0, 0, Math.toRadians(-50)), 0);
        Idle.addAngle(BodyPartModelNames.lwingStrand2Model, new Vec3(0, 0, Math.toRadians(-60)), 0);
        Idle.addAngle(BodyPartModelNames.lwingStrand3Model, new Vec3(0, 0, Math.toRadians(-70)), 0);
        Idle.addAngle(BodyPartModelNames.lwingStrand4Model, new Vec3(0, 0, Math.toRadians(-80)), 0);


        Idle.addAngle(BodyPartModelNames.linsectWing, new Vec3(0, Math.toRadians(0), Math.toRadians(-60)), 0);
        Idle.addAngle(BodyPartModelNames.rinsectWing, new Vec3(0, Math.toRadians(0), Math.toRadians(60)), 0);

        Idle.addAngle(BodyPartModelNames.linsectWingInner, new Vec3(0, Math.toRadians(15), Math.toRadians(-30)), 0);
        Idle.addAngle(BodyPartModelNames.rinsectWingInner, new Vec3(0, Math.toRadians(-15), Math.toRadians(30)), 0);

        GenericPoses.addToIdle(Idle);
    }

    protected static void setupWalking()
    {
        Vec3[] walkAngle = new Vec3[2];

        walkAngle[0] = new Vec3(Math.toRadians(30), Math.toRadians(0), Math.toRadians(0));
        walkAngle[1] = new Vec3(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));

        Walk = AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.reverseJointLeftLegModel, BodyPartModelNames.reverseJointRightLegModel, walkAngle, GenericPoses.walkLegPriority, 10, 1);

        Vec3[] lowerWalkAngle = new Vec3[2];
        lowerWalkAngle[0] = new Vec3(Math.toRadians(-60), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[1] = new Vec3(Math.toRadians(-90), Math.toRadians(0), Math.toRadians(0));

        Walk = Walk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.reverseJointLeftLegLowerModel, BodyPartModelNames.reverseJointRightLegLowerModel, lowerWalkAngle, GenericPoses.walkLegPriority, 10, 1));

        Walk.addAngle(BodyPartModelNames.footLeftModel, new Vec3(Math.toRadians(-60), 0, 0), GenericPoses.walkLegPriority, 20f, 1);
        Walk.addAngle(BodyPartModelNames.footLeftModel, new Vec3(Math.toRadians(60), 0, 0), GenericPoses.walkLegPriority, 20f, 1);

        Walk.addAngle(BodyPartModelNames.footRightModel, new Vec3(Math.toRadians(60), 0, 0), GenericPoses.walkLegPriority, 20f, 1);
        Walk.addAngle(BodyPartModelNames.footRightModel, new Vec3(Math.toRadians(-60), 0, 0), GenericPoses.walkLegPriority, 20f, 1);


        Walk.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vec3(Math.toRadians(-120), Math.toRadians(-160), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vec3(Math.toRadians(-160), Math.toRadians(-110), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vec3(Math.toRadians(-120), Math.toRadians(-110), 0), GenericPoses.walkLegPriority, 5f, 1);

        Walk.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vec3(Math.toRadians(-160), Math.toRadians(-50), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vec3(Math.toRadians(-120), Math.toRadians(-50), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vec3(Math.toRadians(-120), Math.toRadians(-90), 0), GenericPoses.walkLegPriority, 5f, 1);

        Walk.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vec3(Math.toRadians(-120), Math.toRadians(0), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vec3(Math.toRadians(-120), Math.toRadians(-30), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vec3(Math.toRadians(-160), Math.toRadians(0), 0), GenericPoses.walkLegPriority, 5f, 1);

        Walk.addAngle(BodyPartModelNames.hexaRightLegModel, new Vec3(Math.toRadians(-160), Math.toRadians(110), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaRightLegModel, new Vec3(Math.toRadians(-120), Math.toRadians(110), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaRightLegModel, new Vec3(Math.toRadians(-120), Math.toRadians(160), 0), GenericPoses.walkLegPriority, 5f, 1);

        Walk.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vec3(Math.toRadians(-120), Math.toRadians(50), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vec3(Math.toRadians(-120), Math.toRadians(90), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vec3(Math.toRadians(-160), Math.toRadians(50), 0), GenericPoses.walkLegPriority, 5f, 1);

        Walk.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vec3(Math.toRadians(-120), Math.toRadians(30), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vec3(Math.toRadians(-160), Math.toRadians(0), 0), GenericPoses.walkLegPriority, 5f, 1);
        Walk.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vec3(Math.toRadians(-120), Math.toRadians(0), 0), GenericPoses.walkLegPriority, 5f, 1);


        walkAngle = new Vec3[5];

        walkAngle[0] = new Vec3(Math.toRadians(-45), Math.toRadians(0), Math.toRadians(0));
        walkAngle[1] = new Vec3(Math.toRadians(-45), Math.toRadians(0), Math.toRadians(0));
        walkAngle[2] = new Vec3(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));
        walkAngle[3] = new Vec3(Math.toRadians(30), Math.toRadians(0), Math.toRadians(0));
        walkAngle[4] = new Vec3(Math.toRadians(30), Math.toRadians(0), Math.toRadians(0));

        Walk = Walk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.jetLegLeftModel, BodyPartModelNames.jetLegRightModel, walkAngle, GenericPoses.walkLegPriority, AnimationSpeedCalculator.defaultSpeedInTicks / 2, 1));
        Walk = Walk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.largeLegLeftModel, BodyPartModelNames.largeLegRightModel, walkAngle, GenericPoses.walkLegPriority, AnimationSpeedCalculator.defaultSpeedInTicks/2, 1));
        Walk = Walk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.longLegLeftModel, BodyPartModelNames.longLegRightModel, walkAngle, GenericPoses.walkLegPriority, AnimationSpeedCalculator.defaultSpeedInTicks/2, 1));

        lowerWalkAngle = new Vec3[5];
        lowerWalkAngle[0] = new Vec3(Math.toRadians(45), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[1] = new Vec3(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[2] = new Vec3(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[3] = new Vec3(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[4] = new Vec3(Math.toRadians(15), Math.toRadians(0), Math.toRadians(0));

        Walk = Walk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.jetLegLeftLowerModel, BodyPartModelNames.jetLegRightLowerModel, lowerWalkAngle, GenericPoses.walkLegPriority, AnimationSpeedCalculator.defaultSpeedInTicks / 2, 1));
        Walk = Walk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.largeLegLeftLowerModel, BodyPartModelNames.largeLegRightLowerModel, lowerWalkAngle, GenericPoses.walkLegPriority, AnimationSpeedCalculator.defaultSpeedInTicks/2, 1));
        Walk = Walk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.longLegLeftLowerModel, BodyPartModelNames.longLegRightLowerModel, lowerWalkAngle, GenericPoses.walkLegPriority, AnimationSpeedCalculator.defaultSpeedInTicks/2, 1));


        Walk.addAngle(BodyPartModelNames.shortArmLeftModel, new Vec3(Math.toRadians(45.0D), Math.toRadians(0.0D), 0.0D), 5);
        Walk.addAngle(BodyPartModelNames.shortArmLeftModel, new Vec3(Math.toRadians(-45.0D), Math.toRadians(0.0D), 0.0D), 5);
        Walk.addAngle(BodyPartModelNames.shortArmRightModel, new Vec3(Math.toRadians(-45.0D), Math.toRadians(0.0D), 0.0D), 5);
        Walk.addAngle(BodyPartModelNames.shortArmRightModel, new Vec3(Math.toRadians(45.0D), Math.toRadians(0.0D), 0.0D), 5);

        Walk.addAngle(BodyPartModelNames.longArmLeftModel, new Vec3(Math.toRadians(45.0D), Math.toRadians(0.0D), 0.0D), 5);
        Walk.addAngle(BodyPartModelNames.longArmLeftModel, new Vec3(Math.toRadians(-45.0D), Math.toRadians(0.0D), 0.0D), 5);
        Walk.addAngle(BodyPartModelNames.longArmRightModel, new Vec3(Math.toRadians(-45.0D), Math.toRadians(0.0D), 0.0D), 5);
        Walk.addAngle(BodyPartModelNames.longArmRightModel, new Vec3(Math.toRadians(45.0D), Math.toRadians(0.0D), 0.0D), 5);

        Walk.addAngle(BodyPartModelNames.flipperLowerLeftModel, new Vec3(Math.toRadians(0D), Math.toRadians(0.0D), 0), 5);
        Walk.addAngle(BodyPartModelNames.flipperLowerRightModel, new Vec3(Math.toRadians(0D), Math.toRadians(0.0D), 0), 5);
        Walk.addAngle(BodyPartModelNames.flipperLowerLeftModel, new Vec3(Math.toRadians(0D), Math.toRadians(0.0D), Math.toRadians(-45.0D)), 5);
        Walk.addAngle(BodyPartModelNames.flipperLowerRightModel, new Vec3(Math.toRadians(0D), Math.toRadians(0.0D), Math.toRadians(45.0D)), 5);

        // Add the new walking poses to the generic walk pose*/
        GenericPoses.addToWalking(Walk);
    }

    protected static void setupJumping()
    {
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vec3(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vec3(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vec3(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vec3(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointLeftFootModel, new Vec3(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.reverseJointRightFootModel, new Vec3(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);

        GenericPoses.Jumping.addAngle(BodyPartModelNames.singleLegModel, new Vec3(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.singleLegLowerModel, new Vec3(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);

        GenericPoses.Jumping.addAngle(BodyPartModelNames.largeLegLeftModel, new Vec3(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.largeLegRightModel, new Vec3(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority, 1f, -1);

        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vec3(Math.toRadians(-30), Math.toRadians(-160), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLowerLeftLegModel, new Vec3(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vec3(Math.toRadians(-30), Math.toRadians(-90), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLowerLeftLegModelTwo, new Vec3(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vec3(Math.toRadians(-30), Math.toRadians(-30), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLowerLeftLegModelThree, new Vec3(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);

        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaRightLegModel, new Vec3(Math.toRadians(-30), Math.toRadians(160), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLowerRightLegModel, new Vec3(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vec3(Math.toRadians(-30), Math.toRadians(90), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLowerRightLegModelTwo, new Vec3(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vec3(Math.toRadians(-30), Math.toRadians(30), 0), GenericPoses.jumpLegPriority, 1f, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.hexaLowerRightLegModelThree, new Vec3(0, 0, 0), GenericPoses.jumpLegPriority, 1f, -1);


        GenericPoses.Jumping.addAngle(BodyPartModelNames.shortArmLeftModel, new Vec3(Math.toRadians(-180.0D), Math.toRadians(0.0D), Math.toRadians(30.0D)), 10, 5.0F, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.shortArmRightModel, new Vec3(Math.toRadians(-180.0D), Math.toRadians(0.0D), Math.toRadians(-30.0D)), 10, 5.0F, -1);

        GenericPoses.Jumping.addAngle(BodyPartModelNames.longArmLeftModel, new Vec3(Math.toRadians(-180.0D), Math.toRadians(0.0D), Math.toRadians(30.0D)), 10, 5.0F, -1);
        GenericPoses.Jumping.addAngle(BodyPartModelNames.longArmRightModel, new Vec3(Math.toRadians(-180.0D), Math.toRadians(0.0D), Math.toRadians(-30.0D)), 10, 5.0F, -1);
    }

    protected static void setupSwimming()
    {
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.flipperLeftModel, new Vec3(0, Math.toRadians(90), Math.toRadians(-90)), GenericPoses.swimLegPriority, 15f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.flipperRightModel, new Vec3(0, Math.toRadians(-90), Math.toRadians(90)), GenericPoses.swimLegPriority, 15f, 1);

        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.flipperLowerLeftModel, new Vec3(Math.toRadians(0D), Math.toRadians(0.0D), Math.toRadians(45.0D)), GenericPoses.swimLegPriority);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.flipperLowerRightModel, new Vec3(Math.toRadians(0D), Math.toRadians(0.0D), Math.toRadians(-45.0D)), GenericPoses.swimLegPriority);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.flipperLowerLeftModel, new Vec3(Math.toRadians(0D), Math.toRadians(0.0D), Math.toRadians(-45.0D)), GenericPoses.swimLegPriority);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.flipperLowerRightModel, new Vec3(Math.toRadians(0D), Math.toRadians(0.0D), Math.toRadians(45.0D)), GenericPoses.swimLegPriority);

        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.jetLegLeftModel, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.jetLegRightModel, new Vec3(Math.toRadians(30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.jetLegLeftModel, new Vec3(Math.toRadians(30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.jetLegRightModel, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);

        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.longLegLeftModel, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.longLegRightModel, new Vec3(Math.toRadians(30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.longLegLeftModel, new Vec3(Math.toRadians(30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.longLegRightModel, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);

        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.largeLegLeftModel, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.largeLegRightModel, new Vec3(Math.toRadians(30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.largeLegLeftModel, new Vec3(Math.toRadians(30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.largeLegRightModel, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);

        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vec3(Math.toRadians(30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vec3(Math.toRadians(30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.swimLegPriority, 15f, 1);

        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.footLeftModel, new Vec3(Math.toRadians(-60), 0, 0), GenericPoses.walkLegPriority, 10f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.footLeftModel, new Vec3(Math.toRadians(60), 0, 0), GenericPoses.walkLegPriority, 10f, 1);

        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.footRightModel, new Vec3(Math.toRadians(60), 0, 0), GenericPoses.walkLegPriority, 10f, 1);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.footRightModel, new Vec3(Math.toRadians(-60), 0, 0), GenericPoses.walkLegPriority, 10f, 1);


        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.singleLegModel, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.swimLegPriority, 20f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.singleLegLowerModel, new Vec3(Math.toRadians(60), 0, 0), GenericPoses.swimLegPriority, 20f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.singleLegModel, new Vec3(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.singleLegLowerModel, new Vec3(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);


        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vec3(Math.toRadians(-120), Math.toRadians(-160), 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerLeftLegModel, new Vec3(Math.toRadians(110), 0, 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vec3(Math.toRadians(-120), Math.toRadians(-90), 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerLeftLegModelTwo, new Vec3(Math.toRadians(110), 0, 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vec3(Math.toRadians(-120), Math.toRadians(-30), 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerLeftLegModelThree, new Vec3(Math.toRadians(110), 0, 0), GenericPoses.swimLegPriority, 40f, 2);

        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vec3(Math.toRadians(-30), Math.toRadians(-160), 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerLeftLegModel, new Vec3(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vec3(Math.toRadians(-30), Math.toRadians(-90), 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerLeftLegModelTwo, new Vec3(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vec3(Math.toRadians(-30), Math.toRadians(-30), 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerLeftLegModelThree, new Vec3(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);


        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaRightLegModel, new Vec3(Math.toRadians(-120), Math.toRadians(160), 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerRightLegModel, new Vec3(Math.toRadians(110), 0, 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vec3(Math.toRadians(-120), Math.toRadians(90), 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerRightLegModelTwo, new Vec3(Math.toRadians(110), 0, 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vec3(Math.toRadians(-120), Math.toRadians(30), 0), GenericPoses.swimLegPriority, 40f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerRightLegModelThree, new Vec3(Math.toRadians(110), 0, 0), GenericPoses.swimLegPriority, 40f, 2);

        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaRightLegModel, new Vec3(Math.toRadians(-30), Math.toRadians(160), 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerRightLegModel, new Vec3(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vec3(Math.toRadians(-30), Math.toRadians(90), 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerRightLegModelTwo, new Vec3(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vec3(Math.toRadians(-30), Math.toRadians(30), 0), GenericPoses.swimLegPriority, 5f, 2);
        GenericPoses.SwimmingMoving.addAngle(BodyPartModelNames.hexaLowerRightLegModelThree, new Vec3(0, 0, 0), GenericPoses.swimLegPriority, 5f, 2);
    }

    protected static void setupCrawling()
    {
        GenericPoses.Crawling.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.Crawling.addAngle(BodyPartModelNames.hexaLowerLeftLegModel, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.Crawling.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.Crawling.addAngle(BodyPartModelNames.hexaLowerLeftLegModelTwo, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.Crawling.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.Crawling.addAngle(BodyPartModelNames.hexaLowerLeftLegModelThree, new Vec3(0, 0, 0), GenericPoses.crawlPriority);

        GenericPoses.Crawling.addAngle(BodyPartModelNames.hexaRightLegModel, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.Crawling.addAngle(BodyPartModelNames.hexaLowerRightLegModel, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.Crawling.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.Crawling.addAngle(BodyPartModelNames.hexaLowerRightLegModelTwo, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.Crawling.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.Crawling.addAngle(BodyPartModelNames.hexaLowerRightLegModelThree, new Vec3(0, 0, 0), GenericPoses.crawlPriority);

        GenericPoses.CrawlingWalk.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.CrawlingWalk.addAngle(BodyPartModelNames.hexaLowerLeftLegModel, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.CrawlingWalk.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.CrawlingWalk.addAngle(BodyPartModelNames.hexaLowerLeftLegModelTwo, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.CrawlingWalk.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.CrawlingWalk.addAngle(BodyPartModelNames.hexaLowerLeftLegModelThree, new Vec3(0, 0, 0), GenericPoses.crawlPriority);

        GenericPoses.CrawlingWalk.addAngle(BodyPartModelNames.hexaRightLegModel, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.CrawlingWalk.addAngle(BodyPartModelNames.hexaLowerRightLegModel, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.CrawlingWalk.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.CrawlingWalk.addAngle(BodyPartModelNames.hexaLowerRightLegModelTwo, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.CrawlingWalk.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
        GenericPoses.CrawlingWalk.addAngle(BodyPartModelNames.hexaLowerRightLegModelThree, new Vec3(0, 0, 0), GenericPoses.crawlPriority);
    }

    protected static void setupCrouching()
    {
        GenericPoses.Crouching.addAngle(BodyPartModelNames.singleLegModel, new Vec3(Math.toRadians(-70), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.singleLegLowerModel, new Vec3(Math.toRadians(70), 0, 0), GenericPoses.crouchPriority);

        GenericPoses.Crouching.addAngle(BodyPartModelNames.jetLegLeftModel, new Vec3(Math.toRadians(-70), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.jetLegLeftLowerModel, new Vec3(Math.toRadians(70), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.jetLegRightModel, new Vec3(Math.toRadians(-70), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.jetLegRightLowerModel, new Vec3(Math.toRadians(70), 0, 0), GenericPoses.crouchPriority);

        GenericPoses.Crouching.addAngle(BodyPartModelNames.longLegLeftModel, new Vec3(Math.toRadians(-70), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.longLegLeftLowerModel, new Vec3(Math.toRadians(70), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.longLegRightModel, new Vec3(Math.toRadians(-70), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.longLegRightLowerModel, new Vec3(Math.toRadians(70), 0, 0), GenericPoses.crouchPriority);

        GenericPoses.Crouching.addAngle(BodyPartModelNames.largeLegLeftModel, new Vec3(Math.toRadians(-50), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.largeLegLeftLowerModel, new Vec3(Math.toRadians(50), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.largeLegRightModel, new Vec3(Math.toRadians(-50), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.largeLegRightLowerModel, new Vec3(Math.toRadians(50), 0, 0), GenericPoses.crouchPriority);

        GenericPoses.Crouching.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vec3(Math.toRadians(60), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vec3(Math.toRadians(-90), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.reverseJointLeftFootModel, new Vec3(Math.toRadians(30), 0, 0), GenericPoses.crouchPriority);

        GenericPoses.Crouching.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vec3(Math.toRadians(60), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vec3(Math.toRadians(-90), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.reverseJointRightFootModel, new Vec3(Math.toRadians(30), 0, 0), GenericPoses.crouchPriority);


        GenericPoses.Crouching.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vec3(Math.toRadians(-150), Math.toRadians(-160), 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.hexaLowerLeftLegModel, new Vec3(Math.toRadians(140), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vec3(Math.toRadians(-150), Math.toRadians(-90), 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.hexaLowerLeftLegModelTwo, new Vec3(Math.toRadians(140), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vec3(Math.toRadians(-150), Math.toRadians(-30), 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.hexaLowerLeftLegModelThree, new Vec3(Math.toRadians(140), 0, 0), GenericPoses.crouchPriority);

        GenericPoses.Crouching.addAngle(BodyPartModelNames.hexaRightLegModel, new Vec3(Math.toRadians(-150), Math.toRadians(160), 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.hexaLowerRightLegModel, new Vec3(Math.toRadians(140), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vec3(Math.toRadians(-150), Math.toRadians(90), 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.hexaLowerRightLegModelTwo, new Vec3(Math.toRadians(140), 0, 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vec3(Math.toRadians(-150), Math.toRadians(30), 0), GenericPoses.crouchPriority);
        GenericPoses.Crouching.addAngle(BodyPartModelNames.hexaLowerRightLegModelThree, new Vec3(Math.toRadians(140), 0, 0), GenericPoses.crouchPriority);
    }

    public static void setupCrouchingWalk()
    {
        Vec3[] walkAngle = new Vec3[5];

        walkAngle[0] = new Vec3(Math.toRadians(-95), Math.toRadians(0), Math.toRadians(0));
        walkAngle[1] = new Vec3(Math.toRadians(-95), Math.toRadians(0), Math.toRadians(0));
        walkAngle[2] = new Vec3(Math.toRadians(-70), Math.toRadians(0), Math.toRadians(0));
        walkAngle[3] = new Vec3(Math.toRadians(-30), Math.toRadians(0), Math.toRadians(0));
        walkAngle[4] = new Vec3(Math.toRadians(-30), Math.toRadians(0), Math.toRadians(0));

        PlayerPose CrouchingWalk = AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.jetLegLeftModel, BodyPartModelNames.jetLegRightModel, walkAngle, GenericPoses.crouchPriority, AnimationSpeedCalculator.defaultSpeedInTicks / 2, 1);
        CrouchingWalk = CrouchingWalk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.longLegLeftModel, BodyPartModelNames.longLegRightModel, walkAngle, GenericPoses.crouchPriority, AnimationSpeedCalculator.defaultSpeedInTicks / 2, 1));

        Vec3[] lowerWalkAngle = new Vec3[5];
        lowerWalkAngle[0] = new Vec3(Math.toRadians(95), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[1] = new Vec3(Math.toRadians(70), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[2] = new Vec3(Math.toRadians(70), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[3] = new Vec3(Math.toRadians(70), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[4] = new Vec3(Math.toRadians(85), Math.toRadians(0), Math.toRadians(0));

        CrouchingWalk = CrouchingWalk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.jetLegLeftLowerModel, BodyPartModelNames.jetLegRightLowerModel, lowerWalkAngle, GenericPoses.crouchPriority, AnimationSpeedCalculator.defaultSpeedInTicks / 2, 1));
        CrouchingWalk = CrouchingWalk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.longLegLeftLowerModel, BodyPartModelNames.longLegRightLowerModel, lowerWalkAngle, GenericPoses.crouchPriority, AnimationSpeedCalculator.defaultSpeedInTicks / 2, 1));


        walkAngle[0] = new Vec3(Math.toRadians(-70), Math.toRadians(0), Math.toRadians(0));
        walkAngle[1] = new Vec3(Math.toRadians(-70), Math.toRadians(0), Math.toRadians(0));
        walkAngle[2] = new Vec3(Math.toRadians(-50), Math.toRadians(0), Math.toRadians(0));
        walkAngle[3] = new Vec3(Math.toRadians(-30), Math.toRadians(0), Math.toRadians(0));
        walkAngle[4] = new Vec3(Math.toRadians(-30), Math.toRadians(0), Math.toRadians(0));

        CrouchingWalk = CrouchingWalk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.largeLegLeftModel, BodyPartModelNames.largeLegRightModel, walkAngle, GenericPoses.crouchPriority, AnimationSpeedCalculator.defaultSpeedInTicks, 1));

        lowerWalkAngle[0] = new Vec3(Math.toRadians(70), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[1] = new Vec3(Math.toRadians(50), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[2] = new Vec3(Math.toRadians(50), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[3] = new Vec3(Math.toRadians(50), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[4] = new Vec3(Math.toRadians(65), Math.toRadians(0), Math.toRadians(0));

        CrouchingWalk = CrouchingWalk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.largeLegLeftLowerModel, BodyPartModelNames.largeLegRightLowerModel, lowerWalkAngle, GenericPoses.crouchPriority, AnimationSpeedCalculator.defaultSpeedInTicks, 1));


        walkAngle = new Vec3[2];

        walkAngle[0] = new Vec3(Math.toRadians(60), Math.toRadians(0), Math.toRadians(0));
        walkAngle[1] = new Vec3(Math.toRadians(30), Math.toRadians(0), Math.toRadians(0));

        CrouchingWalk = CrouchingWalk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.reverseJointLeftLegModel, BodyPartModelNames.reverseJointRightLegModel, walkAngle, GenericPoses.crouchPriority, AnimationSpeedCalculator.defaultSpeedInTicks * 2, 1));

        lowerWalkAngle = new Vec3[2];
        lowerWalkAngle[0] = new Vec3(Math.toRadians(-90), Math.toRadians(0), Math.toRadians(0));
        lowerWalkAngle[1] = new Vec3(Math.toRadians(-120), Math.toRadians(0), Math.toRadians(0));

        CrouchingWalk = CrouchingWalk.combine(AnimationBuilder.generateRepeatingMirroredLimbs(BodyPartModelNames.reverseJointLeftLegLowerModel, BodyPartModelNames.reverseJointRightLegLowerModel, lowerWalkAngle, GenericPoses.crouchPriority, AnimationSpeedCalculator.defaultSpeedInTicks * 2, 1));


        CrouchingWalk.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vec3(Math.toRadians(-150), Math.toRadians(-160), 0), GenericPoses.crouchPriority, 5f, 1);
        CrouchingWalk.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vec3(Math.toRadians(-200), Math.toRadians(-110), 0), GenericPoses.crouchPriority, 5f, 1);
        CrouchingWalk.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vec3(Math.toRadians(-150), Math.toRadians(-110), 0), GenericPoses.crouchPriority, 5f, 1);

        CrouchingWalk.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vec3(Math.toRadians(-200), Math.toRadians(-50), 0), GenericPoses.crouchPriority, 5f, 1);
        CrouchingWalk.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vec3(Math.toRadians(-150), Math.toRadians(-50), 0), GenericPoses.crouchPriority, 5f, 1);
        CrouchingWalk.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vec3(Math.toRadians(-150), Math.toRadians(-90), 0), GenericPoses.crouchPriority, 5f, 1);

        CrouchingWalk.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vec3(Math.toRadians(-150), Math.toRadians(0), 0), GenericPoses.crouchPriority, 5f, 1);
        CrouchingWalk.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vec3(Math.toRadians(-150), Math.toRadians(-30), 0), GenericPoses.crouchPriority, 5f, 1);
        CrouchingWalk.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vec3(Math.toRadians(-200), Math.toRadians(0), 0), GenericPoses.crouchPriority, 5f, 1);

        CrouchingWalk.addAngle(BodyPartModelNames.hexaRightLegModel, new Vec3(Math.toRadians(-200), Math.toRadians(110), 0), GenericPoses.crouchPriority, 5f, 1);
        CrouchingWalk.addAngle(BodyPartModelNames.hexaRightLegModel, new Vec3(Math.toRadians(-150), Math.toRadians(110), 0), GenericPoses.crouchPriority, 5f, 1);
        CrouchingWalk.addAngle(BodyPartModelNames.hexaRightLegModel, new Vec3(Math.toRadians(-150), Math.toRadians(160), 0), GenericPoses.crouchPriority, 5f, 1);

        CrouchingWalk.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vec3(Math.toRadians(-150), Math.toRadians(50), 0), GenericPoses.crouchPriority, 5f, 1);
        CrouchingWalk.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vec3(Math.toRadians(-150), Math.toRadians(90), 0), GenericPoses.crouchPriority, 5f, 1);
        CrouchingWalk.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vec3(Math.toRadians(-200), Math.toRadians(50), 0), GenericPoses.crouchPriority, 5f, 1);

        CrouchingWalk.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vec3(Math.toRadians(-150), Math.toRadians(30), 0), GenericPoses.crouchPriority, 5f, 1);
        CrouchingWalk.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vec3(Math.toRadians(-200), Math.toRadians(0), 0), GenericPoses.crouchPriority, 5f, 1);
        CrouchingWalk.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vec3(Math.toRadians(-150), Math.toRadians(0), 0), GenericPoses.crouchPriority, 5f, 1);


        GenericPoses.CrouchingWalk = GenericPoses.CrouchingWalk.combine(CrouchingWalk);
    }

    protected static void setupHandsBehind()
    {
        HandsBehind.addAngle(GenericLimbNames.leftArm, new Vec3(Math.toRadians(30), 0, Math.toRadians(15)), GenericPoses.walkArmPriority + 2);
        HandsBehind.addAngle(GenericLimbNames.rightArm, new Vec3(Math.toRadians(30), 0, Math.toRadians(-15)), GenericPoses.walkArmPriority + 2);

        HandsBehind.addAngle(GenericLimbNames.lowerLeftArm, new Vec3(0, 0, Math.toRadians(30)), GenericPoses.walkArmPriority + 2);
        HandsBehind.addAngle(GenericLimbNames.lowerRightArm, new Vec3(0, 0, Math.toRadians(-30)), GenericPoses.walkArmPriority + 2);
    }

    protected static void setupCrossLegs()
    {
        CrossLegs.addAngle(GenericLimbNames.leftArm, new Vec3(Math.toRadians(0), Math.toRadians(-30), 0), GenericPoses.walkArmPriority + 4);
        CrossLegs.addAngle(GenericLimbNames.rightArm, new Vec3(Math.toRadians(0), Math.toRadians(30), 0), GenericPoses.walkArmPriority + 4);

        CrossLegs.addAngle(GenericLimbNames.lowerLeftArm, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.walkArmPriority + 4);
        CrossLegs.addAngle(GenericLimbNames.lowerRightArm, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.walkArmPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.longArmLeftModel, new Vec3(Math.toRadians(0), Math.toRadians(-30), 0), GenericPoses.walkArmPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.longArmRightModel, new Vec3(Math.toRadians(0), Math.toRadians(30), 0), GenericPoses.walkArmPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.longArmLowerLeftModel, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.walkArmPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.longArmLowerRightModel, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.walkArmPriority + 4);

        CrossLegs.addAngle(GenericLimbNames.leftLeg, new Vec3(Math.toRadians(45), Math.toRadians(-180), Math.toRadians(-90)), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(GenericLimbNames.rightLeg, new Vec3(Math.toRadians(45), Math.toRadians(180), Math.toRadians(90)), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(GenericLimbNames.lowerLeftLeg, new Vec3(Math.toRadians(90), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(GenericLimbNames.lowerRightLeg, new Vec3(Math.toRadians(90), 0, 0), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.jetLegLeftModel, new Vec3(Math.toRadians(45), Math.toRadians(-180), Math.toRadians(-90)), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.jetLegRightModel, new Vec3(Math.toRadians(45), Math.toRadians(180), Math.toRadians(90)), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.longLegLeftModel, new Vec3(Math.toRadians(45), Math.toRadians(-180), Math.toRadians(-90)), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.longLegRightModel, new Vec3(Math.toRadians(45), Math.toRadians(180), Math.toRadians(90)), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.jetLegLeftLowerModel, new Vec3(Math.toRadians(90), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.jetLegRightLowerModel, new Vec3(Math.toRadians(90), 0, 0), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.largeLegLeftLowerModel, new Vec3(Math.toRadians(90), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.largeLegRightLowerModel, new Vec3(Math.toRadians(90), 0, 0), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.footLeftModel, new Vec3(Math.toRadians(-90), 0, 0), GenericPoses.walkLegPriority + 4, 20f, -1);
        CrossLegs.addAngle(BodyPartModelNames.footRightModel, new Vec3(Math.toRadians(-90), 0, 0), GenericPoses.walkLegPriority + 4, 20f, -1);

        CrossLegs.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vec3(Math.toRadians(45), Math.toRadians(-180), Math.toRadians(-90)), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vec3(Math.toRadians(45), Math.toRadians(180), Math.toRadians(90)), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vec3(Math.toRadians(-60), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vec3(Math.toRadians(-60), 0, 0), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.singleLegModel, new Vec3(Math.toRadians(-90), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.singleLegLowerModel, new Vec3(Math.toRadians(0), 0, 0), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.hexaLeftLegModel, new Vec3(Math.toRadians(-150), Math.toRadians(-160), 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLowerLeftLegModel, new Vec3(Math.toRadians(150), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLeftLegModelTwo, new Vec3(Math.toRadians(-150), Math.toRadians(-90), 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLowerLeftLegModelTwo, new Vec3(Math.toRadians(150), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLeftLegModelThree, new Vec3(Math.toRadians(-150), Math.toRadians(-30), 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLowerLeftLegModelThree, new Vec3(Math.toRadians(150), 0, 0), GenericPoses.walkLegPriority + 4);

        CrossLegs.addAngle(BodyPartModelNames.hexaRightLegModel, new Vec3(Math.toRadians(-150), Math.toRadians(160), 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLowerRightLegModel, new Vec3(Math.toRadians(150), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaRightLegModelTwo, new Vec3(Math.toRadians(-150), Math.toRadians(90), 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLowerRightLegModelTwo, new Vec3(Math.toRadians(150), 0, 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaRightLegModelThree, new Vec3(Math.toRadians(-150), Math.toRadians(30), 0), GenericPoses.walkLegPriority + 4);
        CrossLegs.addAngle(BodyPartModelNames.hexaLowerRightLegModelThree, new Vec3(Math.toRadians(150), 0, 0), GenericPoses.walkLegPriority + 4);
    }
}
