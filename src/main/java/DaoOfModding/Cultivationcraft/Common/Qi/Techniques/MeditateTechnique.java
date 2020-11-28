package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;

public class MeditateTechnique extends Technique
{
    public MeditateTechnique()
    {
        name = "Meditate";
        elementID = Elements.noElementID;
        type = Technique.useType.Toggle;
        multiple = false;

        // TODO: Make this icon
        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/meditate.png");
    }

    public void tickServer(TickEvent.PlayerTickEvent event)
    {

    }

    public void tickClient(TickEvent.PlayerTickEvent event)
    {

    }
}
