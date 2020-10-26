package DaoOfModding.Cultivationcraft.Common.Qi;

public class QiSourceConfig
{
    // Todo: Scaling, more likely to be 0, but possible to be more than 1
    // Return the number of QiSources to spawn
    public static int getQiSourceInChunk()
    {
        return (int)(Math.random() * 3);
    }
}
