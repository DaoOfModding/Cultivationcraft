package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

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
    public static final double defaultAnimationSpeed = 0.1;

    private static List<PlayerPoseHandler> poses = new ArrayList<PlayerPoseHandler>();

    public static void setupPoseHandler(UUID playerID, PlayerModel model)
    {
        // Do nothing if pose handler for this player already exists
        for (PlayerPoseHandler handler : poses)
            if (handler.getID().compareTo(playerID) == 0)
                return;

        PlayerPoseHandler newHandler = new PlayerPoseHandler(playerID, model);
        poses.add(newHandler);
    }

    public static PlayerPoseHandler getPlayerPoseHandler(UUID playerID)
    {
        for (PlayerPoseHandler handler : poses)
            if (handler.getID().compareTo(playerID) == 0)
                return handler;

        return null;
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

    public static void doPose(UUID playerID, float partialTicks)
    {
        PlayerPoseHandler handler = getPlayerPoseHandler(playerID);

        if (handler != null)
            handler.doPose(partialTicks);
    }

    public static void addPose(UUID PlayerID, PlayerPose pose)
    {
        PlayerPoseHandler handler = getPlayerPoseHandler(PlayerID);

        if (handler != null)
            handler.addPose(pose);
    }

    public static boolean shouldSit(PlayerEntity entityIn)
    {
        return entityIn.isPassenger() && (entityIn.getRidingEntity() != null && entityIn.getRidingEntity().shouldRiderSit());
    }

    public static double getHeightAdjustment(PlayerModel entityModel)
    {
        double MaxAdjustment = 0.65;

        // Get the largest angle change for both legs
        float largestLeft = Math.abs(entityModel.bipedLeftLeg.rotateAngleX);
        if (largestLeft < Math.abs(entityModel.bipedLeftLeg.rotateAngleZ))
            largestLeft = Math.abs(entityModel.bipedLeftLeg.rotateAngleZ);

        float largestRight = Math.abs(entityModel.bipedRightLeg.rotateAngleX);
        if (largestRight < Math.abs(entityModel.bipedRightLeg.rotateAngleZ))
            largestRight = Math.abs(entityModel.bipedRightLeg.rotateAngleZ);

        // Determine which leg has the smallest angle change
        float smallest = largestLeft;

        if (smallest > largestRight)
            smallest = largestRight;

        if (smallest >= Math.toRadians(90))
            return MaxAdjustment;

        return Math.pow(smallest / Math.toRadians(90), 2) * MaxAdjustment;
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
