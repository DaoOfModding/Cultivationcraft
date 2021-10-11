package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;


public class AuraTechnique extends Technique
{
    protected int range = 10;

    public AuraTechnique()
    {
        super();

        type = useType.Toggle;
        multiple = false;
    }

    public int getRange()
    {
        return range;
    }

    public void setRange(int newRange)
    {
        range = newRange;
    }
}
