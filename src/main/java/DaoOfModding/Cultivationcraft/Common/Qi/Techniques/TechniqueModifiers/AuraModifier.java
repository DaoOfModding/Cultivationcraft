package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Element;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class AuraModifier extends TechniqueModifier
{
    public void tick(Player owner, Vec3 position, ResourceLocation element)
    {
        int range = (int)(power * 2);
        Element el = Elements.getElement(element);

        for (int x = (range * -1); x < range; x++)
            for (int y = (range * -1); y < range; y++)
                for (int z =  (range * -1); z < range; z++)
                    el.effectBlock(owner.level, new BlockPos(position.x + x, position.y + y, position.z + z));
    }
}
