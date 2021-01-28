package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.PlayerStatModifications;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;

public class BodyPart
{
    private String ID;
    private ArrayList<String> modelIDs = new ArrayList<String>();
    private String limbPosition;
    private String displayNamePosition;
    private int qiNeeded;
    private PlayerStatModifications stats;

    private ArrayList<String> neededToForge = new ArrayList<String>();

    public BodyPart(String partID, String position, String displayNamePos, int qiToForge)
    {
        ID = partID;

        limbPosition = position;
        displayNamePosition = displayNamePos;

        qiNeeded = qiToForge;

        stats = new PlayerStatModifications();
    }

    public void addModel(String modelID)
    {
        modelIDs.add(modelID);
    }

    public PlayerStatModifications getStatChanges()
    {
        return stats;
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

    public int getQiNeeded()
    {
        return qiNeeded;
    }

    public ArrayList<String> getModelIDs()
    {
        return modelIDs;
    }

    public void addNeededPart(String partID)
    {
        neededToForge.add(partID);
    }

    // TODO: This
    public boolean canBeForged(PlayerEntity player)
    {
        // Ensure the player is a body cultivator
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() != CultivationTypes.BODY_CULTIVATOR)
            return false;

        IBodyModifications modifications = BodyModifications.getBodyModifications(player);
        if (modifications == null)
            return false;

        if (!hasNeededParts(modifications))
            return false;

        // Loop through all player body modifications, return false if a modification for this position already exists
        for (BodyPart part : modifications.getModifications().values())
            if (part.getPosition().compareTo(limbPosition) == 0)
                return false;

        return true;
    }

    protected boolean hasNeededParts(IBodyModifications modifications)
    {
        // Check if the player has all body parts that are needed to forge this part
        for (String testPart : neededToForge)
        {
            BodyPart test = BodyPartNames.getPart(testPart);

            if (test == null)
            {
                test = BodyPartNames.getOption(testPart);

                if (!modifications.hasOption(test.getPosition(), ((BodyPartOption) test).getSubPosition(), test.ID))
                    return false;
            }
            else if (!modifications.hasModification(test.getPosition(), test.ID))
                return false;
        }

        return true;
    }

}
