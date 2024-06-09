package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class GrowModifier extends TechniqueModifier
{
    public GrowModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.grow");
        CATEGORY = SIZE_CATEGORY;

        unlockQuest = new Quest(Quest.GROWPLANT, 1);
        stabiliseQuest = new Quest(Quest.GROW, 100000);

        damageMult = 2;
        size = new Vec3(1.5, 1.5, 1.5);
        itemSize = new Vec3(2, 2, 2);
    }

    // TODO: FlyingSword hitbox resizing
    // TODO: QiProjectile size
    // TODO: Qi cost increase x2
}
