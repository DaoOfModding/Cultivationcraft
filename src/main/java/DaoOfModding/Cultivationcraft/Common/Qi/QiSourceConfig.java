package DaoOfModding.Cultivationcraft.Common.Qi;

public class QiSourceConfig
{
    final static int MaxSources = 3;

    // Return the number of QiSources to spawn
    // Much more likely to be 0, but has a chance to be up to MaxSources
    public static int getQiSourceInChunk()
    {
        return (int)(Math.pow(Math.random(), 10) * (MaxSources + 1));
    }
}
