package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Models.Quads.Quad;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPoseHandler;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import DaoOfModding.mlmanimator.Client.Models.*;
import DaoOfModding.Cultivationcraft.Client.Textures.TextureManager;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CultivatorModelHandler
{
    private static HashMap<UUID, BodyPartModels> models = new HashMap<UUID, BodyPartModels>();

    public static BodyPartModels getPlayerModels(UUID playerID)
    {
        if (!models.containsKey(playerID))
            models.put(playerID, new BodyPartModels(playerID));

        return models.get(playerID);
    }

    public static void updateModifications(AbstractClientPlayerEntity player)
    {
        IBodyModifications modifications = BodyModifications.getBodyModifications(player);

        if (!modifications.hasUpdated())
        {
            // Update the model textures to be using the players textures before adding them to the players model
            TextureManager.updateTextures(player);
            updateModel(player, modifications);

            // If player hasn't loaded their skin, then try to update again next tick
            if (!player.isSkinLoaded())
                modifications.setUpdated(false);
        }
    }

    // Update player model based on the supplied BodyModifications
    public static void updateModel(AbstractClientPlayerEntity player, IBodyModifications modifications)
    {
        PlayerRenderer renderer = (PlayerRenderer)Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
        PlayerPoseHandler handler = PoseHandler.getPlayerPoseHandler(player.getUUID());

        if (handler != null)
        {
            BodyPartModels models = getPlayerModels(player.getUUID());

            // Create a new player model with the specified modifications
            MultiLimbedModel newModel = new MultiLimbedModel(renderer.getModel());

            for (BodyPart part : modifications.getModifications().values())
            {
                // Remove vanilla body parts if the they have been replaced
                if (part.getPosition().equalsIgnoreCase(BodyPartNames.armPosition))
                {
                    newModel.removeLimb(GenericLimbNames.rightArm);
                    newModel.removeLimb(GenericLimbNames.leftArm);
                    newModel.removeLimb(GenericLimbNames.lowerRightArm);
                    newModel.removeLimb(GenericLimbNames.lowerLeftArm);
                }
                else if (part.getPosition().equalsIgnoreCase(BodyPartNames.legPosition))
                {
                    newModel.removeLimb(GenericLimbNames.rightLeg);
                    newModel.removeLimb(GenericLimbNames.leftLeg);
                    newModel.removeLimb(GenericLimbNames.lowerRightLeg);
                    newModel.removeLimb(GenericLimbNames.lowerLeftLeg);
                }
                else if (part.getPosition().equalsIgnoreCase(BodyPartNames.headPosition))
                    newModel.removeLimb(GenericLimbNames.head);

                for (String modelID : part.getModelIDs())
                {
                    ExtendableModelRenderer modelPart = models.getModel(modelID);

                    // Set as the body if this is a body part
                    if (part.getPosition().equalsIgnoreCase(BodyPartNames.bodyPosition))
                        newModel.addBody(modelPart);
                    else
                        newModel.addLimb(modelID, modelPart);

                    // Set this as the players view point if it is set to be a view point
                    if (modelID == part.getViewPoint())
                        newModel.setViewPoint(modelPart);

                    for (Map.Entry<String, ExtendableModelRenderer> entry : models.getReferences(modelID).entrySet())
                        newModel.addLimbReference(entry.getKey(), entry.getValue());

                    if (part.getHand(modelID) != -1)
                        newModel.setHand(part.getHand(modelID), newModel.getLimb(modelID));

                    // Add models for any valid options to this body part
                    for (BodyPartOption option : modifications.getModificationOptions(part.getPosition()).values())
                    {
                        for (String optionModels : option.getDefaultOptionModels())
                        {
                            // Add to the body if the base part is a body part, otherwise reference the base modelID
                            if (part.getPosition().equalsIgnoreCase(BodyPartNames.bodyPosition))
                                newModel.addLimb(optionModels, models.getModel(optionModels));
                            else
                                newModel.addLimb(optionModels, models.getModel(optionModels), modelID);

                            for (Map.Entry<String, ExtendableModelRenderer> entry : models.getReferences(optionModels).entrySet())
                                newModel.addLimbReference(entry.getKey(), entry.getValue());

                            if (option.getHand(optionModels) != -1)
                                newModel.setHand(option.getHand(optionModels), newModel.getLimb(optionModels));
                        }

                        for (String quadID : option.getQuadIDs())
                        {
                            QuadCollection collection = models.getQuadCollection(quadID);

                            for (Quad quad : collection.getQuads())
                                newModel.getBody().addQuad(quad);
                        }
                    }
                }

                for (String modelID : part.getFirstPersonModelIDs())
                {
                    ExtendableModelRenderer modelPart = models.getModel(modelID);
                    newModel.addFirstPersonLimb(modelID, modelPart);

                    for (Map.Entry<String, ExtendableModelRenderer> entry : models.getReferences(modelID).entrySet())
                        newModel.addLimbReference(entry.getKey(), entry.getValue());

                    if (part.getHand(modelID) != -1)
                        newModel.setHand(part.getHand(modelID), newModel.getLimb(modelID));

                    // Add models for any valid options to this body part
                    for (BodyPartOption option : modifications.getModificationOptions(part.getPosition()).values())
                        for (String optionModels : option.getDefaultOptionModels())
                        {
                            newModel.addLimb(optionModels, models.getModel(optionModels), modelID);

                            for (Map.Entry<String, ExtendableModelRenderer> entry : models.getReferences(optionModels).entrySet())
                                newModel.addLimbReference(entry.getKey(), entry.getValue());

                            if (option.getHand(optionModels) != -1)
                                newModel.setHand(option.getHand(optionModels), newModel.getLimb(optionModels));
                        }
                }

                // Add models for any options tied to specific body parts to this body part
                for (BodyPartOption option : modifications.getModificationOptions(part.getPosition()).values())
                    for (Map.Entry<String, ArrayList<String>> optionModelCollections : option.getOptionModels().entrySet())
                    {
                        // Get the ID of the model this model list is connected to
                        String baseModelID = optionModelCollections.getKey();

                        // If the model this collection is tied to exists
                        if (newModel.hasLimb(baseModelID))
                        {
                            for (String optionModels : optionModelCollections.getValue())
                            {
                                newModel.addLimb(optionModels, models.getModel(optionModels), baseModelID);

                                for (Map.Entry<String, ExtendableModelRenderer> entry : models.getReferences(optionModels).entrySet())
                                    newModel.addLimbReference(entry.getKey(), entry.getValue());

                                if (option.getHand(optionModels) != -1)
                                    newModel.setHand(option.getHand(optionModels), newModel.getLimb(optionModels));
                            }
                        }
                    }


                for (String quadID : part.getQuadIDs())
                {
                    QuadCollection collection = models.getQuadCollection(quadID);

                    for (Quad quad : collection.getQuads())
                        newModel.getBody().addQuad(quad);
                }

            }

            // Lock the handler so it can be modified without other threads messing with it
            handler.lock();

            // Update the player model with the new one
            handler.setPlayerModel(newModel);

            // Unlock the handler
            handler.unlock();

            modifications.setUpdated(true);
        }
    }
}
