package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.Physics;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.mlmanimator.Client.Poses.GenericPoses;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

public class RollTechnique extends Technique
{
    protected PlayerPose moveDefaults = new PlayerPose();
    protected PlayerPose move = new PlayerPose();

    public RollTechnique()
    {
        super();

        type = useType.Toggle;
        multiple = false;

        langLocation = "cultivationcraft.technique.roll";
        elementID = Elements.noElementID;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/roll.png");


        moveDefaults.addAngle(GenericLimbNames.leftLeg, new Vec3(Math.toRadians(-145), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        moveDefaults.addAngle(GenericLimbNames.lowerLeftLeg, new Vec3(Math.toRadians(75), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        moveDefaults.addAngle(GenericLimbNames.rightLeg, new Vec3(Math.toRadians(-145), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        moveDefaults.addAngle(GenericLimbNames.lowerRightLeg, new Vec3(Math.toRadians(75), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);

        moveDefaults.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vec3(Math.toRadians(-125), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        moveDefaults.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vec3(Math.toRadians(-60), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        moveDefaults.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vec3(Math.toRadians(-125), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        moveDefaults.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vec3(Math.toRadians(-60), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);

        moveDefaults.addAngle(GenericLimbNames.leftArm, new Vec3(0, Math.toRadians(0), Math.toRadians(0)), GenericPoses.walkArmPriority+2);
        moveDefaults.addAngle(GenericLimbNames.rightArm, new Vec3(0, Math.toRadians(0), Math.toRadians(0)), GenericPoses.walkArmPriority+2);

        moveDefaults.disableHeadLook(true, 5);
    }

    @Override
    public boolean isValid(Player player)
    {
        // TODO: Add
        // Technique is valid if the player is a body cultivator
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR)
            return true;

        return false;
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        // TODO: Slowly increase movement speed to a max speed that increases based on weight

        super.tickClient(event);

        Vec3 motion = Physics.getDelta(event.player);
        motion = motion.multiply(1, 0, 1);

        double speed = motion.length() * 0.75;

        if (speed < 0)
            speed = 0;

        // Determine if going in reverse or not
        if (speed > 0)
        {
            Vec3 direction = new Vec3(event.player.getLookAngle().x, 0, event.player.getLookAngle().z).normalize();
            Vec3 movementDirection = motion.normalize();

            if (direction.subtract(movementDirection).length() > 1.7)
                speed *= -1;
        }

        updateSpeed(speed);

        if (speed != 0)
        {
            PoseHandler.addPose(event.player.getUUID(), move);
        }
    }

    public void updateSpeed(double speed)
    {
        if (speed == 0)
            return;

        move = moveDefaults.clone();

        float movementSpeed = (float)(2f / speed);

        if (speed > 0)
        {
            // Start in the default standing position
            move.addAngle(GenericLimbNames.body, new Vec3(Math.toRadians(0), 0, 0), 5, movementSpeed, -1);
            // roll 180 degrees
            move.addAngle(GenericLimbNames.body, new Vec3(Math.toRadians(180), 0, 0), 5, movementSpeed, -1);
            // Instantly switch back to -180 degrees, which is the same position as 180
            move.addAngle(GenericLimbNames.body, new Vec3(Math.toRadians(-180), 0, 0), 5, 0f, -1);
        }
        else
        {
            movementSpeed = movementSpeed * -1;

            // Start in the default standing position
            move.addAngle(GenericLimbNames.body, new Vec3(Math.toRadians(0), 0, 0), 5, movementSpeed, -1);
            // roll -180 degrees
            move.addAngle(GenericLimbNames.body, new Vec3(Math.toRadians(-180), 0, 0), 5, movementSpeed, -1);
            // Instantly switch back to 180 degrees, which is the same position as 180
            move.addAngle(GenericLimbNames.body, new Vec3(Math.toRadians(180), 0, 0), 5, 0f, -1);
        }
    }
}
