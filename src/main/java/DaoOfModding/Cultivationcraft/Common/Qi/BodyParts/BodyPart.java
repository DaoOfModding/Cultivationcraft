package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Client.Textures.TextureList;
import DaoOfModding.Cultivationcraft.Client.Textures.TextureManager;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.PlayerStatModifications;
import javafx.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BodyPart
{
    private String ID;
    private ArrayList<String> modelIDs = new ArrayList<String>();
    private String limbPosition;
    private String displayNamePosition;
    private int qiNeeded;
    private PlayerStatModifications stats;
    private String textureID = TextureList.skin;

    private ArrayList<String> neededToForge = new ArrayList<String>();
    private ArrayList<Pair<String, String>> neededPositionToForge = new ArrayList<Pair<String, String>>();

    private HashMap<String, ResourceLocation> textureChanges = new HashMap<String, ResourceLocation>();

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

    public void setTexture(String ID)
    {
        textureID = ID;
    }

    public String getTextureID()
    {
        return textureID;
    }

    public void addTextureChange(String textureID, ResourceLocation textureLocation)
    {
        textureChanges.put(textureID, textureLocation);
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

    public void addNeededPosition(String positionID, String subpositionID)
    {
        neededPositionToForge.add(new Pair(positionID, subpositionID));
    }

    public void onLoad(UUID playerID)
    {
        for (Map.Entry<String, ResourceLocation> entry : textureChanges.entrySet())
            TextureManager.addTexture(playerID, entry.getKey(), entry.getValue());
    }

    public void onClientTick(PlayerEntity player)
    {

    }

    public void onServerTick(PlayerEntity player)
    {

    }

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

        // Loop through all player body modifications, return false if a modification for this position already exists
        for (BodyPart part : modifications.getModifications().values())
            if (part.getPosition().compareTo(limbPosition) == 0)
                return false;

        return true;
    }

    protected boolean hasNeededPositions(IBodyModifications modifications)
    {
        // Loop through all positions needed to forge this part
        for (Pair<String, String> position : neededPositionToForge)
        {
            // If the subposition is basePosition then check if there is a modification at this position
            if (position.getValue().compareTo(BodyPartNames.basePosition) == 0)
            {
                if (!modifications.hasModification(position.getKey()))
                    return false;
            }
            // Otherwise check if there is a modification option at this subposition
            else if (!modifications.hasOption(position.getKey(), position.getValue()))
                return false;
        }

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
