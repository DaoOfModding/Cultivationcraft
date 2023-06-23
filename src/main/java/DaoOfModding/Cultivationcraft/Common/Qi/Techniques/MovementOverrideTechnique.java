package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

public class MovementOverrideTechnique extends Technique
{
    protected boolean momentum = false;
    protected Vec3 targetSpeed = new Vec3(0, 0, 0);
    protected Vec3 currentSpeed = new Vec3(0, 0, 0);

    // Lower values means strong momentum
    protected float momentumFactor = 0.05f;

    public MovementOverrideTechnique()
    {
        super();

        multiple = false;
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);

        if (momentum)
            applySpeed(event.player);
    }

    @Override
    public void tickInactiveClient(TickEvent.PlayerTickEvent event)
    {
        super.tickInactiveClient(event);

        if (momentum)
            applySpeed(event.player);
    }

    protected void applySpeed(Player player)
    {
        Vec3 oldSpeed = currentSpeed;

        // Slowly ramp up to the target speed
        currentSpeed = currentSpeed.lerp(targetSpeed, momentumFactor);

        // Adds friction when in air so that speed doesn't increase exponentially
        if (!player.isOnGround())
            player.setDeltaMovement(player.getDeltaMovement().subtract(oldSpeed.scale(0.8)));
        else
            player.setDeltaMovement(player.getDeltaMovement().subtract(oldSpeed.multiply(0, 0.8, 0)));

        player.setDeltaMovement(player.getDeltaMovement().add(currentSpeed));
    }

    public void setMomentum(Vec3 speed)
    {
        targetSpeed = speed;
    }

    // Called when the keybinding for the specified input is pressed
    // If any of these return true, then vanilla movement for that input will be cancelled
    public boolean overwriteForward()
    {
        return false;
    }

    public boolean overwriteLeft()
    {
        return false;
    }

    public boolean overwriteRight()
    {
        return false;
    }

    public boolean overwriteBackward()
    {
        return false;
    }

    public boolean overwriteJump()
    {
        return false;
    }
}
