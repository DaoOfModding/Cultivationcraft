package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
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
    PlayerPose oldRenderPose = new PlayerPose();
    PlayerPose animatingPose = new PlayerPose();

    boolean locked = false;

    private boolean isJumping = false;
    // Ticks before allowing jump to be set to False
    private int jumpCooldown = 0;

    private HashMap<String, Integer> frame = new HashMap<String, Integer>();
    private HashMap<String, Float> animationTime = new HashMap<String, Float>();
    private HashMap<Integer, Integer> aLockedFrame = new HashMap<Integer, Integer>();

    public PlayerPoseHandler(UUID id, PlayerModel playerModel)
    {
        playerID = id;
        model = new MultiLimbedModel(playerModel);
    }

    public MultiLimbedModel getPlayerModel()
    {
        return model;
    }

    public void setPlayerModel(MultiLimbedModel newModel)
    {
        model = newModel;
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
                    currentPose.setAngles(limb, pose.getAngles(limb), pose.getSpeeds(limb), pose.getPriority(limb), pose.getOffset(limb), pose.getAnimationLock(limb));
    }

    // Updated each ExtendableModel in the player model to be looking in the direction of the player if it is set to do so
    protected void updateHeadLook()
    {
        // Loop through all limbs and check if they are set to be looking, updated the current pose for them if they are
        for (String limb : model.getLimbs())
        {
            ExtendableModelRenderer limbModel = model.getLimb(limb);

            if (limbModel.isLooking())
            {
                Vector3d angles = new Vector3d(0, 0, 0);

                // Go through all parents and negate their rotations for this limb
                while (limbModel.getParent() != null)
                {
                    limbModel = limbModel.getParent();
                    angles = angles.subtract(limbModel.rotateAngleX, limbModel.rotateAngleY, limbModel.rotateAngleZ);
                }

                // Add the base head's angles to this model
                //angles = angles.add(model.baseModel.bipedHead.rotateAngleX, model.baseModel.bipedHead.rotateAngleY, model.baseModel.bipedHead.rotateAngleZ);
                //angles = angles.add(0, Math.toRadians(90), 0);

                currentPose.addAngle(limb, angles, 0, 1f, -1);
            }
        }
    }

    protected void updateJumping()
    {
        if (isJumping)
        {
            if (jumpCooldown > 0)
                jumpCooldown -= 1;

            currentPose = currentPose.combine(GenericPoses.Jumping);
        }
    }

    public boolean isJumping()
    {
        if (jumpCooldown > 0)
            return true;

        return isJumping;
    }

    public void updateRenderPose()
    {
        lock();

        oldRenderPose = renderPose;
        renderPose = currentPose;

        currentPose = new PlayerPose();

        updateHeadLook();
        updateJumping();

        unlock();
    }

    public void setJumping(boolean jump)
    {
        if (!jump)
            if (jumpCooldown > 0)
                return;

        if (jumpCooldown <= 0 && jump)
            jumpCooldown = 5;

        isJumping = jump;
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
        // Reset aLocks
        aLockedFrame = new HashMap<Integer, Integer>();

        // Generate the animating pose based on the the target angles in the render pose
        animateLimbs(partialTicks);

        // Rotate each limb to the angle stored in the animating pose plus any offset angles
        for(String limb : animatingPose.getLimbs())
            model.rotateLimb(limb, animatingPose.getAngle(limb).add(animatingPose.getOffset(limb)));
    }

    // Move each limb towards the currentPose by the animation speed
    public void animateLimbs(float partialTicks)
    {
        lock();

        PlayerPose newRender = new PlayerPose();

        // Reset stored animation data for any limbs whose target position have changed
        for (String limb : model.getLimbs())
            if (!(renderPose.hasAngle(limb) && oldRenderPose.hasAngle(limb) && renderPose.getAngle(limb).equals(oldRenderPose.getAngle(limb))))
                animationTime.put(limb, 0f);


        // Calculate animation locks
        for (String limb : model.getLimbs())
            if (renderPose.hasAngle(limb))
                calculateAnimationLocks(limb, getLimbPos(limb), partialTicks);

        // If renderPose has a pose for a limb, move to that position, otherwise move to the base model pose
        for (String limb : model.getLimbs())
        {
            Vector3d angles;

            if (renderPose.hasAngle(limb))
                angles =  animateLimb(limb, getLimbPos(limb), partialTicks);
            else
                angles = animateLimb(getLimbPos(limb), new Vector3d(0, 0, 0), AnimationSpeedCalculator.defaultSpeedPerTick, partialTicks);

            newRender.addAngle(limb, angles, 1);

            newRender.addOffset(limb, animatingPose.getOffset(limb));
        }

        // Add the ticks that have passed into the animationTime map
        for (String limb : model.getLimbs())
            animationTime.put(limb, animationTime.get(limb) + partialTicks);

        unlock();

        animatingPose = newRender;
    }

    // Get the limb angles for the specified limb. If the animating pose does not contain any data for the limb, get it from the model
    public Vector3d getLimbPos(String limb)
    {
        if (animatingPose.hasAngle(limb))
            return animatingPose.getAngle(limb, 0);

        return modelFromLimb(limb);
    }

    private Vector3d modelFromLimb(String limb)
    {
        ModelRenderer limbModel = model.getLimb(limb);

        return new Vector3d(limbModel.rotateAngleX, limbModel.rotateAngleY, limbModel.rotateAngleZ);
    }

    private void calculateAnimationLocks(String limb, Vector3d current, float partialTicks)
    {
        // If frame doesn't exist for this limb yet, set it to 0
        if (!frame.containsKey(limb))
            frame.put(limb, 0);

        // Do nothing if this limb only has one frame
        if (renderPose.getFrames(limb) == 1)
            return;

        int aLock = renderPose.getAnimationLock(limb);

        // Do nothing if this limb is not animation locked
        if (aLock == -1)
            return;

        int currentFrame = frame.get(limb);

        // Grab the renderPos angle for the specified limb
        Vector3d moveTo = renderPose.getAngle(limb, currentFrame);

        // Create a vector of the amount the limb has to move
        Vector3d toMove = new Vector3d(current.x - moveTo.x, current.y - moveTo.y, current.z - moveTo.z);
        double moveAmount = toMove.length();

        // Advance the current frame if the limb has nothing more to move
        if (moveAmount == 0)
            currentFrame++;

        // Reset the frame if the current frame is larger than the maximum frame for this limb
        if (currentFrame >= renderPose.getFrames(limb))
            currentFrame = 0;

        // If there is already a frame stored in the animation lock map for this animation lock
        if (aLockedFrame.containsKey(aLock))
        {
            int lockedFrame = aLockedFrame.get(aLock);

            // Do nothing if this frame is the same as the locked frame
            if (lockedFrame == currentFrame)
                return;

            if (currentFrame == 0)
                if (lockedFrame > 1)
                    return;

            if (lockedFrame == 0)
                if (currentFrame > 1)
                {
                    aLockedFrame.put(aLock, currentFrame);
                    return;
                }

            if (lockedFrame > currentFrame)
                aLockedFrame.put(aLock, currentFrame);
        }
        else
            aLockedFrame.put(aLock, currentFrame);
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
        int aLock = renderPose.getAnimationLock(limb);

        // Grab the renderPos angle for the specified limb
        Vector3d moveTo = renderPose.getAngle(limb, currentFrame);

        // Create a vector of the amount the limb has to move
        Vector3d toMove = current.subtract(moveTo);
        double moveAmount = toMove.length();

        // If the limbs don't need to move and the renderPose has only one frame do nothing more, otherwise advance the frame and try again
        if (moveAmount == 0)
        {
            if (renderPose.getFrames(limb) == 1)
                return current;
            else
            {
                // Do nothing if frame locked to this frame
                if (aLockedFrame.containsKey(aLock))
                    if (aLockedFrame.get(aLock) == currentFrame)
                        return current;

                currentFrame++;
                frame.put(limb, currentFrame);

                // Reset the time stored in this frame
                animationTime.put(limb, 0f);

                return(animateLimb(limb, current, partialTicks));
            }
        }

        // Ensure there is a value stored in animationTime for this limb
        if (!animationTime.containsKey(limb))
            animationTime.put(limb, 0f);

        // Calculate the amount of ticks remaining for this animation
        float TicksRemaining = renderPose.getAnimationSpeed(limb, currentFrame) - animationTime.get(limb);

        // If no ticks remaining then instantly move to the specified position
        if (TicksRemaining <= 0)
            return moveTo;

        double aSpeed = AnimationSpeedCalculator.ticksToSpeed(current, moveTo, TicksRemaining) * partialTicks;

        // If the limbs have to move more that the animation speed, reduce the amount to the animation speed
        if (moveAmount > aSpeed)
            toMove = toMove.normalize().scale(aSpeed);

        // Return a vector of the current positions moved towards moveTo by the animation speed
        return current.subtract(toMove);
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

        // Return a vector of the current position moved towards moveTo by the animation speed
        return new Vector3d(current.x - toMove.x, current.y - toMove.y, current.z - toMove.z);
    }
}
