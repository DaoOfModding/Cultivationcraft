package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class TechniqueModifier
{
    ResourceLocation Element = null;
    float power = 1;

    public ResourceLocation getElement()
    {
        return Element;
    }

    public Boolean hasElement()
    {
        if (getElement() != null)
            return true;

        return false;
    }

    public void tick(Player owner, Vec3 position, ResourceLocation element)
    {

    }

    public boolean canUse(Player player)
    {
        return false;
    }
}
