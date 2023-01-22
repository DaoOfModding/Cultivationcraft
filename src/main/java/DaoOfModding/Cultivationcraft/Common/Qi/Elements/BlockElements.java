package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Material;

import java.util.HashMap;

public class BlockElements
{
    protected static HashMap<Material, ResourceLocation> materialElement = new HashMap<>();

    public static void addMaterial(Material material, ResourceLocation Element)
    {
        materialElement.put(material, Element);
    }

    public static ResourceLocation getMaterialElement(Material material)
    {
        if (materialElement.containsKey(material))
            return materialElement.get(material);

        return Elements.noElement;
    }
}
