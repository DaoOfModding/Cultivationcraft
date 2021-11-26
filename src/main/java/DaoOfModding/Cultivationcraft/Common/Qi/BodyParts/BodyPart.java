package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Client.Textures.TextureList;
import DaoOfModding.Cultivationcraft.Client.Textures.TextureManager;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
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
    private ArrayList<String> quadIDs = new ArrayList<String>();
    private ArrayList<String> firstPersonModelIDs = new ArrayList<String>();
    private String limbPosition;
    private String displayNamePosition;
    private int qiNeeded;
    private PlayerStatModifications stats;
    private String textureID = TextureList.skin;

    private String viewPoint;

    private ArrayList<String> neededToForge = new ArrayList<String>();
    private ArrayList<String> neededNotToForge = new ArrayList<String>();
    private ArrayList<Pair<String, String>> neededPositionToForge = new ArrayList<Pair<String, String>>();
    private ArrayList<Pair<String, String>> needNotPositionToForge = new ArrayList<Pair<String, String>>();
    private ArrayList<String> neededTags = new ArrayList<String>();
    private ArrayList<String> neededNotTags = new ArrayList<String>();

    private ArrayList<String> uniqueTags = new ArrayList<String>();

    private HashMap<String, Integer> hands = new HashMap<String, Integer>();

    private HashMap<String, ResourceLocation> textureChanges = new HashMap<String, ResourceLocation>();

    public BodyPart(String partID, String position, String displayNamePos, int qiToForge)
    {
        ID = partID;

        limbPosition = position;
        displayNamePosition = displayNamePos;

        qiNeeded = qiToForge;

        stats = new PlayerStatModifications();
    }

    public void setViewPoint(String viewPoint)
    {
        this.viewPoint = viewPoint;
    }

    public String getViewPoint()
    {
        return viewPoint;
    }

    public void addModel(String modelID)
    {
        modelIDs.add(modelID);
    }

    public void addHand(String modelID, int slot)
    {
        hands.put(modelID, slot);
    }

    public int getHand(String modelID)
    {
        if (hands.containsKey(modelID))
            return hands.get(modelID);

        return -1;
    }

    public void addQuad(String quadID)
    {
        quadIDs.add(quadID);
    }

    public void addFirstPersonModel(String modelID)
    {
        firstPersonModelIDs.add(modelID);
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

    public String getDescription()
    {
        return new TranslationTextComponent(displayNamePosition + ".description").getString();
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

    public ArrayList<String> getQuadIDs()
    {
        return quadIDs;
    }

    public ArrayList<String> getFirstPersonModelIDs()
    {
        return firstPersonModelIDs;
    }

    public void addNeededPart(String partID)
    {
        neededToForge.add(partID);
    }

    public void addNotNeededPart(String partID)
    {
        neededNotToForge.add(partID);
    }


    public void addNeededPosition(String positionID, String subpositionID)
    {
        neededPositionToForge.add(new Pair(positionID, subpositionID));
    }
    public void addNeedNotPosition(String positionID, String subpositionID)
    {
        needNotPositionToForge.add(new Pair(positionID, subpositionID));
    }

    public void addNeededTags(String tag)
    {
        neededTags.add(tag);
    }
    public void addNeedNotTags(String tag)
    {
        neededNotTags.add(tag);
    }

    public void addUniqueTag(String tag)
    {
        uniqueTags.add(tag);
    }

    public ArrayList<String> getUniqueTags()
    {
        return uniqueTags;
    }

    public void onLoad(UUID playerID)
    {
        for (Map.Entry<String, ResourceLocation> entry : textureChanges.entrySet())
            TextureManager.addTexture(playerID, entry.getKey(), entry.getValue());
    }

    public void onClientTick(ClientPlayerEntity player)
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

        if (hasNotNeededPositions(modifications))
            return false;

        if (!hasNeededParts(modifications))
            return false;

        if (hasNotNeededParts(modifications))
            return false;

        if (hasDuplicateTags(modifications))
            return false;

        if (!hasNeededTags(modifications))
            return false;

        if (hasNotNeededTags(modifications))
            return false;

        // Loop through all player body modifications, return false if a modification for this position already exists
        for (BodyPart part : modifications.getModifications().values())
            if (part.getPosition().compareTo(limbPosition) == 0)
                return false;

        return true;
    }

    protected boolean hasDuplicateTags(IBodyModifications modifications)
    {
        for (String tag : uniqueTags)
            if (modifications.getTags().contains(tag))
                return true;

        return false;
    }

    protected boolean hasNeededTags(IBodyModifications modifications)
    {
        for (String tag : neededTags)
            if (!modifications.getTags().contains(tag))
                return false;

        return true;
    }

    protected boolean hasNotNeededTags(IBodyModifications modifications)
    {
        for (String tag : neededNotTags)
            if (modifications.getTags().contains(tag))
                return true;

        return false;
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

    protected boolean hasNotNeededPositions(IBodyModifications modifications)
    {
        // Loop through all positions needed to not be able to forge this part
        for (Pair<String, String> position : needNotPositionToForge)
        {
            // If the subposition is basePosition then check if there is a modification at this position
            if (position.getValue().compareTo(BodyPartNames.basePosition) == 0)
            {
                if (modifications.hasModification(position.getKey()))
                    return true;
            }
            // Otherwise check if there is a modification option at this subposition
            else if (modifications.hasOption(position.getKey(), position.getValue()))
                return true;
        }

        return false;
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

    protected boolean hasNotNeededParts(IBodyModifications modifications)
    {
        // Check if the player has any body parts that are needed to not have to forge this part
        for (String testPart : neededNotToForge)
        {
            BodyPart test = BodyPartNames.getPart(testPart);

            if (test != null)
                return true;

            test = BodyPartNames.getOption(testPart);

            if (modifications.hasOption(test.getPosition(), ((BodyPartOption) test).getSubPosition(), test.ID))
                return true;

            if (modifications.hasModification(test.getPosition(), test.ID))
                return true;
        }

        return false;
    }

    // Send an int info packet to other clients
    // Client only, only if owner of bodyPart
    public void sendInfo(int info, String partID, String limbID)
    {
        ClientPacketHandler.sendPartInfoToServer(Minecraft.getInstance().player.getUUID(), info, partID, limbID);
    }

    // Process a received int info packet
    public void processInfo(PlayerEntity player, int info)
    {

    }
}
