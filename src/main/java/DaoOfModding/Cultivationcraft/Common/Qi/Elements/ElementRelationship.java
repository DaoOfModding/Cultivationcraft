package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import net.minecraft.resources.ResourceLocation;

public class ElementRelationship
{
    public ResourceLocation defendingElement;
    public double modifier;

    public ElementRelationship(ResourceLocation ID, double damageModifier)
    {
        defendingElement = ID;
        modifier = damageModifier;
    }
}
