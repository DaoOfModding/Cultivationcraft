package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.LungConnection;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung.Lung;
import net.minecraft.resources.ResourceLocation;

public class LeftLungConnection extends LungConnection
{
    public static final ResourceLocation location = new ResourceLocation("leftlung");

    public LeftLungConnection(Lung newLung)
    {
        super(newLung);

        loc = location;
    }

    public void renderLungs(int x, int y)
    {
        lung.render(x, y, false);
    }

    // Returns the amount of air of the current breath type remaining in the lungs
    public float getCurrent(Breath breath)
    {
        if (!lung.canBreath(breath))
            return 0;

        return lung.getCurrent();
    }
}
