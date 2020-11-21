package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Common.PlayerUtils;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Freeze;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;

public class IceWalkTechnique extends Technique
{
    // TODO: This is temporary
    private int power = 20;

    public IceWalkTechnique()
    {
        name = new TranslationTextComponent("cultivationcraft.technique.icewalk").getString();
        elementID = Elements.iceElementID;

        type = useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/icewalk.png");
    }

    public boolean calculateStairOrNot(PlayerEntity player)
    {
        if (PlayerUtils.lookingUp(player) || PlayerUtils.lookingDown(player))
            return true;

        return false;
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        BlockPos pos = event.player.getPosition().down();

        // If the block is already frozen, just update the freeze and do nothing more
        if(Freeze.tryUpdateFreeze(event.player.getEntityWorld(), pos, power))
            return;

        // If moving up or down set the block to either be half or spawn one block above or below the player
        Direction dir = Direction.DOWN;

        if (calculateStairOrNot(event.player))
            dir = PlayerUtils.movementDirection(event.player);

        // If moving up add 1 onto the block position
        if (PlayerUtils.lookingUp(event.player))
        {
            dir = PlayerUtils.invertDirection(dir);
            pos = pos.add(0, 1, 0);

            // If the block is already frozen, just update the freeze and do nothing more
            if(Freeze.tryUpdateFreeze(event.player.getEntityWorld(), pos, power))
                return;
        }

        // Freeze the ground directly below the player
        Freeze.FreezeAir(event.player.getEntityWorld(), pos, power, dir);
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        // TODO: Better way of sliding
        Vector3d test = event.player.getMotion().scale(0.04);
        event.player.addVelocity(test.x, 0, test.z);

        BlockPos pos = event.player.getPosition().down();

        // If the block at the specified position is a frozen block do nothing
        if (Freeze.isFrozen(event.player.world, pos))
            return;

        Direction dir = Direction.DOWN;

        if (calculateStairOrNot(event.player))
            dir = PlayerUtils.movementDirection(event.player);

        // If moving up add 1 onto the block position and 0.5 onto the player position
        if (PlayerUtils.lookingUp(event.player))
        {
            dir = PlayerUtils.invertDirection(dir);
            pos = pos.add(0, 1, 0);

            // If the block at the specified position is a frozen block do nothing
            if (Freeze.isFrozen(event.player.world, pos))
                return;

            float y = pos.getY() + 0.5f;
            event.player.setPosition(event.player.getPosX(), y, event.player.getPosZ());
        }

        // Freeze the ground directly below the player
        Freeze.FreezeAirAsClient(event.player.getEntityWorld(), pos, power, dir);
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        return true;
    }
}
