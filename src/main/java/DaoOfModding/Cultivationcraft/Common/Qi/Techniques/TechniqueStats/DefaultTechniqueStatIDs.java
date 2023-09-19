package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

public class DefaultTechniqueStatIDs
{
    public static final ResourceLocation qiCost = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.tstat.qicost");
    public static final ResourceLocation staminaCost = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.tstat.staminacost");
    public static final ResourceLocation breathCost = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.tstat.breathcost");

    public static final ResourceLocation range = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.tstat.range");
    public static final ResourceLocation damage = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.tstat.damage");

    public static final ResourceLocation movementSpeed = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.tstat.speed");

    protected static ArrayList<ResourceLocation> reverseNegative = new ArrayList<>();

    public static void init()
    {
        addReversedNegativeStat(qiCost);
    }

    public static void addReversedNegativeStat(ResourceLocation stat)
    {
        reverseNegative.add(stat);
    }

    public static boolean isReversedNegative(ResourceLocation stat)
    {
        for (ResourceLocation search : reverseNegative)
            if (stat.compareTo(search) == 0)
                return true;

        return false;
    }
}
