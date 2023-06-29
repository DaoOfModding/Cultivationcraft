package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs;

import java.awt.*;

public class Breath
{
    public static final Breath NONE = new Breath(new Color(0, 0, 0, 0));
    public static final Breath AIR = new Breath(new Color(255, 255, 255, 255));
    public static final Breath WATER = new Breath(new Color(127, 200, 255, 255));
    public static final Breath FIRE = new Breath(new Color(255, 0, 0, 255));

    Color color;

    public Breath(Color col)
    {
        color = col;
    }

    public Color getColor()
    {
        return color;
    }
}
