package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.LungConnection;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung.Lung;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class RightLungConnection extends LungConnection
{
    public static final ResourceLocation location = new ResourceLocation("rightlung");

    public RightLungConnection(Lung newLung)
    {
        super(newLung);

        loc = location;
    }

    public void renderLungs(int x, int y)
    {
        lung.render(x, y, true);
    }

    public void calculateCapacity(Player player)
    {
        lung.setCapacity(BodyPartStatControl.getPlayerStatControl(player).getStats().getStat(StatIDs.lungCapacity) / 2);
    }

    // Returns the amount of air of the current breath type remaining in the lungs
    public float getCurrent(Breath breath)
    {
        if (!lung.canBreath(breath))
            return 0;

        return lung.getCurrent();
    }
}
