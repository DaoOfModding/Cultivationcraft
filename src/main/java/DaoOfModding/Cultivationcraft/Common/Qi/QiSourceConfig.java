package DaoOfModding.Cultivationcraft.Common.Qi;

public class QiSourceConfig
{
    final static int MaxSources = 2;
    final static int MaxSize = 64;
    final static int MinSize = 8;

    // Return the number of QiSources to spawn
    // Much more likely to be 0, but has a chance to be up to MaxSources
    public static int getQiSourceInChunk()
    {
        return (int)(Math.pow(Math.random(), 10) * (MaxSources + 1));
    }

    // Return a randomly generated size for a Qi Source
    // Much more likely to be MinSize, but has a chance to be up to MaxSize
    public static int generateRandomSize()
    {
        return (int)(Math.pow(Math.random(), 10) * (MaxSize - MinSize)) + MinSize;
    }
}
