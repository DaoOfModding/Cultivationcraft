package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Client.Textures.TextureList;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.mlmanimator.Client.AnimationFramework.AnimationSpeedCalculator;
import DaoOfModding.mlmanimator.Client.Models.Quads.Quad;
import DaoOfModding.mlmanimator.Client.Poses.Arm;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPoseHandler;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import DaoOfModding.mlmanimator.Client.Models.*;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class CultivatorModelHandler
{
    protected static HashMap<UUID, BodyPartModels> models = new HashMap<UUID, BodyPartModels>();

    public static BodyPartModels getPlayerModels(UUID playerID)
    {
        if (!models.containsKey(playerID))
            models.put(playerID, new BodyPartModels(playerID));

        return models.get(playerID);
    }

    public static void resetPlayerModels(UUID playerID)
    {
        models.put(playerID, new BodyPartModels(playerID));
    }

    public static void updateModifications(AbstractClientPlayer player)
    {
        IBodyModifications modifications = BodyModifications.getBodyModifications(player);

        if (!modifications.hasUpdated())
            updateModel(player, modifications);
    }

    // Update player model based on the supplied BodyModifications
    public static void updateModel(AbstractClientPlayer player, IBodyModifications modifications)
    {
        PlayerRenderer renderer = (PlayerRenderer)Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
        PlayerPoseHandler handler = PoseHandler.getPlayerPoseHandler(player.getUUID());

        if (handler != null)
        {
            // Reset the player models to fix weird bugs from previous iterations
            resetPlayerModels(player.getUUID());
            BodyPartModels models = getPlayerModels(player.getUUID());

            // Create a new player model with the specified modifications
            MultiLimbedModel newModel = new MultiLimbedModel(renderer.getModel());
            newModel.setTextureHandler(handler.getPlayerModel().getTextureHandler());
            newModel.getTextureHandler().clearTextures();
            TextureList.updateTextures(newModel.getTextureHandler());

            Collection<BodyPart> parts = modifications.getModifications().values();
            HashMap<String, BodyPartLocation> partLocations = getPartLocations(parts, modifications);

            processParts(newModel, parts, models, modifications, partLocations, handler);

            PlayerPose defaultSize = new PlayerPose();



            // Update textures from body parts
            for (BodyPart part : parts)
                part.updateTextures(newModel.getTextureHandler());

            for (HashMap<String, BodyPartOption> options : modifications.getModificationOptions().values())
                for (BodyPartOption option : options.values())
                    option.updateTextures(newModel.getTextureHandler());

            PlayerStatModifications stats = BodyPartStatControl.getStats(player);

            // Lock the handler so it can be modified without other threads messing with it
            handler.lock();

            // Resize the new body and the handler to be the correct size
            float size = stats.getStat(StatIDs.size);
            float width = stats.getStat(StatIDs.width);

            handler.resize(new Vec3(size + width, size, size + width));
            newModel.getBody().addToResizeForThisAndChildren(new Vec3(size + width, size, size + width));

            // Ensure the head is looking in the correct direction
            newModel.getViewPoint().getModelPart().xRot = handler.getPlayerModel().getViewPoint().getModelPart().xRot;
            newModel.getViewPoint().getModelPart().yRot = handler.getPlayerModel().getViewPoint().getModelPart().yRot;
            newModel.getViewPoint().getModelPart().zRot = handler.getPlayerModel().getViewPoint().getModelPart().zRot;
            newModel.setupLookVector(handler.getPlayerModel().getLookVector());

            newModel.getLimb(GenericLimbNames.leftWingElytra).getModelPart().visible = handler.getPlayerModel().getLimb(GenericLimbNames.leftWingElytra).getModelPart().visible;
            newModel.getLimb(GenericLimbNames.rightWingElytra).getModelPart().visible = handler.getPlayerModel().getLimb(GenericLimbNames.rightWingElytra).getModelPart().visible;
            newModel.getLimb(GenericLimbNames.cloak).getModelPart().visible = handler.getPlayerModel().getLimb(GenericLimbNames.cloak).getModelPart().visible;

            // Update the player model with the new one
            handler.setPlayerModel(newModel);
            handler.setCrawling(false);

            // Unlock the handler
            handler.unlock();

            modifications.setUpdated(true);
        }
    }

    // Return list of what part every part supplied connects to
    public static HashMap<String, BodyPartLocation> getPartLocations(Collection<BodyPart> parts, IBodyModifications modifications)
    {
        ArrayList<BodyPartLocation> partLocations = new ArrayList<>();

        // Loop through every part, add it's connection to the arraylist if it has one
        for (BodyPart part : parts)
        {
            if (part.hasConnection())
                partLocations.add(part.getConnection());

            for (BodyPartOption option : modifications.getModificationOptions(part.getPosition()).values())
                if (option.hasConnection())
                    partLocations.add(option.getConnection());
        }

        HashMap<String, BodyPartLocation> connectionList = new HashMap<>();

        for (BodyPart part: parts)
        {
            connectionList.put(part.getID(), getPartConnection(part, partLocations, parts, modifications));

            for (BodyPartOption option : modifications.getModificationOptions(part.getPosition()).values())
                connectionList.put(option.getID(), getPartConnection(option, partLocations, parts, modifications));
        }

        return connectionList;
    }

    protected static BodyPart getPartAtLocation(Collection<BodyPart> parts, String position, String subPosition, IBodyModifications modifications)
    {
        if (subPosition.equalsIgnoreCase(BodyPartNames.basePosition))
            for (BodyPart part : parts)
                if (part.getPosition().equalsIgnoreCase(position))
                    return part;

        for (BodyPartOption option : modifications.getModificationOptions(position).values())
            if (option.getSubPosition().equalsIgnoreCase(subPosition))
                return option;

        return null;
    }

    protected static BodyPartLocation getPartConnection(BodyPart part, ArrayList<BodyPartLocation> partLocations, Collection<BodyPart> parts, IBodyModifications modifications)
    {
        // If this BodyPart has an associated connection
        BodyPartLocation location = getLocation(part, partLocations);

        if (location != null)
        {
            if (location.modelID == null)
            {
                BodyPart connectedPart = getPartAtLocation(parts, location.modelPositionID, location.modelPositionSubID, modifications);

                BodyPartLocation newLocation = new BodyPartLocation(null, null, connectedPart.getModelIDs().get(0));

                if (location.modelPositionID == BodyPartNames.bodyPosition)
                    newLocation.modelID = "Body";

                newLocation.adjustDepth(location.rotationDepth);
                newLocation.adjustPos(location.posOverride);
                newLocation.adjustRotationPoint(location.rotationpointOverride);
                newLocation.adjustFixedPos(location.fixedPosOverride);

                return  newLocation;
            }

            return location;
        }
        else
        {
            return new BodyPartLocation(null, null, "Body");
        }
    }

    // Return BodyPartLocation referring to the specified part in the supplied list of locations
    public static BodyPartLocation getLocation(BodyPart part, ArrayList<BodyPartLocation> Locations)
    {
        String location = part.getPosition();
        String subLocation = BodyPartNames.basePosition;

        if (part instanceof BodyPartOption)
            subLocation = ((BodyPartOption)part).getSubPosition();

        for (BodyPartLocation BPLocation : Locations)
        {
            if (BPLocation.positionID == location)
                if (BPLocation.positionSubID == subLocation)
                    return BPLocation;
        }

        return null;
    }

    protected static void addCultivatorLayers(MultiLimbedModel model)
    {
        // Add cultivator layers to generic body parts
        GenericCultivatorTextureValues.addCultivatorHeadLayers(model.getLimb(GenericLimbNames.head), 0, 0);
        model.getLimb(GenericLimbNames.head).generateCube();
        GenericCultivatorTextureValues.addCultivatorBodyLayers(model.getLimb(GenericLimbNames.body), 0, 0);
        model.getLimb(GenericLimbNames.body).generateCube();

        if (model.getLimb(GenericLimbNames.rightArm) != null)
        {
            GenericCultivatorTextureValues.addCultivatorRightArmLayers(model.getLimb(GenericLimbNames.rightArm), 0, 0);
            model.getLimb(GenericLimbNames.rightArm).generateCube();
        }

        if (model.getLimb(GenericLimbNames.leftArm) != null)
        {
            GenericCultivatorTextureValues.addCultivatorLeftArmLayers(model.getLimb(GenericLimbNames.leftArm), 0, 0);
            model.getLimb(GenericLimbNames.leftArm).generateCube();
        }

        if (model.getLimb(GenericLimbNames.lowerLeftArm) != null)
        {
            model.getLimb(GenericLimbNames.lowerLeftArm).addLayer(new UVPair(GenericTextureValues.leftArm.u(), GenericTextureValues.leftArm.v() + 6), GenericTextureValues.skin_Size, 0.0F, TextureList.skin, false, 4, Direction.UP);
            model.getLimb(GenericLimbNames.lowerLeftArm).addLayer(new UVPair(GenericTextureValues.leftSleeve.u(), GenericTextureValues.leftSleeve.v() + 6), GenericTextureValues.skin_Size, 0.5F, TextureList.skin, false, 4, Direction.UP);
            model.getLimb(GenericLimbNames.lowerLeftArm).generateCube();
        }

        if (model.getLimb(GenericLimbNames.lowerRightArm) != null)
        {
            model.getLimb(GenericLimbNames.lowerRightArm).addLayer(new UVPair(GenericTextureValues.rightArm.u(), GenericTextureValues.rightArm.v() + 6), GenericTextureValues.skin_Size, 0.0F, TextureList.skin, false, 4, Direction.UP);
            model.getLimb(GenericLimbNames.lowerRightArm).addLayer(new UVPair(GenericTextureValues.rightSleeve.u(), GenericTextureValues.rightSleeve.v() + 6), GenericTextureValues.skin_Size, 0.5F, TextureList.skin, false, 4, Direction.UP);
            model.getLimb(GenericLimbNames.lowerRightArm).generateCube();
        }

        if (model.getLimb(GenericLimbNames.rightLeg) != null)
        {
            GenericCultivatorTextureValues.addCultivatorRightLegLayers(model.getLimb(GenericLimbNames.rightLeg), 0, 0);
            model.getLimb(GenericLimbNames.rightLeg).generateCube();
        }

        if (model.getLimb(GenericLimbNames.leftLeg) != null)
        {
            GenericCultivatorTextureValues.addCultivatorLeftLegLayers(model.getLimb(GenericLimbNames.leftLeg), 0, 0);
            model.getLimb(GenericLimbNames.leftLeg).generateCube();
        }

        if (model.getLimb(GenericLimbNames.lowerRightLeg) != null)
        {
            model.getLimb(GenericLimbNames.lowerRightLeg).addLayer(new UVPair(GenericTextureValues.rightLeg.u(), GenericTextureValues.rightLeg.v() + 6), GenericTextureValues.skin_Size, 0.0F, TextureList.skin, false, 4);
            model.getLimb(GenericLimbNames.lowerRightLeg).addLayer(new UVPair(GenericTextureValues.rightPants.u(), GenericTextureValues.rightPants.v() + 6), GenericTextureValues.skin_Size, 0.5F, TextureList.skin, false, 4);
            model.getLimb(GenericLimbNames.lowerRightLeg).generateCube();
        }

        if (model.getLimb(GenericLimbNames.lowerLeftLeg) != null)
        {
            model.getLimb(GenericLimbNames.lowerLeftLeg).addLayer(new UVPair(GenericTextureValues.leftLeg.u(), GenericTextureValues.leftLeg.v() + 6), GenericTextureValues.skin_Size, 0.0F, TextureList.skin, false, 4);
            model.getLimb(GenericLimbNames.lowerLeftLeg).addLayer(new UVPair(GenericTextureValues.leftPants.u(), GenericTextureValues.leftPants.v() + 6), GenericTextureValues.skin_Size, 0.5F, TextureList.skin, false, 4);
            model.getLimb(GenericLimbNames.lowerLeftLeg).generateCube();
        }
    }

    protected static void processParts (MultiLimbedModel model, Collection<BodyPart> parts, BodyPartModels models, IBodyModifications modifications, HashMap<String, BodyPartLocation> partLocations, PlayerPoseHandler handler)
    {
        ArrayList<BodyPart> unprocessedParts = new ArrayList<BodyPart>();

        addCultivatorLayers(model);

        // Try to add all parts in the provided collection to the model
        for (BodyPart part : parts)
        {
            if (!processPart(model, part, models, modifications, partLocations.get(part.getID()), handler))
                unprocessedParts.add(part);
        }

        // Try to add any parts that were not added
        if (unprocessedParts.size() > 0)
            processParts(model, unprocessedParts, models, modifications, partLocations, handler);
    }

    public static boolean processPart(MultiLimbedModel model, BodyPart part, BodyPartModels models, IBodyModifications modifications, BodyPartLocation connectTo, PlayerPoseHandler handler)
    {
        // Don't add this part if the part it connects to has not yet been added to the model
        if (!model.hasLimb(connectTo.modelID))
            return false;

        // Remove vanilla body parts if the they have been replaced
        if (part.getPosition().equalsIgnoreCase(BodyPartNames.armPosition))
        {
            model.removeLimb(GenericLimbNames.rightArm);
            model.removeLimb(GenericLimbNames.leftArm);
            model.removeLimb(GenericLimbNames.lowerRightArm);
            model.removeLimb(GenericLimbNames.lowerLeftArm);

            model.setLeftShoulder(null);
            model.setRightShoulder(null);
            model.setHand(0, null);
            model.setHand(1, null);

            handler.removeArms();
        }
        else if (part.getPosition().equalsIgnoreCase(BodyPartNames.legPosition))
        {
            model.removeLimb(GenericLimbNames.rightLeg);
            model.removeLimb(GenericLimbNames.leftLeg);
            model.removeLimb(GenericLimbNames.lowerRightLeg);
            model.removeLimb(GenericLimbNames.lowerLeftLeg);
        }
        else if (part.getPosition().equalsIgnoreCase(BodyPartNames.headPosition))
            model.removeLimb(GenericLimbNames.head);

        for (String modelID : part.getModelIDs())
        {
            ExtendableModelRenderer modelPart = models.getModel(modelID, part.getPosition());

            if (connectTo.rotationDepth > 0)
                modelPart.setRotationDepth(connectTo.rotationDepth);

            if (connectTo.posOverride != null)
                modelPart.setPos((float)connectTo.posOverride.x, (float)connectTo.posOverride.y, (float)connectTo.posOverride.z);

            if (connectTo.rotationpointOverride != null)
                modelPart.setRotationPoint(connectTo.rotationpointOverride);

            if (connectTo.fixedPosOverride != null)
                modelPart.setFixedPosAdjustment((float)connectTo.fixedPosOverride.x, (float)connectTo.fixedPosOverride.y, (float)connectTo.fixedPosOverride.z);

            // Regenerate the cube in case it's size or rotation points have changed
            modelPart.generateCube();

            // Set as the body if this is a body part
            if (part.getPosition().equalsIgnoreCase(BodyPartNames.bodyPosition))
                model.addBody(modelPart);
            else
                // TODO: Apply this for bodypart options as well?
                model.addLimb(modelID, modelPart, connectTo.modelID);

            for (Map.Entry<String, ExtendableModelRenderer> entry : models.getReferences(modelID).entrySet())
                model.addLimbReference(entry.getKey(), entry.getValue());

            for (Arm arm : part.getArms())
            {
                if (arm.hand == InteractionHand.MAIN_HAND && model.getHand(0) == null)
                {
                    model.setHand(0, model.getLimb(arm.lowerLimb));
                    model.setRightShoulder(model.getLimb(arm.upperLimb));
                }
                else if (arm.hand == InteractionHand.OFF_HAND && model.getHand(1) == null)
                {
                    model.setHand(1, model.getLimb(arm.lowerLimb));
                    model.setLeftShoulder(model.getLimb(arm.upperLimb));
                }

                handler.addArm(arm);
            }

            // Add models for any valid options to this body part
            for (BodyPartOption option : modifications.getModificationOptions(part.getPosition()).values())
            {
                for (String optionModels : option.getDefaultOptionModels())
                {
                    // Add to the body if the base part is a body part, otherwise reference the base modelID
                    if (part.getPosition().equalsIgnoreCase(BodyPartNames.bodyPosition))
                        model.addLimb(optionModels, models.getModel(optionModels, option.getSubPosition()));
                    else
                        model.addLimb(optionModels, models.getModel(optionModels, option.getSubPosition()), modelID);

                    for (Map.Entry<String, ExtendableModelRenderer> entry : models.getReferences(optionModels).entrySet())
                        model.addLimbReference(entry.getKey(), entry.getValue());

                    for (Arm arm : option.getArms())
                    {
                        if (arm.hand == InteractionHand.MAIN_HAND && model.getHand(0) == null)
                        {
                            model.setHand(0, model.getLimb(arm.lowerLimb));
                            model.setRightShoulder(model.getLimb(arm.upperLimb));
                        }
                        else if (arm.hand == InteractionHand.OFF_HAND && model.getHand(1) == null)
                        {
                            model.setHand(1, model.getLimb(arm.lowerLimb));
                            model.setLeftShoulder(model.getLimb(arm.upperLimb));
                        }

                        handler.addArm(arm);
                    }
                }

                for (String quadID : option.getQuadIDs())
                {
                    QuadCollection collection = models.getQuadCollection(quadID);

                    for (Quad quad : collection.getQuads())
                        model.getBody().addQuad(quad);
                }
            }
        }

        for (String modelID : part.getFirstPersonModelIDs())
        {
            ExtendableModelRenderer modelPart = models.getModel(modelID);
            model.addFirstPersonLimb(modelID, modelPart);

            for (Map.Entry<String, ExtendableModelRenderer> entry : models.getReferences(modelID).entrySet())
                model.addLimbReference(entry.getKey(), entry.getValue());
        }

        // Add models for any options tied to specific body parts to this body part
        for (BodyPartOption option : modifications.getModificationOptions(part.getPosition()).values())
            for (Map.Entry<String, ArrayList<String>> optionModelCollections : option.getOptionModels().entrySet()) {
                // Get the ID of the model this model list is connected to
                String baseModelID = optionModelCollections.getKey();

                // If the model this collection is tied to exists
                if (model.hasLimb(baseModelID)) {
                    for (String optionModels : optionModelCollections.getValue()) {
                        model.addLimb(optionModels, models.getModel(optionModels), baseModelID);

                        for (Map.Entry<String, ExtendableModelRenderer> entry : models.getReferences(optionModels).entrySet())
                            model.addLimbReference(entry.getKey(), entry.getValue());

                        for (Arm arm : option.getArms()) {
                            if (arm.hand == InteractionHand.MAIN_HAND && model.getHand(0) == null) {
                                model.setHand(0, model.getLimb(arm.lowerLimb));
                                model.setRightShoulder(model.getLimb(arm.upperLimb));
                            } else if (arm.hand == InteractionHand.OFF_HAND && model.getHand(1) == null) {
                                model.setHand(1, model.getLimb(arm.lowerLimb));
                                model.setLeftShoulder(model.getLimb(arm.upperLimb));
                            }

                            handler.addArm(arm);
                        }
                    }
                }
            }


        for (String quadID : part.getQuadIDs())
        {
            QuadCollection collection = models.getQuadCollection(quadID);

            for (Quad quad : collection.getQuads())
                model.getBody().addQuad(quad);
        }

        // Set this as the players view point if it is set to be a view point
        if (part.getViewPoint() != null)
            model.setViewPoint(model.getLimb(part.getViewPoint()));

        return true;
    }

    public static void reset()
    {
        models = new HashMap<>();
    }
}
