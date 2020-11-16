package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Freeze;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;

public class IceWalkTechnique extends Technique
{
    public IceWalkTechnique()
    {
        name = new TranslationTextComponent("cultivationcraft.technique.icewalk").getString();
        elementID = Elements.iceElementID;

        type = useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/icewalk.png");
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        // TODO: Something better than crouching to cancel ice
        // Freeze the ground directly below the player
        Freeze.Freeze(event.player.getEntityWorld(), event.player.getPosition().down(), !event.player.isCrouching());
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        // TODO: Better way of sliding
        Vector3d test = event.player.getMotion().scale(0.03);
        event.player.addVelocity(test.x, 0, test.z);

        // Freeze the ground directly below the player
        // Yes, I KNOW I shouldn't mess with blocks on the client
        // But this ensures there is always an ice block underneath the player when moving
        // And as they revert after 1 second it SHOULDN'T create any desyncs
        Freeze.Freeze(event.player.getEntityWorld(), event.player.getPosition().down(), !event.player.isCrouching());
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        return true;
    }
}
