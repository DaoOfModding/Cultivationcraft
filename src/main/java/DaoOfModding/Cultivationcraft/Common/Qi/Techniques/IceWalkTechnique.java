package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Common.Blocks.FrozenTileEntity;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Freeze;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;

public class IceWalkTechnique extends Technique
{
    // TODO: This is temporary
    private int power = 200;

    public IceWalkTechnique()
    {
        name = new TranslationTextComponent("cultivationcraft.technique.icewalk").getString();
        elementID = Elements.iceElementID;

        type = useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/icewalk.png");
    }

    public boolean calculateHalfOrNot(PlayerEntity player)
    {
        double playerY = player.getPosY();
        playerY = playerY - (int)playerY;

        Cultivationcraft.LOGGER.info(playerY);

        if (playerY >= 0.5)
            return false;

        return true;
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        Vector3d test = event.player.getMotion().scale(0.04);
        event.player.addVelocity(test.x, 0, test.z);

        BlockPos pos = event.player.getPosition().down();

        // If the block is already frozen, just update the freeze and do nothing more
        if(Freeze.tryUpdateFreeze(event.player.getEntityWorld(), pos, power))
            return;

        boolean half = false;

        // If moving up or down set the block to either be half or spawn one block above or below the player
        if(event.player.getLookVec().y < -0.75 || event.player.getLookVec().y > 0.25)
            half = calculateHalfOrNot(event.player);

        // If moving up add 1 onto the block position
        if (event.player.getLookVec().y > 0.25)
            pos = pos.add(0, 1, 0);

        // Freeze the ground directly below the player
        Freeze.FreezeAir(event.player.getEntityWorld(), pos, power, half);
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        // TODO: Better way of sliding
        Vector3d test = event.player.getMotion().scale(0.04);
        event.player.addVelocity(test.x, 0, test.z);


        BlockPos pos = event.player.getPosition().down();

        TileEntity tileEntity = event.player.getEntityWorld().getTileEntity(pos);

        // If the block at the specified position is a frozen block do nothing
        if (tileEntity!= null && tileEntity instanceof FrozenTileEntity)
            return;

        boolean half = false;

        // If moving up or down set the block to either be half or spawn one block above or below the player
        if(event.player.getLookVec().y < -0.75 || event.player.getLookVec().y > 0.25)
            half = calculateHalfOrNot(event.player);

        // If moving up add 1 onto the block position and 0.5 onto the player position
        if (event.player.getLookVec().y > 0.25)
        {
            pos = pos.add(0, 1, 0);

            // TODO: Work out why this is jumping up too much
            event.player.setPosition(event.player.getPosX(), event.player.getPosY() + 0.5, event.player.getPosZ());
        }

        // Freeze the ground directly below the player
        Freeze.FreezeAirAsClient(event.player.getEntityWorld(), pos, power, half);
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        return true;
    }
}
