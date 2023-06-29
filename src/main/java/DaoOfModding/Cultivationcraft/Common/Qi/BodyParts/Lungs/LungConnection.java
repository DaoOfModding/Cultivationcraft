package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung.Lung;
import net.minecraft.resources.ResourceLocation;

public class LungConnection
{
    public static final ResourceLocation location = new ResourceLocation("default");
    protected Lung lung;

    public LungConnection(Lung newLung)
    {
        lung = newLung;
    }

    public void renderLungs(int x, int y)
    {
        lung.render(x, y, false);
        lung.render(x, y, true);
    }

    public void setLung(Lung newLung)
    {
        lung = newLung;
    }

    // Returns the amount of air of the current breath type remaining in the lungs
    public float getCurrent(Breath breath)
    {
        if (!lung.canBreath(breath))
            return 0;

        // This is multiplied by 2 as there are two of the supplied lung for this connection
        return lung.getCurrent() * 2;
    }

    public Lung getLung()
    {
        return lung;
    }
}
