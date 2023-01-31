package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartLocation;
import DaoOfModding.Cultivationcraft.Client.Textures.TextureList;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import DaoOfModding.mlmanimator.Client.Poses.Arm;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPoseHandler;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BodyPart
{
    protected String ID;
    protected ArrayList<String> modelIDs = new ArrayList<String>();
    protected ArrayList<String> quadIDs = new ArrayList<String>();
    protected ArrayList<String> firstPersonModelIDs = new ArrayList<String>();
    protected String limbPosition;
    protected String displayNamePosition;
    protected PlayerStatModifications stats;
    protected String textureID = TextureList.skin;
    protected boolean texturesUpdated = false;
    protected Quest quest;

    protected BodyPartLocation connection = null;

    protected String viewPoint;

    protected ArrayList<String> neededToForge = new ArrayList<String>();
    protected ArrayList<String> neededNotToForge = new ArrayList<String>();
    protected ArrayList<Pair<String, String>> neededPositionToForge = new ArrayList<Pair<String, String>>();
    protected ArrayList<Pair<String, String>> needNotPositionToForge = new ArrayList<Pair<String, String>>();
    protected ArrayList<String> neededTags = new ArrayList<String>();
    protected ArrayList<String> neededNotTags = new ArrayList<String>();

    protected ArrayList<String> uniqueTags = new ArrayList<String>();

    protected HashMap<String, Integer> hands = new HashMap<String, Integer>();
    protected ArrayList<Arm> arms = new ArrayList<Arm>();

    protected HashMap<String, ResourceLocation> textureChanges = new HashMap<String, ResourceLocation>();
    protected HashMap<String, Vec3> textureColorChanges = new HashMap<String, Vec3>();

    protected ResourceLocation element = null;

    public BodyPart(String partID, String position, String displayNamePos)
    {
        ID = partID;

        limbPosition = position;
        displayNamePosition = displayNamePos;

        stats = new PlayerStatModifications();

        quest = new Quest(Quest.TIME_ALIVE, 0);
    }

    public void setQuest(Quest newQuest)
    {
        quest = newQuest;
    }

    public void setElement(ResourceLocation newElement)
    {
        element = newElement;
    }

    public ResourceLocation getElement()
    {
        return element;
    }

    public Quest getQuest()
    {
        return quest;
    }

    public void setConnection(BodyPartLocation newConnection)
    {
        connection = newConnection;
    }

    public BodyPartLocation getConnection()
    {
        return connection;
    }

    public boolean hasConnection()
    {
        if (connection != null)
            return true;

        return false;
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

    public void addArm(Arm newArm)
    {
        arms.add(newArm);
    }

    public ArrayList<Arm> getArms()
    {
        return arms;
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

    public void addTextureColorChange(String textureID, Vec3 textureColor)
    {
        textureColorChanges.put(textureID, textureColor);
    }
    public void addTextureColorChange(String textureID, Color color)
    {
        textureColorChanges.put(textureID, new Vec3(color.getRed() * 255, color.getGreen() * 255, color.getBlue() * 255));
    }

    public PlayerStatModifications getStatChanges()
    {
        return stats;
    }

    public String getDisplayName()
    {
        return Component.translatable(displayNamePosition).getString();
    }

    public String getDescription()
    {
        return Component.translatable(displayNamePosition + ".description").getString();
    }

    public String getID()
    {
        return ID;
    }

    public String getPosition()
    {
        return limbPosition;
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

    // Maybe not be being called...?
    public void onLoad(UUID playerID)
    {

    }

    public void updateTextures(UUID playerID)
    {
        PlayerPoseHandler handler = PoseHandler.getPlayerPoseHandler(playerID);

        if (handler == null)
            return;

        for (Map.Entry<String, ResourceLocation> entry : textureChanges.entrySet())
            handler.getPlayerModel().getTextureHandler().addTexture(entry.getKey(), entry.getValue());

        for (Map.Entry<String, Vec3> entry : textureColorChanges.entrySet())
            handler.getPlayerModel().getTextureHandler().addColor(entry.getKey(), entry.getValue());

        texturesUpdated = true;
    }

    public void onClientTick(Player player)
    {
        if (!texturesUpdated)
            updateTextures(player.getUUID());
    }

    public void onServerTick(Player player)
    {

    }

    public boolean canBeForged(Player player)
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

        if (!isInCorrectElement(player))
            return false;

        return true;
    }

    protected boolean isInCorrectElement(Player player)
    {
        if (element == null)
            return true;

        List<QiSource> sources = ChunkQiSources.getQiSourcesInRange(player.level, player.position(), (int) BodyPartStatControl.getPlayerStatControl(player.getUUID()).getStats().getStat(StatIDs.qiAbsorbRange));

        for (QiSource source : sources)
            if (source.getElement().compareTo(element) == 0)
                return true;

        return false;
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
            if (position.getSecond().compareTo(BodyPartNames.basePosition) == 0)
            {
                if (!modifications.hasModification(position.getFirst()))
                    return false;
            }
            // Otherwise check if there is a modification option at this subposition
            else if (!modifications.hasOption(position.getFirst(), position.getSecond()))
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
            if (position.getSecond().compareTo(BodyPartNames.basePosition) == 0)
            {
                if (modifications.hasModification(position.getFirst()))
                    return true;
            }
            // Otherwise check if there is a modification option at this subposition
            else if (modifications.hasOption(position.getFirst(), position.getSecond()))
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
            {
                if (modifications.hasModification(test.limbPosition, test.ID))
                    return true;
            }
            else
            {
                test = BodyPartNames.getOption(testPart);

                if (modifications.hasOption(test.getPosition(), ((BodyPartOption) test).getSubPosition(), test.ID))
                    return true;
            }
        }

        return false;
    }

    // Send an int info packet to other clients
    // Client only, only if owner of bodyPart
    public void sendInfo(int info, String partID, String limbID)
    {
        ClientPacketHandler.sendPartInfoToServer(genericClientFunctions.getPlayer().getUUID(), info, partID, limbID);
    }

    // Process a received int info packet
    public void processInfo(Player player, int info)
    {

    }

    // Calls this when "player" joins the server
    public void onJoin(Player player)
    {

    }
}
