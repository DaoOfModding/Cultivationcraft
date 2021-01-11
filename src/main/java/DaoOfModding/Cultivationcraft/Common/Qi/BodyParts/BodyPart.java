package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;

public class BodyPart
{
    String ID;
    ArrayList<String> modelIDs = new ArrayList<String>();
    String limbPosition;
    String limbSubPosition;

    public BodyPart(String partID, ArrayList<String> IDs, String position, String subPosition)
    {
        ID = partID;

        modelIDs = (ArrayList<String>)IDs.clone();

        limbPosition = position;
        limbSubPosition = subPosition;
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

    public ArrayList<String> getModelIDs()
    {
        return modelIDs;
    }

    // TODO: This
    public boolean canBeForged(PlayerEntity player)
    {
        // Loop through all player body modifications, return false if a modification for this subPosition already exists
        for (BodyPart part : BodyModifications.getBodyModifications(player).getModifications().values())
            if (part.getPosition() == limbPosition && limbSubPosition == part.getSubPosition())
                return false;

        return true;
    }

}
