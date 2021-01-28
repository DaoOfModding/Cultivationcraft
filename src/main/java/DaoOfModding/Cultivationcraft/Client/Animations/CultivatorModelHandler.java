package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.*;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import javafx.util.Pair;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Map;

public class CultivatorModelHandler
{
    // Update player model based on the supplied BodyModifications
    public static void updateModel(PlayerRenderer renderer, PlayerEntity player, IBodyModifications modifications)
    {
        PlayerPoseHandler handler = PoseHandler.getPlayerPoseHandler(player.getUniqueID());

        if (handler != null)
        {
            // Create a new player model with the specified modifications
            MultiLimbedModel newModel = new MultiLimbedModel(renderer.getEntityModel());

            for (BodyPart part : modifications.getModifications().values())
            {
                // Remove arms and legs if the they have been replaced
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

                for (String modelID  : part.getModelIDs())
                {
                    newModel.addLimb(modelID, BodyPartModels.getModel(modelID));

                    for (Map.Entry<String, ExtendableModelRenderer> entry : BodyPartModels.getReferences(modelID).entrySet())
                        newModel.addLimbReference(entry.getKey(), entry.getValue());

                    // Add models for any valid options to this body part
                    for (BodyPartOption option : modifications.getModificationOptions(part.getPosition()).values())
                        for (String optionModels : option.getDefaultOptionModels())
                        {
                            newModel.addLimb(optionModels, BodyPartModels.getModel(optionModels), newModel.getLimb(modelID));

                            for (Map.Entry<String, ExtendableModelRenderer> entry : BodyPartModels.getReferences(optionModels).entrySet())
                                newModel.addLimbReference(entry.getKey(), entry.getValue());
                        }
                }

                // Add models for any options tied to specific body parts to this body part
                for (BodyPartOption option : modifications.getModificationOptions(part.getPosition()).values())
                    for (Map.Entry<String, ArrayList<String>> optionModelCollections : option.getOptionModels().entrySet())
                    {
                        // Get the ID of the model this model list is connected to
                        String baseModelID = optionModelCollections.getKey();
                        ExtendableModelRenderer baseModel = newModel.getLimb(baseModelID);

                        // If the model this collection is tied to exists
                        if (baseModel != null)
                        {
                            for (String optionModels : optionModelCollections.getValue())
                            {
                                newModel.addLimb(optionModels, BodyPartModels.getModel(optionModels), baseModel);

                                for (Map.Entry<String, ExtendableModelRenderer> entry : BodyPartModels.getReferences(optionModels).entrySet())
                                    newModel.addLimbReference(entry.getKey(), entry.getValue());
                            }
                        }
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
