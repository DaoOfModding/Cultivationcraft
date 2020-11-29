package DaoOfModding.Cultivationcraft.Common;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.math.vector.Vector3d;

import java.util.UUID;

public class PlayerPoseHandler
{
    UUID playerID;

    PlayerPose currentPose = new PlayerPose();
    PlayerPose renderPose = new PlayerPose();
    PlayerPose animatingPose = new PlayerPose();

    boolean locked = false;

    public PlayerPoseHandler(UUID id)
    {
        playerID = id;
    }

    public UUID getID()
    {
        return playerID;
    }

    public void addPose(PlayerPose pose)
    {
        for (PlayerPose.Limb limb : PlayerPose.Limb.values())
            if (pose.hasAngle(limb))
                // If the current pose has no angle set for this limb, or the new pose has a higher priority for this limb
                if (!currentPose.hasAngle(limb) || currentPose.getPriority(limb) < pose.getPriority(limb))
                    currentPose.setAngle(limb, pose.getAngle(limb), pose.getPriority(limb));
    }

    public void updateRenderPose()
    {
        lock();

        renderPose = currentPose;
        currentPose = new PlayerPose();

        unlock();
    }

    // Should be called whenever the renderPose is interacted with to stop multithreading breaking everything
    public void lock()
    {
        while (locked) { }

        locked = true;
    }

    public void unlock()
    {
        locked = false;
    }

    // Move each limb towards the currentPose by a the animation speed
    public void animateLimbs(PlayerModel modelIn)
    {
        lock();

        PlayerPose newRender = new PlayerPose();

        // If renderPose has a pose for a limb, move to that position, otherwise move to the base model pose
        for (PlayerPose.Limb limb : PlayerPose.Limb.values())
            if (renderPose.hasAngle(limb))
                newRender.setAngle(limb, animateLimb(getLimbPos(limb, modelIn), renderPose.getAngle(limb)), 1);
            else
                newRender.setAngle(limb, animateLimb(getLimbPos(limb, modelIn), modelFromLimb(limb, modelIn)), 1);

        unlock();

        animatingPose = newRender;
    }

    // Get the limb angles at for the specified limb. If the animating pose does not contain any data for the limb, get it from the model
    public Vector3d getLimbPos(PlayerPose.Limb limb, PlayerModel modelIn)
    {
        if (animatingPose.hasAngle(limb))
            return animatingPose.getAngle(limb);

        return modelFromLimb(limb, modelIn);
    }

    public Vector3d modelFromLimb(PlayerPose.Limb limb, PlayerModel modelIn)
    {
        if (limb == PlayerPose.Limb.LEFTARM)
            return new Vector3d(modelIn.bipedLeftArm.rotateAngleX, modelIn.bipedLeftArm.rotateAngleY, modelIn.bipedLeftArm.rotateAngleZ);

        if (limb == PlayerPose.Limb.RIGHTARM)
            return new Vector3d(modelIn.bipedRightArm.rotateAngleX, modelIn.bipedRightArm.rotateAngleY, modelIn.bipedRightArm.rotateAngleZ);

        if (limb == PlayerPose.Limb.LEFTLEG)
            return new Vector3d(modelIn.bipedLeftLeg.rotateAngleX, modelIn.bipedLeftLeg.rotateAngleY, modelIn.bipedLeftLeg.rotateAngleZ);

        if (limb == PlayerPose.Limb.RIGHTLEG)
            return new Vector3d(modelIn.bipedRightLeg.rotateAngleX, modelIn.bipedRightLeg.rotateAngleY, modelIn.bipedRightLeg.rotateAngleZ);

        // This should never be reached
        return new Vector3d(0, 0, 0);
    }

    private Vector3d animateLimb(Vector3d current, Vector3d moveTo)
    {
        // Create a vector of the amount each limb has to move
        Vector3d toMove = new Vector3d(current.x - moveTo.x, current.y - moveTo.y, current.z - moveTo.z);
        double moveAmount = toMove.length();
        double aSpeed = getAnimationSpeed();

        // Do nothing if limbs don't need to move
        if (moveAmount == 0)
            return current;

        // If the limbs have to move more that the animation speed, reduce the amount to the animation speed
        if (moveAmount > aSpeed)
            toMove = toMove.normalize().scale(aSpeed);

        // Return a vector of the current positions moved towards moveTo by the animation speed
        return new Vector3d(current.x - toMove.x, current.y - toMove.y, current.z - toMove.z);
    }

    private double getAnimationSpeed()
    {
        return 0.1;
    }

    // Get the render pose
    public PlayerPose getAnimatingPose()
    {
        return animatingPose;
    }
}
