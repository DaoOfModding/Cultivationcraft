package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Element;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.HashMap;

public class QiSourceConfig
{
    public final static int MaxSources = 1;
    public final static int MaxSize = 64;
    public final static int MinSize = 16;
    public final static int MaxStorage = 1000000000;
    public final static int MinStorage = 1000000;

    protected final static int defaultRarity = 400;
    protected static HashMap<ResourceKey<Level>, Integer> dimensionRarity = new HashMap<>();

    // Ticks to fully regen
    // 20 ticks a second x 60 seconds in a minute = 1200 ticks for one minute
    public final static int MaxRegen = 1200;
    // 20 ticks a second x 60 seconds in a minute x 60 minutes in an hour = 72000 ticks for an hour
    public final static int MinRegen = 72000;

    public static void init()
    {
        QiSourceConfig.addDimensionRarity(Level.NETHER, 100);
    }

    // Return the number of QiSources to spawn
    // Much more likely to be 0, but has a chance to be up to MaxSources
    public static int getQiSourceInLevelChunk(ResourceKey<Level> dimension)
    {
        return (int)Math.floor(Math.pow(Math.random(), getRarity(dimension)) * (MaxSources + 1));
    }

    public static void addDimensionRarity(ResourceKey<Level> dimension, int rarity)
    {
        dimensionRarity.put(dimension, rarity);
    }

    public static int getRarity(ResourceKey<Level> dimension)
    {
        if (dimensionRarity.containsKey(dimension))
            return dimensionRarity.get(dimension);

        return defaultRarity;
    }

    // Return a randomly generated size for a Qi Source
    // Much more likely to be MinSize, but has a chance to be up to MaxSize
    public static int generateRandomSize()
    {
        return (int)(Math.pow(Math.random(), 5) * (MaxSize - MinSize)) + MinSize;
    }

    // Return a randomly generated double
    // 0 = MinStorage, 1 = MaxStorage
    public static double generateRandomQiStorage()
    {
        return Math.pow(Math.random(), 10);
    }

    // Return a randomly generated int
    // Specifies the number of ticks it takes to fully regen the qi source
    public static int generateRandomQiRegen()
    {
        return (int)(Math.pow(Math.random(), 0.25) * (MinRegen - MaxRegen)) + MaxRegen;
    }
}
