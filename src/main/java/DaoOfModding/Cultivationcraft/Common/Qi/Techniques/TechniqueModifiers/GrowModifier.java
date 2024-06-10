package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques.ItemEnhanceTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques.QiBarrierTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
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
        qiMult = 2;
        size = new Vec3(1.5, 1.5, 1.5);
        itemSize = 2;
    }

    @Override
    public float getQiCostModifier(Technique tech)
    {
        if (tech instanceof QiBarrierTechnique || tech instanceof ItemEnhanceTechnique)
            return qiMult;

        return 1;
    }

    @Override
    public void doResize(Player owner, Technique tech)
    {
        super.doResize(owner, tech);

        if (tech instanceof QiBarrierTechnique || tech instanceof ItemEnhanceTechnique)
            QuestHandler.progressQuest(owner, Quest.GROW, 1);
    }

    // TODO: FlyingSword hitbox resizing
    // TODO: QiProjectile size
}
