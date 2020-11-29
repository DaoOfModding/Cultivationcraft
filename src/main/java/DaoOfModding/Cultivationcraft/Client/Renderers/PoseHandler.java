package DaoOfModding.Cultivationcraft.Client.Renderers;

import DaoOfModding.Cultivationcraft.Common.PlayerPose;
import DaoOfModding.Cultivationcraft.Common.PlayerPoseHandler;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PoseHandler
{
    private static final double animationSpeed = 0.2;

    private static List<PlayerPoseHandler> poses = new ArrayList<PlayerPoseHandler>();

    public static PlayerPoseHandler getPlayerPoseHandler(UUID playerID)
    {
        for (PlayerPoseHandler handler : poses)
            if (handler.getID().compareTo(playerID) == 0)
                return handler;

        PlayerPoseHandler newHandler = new PlayerPoseHandler(playerID);
        poses.add(newHandler);

        return newHandler;
    }

    public static void applyRotations(PlayerEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks)
    {
        Pose pose = entityLiving.getPose();
        if (pose != Pose.SLEEPING) {
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
        }

        if (entityLiving.deathTime > 0)
        {
            float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f * 90.0F));
        }
        else if (pose == Pose.SLEEPING)
        {
            Direction direction = entityLiving.getBedDirection();
            float f1 = direction != null ? getFacingAngle(direction) : rotationYaw;
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f1));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270.0F));
        }
    }

    public static void updatePoses()
    {
        for (PlayerPoseHandler handlers: poses)
            handlers.updateRenderPose();
    }

    public static void doPose(PlayerEntity entityIn, PlayerModel modelIn)
    {
        PlayerPoseHandler handler = getPlayerPoseHandler(entityIn.getUniqueID());
        handler.animateLimbs(modelIn);

        PlayerPose pose = handler.getAnimatingPose();

        if (pose.hasAngle(PlayerPose.Limb.LEFTARM))
            applyPose(modelIn.bipedLeftArm, pose.getAngle(PlayerPose.Limb.LEFTARM));

        if (pose.hasAngle(PlayerPose.Limb.RIGHTARM))
            applyPose(modelIn.bipedRightArm, pose.getAngle(PlayerPose.Limb.RIGHTARM));

        if (pose.hasAngle(PlayerPose.Limb.LEFTLEG))
            applyPose(modelIn.bipedLeftLeg, pose.getAngle(PlayerPose.Limb.LEFTLEG));

        if (pose.hasAngle(PlayerPose.Limb.RIGHTLEG))
            applyPose(modelIn.bipedRightLeg, pose.getAngle(PlayerPose.Limb.RIGHTLEG));
    }

    private static void applyPose(ModelRenderer limb, Vector3d angles)
    {
        limb.rotateAngleX = (float)angles.x;
        limb.rotateAngleY = (float)angles.y;
        limb.rotateAngleZ = (float)angles.z;
    }

    public static void addPose(UUID PlayerID, PlayerPose pose)
    {
        getPlayerPoseHandler(PlayerID).addPose(pose);
    }

    public static boolean shouldSit(PlayerEntity entityIn)
    {
        return entityIn.isPassenger() && (entityIn.getRidingEntity() != null && entityIn.getRidingEntity().shouldRiderSit());
    }

    private static float getFacingAngle(Direction facingIn)
    {
        switch(facingIn) {
            case SOUTH:
                return 90.0F;
            case WEST:
                return 0.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }
}
