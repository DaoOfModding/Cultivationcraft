package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class EarthModifier extends TechniqueModifier
{
    public EarthModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.earth");
        CATEGORY = ELEMENTAL_CATEGORY;

        Element = Elements.earthElement;

        unlockQuest = new Quest(Quest.QI_SOURCE_MEDITATION, 1, Element.toString());
        stabiliseQuest = new Quest(Quest.DAMAGE_DEALT, 1000, Element.toString());

        coreTexture = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/cores/earth.png"));

        flyingMount = true;
    }

    public BlockState getMountSource(Player owner)
    {
        if (owner.isOnGround())
            return owner.getFeetBlockState();

        return null;
    }
}
