package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.mlmanimator.Client.Poses.GenericPoses;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.MovementOverrideTechnique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;

public class RollTechnique extends MovementOverrideTechnique
{
    protected PlayerPose moveDefaults = new PlayerPose();

    public RollTechnique()
    {
        super();

        name = new TranslationTextComponent("cultivationcraft.technique.roll").getString();
        elementID = Elements.noElementID;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/roll.png");


        moveDefaults.addAngle(GenericLimbNames.leftLeg, new Vector3d(Math.toRadians(-145), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        moveDefaults.addAngle(GenericLimbNames.lowerLeftLeg, new Vector3d(Math.toRadians(75), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        moveDefaults.addAngle(GenericLimbNames.rightLeg, new Vector3d(Math.toRadians(-145), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        moveDefaults.addAngle(GenericLimbNames.lowerRightLeg, new Vector3d(Math.toRadians(75), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);

        moveDefaults.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vector3d(Math.toRadians(-125), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        moveDefaults.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vector3d(Math.toRadians(-60), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        moveDefaults.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vector3d(Math.toRadians(-125), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);
        moveDefaults.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vector3d(Math.toRadians(-60), Math.toRadians(0), Math.toRadians(0)), GenericPoses.jumpLegPriority+1);

        moveDefaults.addAngle(GenericLimbNames.leftArm, new Vector3d(0, Math.toRadians(0), Math.toRadians(0)), GenericPoses.walkArmPriority+2);
        moveDefaults.addAngle(GenericLimbNames.rightArm, new Vector3d(0, Math.toRadians(0), Math.toRadians(0)), GenericPoses.walkArmPriority+2);

        moveDefaults.disableHeadLook(true, 5);
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        // TODO: Add
        // Technique is valid if the player is a body cultivator
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR)
            return true;

        return false;
    }

    @Override
    public void updateSpeed(double speed)
    {
        if (speed == 0)
            return;

        move = moveDefaults.clone();

        float movementSpeed = (float)(2f / speed);

        if (speed > 0)
        {
            // Start in the default standing position
            move.addAngle(GenericLimbNames.body, new Vector3d(Math.toRadians(0), 0, 0), 5, movementSpeed, -1);
            // roll 180 degrees
            move.addAngle(GenericLimbNames.body, new Vector3d(Math.toRadians(180), 0, 0), 5, movementSpeed, -1);
            // Instantly switch back to -180 degrees, which is the same position as 180
            move.addAngle(GenericLimbNames.body, new Vector3d(Math.toRadians(-180), 0, 0), 5, 0f, -1);
        }
        else
        {
            movementSpeed = movementSpeed * -1;

            // Start in the default standing position
            move.addAngle(GenericLimbNames.body, new Vector3d(Math.toRadians(0), 0, 0), 5, movementSpeed, -1);
            // roll -180 degrees
            move.addAngle(GenericLimbNames.body, new Vector3d(Math.toRadians(-180), 0, 0), 5, movementSpeed, -1);
            // Instantly switch back to 180 degrees, which is the same position as 180
            move.addAngle(GenericLimbNames.body, new Vector3d(Math.toRadians(180), 0, 0), 5, 0f, -1);
        }
    }
}
