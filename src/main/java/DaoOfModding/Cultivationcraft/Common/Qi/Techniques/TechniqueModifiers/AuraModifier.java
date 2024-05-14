package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Element;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class AuraModifier extends TechniqueModifier
{
    public AuraModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.aura");
        CATEGORY = MODIFIER_CATEGORY;

        allowSameCategory = true;
    }

    public void tick(Player owner, Vec3 position, ResourceLocation element)
    {
        int range = (int)(power * 2);
        Element el = Elements.getElement(element);

        for (int x = (range * -1); x < range; x++)
            for (int y = (range * -1); y < range; y++)
                for (int z =  (range * -1); z < range; z++)
                    el.effectBlock(owner.level, new BlockPos(position.x + x, position.y + y, position.z + z));
    }

    public boolean canLearn(Player player)
    {
        if (unlockQuest == null)
            return false;

        // This modifier can only be learnt if an elemental modifier (not pure) is learnt
        for (TechniqueModifier modifier : CultivatorStats.getCultivatorStats(player).getCultivation().getModifiers())
            if (modifier.CATEGORY.compareTo(ELEMENTAL_CATEGORY) == 0 && modifier.ID.compareTo(QiModifier.QI_ID) != 0)
                return true;


        return false;
    }
}
