package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Client.Textures.TextureList;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.mlmanimator.Client.Models.*;
import DaoOfModding.mlmanimator.Client.Models.Quads.Quad;
import DaoOfModding.mlmanimator.Client.Models.Quads.QuadLinkage;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import com.mojang.math.Vector4f;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BodyPartModels
{
    protected HashMap<String, ExtendableModelRenderer> models = new HashMap<String, ExtendableModelRenderer>();
    protected HashMap<String, HashMap<String, ExtendableModelRenderer>> nonRenderingModels = new HashMap<String, HashMap<String, ExtendableModelRenderer>>();
    protected HashMap<String, QuadCollection> quads = new HashMap<String, QuadCollection>();
    protected HashMap<String, ArrayList<QuadLinkage>> quadLinks = new HashMap<String, ArrayList<QuadLinkage>>();

    protected boolean slim = false;

    public BodyPartModels(UUID playerID)
    {
        slim = PoseHandler.getPlayerPoseHandler(playerID).isSlim();

        setupBodyModels();
        setupHeadModels();
        setupLegModels();
        setupArmModels();

        setupBackModels();
    }
    
    public ArrayList<QuadLinkage> getQuadLinks(String position)
    {
        if (!quadLinks.containsKey(position))
            quadLinks.put(position, new ArrayList<QuadLinkage>());

        return quadLinks.get(position);
    }
    
    protected void addQuadLink(String position, QuadLinkage link)
    {
        getQuadLinks(position).add(link);
    }

    public void transferQuadLinks(String position, ExtendableModelRenderer model)
    {
        for (QuadLinkage link : getQuadLinks(position))
            model.addQuadLinkage(link);
    }

    protected void setupBodyModels()
    {
        ExtendableModelRenderer body = new ExtendableModelRenderer(GenericLimbNames.body);
        GenericCultivatorTextureValues.addGenericBodyLayers(body);
        body.setPos(0.0F, 0.0F, 0.0F);
        body.setRotationPoint(new Vec3(0.5, 0.5, 0.5));
        body.extend(GenericResizers.getBodyResizer());

        addModel(GenericLimbNames.body, body);


        ExtendableModelRenderer shortbody = new ExtendableModelRenderer(BodyPartModelNames.shortBody);
        GenericCultivatorTextureValues.addGenericBodyLayers(shortbody);
        shortbody.setPos(0.0F, 0.0F, 0.0F);
        shortbody.setRotationPoint(new Vec3(0.5, 0.5, 0.5));
        shortbody.setDefaultResize(new Vec3(1, 0.5, 1));
        shortbody.extend(GenericResizers.getBodyResizer());

        addModel(BodyPartModelNames.shortBody, shortbody);
    }

    protected void setupArmModels()
    {
        ExtendableModelRenderer rightArm = new ExtendableModelRenderer(GenericLimbNames.rightArm);
        GenericCultivatorTextureValues.addGenericRightArmLayers(rightArm);
        rightArm.setRotationPoint(new Vec3(0.5D, 0.66D, 0.5D));
        rightArm.setPos(0.0F, 0.0F, 0.5F);

        if (slim)
            rightArm.setFixedPosAdjustment(-1.5F, 2F, 0.0F);
        else
            rightArm.setFixedPosAdjustment(-2.0F, 2F, 0.0F);

        if (slim)
            rightArm.extend(GenericResizers.getSlimArmResizer());
        else
            rightArm.extend(GenericResizers.getArmResizer());

        rightArm.setHands(true);
        rightArm.getChildren().get(0).setHands(true);

        ExtendableModelRenderer leftArm = new ExtendableModelRenderer(GenericLimbNames.leftArm);
        GenericCultivatorTextureValues.addGenericLeftArmLayers(leftArm);
        leftArm.setRotationPoint(new Vec3(0.5D, 0.66D, 0.5D));
        leftArm.setPos(1.0F, 0.0F, 0.5F);
        if (slim)
            leftArm.setFixedPosAdjustment(1.5F, 2F, 0.0F);
        else
            leftArm.setFixedPosAdjustment(2.0F, 2F, 0.0F);

        if (slim)
            leftArm.extend(GenericResizers.getSlimArmResizer());
        else
            leftArm.extend(GenericResizers.getArmResizer());

        leftArm.setHands(true);
        leftArm.getChildren().get(0).setHands(true);

        leftArm.setHitbox(false);
        rightArm.setHitbox(false);

        addModel(GenericLimbNames.leftArm, leftArm);
        addReference(GenericLimbNames.leftArm, GenericLimbNames.lowerLeftArm, leftArm.getChildren().get(0));
        addModel(GenericLimbNames.rightArm, rightArm);
        addReference(GenericLimbNames.rightArm, GenericLimbNames.lowerRightArm, rightArm.getChildren().get(0));


        ExtendableModelRenderer lFlipper = leftArm.clone();
        ExtendableModelRenderer rFlipper = rightArm.clone();
        lFlipper.setDefaultResize(new Vec3(0.1, 1, 1));
        rFlipper.setDefaultResize(new Vec3(0.1, 1, 1));
        lFlipper.getChildren().get(0).setDefaultResize(new Vec3(0.1, 1, 1));
        rFlipper.getChildren().get(0).setDefaultResize(new Vec3(0.1, 1, 1));
        lFlipper.setFixedPosAdjustment(0F, 2F, 0.0F);
        rFlipper.setFixedPosAdjustment(0F, 2F, 0.0F);
        lFlipper.generateCube();
        rFlipper.generateCube();

        lFlipper.setHands(true);
        lFlipper.getChildren().get(0).setHands(true);
        rFlipper.setHands(true);
        rFlipper.getChildren().get(0).setHands(true);

        lFlipper.setHitbox(false);
        rFlipper.setHitbox(false);

        addModel(BodyPartModelNames.flipperLeftModel, lFlipper);
        addReference(BodyPartModelNames.flipperLeftModel, BodyPartModelNames.flipperLowerLeftModel, lFlipper.getChildren().get(0));
        addModel(BodyPartModelNames.flipperRightModel, rFlipper);
        addReference(BodyPartModelNames.flipperRightModel, BodyPartModelNames.flipperLowerRightModel, rFlipper.getChildren().get(0));


        ExtendableModelRenderer leftLongArm = leftArm.clone();
        ExtendableModelRenderer rightLongArm = rightArm.clone();
        leftLongArm.setDefaultResize(new Vec3(1, 1.5, 1));
        rightLongArm.setDefaultResize(new Vec3(1, 1.5, 1));
        leftLongArm.getChildren().get(0).setDefaultResize(new Vec3(1, 1.5, 1));
        rightLongArm.getChildren().get(0).setDefaultResize(new Vec3(1, 1.5, 1));
        leftLongArm.setFixedPosAdjustment(2F, 2F, 0.0F);
        rightLongArm.setFixedPosAdjustment(-2F, 2F, 0.0F);
        leftLongArm.generateCube();
        rightLongArm.generateCube();

        leftLongArm.setHands(true);
        leftLongArm.getChildren().get(0).setHands(true);
        rightLongArm.setHands(true);
        rightLongArm.getChildren().get(0).setHands(true);

        leftLongArm.setHitbox(false);
        rightLongArm.setHitbox(false);

        addModel(BodyPartModelNames.longArmLeftModel, leftLongArm);
        addReference(BodyPartModelNames.longArmLeftModel, BodyPartModelNames.longArmLowerLeftModel, leftLongArm.getChildren().get(0));
        addModel(BodyPartModelNames.longArmRightModel, rightLongArm);
        addReference(BodyPartModelNames.longArmRightModel, BodyPartModelNames.longArmLowerRightModel, rightLongArm.getChildren().get(0));


        ExtendableModelRenderer leftBulkyArm = leftArm.clone();
        ExtendableModelRenderer rightBulkyArm = rightArm.clone();
        leftBulkyArm.setDefaultResize(new Vec3(1.5, 0.75, 1.5));
        rightBulkyArm.setDefaultResize(new Vec3(1.5, 0.75, 1.5));
        leftBulkyArm.getChildren().get(0).setDefaultResize(new Vec3(1.5, 0.75, 1.5));
        rightBulkyArm.getChildren().get(0).setDefaultResize(new Vec3(1.5, 0.75, 1.5));
        leftBulkyArm.setFixedPosAdjustment(2F, 2F, 0.0F);
        rightBulkyArm.setFixedPosAdjustment(-2F, 2F, 0.0F);
        leftBulkyArm.generateCube();
        rightBulkyArm.generateCube();

        leftBulkyArm.setHands(true);
        leftBulkyArm.getChildren().get(0).setHands(true);
        rightBulkyArm.setHands(true);
        rightBulkyArm.getChildren().get(0).setHands(true);

        leftBulkyArm.setHitbox(false);
        rightBulkyArm.setHitbox(false);

        addModel(BodyPartModelNames.bulkyArmLeftModel, leftBulkyArm);
        addReference(BodyPartModelNames.bulkyArmLeftModel, BodyPartModelNames.bulkyArmLowerLeftModel, leftBulkyArm.getChildren().get(0));
        addModel(BodyPartModelNames.bulkyArmRightModel, rightBulkyArm);
        addReference(BodyPartModelNames.bulkyArmRightModel, BodyPartModelNames.bulkyArmLowerRightModel, rightBulkyArm.getChildren().get(0));

        ExtendableModelRenderer shortRightArm = new ExtendableModelRenderer(BodyPartModelNames.shortArmRightModel);
        GenericCultivatorTextureValues.addGenericRightArmLayers(shortRightArm);
        shortRightArm.setRotationPoint(new Vec3(0.5D, 0.66D, 0.5D));
        shortRightArm.setPos(0.0F, 0.0F, 0.5F);
        shortRightArm.setDefaultResize(new Vec3(1, 0.5, 1));
        if (slim)
            shortRightArm.setFixedPosAdjustment(-1.5F, 2F, 0.0F);
        else
            shortRightArm.setFixedPosAdjustment(-2.0F, 2F, 0.0F);

        if (slim)
            shortRightArm.extend(new defaultResizeModule(new Vec3(3.0D, 12.0D ,4.0D)));
        else
            shortRightArm.extend(new defaultResizeModule(new Vec3(4.0D, 12.0D ,4.0D)));

        shortRightArm.setHands(true);

        ExtendableModelRenderer shortLeftArm = new ExtendableModelRenderer(BodyPartModelNames.shortArmLeftModel);
        GenericCultivatorTextureValues.addGenericLeftArmLayers(shortLeftArm);
        shortLeftArm.setRotationPoint(new Vec3(0.5D, 0.66D, 0.5D));
        shortLeftArm.setPos(1.0F, 0.0F, 0.5F);
        shortLeftArm.setDefaultResize(new Vec3(1, 0.5, 1));
        if (slim)
            shortLeftArm.setFixedPosAdjustment(1.5F, 2F, 0.0F);
        else
            shortLeftArm.setFixedPosAdjustment(1.5F, 2F, 0.0F);

        if (slim)
            shortLeftArm.extend(new defaultResizeModule(new Vec3(3.0D, 12.0D ,4.0D)));
        else
            shortLeftArm.extend(new defaultResizeModule(new Vec3(4.0D, 12.0D ,4.0D)));

        shortLeftArm.setHands(true);

        shortLeftArm.setHitbox(false);
        shortRightArm.setHitbox(false);

        addModel(BodyPartModelNames.shortArmLeftModel, shortLeftArm);
        addModel(BodyPartModelNames.shortArmRightModel, shortRightArm);


        Quad larmConnector = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0),new Vec3(0, 0, 0),new Vec3(0, 0, 0));
        Quad rarmConnector = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0),new Vec3(0, 0, 0),new Vec3(0, 0, 0));
        addQuadLink(BodyPartNames.bodyPosition, new QuadLinkage(larmConnector, Quad.QuadVertex.TopLeft, new Vec3(1, 0, 0.5)));
        addQuadLink(BodyPartNames.bodyPosition, new QuadLinkage(larmConnector, Quad.QuadVertex.TopRight, new Vec3(1, 1, 0.5)));

        addQuadLink(BodyPartNames.bodyPosition, new QuadLinkage(rarmConnector, Quad.QuadVertex.TopLeft, new Vec3(0, 0, 0.5)));
        addQuadLink(BodyPartNames.bodyPosition, new QuadLinkage(rarmConnector, Quad.QuadVertex.BottomLeft, new Vec3(0, 1, 0.5)));

        getModel(GenericLimbNames.leftArm).addQuadLinkage(new QuadLinkage(larmConnector, Quad.QuadVertex.BottomLeft, new Vec3(0, 1, 0.5)));
        getModel(GenericLimbNames.leftArm).getChildren().get(0).addQuadLinkage(new QuadLinkage(larmConnector, Quad.QuadVertex.BottomRight, new Vec3(0, 0.5, 0.5)));

        getModel(GenericLimbNames.rightArm).addQuadLinkage(new QuadLinkage(rarmConnector, Quad.QuadVertex.TopRight, new Vec3(1, 1, 0.5)));
        getModel(GenericLimbNames.rightArm).getChildren().get(0).addQuadLinkage(new QuadLinkage(rarmConnector, Quad.QuadVertex.BottomRight, new Vec3(1, 0.5, 0.5)));

        larmConnector.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureHandler.BLANK);
        larmConnector.addSmallLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureList.skin);
        rarmConnector.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureHandler.BLANK);
        rarmConnector.addSmallLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureList.skin);

        QuadCollection glidingArms = new QuadCollection();
        glidingArms.addQuad(larmConnector);
        glidingArms.addQuad(rarmConnector);

        addQuadCollection(BodyPartModelNames.armglidequad, glidingArms);

        setupArmOptions();
    }

    protected void setupArmOptions()
    {
    }

    protected void setupHeadModels()
    {
        defaultResizeModule neckResizer = new defaultResizeModule(9, new Vec3(0, -1, 0), new Vec3(0.5, 1, 0.5), new Vec3(8, 8, 8), new Vec3(0.5, -1, 0.5));

        ExtendableModelRenderer neckPart = new ExtendableModelRenderer(BodyPartModelNames.longNeckModel);
        GenericCultivatorTextureValues.addGenericHeadLayers(neckPart, 0, 7);
        neckPart.setRotationPoint(new Vec3(0.5D, 0.0D, 0.5D));
        neckPart.setPos(0.5F, 0.0F, 0.5F);
        neckPart.setDefaultResize(new Vec3(0.5, 1, 0.5));
        neckPart.extend(neckResizer);

        ExtendableModelRenderer neckExplorer = neckPart;

        while (neckExplorer.getChildren().size() > 0)
        {
            neckExplorer = neckExplorer.getChildren().get(0);
            neckExplorer.setDefaultResize(new Vec3(0.5, 1, 0.5));
        }

        addModel(BodyPartModelNames.longNeckModel, neckPart);
        addReference(BodyPartModelNames.longNeckModel, BodyPartModelNames.longNeckModelEnd, neckExplorer);


        defaultResizeModule semiHeadResizer = new defaultResizeModule(new Vec3(8, 5, 8));
        defaultResizeModule jawResizer = new defaultResizeModule(new Vec3(8, 3, 8));

        // Create the top half of the head
        ExtendableModelRenderer semiHeadPart = new ExtendableModelRenderer(BodyPartModelNames.jawModel + "1");
        GenericCultivatorTextureValues.addGenericHeadLayers(semiHeadPart);
        semiHeadPart.setRotationPoint(new Vec3(0.5D, 0.0D, 0.5D));
        semiHeadPart.setPos(0.5F, 0.0F, 0.5F);
        semiHeadPart.setFixedPosAdjustment(0, -3, 0);
        semiHeadPart.extend(semiHeadResizer);
        semiHeadPart.setLooking(true);
        semiHeadPart.setFirstPersonRender(false);
        semiHeadPart.setHitbox(false);

        // Create the jaw
        ExtendableModelRenderer jawPart = new ExtendableModelRenderer( BodyPartModelNames.jawModel + "2");
        jawPart.addLayer(new UVPair(GenericTextureValues.head.u(), GenericTextureValues.head.v() + 5), GenericTextureValues.skin_Size, 0.0F, "PLAYERSKIN");
        jawPart.addLayer(new UVPair(GenericTextureValues.hat.u(), GenericTextureValues.hat.v() + 5), GenericTextureValues.skin_Size, 0.51F, "PLAYERSKIN", Direction.UP);
        jawPart.addLayer(new UVPair(GenericTextureValues.head.u(), GenericTextureValues.head.v() + 5), GenericTextureValues.armor_Size, 1.01F, "HEADARMOR", Direction.UP);
        jawPart.addLayer(new UVPair(GenericTextureValues.head.u(), GenericTextureValues.head.v() + 5), GenericTextureValues.skin_Size, 0.0F, TextureList.skin, false, 4);
        jawPart.addLayer(new UVPair(GenericTextureValues.hat.u(), GenericTextureValues.hat.v() + 5), GenericTextureValues.skin_Size, 0.5F, TextureList.skin, false, 4, Direction.UP);
        jawPart.setRotationPoint(new Vec3(0.5D, 1, 0.3));
        jawPart.setPos(0.5F, 1F, 0.7F);
        jawPart.extend(jawResizer);
        jawPart.setFirstPersonRender(false);
        jawPart.setUsedSize(new Vec3(0, 5, 0));
        jawPart.setHitbox(false);

        // Attach the jaw to the head
        semiHeadPart.addChild(jawPart);

        addModel(BodyPartModelNames.jawModel, semiHeadPart);
        addReference(BodyPartModelNames.jawModel, BodyPartModelNames.jawModelLower, jawPart);

        setupTeethModels();

        // Setup first person jaw models
        semiHeadResizer = new defaultResizeModule(new Vec3(8, 5, 8));
        jawResizer = new defaultResizeModule(new Vec3(8, 3, 8));

        // Create the top half of the head
        ExtendableModelRenderer FPsemiHeadPart = new ExtendableModelRenderer(BodyPartModelNames.FPjawModel + "1");
        FPsemiHeadPart.addLayer(GenericTextureValues.head, GenericTextureValues.skin_Size, 0.0F, "PLAYERSKIN");
        FPsemiHeadPart.addLayer(GenericTextureValues.head, GenericTextureValues.skin_Size, 0.0F, TextureList.skin, false, 4);
        FPsemiHeadPart.setRotationPoint(new Vec3(0.5D, 0.0D, 0.5D));
        FPsemiHeadPart.setPos(0F, 0.0F, 0.5F);
        FPsemiHeadPart.setFixedPosAdjustment(0, -0.5f, -1.25f);
        FPsemiHeadPart.extend(semiHeadResizer);
        FPsemiHeadPart.setLooking(true);
        FPsemiHeadPart.setHitbox(false);
        FPsemiHeadPart.setNoResize(true);

        // Create the jaw
        ExtendableModelRenderer FPjawPart = new ExtendableModelRenderer(BodyPartModelNames.FPjawModel + "2");
        FPjawPart.addLayer(new UVPair(GenericTextureValues.head.u(), GenericTextureValues.head.v() + 5), GenericTextureValues.skin_Size, 0.01F, "PLAYERSKIN");
        FPjawPart.addLayer(new UVPair(GenericTextureValues.head.u(), GenericTextureValues.head.v() + 5), GenericTextureValues.skin_Size, 0.0F, TextureList.skin, false, 4);
        FPjawPart.setRotationPoint(new Vec3(0.5D, 1, 0));
        FPjawPart.setPos(0.5F, 1F, 0.7F);
        FPjawPart.setFixedPosAdjustment(0, -2, 2f);
        FPjawPart.extend(jawResizer);
        FPjawPart.setUsedSize(new Vec3(0, 5, 0));
        FPjawPart.setHitbox(false);
        FPjawPart.setNoResize(true);

        // Attach the jaw to the head
        FPsemiHeadPart.addChild(FPjawPart);

        addModel(BodyPartModelNames.FPjawModel, FPsemiHeadPart);
        addReference(BodyPartModelNames.FPjawModel, BodyPartModelNames.FPjawModelLower, FPjawPart);

        setupHeadOptions();
    }

    protected void setupHeadOptions()
    {
        Quad finQuad = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0),new Vec3(0, 0, 0),new Vec3(0, 0, 0));
        addQuadLink(BodyPartNames.headPosition, new QuadLinkage(finQuad, Quad.QuadVertex.TopLeft, new Vec3(0.5, 0, 0.2)));
        addQuadLink(BodyPartNames.headPosition, new QuadLinkage(finQuad, Quad.QuadVertex.BottomLeft, new Vec3(0.5, 0, 0.8)));
        addQuadLink(BodyPartNames.headPosition, new QuadLinkage(finQuad, Quad.QuadVertex.BottomRight, new Vec3(0.5, 0, 0.8)));
        addQuadLink(BodyPartNames.headPosition, new QuadLinkage(finQuad, Quad.QuadVertex.TopRight, new Vec3(0.5, -2, 0.8)));

        finQuad.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureHandler.PLAYER_SKIN, new UVPair(12 / GenericTextureValues.skin_Size.u(), 4 / GenericTextureValues.skin_Size.v()));
        finQuad.addSmallLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureList.skin);

        QuadCollection finQuads = new QuadCollection();
        finQuads.addQuad(finQuad);

        addQuadCollection(BodyPartModelNames.headFinQuad, finQuads);

        ExtendableModelRenderer flowerPart = new ExtendableModelRenderer(BodyPartModelNames.headFlowerModel);
        flowerPart.addLayer(new UVPair(GenericTextureValues.head.u() + 12, GenericTextureValues.head.v() + 4), GenericTextureValues.skin_Size, 0.01F, "PLAYERSKIN");
        flowerPart.setRotationPoint(new Vec3(0.5D, 1, 0.5D));
        flowerPart.setPos(0.5F, 0F, 0.5F);
        flowerPart.extend(new defaultResizeModule(new Vec3(1, -0.5f, 1)));
        flowerPart.setHitbox(false);
        flowerPart.setFirstPersonRender(false);

        QuadCollection flowerQuads = new QuadCollection();
        addPetals(2.5f, 6, 2, flowerQuads, flowerPart);

        addModel(BodyPartModelNames.headFlowerModel, flowerPart);
        addQuadCollection(BodyPartModelNames.headFlowerQuads, flowerQuads);
    }

    protected void addPetals(float petalWidth, float petalLength, float petalLift, QuadCollection flowerQuads, ExtendableModelRenderer flowerPart)
    {
        addPetals(petalWidth, petalLength, petalLift, flowerQuads, flowerPart, 0, 0, 0);
        /*addPetals(petalWidth, petalLength * 0.75f, petalLift * 0.2f, flowerQuads, flowerPart, 0, 0, 0);
        addPetals(petalWidth, petalLength, petalLift, flowerQuads, flowerPart, petalWidth, petalLength * 0.75f, petalLift * 0.2f);*/
    }

    protected void addPetals(float petalWidth, float petalLength, float petalLift, QuadCollection flowerQuads, ExtendableModelRenderer flowerPart, float baseWidth, float baseLength, float baseLift)
    {
        petalLift += 0.5;
        baseLift += 0.5;

        float seperator = 0.25f;

        float zeroWidth = -baseWidth;
        float oneWidth = 1 + baseWidth;

        float zeroLength = -baseLength;
        float oneLength = 1 + baseLength;

        petalLength -= baseLength;
        petalWidth -= seperator;

        addPetal(new Vec3(zeroWidth - petalWidth, petalLift, oneLength + petalLength),
                new Vec3(zeroWidth + 0.25, baseLift, oneLength),
                new Vec3(oneWidth - 0.25, baseLift, oneLength),
                new Vec3(oneWidth + petalWidth, petalLift, oneLength + petalLength),
                flowerQuads, flowerPart);

        addPetal(new Vec3(oneWidth + petalWidth, petalLift, zeroLength - petalLength),
                new Vec3(oneWidth - 0.25, baseLift, zeroLength),
                new Vec3(zeroWidth + 0.25, baseLift, zeroLength),
                new Vec3(zeroWidth - petalWidth, petalLift, zeroLength - petalLength),
                flowerQuads, flowerPart);

        addPetal(new Vec3(zeroLength - petalLength, petalLift, zeroWidth - petalWidth),
                new Vec3(zeroLength, baseLift, zeroWidth + 0.25),
                new Vec3(zeroLength, baseLift, oneWidth - 0.25),
                new Vec3(zeroLength - petalLength, petalLift, oneWidth + petalWidth),
                flowerQuads, flowerPart);

        addPetal(new Vec3(oneLength + petalLength, petalLift, oneWidth + petalWidth),
                new Vec3(oneLength, baseLift, oneWidth - 0.25),
                new Vec3(oneLength, baseLift, zeroWidth + 0.25),
                new Vec3(oneLength + petalLength, petalLift, zeroWidth - petalWidth),
                flowerQuads, flowerPart);

        petalWidth += seperator;

        // Add diagonal petals
        addPetal(new Vec3(oneWidth + petalWidth, petalLift, oneLength + petalLength),
                new Vec3(oneWidth - 0.25, baseLift, oneLength),
                new Vec3(oneLength, baseLift, oneWidth - 0.25),
                new Vec3(oneLength + petalLength, petalLift, oneWidth + petalWidth),
                flowerQuads, flowerPart);

        addPetal(new Vec3(oneLength + petalLength, petalLift, zeroWidth - petalWidth),
                new Vec3(oneLength, baseLift, zeroWidth + 0.25),
                new Vec3(oneWidth - 0.25, baseLift, zeroLength),
                new Vec3(oneWidth + petalWidth, petalLift, zeroLength - petalLength),
                flowerQuads, flowerPart);

        addPetal(new Vec3(zeroLength - petalLength, petalLift, oneWidth + petalWidth),
                new Vec3(zeroLength, baseLift, oneWidth - 0.25),
                new Vec3(zeroWidth + 0.25, baseLift, oneLength),
                new Vec3(zeroWidth - petalWidth, petalLift, oneLength + petalLength),
                flowerQuads, flowerPart);

        addPetal(new Vec3(zeroWidth - petalWidth, petalLift, zeroLength - petalLength),
                new Vec3(zeroWidth + 0.25, baseLift, zeroLength),
                new Vec3(zeroLength, baseLift, zeroWidth + 0.25),
                new Vec3(zeroLength - petalLength, petalLift, zeroWidth - petalWidth),
                flowerQuads, flowerPart);
    }

    protected void addPetal(Vec3 quad1, Vec3 quad2,Vec3 quad3,Vec3 quad4, QuadCollection collection, ExtendableModelRenderer flowerPart)
    {
        Quad flowerQuad = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0), new Vec3(0, 0, 0), new Vec3(0, 0, 0));
        flowerPart.addQuadLinkage(new QuadLinkage(flowerQuad, Quad.QuadVertex.TopLeft, quad1));
        flowerPart.addQuadLinkage(new QuadLinkage(flowerQuad, Quad.QuadVertex.BottomLeft, quad2));
        flowerPart.addQuadLinkage(new QuadLinkage(flowerQuad, Quad.QuadVertex.BottomRight, quad3));
        flowerPart.addQuadLinkage(new QuadLinkage(flowerQuad, Quad.QuadVertex.TopRight, quad4));

        flowerQuad.addLayer(new UVPair(0, 0), TextureList.petalSize, 0, TextureList.petal);
        flowerQuad.setRenderFirstPerson(false);
        collection.addQuad(flowerQuad);
    }

    protected void setupBackModels()
    {
        setupWingModels();
        setupInsectWingModels();
        setupJetModels();
        setupFinModels();
    }

    protected void setupFinModels()
    {
        Quad finQuad = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0),new Vec3(0, 0, 0),new Vec3(0, 0, 0));
        addQuadLink(BodyPartNames.bodyPosition, new QuadLinkage(finQuad, Quad.QuadVertex.TopLeft, new Vec3(0.5, 0.2, 1)));
        addQuadLink(BodyPartNames.bodyPosition, new QuadLinkage(finQuad, Quad.QuadVertex.BottomLeft, new Vec3(0.5, 0.8, 1)));
        addQuadLink(BodyPartNames.bodyPosition, new QuadLinkage(finQuad, Quad.QuadVertex.BottomRight, new Vec3(0.5, 0.8, 3)));
        addQuadLink(BodyPartNames.bodyPosition, new QuadLinkage(finQuad, Quad.QuadVertex.TopRight, new Vec3(0.5, 0.8, 3)));

        finQuad.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureHandler.PLAYER_SKIN, new UVPair(32 / GenericTextureValues.skin_Size.u(), 24 / GenericTextureValues.skin_Size.v()));
        finQuad.addSmallLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureList.skin);

        QuadCollection finQuads = new QuadCollection();
        finQuads.addQuad(finQuad);

        addQuadCollection(BodyPartModelNames.dorsalFinQuad, finQuads);
    }

    protected void setupJetModels()
    {
        defaultResizeModule jet = new defaultResizeModule(new Vec3(0.1, 0.1, 4));

        ExtendableModelRenderer JetModel = new ExtendableModelRenderer(BodyPartModelNames.jetLeft);
        JetModel.addLayer(GenericTextureValues.chest, GenericTextureValues.skin_Size, 0.0F, "PLAYERSKIN");
        JetModel.setPos(0.25F, 0.25F, 1F);
        JetModel.setRotationPoint(new Vec3(0.5, 0.5f, 1f));
        JetModel.setDefaultResize(new Vec3(20, 5, 1));
        JetModel.extend(jet);

        jet = new defaultResizeModule(new Vec3(0.1, 0.1, 4));

        ExtendableModelRenderer JetModelRight = new ExtendableModelRenderer(BodyPartModelNames.jetRight);
        JetModelRight.addLayer(GenericTextureValues.chest, GenericTextureValues.skin_Size, 0.0F, "PLAYERSKIN");
        JetModelRight.setPos(1F, 1F, 0F);
        JetModelRight.setRotationPoint(new Vec3(0, 1f, 1f));
        JetModelRight.setDefaultResize(new Vec3(5, 10, 1));
        JetModelRight.extend(jet);

        jet = new defaultResizeModule(new Vec3(0.1, 0.1, 4));

        ExtendableModelRenderer JetModelBottom = new ExtendableModelRenderer(BodyPartModelNames.jetLeft + "2");
        JetModelBottom.addLayer(GenericTextureValues.chest, GenericTextureValues.skin_Size, 0.0F, "PLAYERSKIN");
        JetModelBottom.setPos(1F, 1F, 0F);
        JetModelBottom.setRotationPoint(new Vec3(0, 1f, 1f));
        JetModelBottom.setDefaultResize(new Vec3(20, 5, 1));
        JetModelBottom.extend(jet);

        jet = new defaultResizeModule(new Vec3(0.1, 0.1, 4));

        ExtendableModelRenderer JetModelLeft = new ExtendableModelRenderer(BodyPartModelNames.jetRight + "2");
        JetModelLeft.addLayer(GenericTextureValues.chest, GenericTextureValues.skin_Size, 0.0F, "PLAYERSKIN");
        JetModelLeft.setPos(0F, 0F, 0F);
        JetModelLeft.setRotationPoint(new Vec3(1, 0f, 1f));
        JetModelLeft.setDefaultResize(new Vec3(5, 10, 1));
        JetModelLeft.extend(jet);

        JetModelBottom.addChild(JetModelLeft);
        JetModelRight.addChild(JetModelBottom);
        JetModel.addChild(JetModelRight);

        ExtendableModelRenderer JetRight = JetModel.clone();
        JetRight.setPos(0.75F, 0.25F, 1F);

        ParticleEmitter jetSmoke = new ParticleEmitter(ParticleTypes.SMOKE, BodyPartModelNames.jetLeftSmoke);
        jetSmoke.setInterval(20);
        jetSmoke.setVelocity(new Vec3(0, 0f, 0f));
        jetSmoke.setPos(0.5f, 2, 1);

        ParticleEmitter jetSmokeRight = new ParticleEmitter(ParticleTypes.SMOKE, BodyPartModelNames.jetRightSmoke);
        jetSmokeRight.setInterval(20);
        jetSmokeRight.setVelocity(new Vec3(0, 0f, 0f));
        jetSmokeRight.setPos(0.5f, 2, 1);

        ParticleEmitter jetFlame = new ParticleEmitter(ParticleTypes.FLAME, BodyPartModelNames.jetLeftFlame);
        jetFlame.setInterval(1);
        jetFlame.setPos(0.5f, 2, 1);
        jetFlame.setVelocity(new Vec3(0, 0, -0.4f));
        jetFlame.getModelPart().visible = false;

        ParticleEmitter jetFlameRight = new ParticleEmitter(ParticleTypes.FLAME, BodyPartModelNames.jetRightFlame);
        jetFlameRight.setInterval(1);
        jetFlameRight.setVelocity(new Vec3(0, 0f, -0.4f));
        jetFlameRight.setPos(0.5f, 2, 1);
        jetFlameRight.getModelPart().visible = false;

        JetModel.addChild(jetSmoke);
        JetRight.addChild(jetSmokeRight);
        JetModel.addChild(jetFlame);
        JetRight.addChild(jetFlameRight);

        addModel(BodyPartModelNames.jetLeft, JetModel);
        addModel(BodyPartModelNames.jetRight, JetRight);
        addReference(BodyPartModelNames.jetLeft, BodyPartModelNames.jetLeftSmoke, jetSmoke);
        addReference(BodyPartModelNames.jetRight, BodyPartModelNames.jetRightSmoke, jetSmokeRight);
        addReference(BodyPartModelNames.jetLeft, BodyPartModelNames.jetLeftFlame, jetFlame);
        addReference(BodyPartModelNames.jetRight, BodyPartModelNames.jetRightFlame, jetFlameRight);
    }

    protected void setupWingModels()
    {
        defaultResizeModule wingTop = new defaultResizeModule(new Vec3(16, 0.5, 1));
        defaultResizeModule lwingTop = new defaultResizeModule(new Vec3(-16, 0.5, 1));

        ExtendableModelRenderer wingTopModel = new ExtendableModelRenderer(BodyPartModelNames.rwingUpperArmModel);
        wingTopModel.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        wingTopModel.setPos(0.5F, 0.25F, 1F);
        wingTopModel.setRotationPoint(new Vec3(1, 0.5f, 0f));
        wingTopModel.setHitbox(false);
        wingTopModel.extend(wingTop);

        ExtendableModelRenderer lwingTopModel = new ExtendableModelRenderer(BodyPartModelNames.lwingUpperArmModel);
        lwingTopModel.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        lwingTopModel.setPos(0.5F, 0.25F, 1F);
        lwingTopModel.setRotationPoint(new Vec3(1, 0.5f, 0f));
        lwingTopModel.setHitbox(false);
        lwingTopModel.extend(lwingTop);


        defaultResizeModule wingStrand = new defaultResizeModule(new Vec3(0.5, 16, 1));
        defaultResizeModule lwingStrand = new defaultResizeModule(new Vec3(0.5, 16, 1));

        ExtendableModelRenderer wingStrandModel = new ExtendableModelRenderer(BodyPartModelNames.rwingStrand1Model);
        wingStrandModel.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        wingStrandModel.setPos(1, 1F, 0.5F);
        wingStrandModel.setRotationPoint(new Vec3(0.5F, 1, 0.5f));
        wingStrandModel.setHitbox(false);
        wingStrandModel.extend(wingStrand);

        ExtendableModelRenderer lwingStrandModel = new ExtendableModelRenderer( BodyPartModelNames.lwingStrand1Model);
        lwingStrandModel.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        lwingStrandModel.setPos(1, 1F, 0.5F);
        lwingStrandModel.setRotationPoint(new Vec3(0.5F, 1, 0.5f));
        lwingStrandModel.setHitbox(false);
        lwingStrandModel.extend(lwingStrand);


        ExtendableModelRenderer wingStrandModel2 = wingStrandModel.clone();
        ExtendableModelRenderer wingStrandModel3 = wingStrandModel.clone();
        ExtendableModelRenderer wingStrandModel4 = wingStrandModel.clone();
        ExtendableModelRenderer lwingStrandModel2 = lwingStrandModel.clone();
        ExtendableModelRenderer lwingStrandModel3 = lwingStrandModel.clone();
        ExtendableModelRenderer lwingStrandModel4 = lwingStrandModel.clone();

        wingTopModel.addChild(wingStrandModel);
        wingTopModel.addChild(wingStrandModel2);
        wingTopModel.addChild(wingStrandModel3);
        wingTopModel.addChild(wingStrandModel4);
        lwingTopModel.addChild(lwingStrandModel);
        lwingTopModel.addChild(lwingStrandModel2);
        lwingTopModel.addChild(lwingStrandModel3);
        lwingTopModel.addChild(lwingStrandModel4);


        Quad wing12Connector = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0),new Vec3(0, 0, 0),new Vec3(0, 0, 0));
        wingStrandModel.addQuadLinkage(new QuadLinkage(wing12Connector, Quad.QuadVertex.TopLeft, new Vec3(0, 0, 0.5)));
        wingStrandModel.addQuadLinkage(new QuadLinkage(wing12Connector, Quad.QuadVertex.BottomLeft, new Vec3(0, 0.95, 0.5)));
        wingStrandModel2.addQuadLinkage(new QuadLinkage(wing12Connector, Quad.QuadVertex.TopRight, new Vec3(1, 0, 0.5)));
        wingStrandModel2.addQuadLinkage(new QuadLinkage(wing12Connector, Quad.QuadVertex.BottomRight, new Vec3(1, 0.95, 0.5)));

        Quad wing23Connector = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0),new Vec3(0, 0, 0),new Vec3(0, 0, 0));
        wingStrandModel2.addQuadLinkage(new QuadLinkage(wing23Connector, Quad.QuadVertex.TopLeft, new Vec3(0, 0, 0.5)));
        wingStrandModel2.addQuadLinkage(new QuadLinkage(wing23Connector, Quad.QuadVertex.BottomLeft, new Vec3(0, 0.95, 0.5)));
        wingStrandModel3.addQuadLinkage(new QuadLinkage(wing23Connector, Quad.QuadVertex.TopRight, new Vec3(1, 0, 0.5)));
        wingStrandModel3.addQuadLinkage(new QuadLinkage(wing23Connector, Quad.QuadVertex.BottomRight, new Vec3(1, 0.95, 0.5)));

        Quad wing34Connector = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0),new Vec3(0, 0, 0),new Vec3(0, 0, 0));
        wingStrandModel3.addQuadLinkage(new QuadLinkage(wing34Connector, Quad.QuadVertex.TopLeft, new Vec3(0, 0, 0.5)));
        wingStrandModel3.addQuadLinkage(new QuadLinkage(wing34Connector, Quad.QuadVertex.BottomLeft, new Vec3(0, 0.95, 0.5)));
        wingStrandModel4.addQuadLinkage(new QuadLinkage(wing34Connector, Quad.QuadVertex.TopRight, new Vec3(1, 0, 0.5)));
        wingStrandModel4.addQuadLinkage(new QuadLinkage(wing34Connector, Quad.QuadVertex.BottomRight, new Vec3(1, 0.95, 0.5)));

        Quad wing45Connector = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0),new Vec3(0, 0, 0),new Vec3(0, 0, 0));
        wingStrandModel4.addQuadLinkage(new QuadLinkage(wing45Connector, Quad.QuadVertex.TopLeft, new Vec3(0, 0, 0.5)));
        wingStrandModel4.addQuadLinkage(new QuadLinkage(wing45Connector, Quad.QuadVertex.BottomLeft, new Vec3(0, 0.95, 0.5)));
        wingTopModel.addQuadLinkage(new QuadLinkage(wing45Connector, Quad.QuadVertex.TopRight, new Vec3(1, 0, 0.5)));
        wingTopModel.addQuadLinkage(new QuadLinkage(wing45Connector, Quad.QuadVertex.BottomRight, new Vec3(0, 0, 0.5)));


        Quad lwing12Connector = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0),new Vec3(0, 0, 0),new Vec3(0, 0, 0));
        lwingStrandModel.addQuadLinkage(new QuadLinkage(lwing12Connector, Quad.QuadVertex.TopLeft, new Vec3(0, 0, 0.5)));
        lwingStrandModel.addQuadLinkage(new QuadLinkage(lwing12Connector, Quad.QuadVertex.TopRight, new Vec3(0, 0.95, 0.5)));
        lwingStrandModel2.addQuadLinkage(new QuadLinkage(lwing12Connector, Quad.QuadVertex.BottomLeft, new Vec3(1, 0, 0.5)));
        lwingStrandModel2.addQuadLinkage(new QuadLinkage(lwing12Connector, Quad.QuadVertex.BottomRight, new Vec3(1, 0.95, 0.5)));

        Quad lwing23Connector = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0),new Vec3(0, 0, 0),new Vec3(0, 0, 0));
        lwingStrandModel2.addQuadLinkage(new QuadLinkage(lwing23Connector, Quad.QuadVertex.TopLeft, new Vec3(0, 0, 0.5)));
        lwingStrandModel2.addQuadLinkage(new QuadLinkage(lwing23Connector, Quad.QuadVertex.TopRight, new Vec3(0, 0.95, 0.5)));
        lwingStrandModel3.addQuadLinkage(new QuadLinkage(lwing23Connector, Quad.QuadVertex.BottomLeft, new Vec3(1, 0, 0.5)));
        lwingStrandModel3.addQuadLinkage(new QuadLinkage(lwing23Connector, Quad.QuadVertex.BottomRight, new Vec3(1, 0.95, 0.5)));

        Quad lwing34Connector = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0),new Vec3(0, 0, 0),new Vec3(0, 0, 0));
        lwingStrandModel3.addQuadLinkage(new QuadLinkage(lwing34Connector, Quad.QuadVertex.TopLeft, new Vec3(0, 0, 0.5)));
        lwingStrandModel3.addQuadLinkage(new QuadLinkage(lwing34Connector, Quad.QuadVertex.TopRight, new Vec3(0, 0.95, 0.5)));
        lwingStrandModel4.addQuadLinkage(new QuadLinkage(lwing34Connector, Quad.QuadVertex.BottomLeft, new Vec3(1, 0, 0.5)));
        lwingStrandModel4.addQuadLinkage(new QuadLinkage(lwing34Connector, Quad.QuadVertex.BottomRight, new Vec3(1, 0.95, 0.5)));

        Quad lwing45Connector = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0),new Vec3(0, 0, 0),new Vec3(0, 0, 0));
        lwingStrandModel4.addQuadLinkage(new QuadLinkage(lwing45Connector, Quad.QuadVertex.TopLeft, new Vec3(0, 0, 0.5)));
        lwingStrandModel4.addQuadLinkage(new QuadLinkage(lwing45Connector, Quad.QuadVertex.TopRight, new Vec3(0, 0.95, 0.5)));
        lwingTopModel.addQuadLinkage(new QuadLinkage(lwing45Connector, Quad.QuadVertex.BottomLeft, new Vec3(1, 0, 0.5)));
        lwingTopModel.addQuadLinkage(new QuadLinkage(lwing45Connector, Quad.QuadVertex.BottomRight, new Vec3(0, 0, 0.5)));

        QuadCollection wingQuads = new QuadCollection();
        wingQuads.addQuad(wing12Connector);
        wingQuads.addQuad(wing23Connector);
        wingQuads.addQuad(wing34Connector);
        wingQuads.addQuad(wing45Connector);
        wingQuads.addQuad(lwing12Connector);
        wingQuads.addQuad(lwing23Connector);
        wingQuads.addQuad(lwing34Connector);
        wingQuads.addQuad(lwing45Connector);

        wing12Connector.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureHandler.BLANK);
        wing23Connector.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureHandler.BLANK);
        wing34Connector.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureHandler.BLANK);
        wing45Connector.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureHandler.BLANK);
        wing12Connector.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureHandler.BLANK);
        lwing12Connector.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureHandler.BLANK);
        lwing23Connector.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureHandler.BLANK);
        lwing34Connector.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureHandler.BLANK);
        lwing45Connector.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureHandler.BLANK);

        wing12Connector.addSmallLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureList.skin);
        wing23Connector.addSmallLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureList.skin);
        wing34Connector.addSmallLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureList.skin);
        wing45Connector.addSmallLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureList.skin);
        lwing12Connector.addSmallLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureList.skin);
        lwing23Connector.addSmallLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureList.skin);
        lwing34Connector.addSmallLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureList.skin);
        lwing45Connector.addSmallLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureList.skin);

        addModel(BodyPartModelNames.rwingUpperArmModel, wingTopModel);
        addReference(BodyPartModelNames.rwingUpperArmModel, BodyPartModelNames.rwingStrand1Model, wingStrandModel);
        addReference(BodyPartModelNames.rwingUpperArmModel, BodyPartModelNames.rwingStrand2Model, wingStrandModel2);
        addReference(BodyPartModelNames.rwingUpperArmModel, BodyPartModelNames.rwingStrand3Model, wingStrandModel3);
        addReference(BodyPartModelNames.rwingUpperArmModel, BodyPartModelNames.rwingStrand4Model, wingStrandModel4);
        addModel(BodyPartModelNames.lwingUpperArmModel, lwingTopModel);
        addReference(BodyPartModelNames.lwingUpperArmModel, BodyPartModelNames.lwingStrand1Model, lwingStrandModel);
        addReference(BodyPartModelNames.lwingUpperArmModel, BodyPartModelNames.lwingStrand2Model, lwingStrandModel2);
        addReference(BodyPartModelNames.lwingUpperArmModel, BodyPartModelNames.lwingStrand3Model, lwingStrandModel3);
        addReference(BodyPartModelNames.lwingUpperArmModel, BodyPartModelNames.lwingStrand4Model, lwingStrandModel4);
        addQuadCollection(BodyPartModelNames.wingquad, wingQuads);
    }

    protected void setupInsectWingModels()
    {
        defaultResizeModule wingTop = new defaultResizeModule(new Vec3(16, 0.25, 0.25));
        defaultResizeModule lwingTop = new defaultResizeModule(new Vec3(-16, 0.25, 0.25));

        ExtendableModelRenderer wingTopModel = new ExtendableModelRenderer(BodyPartModelNames.rinsectWing);
        wingTopModel.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        wingTopModel.setPos(0.5F, 0.1F, 1F);
        wingTopModel.setRotationPoint(new Vec3(1, 0.5f, 0f));
        wingTopModel.setHitbox(false);
        wingTopModel.extend(wingTop);

        ExtendableModelRenderer lwingTopModel = new ExtendableModelRenderer(BodyPartModelNames.linsectWing);
        lwingTopModel.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        lwingTopModel.setPos(0.5F, 0.1F, 1F);
        lwingTopModel.setRotationPoint(new Vec3(1, 0.5f, 0f));
        lwingTopModel.setHitbox(false);
        lwingTopModel.extend(lwingTop);


        defaultResizeModule innerWingTop = new defaultResizeModule(new Vec3(-14, 0.25, 0.25));
        defaultResizeModule linnerWingTop = new defaultResizeModule(new Vec3(14, 0.25, 0.25));

        ExtendableModelRenderer innerWingModel = new ExtendableModelRenderer(BodyPartModelNames.rinsectWingInner);
        innerWingModel.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        innerWingModel.setPos(0, 0F, 0.5F);
        innerWingModel.setRotationPoint(new Vec3(0F, 1, 0.5f));
        innerWingModel.setHitbox(false);
        innerWingModel.extend(innerWingTop);

        ExtendableModelRenderer linnerWingModel = new ExtendableModelRenderer( BodyPartModelNames.linsectWingInner);
        linnerWingModel.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        linnerWingModel.setPos(0, 0F, 0.5F);
        linnerWingModel.setRotationPoint(new Vec3(0F, 1, 0.5f));
        linnerWingModel.setHitbox(false);
        linnerWingModel.extend(linnerWingTop);

        wingTopModel.addChild(innerWingModel);
        lwingTopModel.addChild(linnerWingModel);


        Quad lwingConnector = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0),new Vec3(0, 0, 0),new Vec3(0, 0, 0));
        lwingTopModel.addQuadLinkage(new QuadLinkage(lwingConnector, Quad.QuadVertex.TopLeft, new Vec3(0, 0, 0.5)));
        lwingTopModel.addQuadLinkage(new QuadLinkage(lwingConnector, Quad.QuadVertex.BottomLeft, new Vec3(1, 0, 0.5)));
        linnerWingModel.addQuadLinkage(new QuadLinkage(lwingConnector, Quad.QuadVertex.TopRight, new Vec3(1, 0, 0.5)));
        linnerWingModel.addQuadLinkage(new QuadLinkage(lwingConnector, Quad.QuadVertex.BottomRight, new Vec3(0, 0, 0.5)));
        lwingConnector.setAlpha(0.33f);
        lwingConnector.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureList.elementalColored);

        Quad rwingConnector = new Quad(new Vec3(0, 0, 0), new Vec3(0, 0, 0),new Vec3(0, 0, 0),new Vec3(0, 0, 0));
        wingTopModel.addQuadLinkage(new QuadLinkage(rwingConnector, Quad.QuadVertex.TopLeft, new Vec3(0, 0, 0.5)));
        wingTopModel.addQuadLinkage(new QuadLinkage(rwingConnector, Quad.QuadVertex.BottomLeft, new Vec3(1, 0, 0.5)));
        innerWingModel.addQuadLinkage(new QuadLinkage(rwingConnector, Quad.QuadVertex.TopRight, new Vec3(1, 0, 0.5)));
        innerWingModel.addQuadLinkage(new QuadLinkage(rwingConnector, Quad.QuadVertex.BottomRight, new Vec3(0, 0, 0.5)));
        rwingConnector.setAlpha(0.33f);
        rwingConnector.addLayer(new UVPair(0, 0), GenericTextureValues.skin_Size, 0, TextureList.elementalColored);

        QuadCollection wingQuads = new QuadCollection();
        wingQuads.addQuad(lwingConnector);
        wingQuads.addQuad(rwingConnector);


        addModel(BodyPartModelNames.rinsectWing, wingTopModel);
        addReference(BodyPartModelNames.rinsectWing, BodyPartModelNames.rinsectWingInner, innerWingModel);

        addModel(BodyPartModelNames.linsectWing, lwingTopModel);
        addReference(BodyPartModelNames.linsectWing, BodyPartModelNames.linsectWingInner, linnerWingModel);

        addQuadCollection(BodyPartModelNames.insectQuads, wingQuads);
    }

    protected void setupTeethModels()
    {
        setupFlatTeethModels();
        setupSharpTeethModels();
    }

    protected void setupFlatTeethModels()
    {
        // Create row of top teeth
        defaultResizeModule flatToothResizer = QiResizers.getTeethResizer(8, 8f, 1, 0.1f);

        ExtendableModelRenderer flatToothPart = new ExtendableModelRenderer(BodyPartModelNames.flatToothModel);
        flatToothPart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        flatToothPart.setPos(0f, 1f, 0f);
        flatToothPart.setFixedPosAdjustment(0.05f, 0, 0.05f);
        flatToothPart.setRotationPoint(new Vec3(1f, 1, 1));
        flatToothPart.extend(flatToothResizer);

        ExtendableModelRenderer lastTooth = flatToothPart;

        while (lastTooth.getChildren().size() > 0)
            lastTooth = lastTooth.getChildren().get(0);


        //Setup side teeth
        flatToothResizer = QiResizers.getSideTeethResizer(6, 5.8f, 1, 0.1f);

        ExtendableModelRenderer flatToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.flatToothModel + "_SIDE");
        flatToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        flatToothSidePart.setPos(1f, 0.5f, 1f);
        flatToothSidePart.setFixedPosAdjustment(0f, 0, 0.1f);
        flatToothSidePart.setRotationPoint(new Vec3(0f, 0.5, 1));
        flatToothSidePart.extend(flatToothResizer);

        flatToothPart.addChild(flatToothSidePart);

        flatToothResizer = QiResizers.getSideTeethResizer(6, 5.8f, 1, 0.1f);

        flatToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.flatToothModel + "_OTHER_SIDE");
        flatToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        flatToothSidePart.setPos(1f, 0.5f, 1f);
        flatToothSidePart.setFixedPosAdjustment(0f, 0, 0.1f);
        flatToothSidePart.setRotationPoint(new Vec3(0f, 0.5, 1));
        flatToothSidePart.extend(flatToothResizer);

        lastTooth.addChild(flatToothSidePart);

        flatToothPart.setFirstPersonRenderForSelfAndChildren(false);
        flatToothPart.setHitbox(false);
        addModel(BodyPartModelNames.flatToothModel, flatToothPart);

        ExtendableModelRenderer FPflatToothPart = flatToothPart.clone("FP");
        FPflatToothPart.setNoResizeForSelfAndChildren(true);
        FPflatToothPart.setName(BodyPartModelNames.FPflatToothModel);
        addModel(BodyPartModelNames.FPflatToothModel, FPflatToothPart);


        // Create row of bottom teeth
        flatToothResizer = QiResizers.getTeethResizer(8, 7.8f, 1, 0.1f);

        flatToothPart = new ExtendableModelRenderer(BodyPartModelNames.flatToothLowerModel);
        flatToothPart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        flatToothPart.setPos(0f, 0, 0f);
        flatToothPart.setFixedPosAdjustment(0.05f, 0, 0.05f);
        flatToothPart.setRotationPoint(new Vec3(1f, 0, 1));
        flatToothPart.extend(flatToothResizer);
        flatToothPart.setRotationOffset(new Vec3(Math.toRadians(-20), 0, 0));

        lastTooth = flatToothPart;

        while (lastTooth.getChildren().size() > 0)
            lastTooth = lastTooth.getChildren().get(0);

        // Create row of bottom teeth
        flatToothResizer = QiResizers.getSideTeethResizer(6, 5.8f, 1, 0.1f);

        flatToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.flatToothLowerModel + "_SIDE");
        flatToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        flatToothSidePart.setPos(1f, 0.5f, 1f);
        flatToothSidePart.setFixedPosAdjustment(0f, 0, 0.1f);
        flatToothSidePart.setRotationPoint(new Vec3(0f, 0.5, 1));
        flatToothSidePart.extend(flatToothResizer);

        flatToothPart.addChild(flatToothSidePart);

        flatToothResizer = QiResizers.getSideTeethResizer(6, 5.8f, 1, 0.1f);

        flatToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.flatToothLowerModel + "_OTHER_SIDE");
        flatToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        flatToothSidePart.setPos(1f, 0.5f, 1f);
        flatToothSidePart.setFixedPosAdjustment(0f, 0, 0.1f);
        flatToothSidePart.setRotationPoint(new Vec3(0f, 0.5, 1));
        flatToothSidePart.extend(flatToothResizer);

        lastTooth.addChild(flatToothSidePart);

        flatToothPart.setFirstPersonRenderForSelfAndChildren(false);
        flatToothPart.setHitbox(false);
        addModel(BodyPartModelNames.flatToothLowerModel, flatToothPart);

        FPflatToothPart = flatToothPart.clone("FP");
        FPflatToothPart.setFirstPersonRenderForSelfAndChildren(true);
        FPflatToothPart.setNoResizeForSelfAndChildren(true);
        FPflatToothPart.setName(BodyPartModelNames.FPflatToothLowerModel);
        addModel(BodyPartModelNames.FPflatToothLowerModel, FPflatToothPart);
    }

    protected void setupSharpTeethModels()
    {
        // Create row of top teeth
        defaultResizeModule ToothResizer = QiResizers.getTeethResizer(8, 7.9f, 0.5f, 0.1f);
        ExtendableModelRenderer ToothPart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothModel);
        ToothPart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        ToothPart.setPos(0f, 1f, 0f);
        ToothPart.setFixedPosAdjustment(0.05f, 0, 0.05f);
        ToothPart.setRotationPoint(new Vec3(1f, 1, 1));
        ToothPart.extend(ToothResizer);

        ToothResizer = QiResizers.getTeethResizer(8, 7.9f, 0.5f, 0.3f);
        ExtendableModelRenderer LowerToothPart = new ExtendableModelRenderer( BodyPartModelNames.sharpToothModel + "1");
        LowerToothPart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothPart.setPos(0.1f, 0.5f, 0f);
        LowerToothPart.extend(ToothResizer);

        ToothPart.addChild(LowerToothPart);

        ToothResizer = QiResizers.getTeethResizer(8, 7.9f, 0.5f, 0.5f);
        LowerToothPart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothModel + "2");
        LowerToothPart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothPart.setPos(0.2f, 1f, 0f);
        LowerToothPart.extend(ToothResizer);

        ToothPart.addChild(LowerToothPart);

        ToothResizer = QiResizers.getTeethResizer(8, 7.9f, 0.49f, 0.7f);
        LowerToothPart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothModel + "3");
        LowerToothPart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothPart.setPos(0.3f, 1.5f, 0f);
        LowerToothPart.extend(ToothResizer);

        ToothPart.addChild(LowerToothPart);

        //Setup side teeth
        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.5f, 0.1f);

        ExtendableModelRenderer ToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothModel + "4");
        ToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        ToothSidePart.setPos(0f, 0f, 1.1f);
        ToothSidePart.extend(ToothResizer);

        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.5f, 0.3f);
        ExtendableModelRenderer LowerToothSidePart = new ExtendableModelRenderer( BodyPartModelNames.sharpToothModel + "5");
        LowerToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothSidePart.setPos(0f, 0.5f, 0.1f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.5f, 0.5f);
        LowerToothSidePart = new ExtendableModelRenderer( BodyPartModelNames.sharpToothModel + "6");
        LowerToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothSidePart.setPos(0f, 1f, 0.2f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.49f, 0.7f);
        LowerToothSidePart = new ExtendableModelRenderer( BodyPartModelNames.sharpToothModel + "7");
        LowerToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothSidePart.setPos(0f, 1.5f, 0.3f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothPart.addChild(ToothSidePart);


        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.5f, 0.1f);

        ToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothModel + "8");
        ToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        ToothSidePart.setPos(7.8f, 0f, 1.1f);
        ToothSidePart.extend(ToothResizer);

        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.5f, 0.3f);
        LowerToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothModel + "9");
        LowerToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothSidePart.setPos(0f, 0.5f, 0.1f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.5f, 0.5f);
        LowerToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothModel + "10");
        LowerToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothSidePart.setPos(0f, 1f, 0.2f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.49f, 0.7f);
        LowerToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothModel + "11");
        LowerToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothSidePart.setPos(0f, 1.5f, 0.3f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothPart.addChild(ToothSidePart);
        ToothPart.setFirstPersonRenderForSelfAndChildren(false);

        ToothPart.setHitbox(false);
        addModel(BodyPartModelNames.sharpToothModel, ToothPart);

        ExtendableModelRenderer FPToothPart = ToothPart.clone();
        FPToothPart.setName(BodyPartModelNames.FPsharpToothModel);
        FPToothPart.setNoResizeForSelfAndChildren(true);
        addModel(BodyPartModelNames.FPsharpToothModel, FPToothPart);

        // Create row of bottom teeth
        ToothResizer = QiResizers.getTeethResizer(7, 7f, 0.5f, 0.1f);
        ToothPart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothLowerModel);
        ToothPart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        ToothPart.setPos(0f, 0f, 0f);
        ToothPart.setFixedPosAdjustment(0.55f, 0, 0.1f);
        ToothPart.setRotationPoint(new Vec3(1f, 0, 1));
        ToothPart.extend(ToothResizer);
        ToothPart.setRotationOffset(new Vec3(Math.toRadians(-20), 0, 0));

        ToothResizer = QiResizers.getTeethResizer(7, 7f, 0.5f, 0.3f);
        LowerToothPart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothLowerModel + "1");
        LowerToothPart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothPart.setPos(0.1f, -0.5f, 0f);
        LowerToothPart.extend(ToothResizer);

        ToothPart.addChild(LowerToothPart);

        ToothResizer = QiResizers.getTeethResizer(7, 7f, 0.5f, 0.5f);
        LowerToothPart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothLowerModel + "2");
        LowerToothPart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothPart.setPos(0.2f, -1f, 0f);
        LowerToothPart.extend(ToothResizer);

        ToothPart.addChild(LowerToothPart);

        ToothResizer = QiResizers.getTeethResizer(7, 7f, 0.49f, 0.7f);
        LowerToothPart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothLowerModel + "3");
        LowerToothPart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothPart.setPos(0.3f, -1.5f, 0f);
        LowerToothPart.extend(ToothResizer);

        ToothPart.addChild(LowerToothPart);


        float sideTeethLength = 3.7f;

        //Setup side teeth
        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.5f, 0.1f);

        ToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothLowerModel + "4");
        ToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        ToothSidePart.setPos(-0.5f, 0f, 0.4f);
        ToothSidePart.extend(ToothResizer);

        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.5f, 0.3f);
        LowerToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothLowerModel + "5");
        LowerToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothSidePart.setPos(0f, -0.5f, 0.1f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.5f, 0.5f);
        LowerToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothLowerModel + "6");
        LowerToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothSidePart.setPos(0f, -1f, 0.2f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.49f, 0.7f);
        LowerToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothLowerModel + "7");
        LowerToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothSidePart.setPos(0f, -1.5f, 0.3f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothPart.addChild(ToothSidePart);


        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.5f, 0.1f);

        ToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothLowerModel + "8");
        ToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        ToothSidePart.setPos(7.2f, 0f, 0.4f);
        ToothSidePart.extend(ToothResizer);

        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.5f, 0.3f);
        LowerToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothLowerModel + "9");
        LowerToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothSidePart.setPos(0f, -0.5f, 0.1f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.5f, 0.5f);
        LowerToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothLowerModel + "10");
        LowerToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothSidePart.setPos(0f, -1f, 0.2f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.49f, 0.7f);
        LowerToothSidePart = new ExtendableModelRenderer(BodyPartModelNames.sharpToothLowerModel + "11");
        LowerToothSidePart.addLayer(new UVPair(0, 0), TextureList.boneSize, 0, TextureList.bone);
        LowerToothSidePart.setPos(0f, -1.5f, 0.3f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothPart.addChild(ToothSidePart);
        ToothPart.setFirstPersonRenderForSelfAndChildren(false);

        ToothPart.setHitbox(false);
        addModel(BodyPartModelNames.sharpToothLowerModel, ToothPart);

        FPToothPart = ToothPart.clone();
        FPToothPart.setName(BodyPartModelNames.FPsharpToothLowerModel);
        FPToothPart.setNoResizeForSelfAndChildren(true);
        addModel(BodyPartModelNames.FPsharpToothLowerModel, FPToothPart);
    }

    protected void setupLegModels()
    {
        defaultResizeModule reverseJointResizer = new defaultResizeModule(2, new Vec3(0, 1, 0), new Vec3(0.5, 1, 1), new Vec3(3.98, 10, 3.98), new Vec3(0.5, 1, 0));
        defaultResizeModule footResizer = new defaultResizeModule(new Vec3(4, 2, 4));

        ExtendableModelRenderer leftReverseJoint = new ExtendableModelRenderer(BodyPartModelNames.reverseJointLeftLegModel);
        GenericCultivatorTextureValues.addGenericLeftLegLayers(leftReverseJoint);
        leftReverseJoint.setPos(0.75F, 1.0F, 0.5F);
        leftReverseJoint.setRotationPoint(new Vec3(0.5D, 1D, 0.5D));
        leftReverseJoint.setFixedPosAdjustment(0.0F, 0.0F, 0.0F);
        leftReverseJoint.extend(reverseJointResizer);

        ExtendableModelRenderer leftFoot = new ExtendableModelRenderer(BodyPartModelNames.reverseJointLeftFootModel);
        GenericCultivatorTextureValues.addGenericLeftLegLayers(leftFoot, 0, 10);
        leftFoot.setPos(0.5F, 1F, 0F);
        leftFoot.setRotationPoint(new Vec3(0.5D, 1D, 1D));
        leftFoot.extend(footResizer);

        leftReverseJoint.getChildren().get(0).addChild(leftFoot);

        // Reinitialize the resizers
        reverseJointResizer = new defaultResizeModule(2, new Vec3(0, 1, 0), new Vec3(0.5, 1, 1), new Vec3(3.98, 10, 3.98), new Vec3(0.5, 1, 0));
        footResizer = new defaultResizeModule(new Vec3(4, 2, 4));

        ExtendableModelRenderer rightReverseJoint = new ExtendableModelRenderer(BodyPartModelNames.reverseJointRightLegModel);
        GenericCultivatorTextureValues.addGenericRightLegLayers(rightReverseJoint);
        rightReverseJoint.setPos(0.25F, 1.0F, 0.5F);
        rightReverseJoint.setRotationPoint(new Vec3(0.5D, 1, 0.5));
        rightReverseJoint.setFixedPosAdjustment(0.0F, 0.0F, 0F);
        rightReverseJoint.extend(reverseJointResizer);

        ExtendableModelRenderer rightFoot = new ExtendableModelRenderer(BodyPartModelNames.reverseJointRightFootModel);
        GenericCultivatorTextureValues.addGenericRightLegLayers(rightFoot, 0, 10);
        rightFoot.setPos(0.5F, 1F, 0F);
        rightFoot.setRotationPoint(new Vec3(0.5D, 1D, 1D));
        rightFoot.extend(footResizer);

        rightReverseJoint.getChildren().get(0).addChild(rightFoot);

        addModel(BodyPartModelNames.reverseJointLeftLegModel, leftReverseJoint);
        addReference(BodyPartModelNames.reverseJointLeftLegModel, BodyPartModelNames.reverseJointLeftLegLowerModel, leftReverseJoint.getChildren().get(0));
        addReference(BodyPartModelNames.reverseJointLeftLegModel, BodyPartModelNames.reverseJointLeftFootModel, leftFoot);

        addModel(BodyPartModelNames.reverseJointRightLegModel, rightReverseJoint);
        addReference(BodyPartModelNames.reverseJointRightLegModel, BodyPartModelNames.reverseJointRightLegLowerModel, rightReverseJoint.getChildren().get(0));
        addReference(BodyPartModelNames.reverseJointRightLegModel, BodyPartModelNames.reverseJointRightFootModel, rightFoot);


        footResizer = new defaultResizeModule(new Vec3(4, 2, 4));
        ExtendableModelRenderer leftFootModel = new ExtendableModelRenderer( BodyPartModelNames.footLeftModel);
        GenericCultivatorTextureValues.addGenericLeftLegLayers(leftFootModel, 0, 10);
        leftFootModel.setPos(0.75F, 1F, 0.5F);
        leftFootModel.setRotationPoint(new Vec3(0.5D, 1D, 0.5D));
        leftFootModel.extend(footResizer);

        footResizer = new defaultResizeModule(new Vec3(4, 2, 4));
        ExtendableModelRenderer rightFootModel = new ExtendableModelRenderer(BodyPartModelNames.footRightModel);
        GenericCultivatorTextureValues.addGenericRightLegLayers(rightFootModel, 0, 10);
        rightFootModel.setPos(0.25F, 1F, 0.5F);
        rightFootModel.setRotationPoint(new Vec3(0.5D, 1D, 0.5D));
        rightFootModel.extend(footResizer);

        addModel(BodyPartModelNames.footLeftModel, leftFootModel);
        addModel(BodyPartModelNames.footRightModel, rightFootModel);


        ExtendableModelRenderer singleLeg = new ExtendableModelRenderer(BodyPartModelNames.singleLegModel);
        GenericCultivatorTextureValues.addGenericLeftLegLayers(singleLeg);
        singleLeg.setPos(0.5F, 1.0F, 0.5F);
        singleLeg.setRotationPoint(new Vec3(0.5, 0.66, 0.5));
        singleLeg.setFixedPosAdjustment(0F, 2F, 0.0F);
        singleLeg.extend(GenericResizers.getLegResizer());

        addModel(BodyPartModelNames.singleLegModel, singleLeg);
        addReference(BodyPartModelNames.singleLegModel, BodyPartModelNames.singleLegLowerModel, singleLeg.getChildren().get(0));


        defaultResizeModule hexaResizer = new defaultResizeModule(2, new Vec3(0, 1, 0), new Vec3(0.5, 1, 0.5), new Vec3(4, 10, 4), new Vec3(0.5, 1, 0.5));

        ExtendableModelRenderer hexaLeft = new ExtendableModelRenderer(BodyPartModelNames.hexaLeftLegModel);
        GenericCultivatorTextureValues.addGenericLeftLegLayers(hexaLeft);
        hexaLeft.setPos(1F, 1.0F, 0.5F);
        hexaLeft.setRotationPoint(new Vec3(0.5D, 1D, 0.5D));
        hexaLeft.extend(hexaResizer);
        hexaLeft.setDefaultResize(new Vec3(0.5, 1.5, 0.5));
        hexaLeft.getChildren().get(0).setDefaultResize(new Vec3(0.5, 1.5, 0.5));

        ExtendableModelRenderer hexaLeftTwo = hexaLeft.clone();
        ExtendableModelRenderer hexaLeftThree = hexaLeft.clone();

        hexaResizer = new defaultResizeModule(2, new Vec3(0, 1, 0), new Vec3(0.5, 1, 0.5), new Vec3(4, 10, 4), new Vec3(0.5, 1, 0.5));

        ExtendableModelRenderer hexaRight = new ExtendableModelRenderer(BodyPartModelNames.hexaRightLegModel);
        GenericCultivatorTextureValues.addGenericRightLegLayers(hexaRight);
        hexaRight.setPos(0F, 1.0F, 0.5F);
        hexaRight.setRotationPoint(new Vec3(0.5D, 1D, 0.5D));
        hexaRight.extend(hexaResizer);
        hexaRight.setDefaultResize(new Vec3(0.5, 1.5, 0.5));
        hexaRight.getChildren().get(0).setDefaultResize(new Vec3(0.5, 1.5, 0.5));

        ExtendableModelRenderer hexaRightTwo = hexaRight.clone();
        ExtendableModelRenderer hexaRightThree = hexaRight.clone();

        addModel(BodyPartModelNames.hexaLeftLegModel, hexaLeft);
        addReference(BodyPartModelNames.hexaLeftLegModel, BodyPartModelNames.hexaLowerLeftLegModel, hexaLeft.getChildren().get(0));
        addModel(BodyPartModelNames.hexaLeftLegModelTwo, hexaLeftTwo);
        addReference(BodyPartModelNames.hexaLeftLegModelTwo, BodyPartModelNames.hexaLowerLeftLegModelTwo, hexaLeftTwo.getChildren().get(0));
        addModel(BodyPartModelNames.hexaLeftLegModelThree, hexaLeftThree);
        addReference(BodyPartModelNames.hexaLeftLegModelThree, BodyPartModelNames.hexaLowerLeftLegModelThree, hexaLeftThree.getChildren().get(0));

        addModel(BodyPartModelNames.hexaRightLegModel, hexaRight);
        addReference(BodyPartModelNames.hexaRightLegModel, BodyPartModelNames.hexaLowerRightLegModel, hexaRight.getChildren().get(0));
        addModel(BodyPartModelNames.hexaRightLegModelTwo, hexaRightTwo);
        addReference(BodyPartModelNames.hexaRightLegModelTwo, BodyPartModelNames.hexaLowerRightLegModelTwo, hexaRightTwo.getChildren().get(0));
        addModel(BodyPartModelNames.hexaRightLegModelThree, hexaRightThree);
        addReference(BodyPartModelNames.hexaRightLegModelThree, BodyPartModelNames.hexaLowerRightLegModelThree, hexaRightThree.getChildren().get(0));


        ExtendableModelRenderer jLeftLeg = new ExtendableModelRenderer(BodyPartModelNames.jetLegLeftModel);
        GenericCultivatorTextureValues.addGenericLeftLegLayers(jLeftLeg);
        jLeftLeg.setPos(0.25F, 1.0F, 0.5F);
        jLeftLeg.setRotationPoint(new Vec3(0.5, 0.66, 0.5));
        jLeftLeg.setFixedPosAdjustment(0F, 2F, 0.0F);
        jLeftLeg.extend(GenericResizers.getLegResizer());

        ExtendableModelRenderer jRightLeg = new ExtendableModelRenderer(BodyPartModelNames.jetLegRightModel);
        GenericCultivatorTextureValues.addGenericRightLegLayers(jRightLeg);
        jRightLeg.setPos(0.75F, 1.0F, 0.5F);
        jRightLeg.setRotationPoint(new Vec3(0.5, 0.66, 0.5));
        jRightLeg.setFixedPosAdjustment(0F, 2F, 0.0F);
        jRightLeg.extend(GenericResizers.getLegResizer());

        defaultResizeModule rightLegJetResizer = new defaultResizeModule(new Vec3(2, 0.5, -0.1));
        ExtendableModelRenderer rightLegJet = new ExtendableModelRenderer(BodyPartModelNames.jetLegRightModel + "1");
        rightLegJet.addLayer(new UVPair(GenericTextureValues.rightLeg.u() + 1, GenericTextureValues.rightLeg.v() + 6), GenericTextureValues.skin_Size, 0.0F, "PLAYERSKIN");
        rightLegJet.setPos(0.75F, 0F, 1F);
        rightLegJet.setRotationPoint(new Vec3(0, 0, 0));
        rightLegJet.setDefaultResize(new Vec3(1, 1, 20));
        rightLegJet.extend(rightLegJetResizer);

        rightLegJetResizer = new defaultResizeModule(new Vec3(0.5, -4, -0.1));
        ExtendableModelRenderer rightLegJetLeft = new ExtendableModelRenderer( BodyPartModelNames.jetLegRightModel + "2");
        rightLegJetLeft.addLayer(new UVPair(GenericTextureValues.rightLeg.u() + 1, GenericTextureValues.rightLeg.v() + 10), GenericTextureValues.skin_Size, 0.0F, "PLAYERSKIN");
        rightLegJetLeft.setPos(1F, 0F, 1F);
        rightLegJetLeft.setRotationPoint(new Vec3(0, 0, 0));
        rightLegJetLeft.setDefaultResize(new Vec3(1, 1, 20));
        rightLegJetLeft.extend(rightLegJetResizer);

        rightLegJetResizer = new defaultResizeModule(new Vec3(0.5, -4, -0.1));
        ExtendableModelRenderer rightLegJetRight = new ExtendableModelRenderer(BodyPartModelNames.jetLegRightModel + "3");
        rightLegJetRight.addLayer(new UVPair(GenericTextureValues.rightLeg.u() + 3, GenericTextureValues.rightLeg.v() + 10), GenericTextureValues.skin_Size, 0.0F, "PLAYERSKIN");
        rightLegJetRight.setPos(0F, 0F, 1F);
        rightLegJetRight.setRotationPoint(new Vec3(1, 0, 0));
        rightLegJetRight.setDefaultResize(new Vec3(1, 1, 20));
        rightLegJetRight.extend(rightLegJetResizer);

        rightLegJetResizer = new defaultResizeModule(new Vec3(2, -4, -0.1));
        ExtendableModelRenderer rightLegJetFront = new ExtendableModelRenderer( BodyPartModelNames.jetLegRightModel + "4");
        rightLegJetFront.addLayer(new UVPair(GenericTextureValues.rightLeg.u() + 1, GenericTextureValues.rightLeg.v() + 10), GenericTextureValues.skin_Size, 0.0F, "PLAYERSKIN");
        rightLegJetFront.setPos(1F, 0F, 0F);
        rightLegJetFront.setRotationPoint(new Vec3(0, 0, 1));
        rightLegJetFront.setDefaultResize(new Vec3(1, 1, 5));
        rightLegJetFront.extend(rightLegJetResizer);

        rightLegJet.addChild(rightLegJetLeft);
        rightLegJet.addChild(rightLegJetRight);
        rightLegJet.addChild(rightLegJetFront);
        jRightLeg.getChildren().get(0).addChild(rightLegJet);


        defaultResizeModule leftLegJetResizer = new defaultResizeModule(new Vec3(2, 0.5, -0.1));
        ExtendableModelRenderer leftLegJet = new ExtendableModelRenderer(BodyPartModelNames.jetLegLeftModel + "1");
        leftLegJet.addLayer(new UVPair(GenericTextureValues.leftLeg.u() + 1, GenericTextureValues.leftLeg.v() + 6), GenericTextureValues.skin_Size, 0.0F, "PLAYERSKIN");
        leftLegJet.setPos(0.75F, 0F, 1F);
        leftLegJet.setRotationPoint(new Vec3(0, 0, 0));
        leftLegJet.setDefaultResize(new Vec3(1, 1, 20));
        leftLegJet.extend(leftLegJetResizer);

        leftLegJetResizer = new defaultResizeModule(new Vec3(0.5, -4, -0.1));
        ExtendableModelRenderer leftLegJetLeft = new ExtendableModelRenderer(BodyPartModelNames.jetLegLeftModel + "2");
        leftLegJetLeft.addLayer(new UVPair(GenericTextureValues.leftLeg.u() + 1, GenericTextureValues.leftLeg.v() + 10), GenericTextureValues.skin_Size, 0.0F, "PLAYERSKIN");
        leftLegJetLeft.setPos(1F, 0F, 1F);
        leftLegJetLeft.setRotationPoint(new Vec3(0, 0, 0));
        leftLegJetLeft.setDefaultResize(new Vec3(1, 1, 20));
        leftLegJetLeft.extend(leftLegJetResizer);

        leftLegJetResizer = new defaultResizeModule(new Vec3(0.5, -4, -0.1));
        ExtendableModelRenderer leftLegJetRight = new ExtendableModelRenderer(BodyPartModelNames.jetLegLeftModel + "3");
        leftLegJetRight.addLayer(new UVPair(GenericTextureValues.leftLeg.u() + 3, GenericTextureValues.leftLeg.v() + 10), GenericTextureValues.skin_Size, 0.0F, "PLAYERSKIN");
        leftLegJetRight.setPos(0F, 0F, 1F);
        leftLegJetRight.setRotationPoint(new Vec3(1, 0, 0));
        leftLegJetRight.setDefaultResize(new Vec3(1, 1, 20));
        leftLegJetRight.extend(leftLegJetResizer);

        leftLegJetResizer = new defaultResizeModule(new Vec3(2, -4, -0.1));
        ExtendableModelRenderer leftLegJetFront = new ExtendableModelRenderer( BodyPartModelNames.jetLegLeftModel + "4");
        leftLegJetFront.addLayer(new UVPair(GenericTextureValues.leftLeg.u() + 1, GenericTextureValues.leftLeg.v() + 10), GenericTextureValues.skin_Size, 0.0F, "PLAYERSKIN");
        leftLegJetFront.setPos(1F, 0F, 0F);
        leftLegJetFront.setRotationPoint(new Vec3(0, 0, 1));
        leftLegJetFront.setDefaultResize(new Vec3(1, 1, 5));
        leftLegJetFront.extend(leftLegJetResizer);

        leftLegJet.addChild(leftLegJetLeft);
        leftLegJet.addChild(leftLegJetRight);
        leftLegJet.addChild(leftLegJetFront);
        jLeftLeg.getChildren().get(0).addChild(leftLegJet);

        ParticleEmitter leftLegEmitter = new ParticleEmitter(ParticleTypes.FLAME, BodyPartModelNames.jetLegLeftEmitter);
        leftLegEmitter.setPos(0.5f, 8, 0.5f);
        leftLegEmitter.setVelocity(new Vec3(0, -0.5f, 0));
        leftLegEmitter.setInterval(1);
        leftLegEmitter.getModelPart().visible = false;

        ParticleEmitter rightLegEmitter = new ParticleEmitter(ParticleTypes.FLAME, BodyPartModelNames.jetLegRightEmitter);
        rightLegEmitter.setPos(0.5f, 8, 0.5f);
        rightLegEmitter.setVelocity(new Vec3(0, -0.5f, 0));
        rightLegEmitter.setInterval(1);
        rightLegEmitter.getModelPart().visible = false;

        leftLegJet.addChild(leftLegEmitter);
        rightLegJet.addChild(rightLegEmitter);

        addModel(BodyPartModelNames.jetLegLeftModel, jLeftLeg);
        addModel(BodyPartModelNames.jetLegRightModel, jRightLeg);
        addReference(BodyPartModelNames.jetLegLeftModel, BodyPartModelNames.jetLegLeftLowerModel, jLeftLeg.getChildren().get(0));
        addReference(BodyPartModelNames.jetLegRightModel, BodyPartModelNames.jetLegRightLowerModel, jRightLeg.getChildren().get(0));
        addReference(BodyPartModelNames.jetLegLeftModel, BodyPartModelNames.jetLegLeftEmitter, leftLegEmitter);
        addReference(BodyPartModelNames.jetLegRightModel, BodyPartModelNames.jetLegRightEmitter, rightLegEmitter);


        ExtendableModelRenderer largeRightLeg = new ExtendableModelRenderer(BodyPartModelNames.largeLegRightModel);
        GenericCultivatorTextureValues.addGenericRightLegLayers(largeRightLeg);
        largeRightLeg.setPos(0F, 0.25F, 0.5F);
        largeRightLeg.setRotationPoint(new Vec3(0, 0.75, 0.5));
        largeRightLeg.setDefaultResize(new Vec3(2, 2, 2));
        //largeRightLeg.setFixedPosAdjustment(0F, 2F, 0.0F);
        largeRightLeg.extend(GenericResizers.getLegResizer());
        largeRightLeg.getChildren().get(0).setDefaultResize(new Vec3(2, 2, 2));

        ExtendableModelRenderer largeLeftLeg = new ExtendableModelRenderer(BodyPartModelNames.largeLegLeftModel);
        GenericCultivatorTextureValues.addGenericLeftLegLayers(largeLeftLeg);
        largeLeftLeg.setPos(1F, 0.25F, 0.5F);
        largeLeftLeg.setRotationPoint(new Vec3(1, 0.75, 0.5));
        largeLeftLeg.setDefaultResize(new Vec3(2, 2, 2));
        //largeLeftLeg.setFixedPosAdjustment(0F, 2F, 0.0F);
        largeLeftLeg.extend(GenericResizers.getLegResizer());
        largeLeftLeg.getChildren().get(0).setDefaultResize(new Vec3(2, 2, 2));

        addModel(BodyPartModelNames.largeLegLeftModel, largeLeftLeg);
        addModel(BodyPartModelNames.largeLegRightModel, largeRightLeg);
        addReference(BodyPartModelNames.largeLegLeftModel, BodyPartModelNames.largeLegLeftLowerModel, largeLeftLeg.getChildren().get(0));
        addReference(BodyPartModelNames.largeLegRightModel, BodyPartModelNames.largeLegRightLowerModel, largeRightLeg.getChildren().get(0));


        ExtendableModelRenderer leftLongLeg = new ExtendableModelRenderer(BodyPartModelNames.longLegLeftModel);
        GenericCultivatorTextureValues.addGenericLeftLegLayers(leftLongLeg);
        leftLongLeg.setPos(0.25F, 1.0F, 0.5F);
        leftLongLeg.setRotationPoint(new Vec3(0.5, 0.82, 0.5));
        leftLongLeg.setFixedPosAdjustment(0F, 1F, 0.0F);
        leftLongLeg.setDefaultResize(new Vec3(1, 1.5f, 1));
        leftLongLeg.extend(GenericResizers.getLegResizer());
        leftLongLeg.getChildren().get(0).setDefaultResize(new Vec3(1, 1.5f, 1));

        ExtendableModelRenderer rightLongLeg = new ExtendableModelRenderer(BodyPartModelNames.longLegRightModel);
        GenericCultivatorTextureValues.addGenericRightLegLayers(rightLongLeg);
        rightLongLeg.setPos(0.75F, 1.0F, 0.5F);
        rightLongLeg.setRotationPoint(new Vec3(0.5, 0.82, 0.5));
        rightLongLeg.setFixedPosAdjustment(0F, 1F, 0.0F);
        rightLongLeg.setDefaultResize(new Vec3(1, 1.5f, 1));
        rightLongLeg.extend(GenericResizers.getLegResizer());
        rightLongLeg.getChildren().get(0).setDefaultResize(new Vec3(1, 1.5f, 1));

        addModel(BodyPartModelNames.longLegLeftModel, leftLongLeg);
        addModel(BodyPartModelNames.longLegRightModel, rightLongLeg);
        addReference(BodyPartModelNames.longLegLeftModel, BodyPartModelNames.longLegLeftLowerModel, leftLongLeg.getChildren().get(0));
        addReference(BodyPartModelNames.longLegRightModel, BodyPartModelNames.longLegRightLowerModel, rightLongLeg.getChildren().get(0));

        ExtendableModelRenderer defaultLeftLeg = new ExtendableModelRenderer(GenericLimbNames.leftLeg);
        GenericCultivatorTextureValues.addGenericLeftLegLayers(defaultLeftLeg);
        defaultLeftLeg.setPos(0.25F, 1.0F, 0.5F);
        defaultLeftLeg.setRotationPoint(new Vec3(0.5, 0.66, 0.5));
        defaultLeftLeg.setFixedPosAdjustment(0F, 2F, 0.0F);
        defaultLeftLeg.extend(GenericResizers.getLegResizer());

        ExtendableModelRenderer defaultRightLeg = new ExtendableModelRenderer(GenericLimbNames.rightLeg);
        GenericCultivatorTextureValues.addGenericRightLegLayers(defaultRightLeg);
        defaultRightLeg.setPos(0.75F, 1.0F, 0.5F);
        defaultRightLeg.setRotationPoint(new Vec3(0.5, 0.66, 0.5));
        defaultRightLeg.setFixedPosAdjustment(0F, 2F, 0.0F);
        defaultRightLeg.extend(GenericResizers.getLegResizer());

        addModel(GenericLimbNames.leftLeg, defaultLeftLeg);
        addModel(GenericLimbNames.rightLeg, defaultRightLeg);
        addReference(GenericLimbNames.leftLeg, GenericLimbNames.lowerLeftLeg, defaultLeftLeg.getChildren().get(0));
        addReference(GenericLimbNames.rightLeg, GenericLimbNames.lowerRightLeg, defaultRightLeg.getChildren().get(0));
    }

    public void addQuadCollection(String ID, QuadCollection quad)
    {
        quads.put(ID, quad);
    }

    public void addModel(String ID, ExtendableModelRenderer model)
    {
        models.put(ID, model);
    }

    public void addReference(String ID, String referenceID, ExtendableModelRenderer model)
    {
        if (!nonRenderingModels.containsKey(ID))
            nonRenderingModels.put(ID, new HashMap<String, ExtendableModelRenderer>());

        nonRenderingModels.get(ID).put(referenceID, model);
    }

    public HashMap<String, ExtendableModelRenderer> getReferences(String ID)
    {
        if (!nonRenderingModels.containsKey(ID))
            nonRenderingModels.put(ID, new HashMap<String, ExtendableModelRenderer>());

        return nonRenderingModels.get(ID);
    }

    // Returns the specified model
    public ExtendableModelRenderer getModel(String ID)
    {
        return models.get(ID);
    }

    // Returns the specified model
    public ExtendableModelRenderer getModel(String ID, String position)
    {
        ExtendableModelRenderer model = models.get(ID);
        transferQuadLinks(position, model);

        return model;
    }

    // Returns the specified quad
    public QuadCollection getQuadCollection(String ID)
    {
        return quads.get(ID);
    }
}
