package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.GenericLimbNames;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.MultiLimbedModel;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.PlayerPoseHandler;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.PoseHandler;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

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
                    newModel.removeLimb(GenericLimbNames.lowerRightLeg);
                    newModel.removeLimb(GenericLimbNames.lowerLeftArm);
                }
                else if (part.getPosition().equalsIgnoreCase(BodyPartNames.legPosition))
                {
                    newModel.removeLimb(GenericLimbNames.rightLeg);
                    newModel.removeLimb(GenericLimbNames.leftLeg);
                    newModel.removeLimb(GenericLimbNames.lowerRightLeg);
                    newModel.removeLimb(GenericLimbNames.lowerLeftLeg);
                }

                for (String modelID : part.getModelIDs())
                    newModel.addLimb(part.getPosition(), BodyPartList.getModel(player.getUniqueID(), modelID));
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
