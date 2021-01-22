package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;

public class BodyPart
{
    String ID;
    ArrayList<String> modelIDs = new ArrayList<String>();
    String limbPosition;
    String limbSubPosition;
    String displayNamePosition;
    int qiNeeded;

    public BodyPart(String partID, ArrayList<String> IDs, String position, String subPosition, String displayNamePos, int qiToForge)
    {
        ID = partID;

        modelIDs = (ArrayList<String>)IDs.clone();

        limbPosition = position;
        limbSubPosition = subPosition;
        displayNamePosition = displayNamePos;

        qiNeeded = qiToForge;
    }

    public String getDisplayName()
    {
        return new TranslationTextComponent(displayNamePosition).getString();
    }

    public String getID()
    {
        return ID;
    }

    public String getPosition()
    {
        return limbPosition;
    }

    public String getSubPosition()
    {
        return limbSubPosition;
    }

    public int getQiNeeded()
    {
        return qiNeeded;
    }

    public ArrayList<String> getModelIDs()
    {
        return modelIDs;
    }

    // TODO: This
    public boolean canBeForged(PlayerEntity player)
    {
        // Ensure the player is a body cultivator
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() != CultivationTypes.BODY_CULTIVATOR)
            return false;

        // Loop through all player body modifications, return false if a modification for this subPosition already exists
        // Or if this body part is already forged
        for (BodyPart part : BodyModifications.getBodyModifications(player).getModifications().values())
            if (part.ID == ID || (part.getPosition() == limbPosition && limbSubPosition == part.getSubPosition()))
                return false;

        return true;
    }

}
