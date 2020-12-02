package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Vector3d;

import java.util.HashMap;
import java.util.UUID;

public class PlayerPoseHandler
{
    UUID playerID;

    MultiLimbedModel model;

    PlayerPose currentPose = new PlayerPose();
    PlayerPose renderPose = new PlayerPose();
    PlayerPose animatingPose = new PlayerPose();

    boolean locked = false;

    private HashMap<String, Integer> frame = new HashMap<String, Integer>();

    public PlayerPoseHandler(UUID id, PlayerModel playerModel)
    {
        playerID = id;
        model = new MultiLimbedModel(playerModel);
    }

    public UUID getID()
    {
        return playerID;
    }

    public void addPose(PlayerPose pose)
    {
        // Loop through every limb in the new pose
        for (String limb : pose.getLimbs())
            if (pose.hasAngle(limb))
                // If the current pose has no angle set for this limb, or the new pose has a higher priority for this limb
                // Then add the limb pose from the new pose to the current pose
                if (!currentPose.hasAngle(limb) || currentPose.getPriority(limb) < pose.getPriority(limb))
                    currentPose.setAngles(limb, pose.getAngles(limb), pose.getSpeeds(limb), pose.getPriority(limb));
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

    // Animate the model as defined in the animatingPose
    public void doPose(float partialTicks)
    {
        animateLimbs(partialTicks);

        for(String limb : animatingPose.getLimbs())
            model.rotateLimb(limb, animatingPose.getAngle(limb));
    }

    // Move each limb towards the currentPose by the animation speed
    public void animateLimbs(float partialTicks)
    {
        lock();

        PlayerPose newRender = new PlayerPose();

        // If renderPose has a pose for a limb, move to that position, otherwise move to the base model pose
        for (String limb : model.getLimbs())
            if (renderPose.hasAngle(limb))
                newRender.addAngle(limb, animateLimb(limb, getLimbPos(limb), partialTicks), 1);
            else
                newRender.addAngle(limb, animateLimb(getLimbPos(limb), modelFromLimb(limb), PoseHandler.defaultAnimationSpeed, partialTicks), 1);

        unlock();

        animatingPose = newRender;
    }

    // Get the limb angles at for the specified limb. If the animating pose does not contain any data for the limb, get it from the model
    public Vector3d getLimbPos(String limb)
    {
        if (animatingPose.hasAngle(limb))
            return animatingPose.getAngle(limb, 0);

        return modelFromLimb(limb);
    }

    private Vector3d modelFromLimb(String limb)
    {
        ModelRenderer limbModel = model.getLimb(limb);

        return  new Vector3d(limbModel.rotateAngleX, limbModel.rotateAngleY, limbModel.rotateAngleZ);
    }

    // Return a vector moving the specified vector towards the renderPose
    private Vector3d animateLimb(String limb, Vector3d current, float partialTicks)
    {
        // If frame doesn't exist for this limb yet, set it to 0
        if (!frame.containsKey(limb))
            frame.put(limb, 0);
        // If the current frame is greater than the amount of frames the renderPose has, reset to frame 0
        else if (frame.get(limb) >= renderPose.getFrames(limb))
            frame.put(limb, 0);

        int currentFrame = frame.get(limb);

        // Grab the renderPos angle for the specified limb
        Vector3d moveTo = renderPose.getAngle(limb, currentFrame);

        // Create a vector of the amount the limb has to move
        Vector3d toMove = new Vector3d(current.x - moveTo.x, current.y - moveTo.y, current.z - moveTo.z);
        double moveAmount = toMove.length();

        // If the limbs don't need to move and the renderPose has only one frame do nothing more, otherwise advance the frame and try again
        if (moveAmount == 0)
        {
            if (renderPose.getFrames(limb) == 1)
                return current;
            else
            {
                currentFrame++;
                frame.put(limb, currentFrame);

                return(animateLimb(limb, current, partialTicks));
            }
        }

        double aSpeed = renderPose.getAnimationSpeed(limb, currentFrame) * partialTicks;

        // If the limbs have to move more that the animation speed, reduce the amount to the animation speed
        if (moveAmount > aSpeed)
            toMove = toMove.normalize().scale(aSpeed);

        // Return a vector of the current positions moved towards moveTo by the animation speed
        return new Vector3d(current.x - toMove.x, current.y - toMove.y, current.z - toMove.z);
    }

    // Return a vector moving the specified vector towards the 'moveTo' vector
    private Vector3d animateLimb(Vector3d current, Vector3d moveTo, double aSpeed, float partialTicks)
    {
        aSpeed = aSpeed * partialTicks;

        // Create a vector of the amount the limb has to move
        Vector3d toMove = new Vector3d(current.x - moveTo.x, current.y - moveTo.y, current.z - moveTo.z);
        double moveAmount = toMove.length();

        // If the limbs don't need to move, do nothing more
        if (moveAmount == 0)
            return current;

        // If the limbs have to move more that the animation speed, reduce the amount to the animation speed
        if (moveAmount > aSpeed)
            toMove = toMove.normalize().scale(aSpeed);

        // Return a vector of the current positions moved towards moveTo by the animation speed
        return new Vector3d(current.x - toMove.x, current.y - toMove.y, current.z - toMove.z);
    }
}
