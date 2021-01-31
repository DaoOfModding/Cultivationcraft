package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import javafx.util.Pair;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class BodyPartOption extends BodyPart
{
    String limbSubPosition;

    // This array list contains modelID's tied to specific models
    // If the key is "" then it is applied to all models in this position
    HashMap<String, ArrayList<String>> modelIDs = new HashMap<String, ArrayList<String>>();

    public BodyPartOption(String partID, String position, String subPosition, String displayNamePos, int qiToForge)
    {
        super(partID, position, displayNamePos, qiToForge);

        limbSubPosition = subPosition;
    }

    public HashMap<String, ArrayList<String>> getOptionModels()
    {
        return modelIDs;
    }

    public void addModel(String modelID)
    {
        addModel(modelID, "");
    }

    public void addModel(String modelID, String attachedModelID)
    {
        if (!modelIDs.containsKey(attachedModelID))
            modelIDs.put(attachedModelID, new ArrayList<String>());

        modelIDs.get(attachedModelID).add(modelID);
    }

    public ArrayList<String> getDefaultOptionModels()
    {
        return getOptionModels("");
    }

    public ArrayList<String> getOptionModels(String ModelID)
    {
        if (!modelIDs.containsKey(ModelID))
            return new ArrayList<String>();

        return modelIDs.get(ModelID);
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

        IBodyModifications modifications = BodyModifications.getBodyModifications(player);
        if (modifications == null)
            return false;

        if (!hasNeededPositions(modifications))
            return false;

        if (!hasNeededParts(modifications))
            return false;

        // Return false if there exists an option in this position and subposition already
        for (String subPos : modifications.getModificationOptions(getPosition()).keySet())
            if (subPos.compareTo(getSubPosition()) == 0)
                return false;

        return true;
    }
}
