package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class BodyPartOption extends BodyPart
{
    String limbSubPosition;

    public BodyPartOption(String partID, String position, String subPosition, String displayNamePos, int qiToForge)
    {
        super(partID, new ArrayList<String>(), position, displayNamePos, qiToForge);

        limbSubPosition = subPosition;
    }

    public String getSubPosition()
    {
        return limbSubPosition;
    }

    @Override
    public boolean canBeForged(PlayerEntity player)
    {
        // Ensure the player is a body cultivator
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() != CultivationTypes.BODY_CULTIVATOR)
            return false;

        // Return false if there exists an option in this position and subposition already
        for (String subPos : BodyModifications.getBodyModifications(player).getModificationOptions(getPosition()).keySet())
            if (subPos.compareTo(getSubPosition()) == 0)
                return false;

        return true;
    }
}
