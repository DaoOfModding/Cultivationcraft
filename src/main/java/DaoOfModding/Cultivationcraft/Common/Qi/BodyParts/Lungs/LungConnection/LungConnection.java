package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.LungConnection;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung.Lung;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class LungConnection
{
    public static final ResourceLocation location = new ResourceLocation("default");
    protected ResourceLocation loc;
    protected Lung lung;

    public LungConnection(Lung newLung)
    {
        lung = newLung;
        loc = location;
    }

    public void renderLungs(int x, int y)
    {
        lung.render(x, y, false);
        lung.render(x, y, true);
    }

    public void calculateCapacity(Player player)
    {
        lung.setCapacity(BodyPartStatControl.getPlayerStatControl(player).getStats().getStat(StatIDs.lungCapacity));
    }

    public float drain(Breath breath, float amount)
    {
        return lung.drain(breath, amount);
    }

    public ResourceLocation getLocation()
    {
        return loc;
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
