package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;

public class FireModifier extends TechniqueModifier
{
    public FireModifier()
    {
        Element = Elements.fireElement;
        coreTexture = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/orbfillingfire.png"), 32);
    }
}
