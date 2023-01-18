package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import net.minecraft.world.level.material.Material;

import java.util.HashMap;

public class BlockElements
{
    protected static HashMap<Material, Integer> materialElement = new HashMap<>();

    public static void addMaterial(Material material, int ElementID)
    {
        materialElement.put(material, ElementID);
    }

    public static int getMaterialElement(Material material)
    {
        if (materialElement.containsKey(material))
            return materialElement.get(material);

        return Elements.noElementID;
    }
}
