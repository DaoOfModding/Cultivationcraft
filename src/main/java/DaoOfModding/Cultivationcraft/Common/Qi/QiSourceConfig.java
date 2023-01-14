package DaoOfModding.Cultivationcraft.Common.Qi;

public class QiSourceConfig
{
    public final static int MaxSources = 1;
    public final static int MaxSize = 64;
    public final static int MinSize = 16;
    public final static int rarity = 500;
    public final static int MaxStorage = 1000000000;
    public final static int MinStorage = 100000;
    public final static int MaxRegen = 100000;
    public final static int MinRegen = 10;

    // Return the number of QiSources to spawn
    // Much more likely to be 0, but has a chance to be up to MaxSources
    public static int getQiSourceInLevelChunk()
    {
        return (int)Math.floor(Math.pow(Math.random(), rarity) * (MaxSources + 1));
    }

    // Return a randomly generated size for a Qi Source
    // Much more likely to be MinSize, but has a chance to be up to MaxSize
    public static int generateRandomSize()
    {
        return (int)(Math.pow(Math.random(), 5) * (MaxSize - MinSize)) + MinSize;
    }

    public static int generateRandomQiStorage()
    {
        return (int)(Math.pow(Math.random(), 10) * (MaxStorage - MinStorage)) + MinStorage;
    }

    public static int generateRandomQiRegen()
    {
        return (int)(Math.pow(Math.random(), 10) * (MaxRegen - MinRegen)) + MinRegen;
    }
}
