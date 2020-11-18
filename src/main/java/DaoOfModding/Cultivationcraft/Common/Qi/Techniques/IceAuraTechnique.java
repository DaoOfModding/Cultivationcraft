package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Freeze;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;

public class IceAuraTechnique extends AuraTechnique
{
    // TODO: Temp
    protected int power = 20;

    public IceAuraTechnique()
    {
        super();

        name = "Ice Aura";
        elementID = Elements.iceElementID;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/iceaura.png");
    }

    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        Freeze.FreezeArea(event.player.getEntityWorld(), event.player.getPosition(), getRange(), power);
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        return true;
    }
}
