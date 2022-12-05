package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Physics;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.mlmanimator.Client.Poses.GenericPoses;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

public class BounceTechnique extends Technique
{
    protected PlayerPose fall = new PlayerPose();

    public BounceTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.bounce";
        elementID = Elements.noElementID;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/bounce.png");

        stats = new PlayerStatModifications();
        stats.setStat(StatIDs.bounceHeight, 0.3f);

        type = useType.Toggle;


        fall.addAngle(GenericLimbNames.leftLeg, new Vec3(Math.toRadians(-145), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        fall.addAngle(GenericLimbNames.lowerLeftLeg, new Vec3(Math.toRadians(75), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        fall.addAngle(GenericLimbNames.rightLeg, new Vec3(Math.toRadians(-145), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        fall.addAngle(GenericLimbNames.lowerRightLeg, new Vec3(Math.toRadians(75), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);

        fall.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vec3(Math.toRadians(-125), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        fall.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vec3(Math.toRadians(-60), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        fall.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vec3(Math.toRadians(-125), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        fall.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vec3(Math.toRadians(-60), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);

        //fall.addAngle(GenericLimbNames.leftArm, new Vec3(0, Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpArmPriority+2);
        //fall.addAngle(GenericLimbNames.rightArm, new Vec3(0, Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpArmPriority+2);

        //fall.addAngle(GenericLimbNames.body, new Vec3(Math.toRadians(-45), 0, 0), GenericPoses.jumpArmPriority+1, 1f, -1);
    }

    @Override
    public boolean isValid(Player player)
    {
        // Technique is valid if the player is a body cultivator and has any bounceHeight stat
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR &&
                BodyPartStatControl.getStats(player.getUUID()).getStat(StatIDs.bounceHeight) > 0)
            return true;

        return false;
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);

        // Do nothing if the player is on the ground or in the water
        if (event.player.isInWater())
            return;

        if (event.player.isOnGround() && Physics.getDelta(event.player).y <= 0)
            return;

        // Add the falling pose to the player if they are in the air
        // if (PoseHandler.getPlayerPoseHandler(event.player.getUUID()).getDeltaMovement().y < 0)
            PoseHandler.addPose(event.player.getUUID(), fall);
    }
}
