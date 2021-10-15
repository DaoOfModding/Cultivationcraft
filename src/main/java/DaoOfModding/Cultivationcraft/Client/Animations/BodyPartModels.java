package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.mlmanimator.Client.Models.*;
import DaoOfModding.mlmanimator.Client.Models.Quads.Quad;
import DaoOfModding.mlmanimator.Client.Models.Quads.QuadLinkage;
import net.minecraft.util.math.vector.Vector3d;

import java.util.HashMap;

public class BodyPartModels
{
    private HashMap<String, ExtendableModelRenderer> models = new HashMap<String, ExtendableModelRenderer>();
    private HashMap<String, HashMap<String, ExtendableModelRenderer>> nonRenderingModels = new HashMap<String, HashMap<String, ExtendableModelRenderer>>();
    private HashMap<String, QuadCollection> quads = new HashMap<String, QuadCollection>();

    public BodyPartModels()
    {
        setupBodyModels();
        setupHeadModels();
        setupLegModels();
        setupArmModels();

        setupWingModels();
    }

    private void setupBodyModels()
    {
        ExtendableModelRenderer body = new ExtendableModelRenderer(16, 16);
        body.setPos(0.0F, 0.0F, 0.0F);
        body.extend(GenericResizers.getBodyResizer());

        addModel(GenericLimbNames.body, body);
    }

    private void setupArmModels()
    {
        ExtendableModelRenderer rightArm = new ExtendableModelRenderer(40, 16);
        rightArm.setRotationPoint(new Vector3d(0.5D, 0.66D, 0.5D));
        rightArm.setPos(0.0F, 0.0F, 0.5F);
        rightArm.setFixedPosAdjustment(-2.0F, 2F, 0.0F);
        rightArm.extend(GenericResizers.getRightArmResizer());

        ExtendableModelRenderer leftArm = new ExtendableModelRenderer(32, 48);
        leftArm.setRotationPoint(new Vector3d(0.5D, 0.66D, 0.5D));
        leftArm.setPos(1.0F, 0.0F, 0.5F);
        leftArm.setFixedPosAdjustment(2.0F, 2F, 0.0F);
        leftArm.extend(GenericResizers.getLeftArmResizer());
        leftArm.mirror = true;

        addModel(GenericLimbNames.leftArm, leftArm);
        addReference(GenericLimbNames.leftArm, GenericLimbNames.lowerLeftArm, leftArm.getChildren().get(0));
        addModel(GenericLimbNames.rightArm, rightArm);
        addReference(GenericLimbNames.rightArm, GenericLimbNames.lowerRightArm, rightArm.getChildren().get(0));

        setupArmOptions();
    }

    private void setupArmOptions()
    {
        Quad larmConnector = new Quad(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),new Vector3d(0, 0, 0),new Vector3d(0, 0, 0));
        Quad rarmConnector = new Quad(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),new Vector3d(0, 0, 0),new Vector3d(0, 0, 0));
        getModel(GenericLimbNames.body).addQuadLinkage(new QuadLinkage(larmConnector, Quad.QuadVertex.TopLeft, new Vector3d(1, 1, 0.5)));
        getModel(GenericLimbNames.body).addQuadLinkage(new QuadLinkage(larmConnector, Quad.QuadVertex.TopRight, new Vector3d(1, 0, 0.5)));

        getModel(GenericLimbNames.body).addQuadLinkage(new QuadLinkage(rarmConnector, Quad.QuadVertex.TopLeft, new Vector3d(0, 0, 0.5)));
        getModel(GenericLimbNames.body).addQuadLinkage(new QuadLinkage(rarmConnector, Quad.QuadVertex.BottomLeft, new Vector3d(0, 1, 0.5)));

        getModel(GenericLimbNames.leftArm).addQuadLinkage(new QuadLinkage(larmConnector, Quad.QuadVertex.BottomLeft, new Vector3d(0, 1, 0.5)));
        getModel(GenericLimbNames.leftArm).getChildren().get(0).addQuadLinkage(new QuadLinkage(larmConnector, Quad.QuadVertex.BottomRight, new Vector3d(0, 0.5, 0.5)));

        getModel(GenericLimbNames.rightArm).addQuadLinkage(new QuadLinkage(rarmConnector, Quad.QuadVertex.TopRight, new Vector3d(0, 1, 0.5)));
        getModel(GenericLimbNames.rightArm).getChildren().get(0).addQuadLinkage(new QuadLinkage(rarmConnector, Quad.QuadVertex.BottomRight, new Vector3d(1, 0.5, 0.5)));


        QuadCollection glidingArms = new QuadCollection();
        glidingArms.addQuad(larmConnector);
        glidingArms.addQuad(rarmConnector);

        addQuadCollection(BodyPartModelNames.armglidequad, glidingArms);
    }

    private void setupHeadModels()
    {
        defaultResizeModule semiHeadResizer = new defaultResizeModule(new Vector3d(8, 5, 8));
        defaultResizeModule jawResizer = new defaultResizeModule(new Vector3d(8, 3, 8));

        // Create the top half of the head
        ExtendableModelRenderer semiHeadPart = new ExtendableModelRenderer(0, 0);
        semiHeadPart.setRotationPoint(new Vector3d(0.5D, 0.0D, 0.5D));
        semiHeadPart.setPos(0.5F, 0.0F, 0.5F);
        semiHeadPart.setFixedPosAdjustment(0, -3, 0);
        semiHeadPart.extend(semiHeadResizer);
        semiHeadPart.setLooking(true);
        semiHeadPart.setFirstPersonRender(false);

        // Create the jaw
        ExtendableModelRenderer jawPart = new ExtendableModelRenderer(0, 5);
        jawPart.setRotationPoint(new Vector3d(0.5D, 1, 0.33));
        jawPart.setPos(0.5F, 1F, 0.66F);
        jawPart.extend(jawResizer);
        jawPart.setFirstPersonRender(false);

        // Attach the jaw to the head
        semiHeadPart.addChild(jawPart);

        addModel(BodyPartModelNames.jawModel, semiHeadPart);
        addReference(BodyPartModelNames.jawModel, BodyPartModelNames.jawModelLower, jawPart);

        setupTeethModels();

        // TODO: Fix first person animations
        // Setup first person jaw models
        semiHeadResizer = new defaultResizeModule(new Vector3d(8, 5, 8));
        jawResizer = new defaultResizeModule(new Vector3d(8, 3, 8));

        // Create the top half of the head
        ExtendableModelRenderer FPsemiHeadPart = new ExtendableModelRenderer(0, 0);
        FPsemiHeadPart.setRotationPoint(new Vector3d(0.5D, 0.0D, 0.5D));
        FPsemiHeadPart.setPos(0.5F, 0.0F, 0.5F);
        FPsemiHeadPart.setFixedPosAdjustment(0, -3, 0);
        FPsemiHeadPart.extend(semiHeadResizer);
        FPsemiHeadPart.setLooking(true);

        // Create the jaw
        ExtendableModelRenderer FPjawPart = new ExtendableModelRenderer(0, 5);
        FPjawPart.setRotationPoint(new Vector3d(0.5D, 1, 0));
        FPjawPart.setPos(0.5F, 1F, 1F);
        FPjawPart.extend(jawResizer);

        // Attach the jaw to the head
        FPsemiHeadPart.addChild(FPjawPart);

        addModel(BodyPartModelNames.FPjawModel, FPsemiHeadPart);
        addReference(BodyPartModelNames.FPjawModel, BodyPartModelNames.FPjawModelLower, FPjawPart);
    }

    private void setupWingModels()
    {
        defaultResizeModule wingTop = new defaultResizeModule(new Vector3d(16, 0.5, 1));
        defaultResizeModule lwingTop = new defaultResizeModule(new Vector3d(-16, 0.5, 1));

        ExtendableModelRenderer wingTopModel = new ExtendableModelRenderer(0, 0);
        wingTopModel.setPos(0.5F, 0.25F, 1F);
        wingTopModel.setRotationPoint(new Vector3d(1, 0.5f, 0f));
        wingTopModel.extend(wingTop);

        ExtendableModelRenderer lwingTopModel = new ExtendableModelRenderer(0, 0);
        lwingTopModel.setPos(0.5F, 0.25F, 1F);
        lwingTopModel.setRotationPoint(new Vector3d(1, 0.5f, 0f));
        lwingTopModel.extend(lwingTop);


        defaultResizeModule wingStrand = new defaultResizeModule(new Vector3d(0.5, 16, 1));
        defaultResizeModule lwingStrand = new defaultResizeModule(new Vector3d(0.5, 16, 1));

        ExtendableModelRenderer wingStrandModel = new ExtendableModelRenderer(0, 0);
        wingStrandModel.setPos(1, 1F, 0.5F);
        wingStrandModel.setRotationPoint(new Vector3d(0.5F, 1, 0.5f));
        wingStrandModel.extend(wingStrand);

        ExtendableModelRenderer lwingStrandModel = new ExtendableModelRenderer(0, 0);
        lwingStrandModel.setPos(1, 1F, 0.5F);
        lwingStrandModel.setRotationPoint(new Vector3d(0.5F, 1, 0.5f));
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


        Quad wing12Connector = new Quad(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),new Vector3d(0, 0, 0),new Vector3d(0, 0, 0));
        wingStrandModel.addQuadLinkage(new QuadLinkage(wing12Connector, Quad.QuadVertex.TopLeft, new Vector3d(0, 0, 0.5)));
        wingStrandModel.addQuadLinkage(new QuadLinkage(wing12Connector, Quad.QuadVertex.BottomLeft, new Vector3d(0, 0.95, 0.5)));
        wingStrandModel2.addQuadLinkage(new QuadLinkage(wing12Connector, Quad.QuadVertex.TopRight, new Vector3d(1, 0, 0.5)));
        wingStrandModel2.addQuadLinkage(new QuadLinkage(wing12Connector, Quad.QuadVertex.BottomRight, new Vector3d(1, 0.95, 0.5)));

        Quad wing23Connector = new Quad(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),new Vector3d(0, 0, 0),new Vector3d(0, 0, 0));
        wingStrandModel2.addQuadLinkage(new QuadLinkage(wing23Connector, Quad.QuadVertex.TopLeft, new Vector3d(0, 0, 0.5)));
        wingStrandModel2.addQuadLinkage(new QuadLinkage(wing23Connector, Quad.QuadVertex.BottomLeft, new Vector3d(0, 0.95, 0.5)));
        wingStrandModel3.addQuadLinkage(new QuadLinkage(wing23Connector, Quad.QuadVertex.TopRight, new Vector3d(1, 0, 0.5)));
        wingStrandModel3.addQuadLinkage(new QuadLinkage(wing23Connector, Quad.QuadVertex.BottomRight, new Vector3d(1, 0.95, 0.5)));

        Quad wing34Connector = new Quad(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),new Vector3d(0, 0, 0),new Vector3d(0, 0, 0));
        wingStrandModel3.addQuadLinkage(new QuadLinkage(wing34Connector, Quad.QuadVertex.TopLeft, new Vector3d(0, 0, 0.5)));
        wingStrandModel3.addQuadLinkage(new QuadLinkage(wing34Connector, Quad.QuadVertex.BottomLeft, new Vector3d(0, 0.95, 0.5)));
        wingStrandModel4.addQuadLinkage(new QuadLinkage(wing34Connector, Quad.QuadVertex.TopRight, new Vector3d(1, 0, 0.5)));
        wingStrandModel4.addQuadLinkage(new QuadLinkage(wing34Connector, Quad.QuadVertex.BottomRight, new Vector3d(1, 0.95, 0.5)));

        Quad wing45Connector = new Quad(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),new Vector3d(0, 0, 0),new Vector3d(0, 0, 0));
        wingStrandModel4.addQuadLinkage(new QuadLinkage(wing45Connector, Quad.QuadVertex.TopLeft, new Vector3d(0, 0, 0.5)));
        wingStrandModel4.addQuadLinkage(new QuadLinkage(wing45Connector, Quad.QuadVertex.BottomLeft, new Vector3d(0, 0.95, 0.5)));
        wingTopModel.addQuadLinkage(new QuadLinkage(wing45Connector, Quad.QuadVertex.TopRight, new Vector3d(1, 0, 0.5)));
        wingTopModel.addQuadLinkage(new QuadLinkage(wing45Connector, Quad.QuadVertex.BottomRight, new Vector3d(0, 0, 0.5)));


        Quad lwing12Connector = new Quad(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),new Vector3d(0, 0, 0),new Vector3d(0, 0, 0));
        lwingStrandModel.addQuadLinkage(new QuadLinkage(lwing12Connector, Quad.QuadVertex.TopLeft, new Vector3d(0, 0, 0.5)));
        lwingStrandModel.addQuadLinkage(new QuadLinkage(lwing12Connector, Quad.QuadVertex.TopRight, new Vector3d(0, 0.95, 0.5)));
        lwingStrandModel2.addQuadLinkage(new QuadLinkage(lwing12Connector, Quad.QuadVertex.BottomLeft, new Vector3d(1, 0, 0.5)));
        lwingStrandModel2.addQuadLinkage(new QuadLinkage(lwing12Connector, Quad.QuadVertex.BottomRight, new Vector3d(1, 0.95, 0.5)));

        Quad lwing23Connector = new Quad(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),new Vector3d(0, 0, 0),new Vector3d(0, 0, 0));
        lwingStrandModel2.addQuadLinkage(new QuadLinkage(lwing23Connector, Quad.QuadVertex.TopLeft, new Vector3d(0, 0, 0.5)));
        lwingStrandModel2.addQuadLinkage(new QuadLinkage(lwing23Connector, Quad.QuadVertex.TopRight, new Vector3d(0, 0.95, 0.5)));
        lwingStrandModel3.addQuadLinkage(new QuadLinkage(lwing23Connector, Quad.QuadVertex.BottomLeft, new Vector3d(1, 0, 0.5)));
        lwingStrandModel3.addQuadLinkage(new QuadLinkage(lwing23Connector, Quad.QuadVertex.BottomRight, new Vector3d(1, 0.95, 0.5)));

        Quad lwing34Connector = new Quad(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),new Vector3d(0, 0, 0),new Vector3d(0, 0, 0));
        lwingStrandModel3.addQuadLinkage(new QuadLinkage(lwing34Connector, Quad.QuadVertex.TopLeft, new Vector3d(0, 0, 0.5)));
        lwingStrandModel3.addQuadLinkage(new QuadLinkage(lwing34Connector, Quad.QuadVertex.TopRight, new Vector3d(0, 0.95, 0.5)));
        lwingStrandModel4.addQuadLinkage(new QuadLinkage(lwing34Connector, Quad.QuadVertex.BottomLeft, new Vector3d(1, 0, 0.5)));
        lwingStrandModel4.addQuadLinkage(new QuadLinkage(lwing34Connector, Quad.QuadVertex.BottomRight, new Vector3d(1, 0.95, 0.5)));

        Quad lwing45Connector = new Quad(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),new Vector3d(0, 0, 0),new Vector3d(0, 0, 0));
        lwingStrandModel4.addQuadLinkage(new QuadLinkage(lwing45Connector, Quad.QuadVertex.TopLeft, new Vector3d(0, 0, 0.5)));
        lwingStrandModel4.addQuadLinkage(new QuadLinkage(lwing45Connector, Quad.QuadVertex.TopRight, new Vector3d(0, 0.95, 0.5)));
        lwingTopModel.addQuadLinkage(new QuadLinkage(lwing45Connector, Quad.QuadVertex.BottomLeft, new Vector3d(1, 0, 0.5)));
        lwingTopModel.addQuadLinkage(new QuadLinkage(lwing45Connector, Quad.QuadVertex.BottomRight, new Vector3d(0, 0, 0.5)));


        QuadCollection wingQuads = new QuadCollection();
        wingQuads.addQuad(wing12Connector);
        wingQuads.addQuad(wing23Connector);
        wingQuads.addQuad(wing34Connector);
        wingQuads.addQuad(wing45Connector);
        wingQuads.addQuad(lwing12Connector);
        wingQuads.addQuad(lwing23Connector);
        wingQuads.addQuad(lwing34Connector);
        wingQuads.addQuad(lwing45Connector);


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

    private void setupTeethModels()
    {
        setupFlatTeethModels();
        setupSharpTeethModels();
    }

    private void setupFlatTeethModels()
    {
        // Create row of top teeth
        defaultResizeModule flatToothResizer = QiResizers.getTeethResizer(8, 8f, 1, 0.1f);

        ExtendableModelRenderer flatToothPart = new ExtendableModelRenderer(64, 64, 0, 0);
        flatToothPart.setPos(0f, 1f, 0f);
        flatToothPart.setFixedPosAdjustment(0.05f, 0, 0.05f);
        flatToothPart.setRotationPoint(new Vector3d(1f, 1, 1));
        flatToothPart.extend(flatToothResizer);

        ExtendableModelRenderer lastTooth = flatToothPart;

        while (lastTooth.getChildren().size() > 0)
            lastTooth = lastTooth.getChildren().get(0);


        //Setup side teeth
        flatToothResizer = QiResizers.getSideTeethResizer(6, 5.8f, 1, 0.1f);

        ExtendableModelRenderer flatToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        flatToothSidePart.setPos(1f, 0.5f, 1f);
        flatToothSidePart.setFixedPosAdjustment(0f, 0, 0.1f);
        flatToothSidePart.setRotationPoint(new Vector3d(0f, 0.5, 1));
        flatToothSidePart.extend(flatToothResizer);

        flatToothPart.addChild(flatToothSidePart);

        flatToothResizer = QiResizers.getSideTeethResizer(6, 5.8f, 1, 0.1f);

        flatToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        flatToothSidePart.setPos(1f, 0.5f, 1f);
        flatToothSidePart.setFixedPosAdjustment(0f, 0, 0.1f);
        flatToothSidePart.setRotationPoint(new Vector3d(0f, 0.5, 1));
        flatToothSidePart.extend(flatToothResizer);

        lastTooth.addChild(flatToothSidePart);


        addModel(BodyPartModelNames.flatToothModel, flatToothPart);

        // Create row of bottom teeth
        flatToothResizer = QiResizers.getTeethResizer(8, 7.8f, 1, 0.1f);

        flatToothPart = new ExtendableModelRenderer(64, 64, 0, 0);
        flatToothPart.setPos(0f, 0, 0);
        flatToothPart.setFixedPosAdjustment(0.05f, 0, 0.05f);
        flatToothPart.setRotationPoint(new Vector3d(1f, 0, 1));
        flatToothPart.extend(flatToothResizer);
        flatToothPart.setRotationOffset(new Vector3d(Math.toRadians(-20), 0, 0));

        lastTooth = flatToothPart;

        while (lastTooth.getChildren().size() > 0)
            lastTooth = lastTooth.getChildren().get(0);

        // Create row of bottom teeth
        flatToothResizer = QiResizers.getSideTeethResizer(6, 5.8f, 1, 0.1f);

        flatToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        flatToothSidePart.setPos(1f, 0.5f, 1f);
        flatToothSidePart.setFixedPosAdjustment(0f, 0, 0.1f);
        flatToothSidePart.setRotationPoint(new Vector3d(0f, 0.5, 1));
        flatToothSidePart.extend(flatToothResizer);

        flatToothPart.addChild(flatToothSidePart);

        flatToothResizer = QiResizers.getSideTeethResizer(6, 5.8f, 1, 0.1f);

        flatToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        flatToothSidePart.setPos(1f, 0.5f, 1f);
        flatToothSidePart.setFixedPosAdjustment(0f, 0, 0.1f);
        flatToothSidePart.setRotationPoint(new Vector3d(0f, 0.5, 1));
        flatToothSidePart.extend(flatToothResizer);

        lastTooth.addChild(flatToothSidePart);

        flatToothPart.setFirstPersonRenderForSelfAndChildren(false);

        addModel(BodyPartModelNames.flatToothLowerModel, flatToothPart);
    }

    private void setupSharpTeethModels()
    {
        // Create row of top teeth
        defaultResizeModule ToothResizer = QiResizers.getTeethResizer(8, 7.9f, 0.5f, 0.1f);
        ExtendableModelRenderer ToothPart = new ExtendableModelRenderer(64, 64, 0, 0);
        ToothPart.setPos(0f, 1f, 0f);
        ToothPart.setFixedPosAdjustment(0.05f, 0, 0.05f);
        ToothPart.setRotationPoint(new Vector3d(1f, 1, 1));
        ToothPart.extend(ToothResizer);

        ToothResizer = QiResizers.getTeethResizer(8, 7.9f, 0.5f, 0.3f);
        ExtendableModelRenderer LowerToothPart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothPart.setPos(0.1f, 0.5f, 0f);
        LowerToothPart.extend(ToothResizer);

        ToothPart.addChild(LowerToothPart);

        ToothResizer = QiResizers.getTeethResizer(8, 7.9f, 0.5f, 0.5f);
        LowerToothPart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothPart.setPos(0.2f, 1f, 0f);
        LowerToothPart.extend(ToothResizer);

        ToothPart.addChild(LowerToothPart);

        ToothResizer = QiResizers.getTeethResizer(8, 7.9f, 0.49f, 0.7f);
        LowerToothPart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothPart.setPos(0.3f, 1.5f, 0f);
        LowerToothPart.extend(ToothResizer);

        ToothPart.addChild(LowerToothPart);

        //Setup side teeth
        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.5f, 0.1f);

        ExtendableModelRenderer ToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        ToothSidePart.setPos(0f, 0f, 1.1f);
        ToothSidePart.extend(ToothResizer);

        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.5f, 0.3f);
        ExtendableModelRenderer LowerToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothSidePart.setPos(0f, 0.5f, 0.1f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.5f, 0.5f);
        LowerToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothSidePart.setPos(0f, 1f, 0.2f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.49f, 0.7f);
        LowerToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothSidePart.setPos(0f, 1.5f, 0.3f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothPart.addChild(ToothSidePart);


        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.5f, 0.1f);

        ToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        ToothSidePart.setPos(7.8f, 0f, 1.1f);
        ToothSidePart.extend(ToothResizer);

        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.5f, 0.3f);
        LowerToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothSidePart.setPos(0f, 0.5f, 0.1f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.5f, 0.5f);
        LowerToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothSidePart.setPos(0f, 1f, 0.2f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(5, 4.8f, 0.49f, 0.7f);
        LowerToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothSidePart.setPos(0f, 1.5f, 0.3f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothPart.addChild(ToothSidePart);
        ToothPart.setFirstPersonRenderForSelfAndChildren(false);


        addModel(BodyPartModelNames.sharpToothModel, ToothPart);


        // Create row of bottom teeth
        ToothResizer = QiResizers.getTeethResizer(7, 7f, 0.5f, 0.1f);
        ToothPart = new ExtendableModelRenderer(64, 64, 0, 0);
        ToothPart.setPos(0f, 0f, 0.33f);
        ToothPart.setFixedPosAdjustment(0.55f, 0, 0.1f);
        ToothPart.setRotationPoint(new Vector3d(1f, 0, 1));
        ToothPart.extend(ToothResizer);
        ToothPart.setRotationOffset(new Vector3d(Math.toRadians(-20), 0, 0));

        ToothResizer = QiResizers.getTeethResizer(7, 7f, 0.5f, 0.3f);
        LowerToothPart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothPart.setPos(0.1f, -0.5f, 0f);
        LowerToothPart.extend(ToothResizer);

        ToothPart.addChild(LowerToothPart);

        ToothResizer = QiResizers.getTeethResizer(7, 7f, 0.5f, 0.5f);
        LowerToothPart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothPart.setPos(0.2f, -1f, 0f);
        LowerToothPart.extend(ToothResizer);

        ToothPart.addChild(LowerToothPart);

        ToothResizer = QiResizers.getTeethResizer(7, 7f, 0.49f, 0.7f);
        LowerToothPart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothPart.setPos(0.3f, -1.5f, 0f);
        LowerToothPart.extend(ToothResizer);

        ToothPart.addChild(LowerToothPart);


        float sideTeethLength = 3.7f;

        //Setup side teeth
        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.5f, 0.1f);

        ToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        ToothSidePart.setPos(-0.5f, 0f, 0.4f);
        ToothSidePart.extend(ToothResizer);

        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.5f, 0.3f);
        LowerToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothSidePart.setPos(0f, -0.5f, 0.1f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.5f, 0.5f);
        LowerToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothSidePart.setPos(0f, -1f, 0.2f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.49f, 0.7f);
        LowerToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothSidePart.setPos(0f, -1.5f, 0.3f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothPart.addChild(ToothSidePart);


        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.5f, 0.1f);

        ToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        ToothSidePart.setPos(7.2f, 0f, 0.4f);
        ToothSidePart.extend(ToothResizer);

        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.5f, 0.3f);
        LowerToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothSidePart.setPos(0f, -0.5f, 0.1f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.5f, 0.5f);
        LowerToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothSidePart.setPos(0f, -1f, 0.2f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothResizer = QiResizers.getSideTeethResizer(4, sideTeethLength, 0.49f, 0.7f);
        LowerToothSidePart = new ExtendableModelRenderer(64, 64, 0, 0);
        LowerToothSidePart.setPos(0f, -1.5f, 0.3f);
        LowerToothSidePart.extend(ToothResizer);
        ToothSidePart.addChild(LowerToothSidePart);

        ToothPart.addChild(ToothSidePart);
        ToothPart.setFirstPersonRenderForSelfAndChildren(false);

        addModel(BodyPartModelNames.sharpToothLowerModel, ToothPart);
    }

    private void setupLegModels()
    {
        defaultResizeModule reverseJointResizer = new defaultResizeModule(2, new Vector3d(0, 1, 0), new Vector3d(0.5, 1, 1), new Vector3d(3.98, 10, 3.98), new Vector3d(0.5, 1, 0));
        defaultResizeModule footResizer = new defaultResizeModule(new Vector3d(4, 2, 4));

        ExtendableModelRenderer leftReverseJoint = new ExtendableModelRenderer(0, 16);
        leftReverseJoint.setPos(1.0F, 1.0F, 0F);
        leftReverseJoint.setRotationPoint(new Vector3d(0.0D, 1D, 1D));
        //leftReverseJoint.setFixedPosAdjustment(0.0F, -1.0F, 0.0F);
        leftReverseJoint.extend(reverseJointResizer);
        leftReverseJoint.mirror = true;

        ExtendableModelRenderer leftFoot = new ExtendableModelRenderer(0, 26);
        leftFoot.setPos(0.5F, 1F, 0F);
        leftFoot.setRotationPoint(new Vector3d(0.5D, 1D, 1D));
        leftFoot.extend(footResizer);
        leftFoot.mirror = true;

        leftReverseJoint.getChildren().get(0).addChild(leftFoot);

        // Reinitialize the resizers
        reverseJointResizer = new defaultResizeModule(2, new Vector3d(0, 1, 0), new Vector3d(0.5, 1, 1), new Vector3d(3.98, 10, 3.98), new Vector3d(0.5, 1, 0));
        footResizer = new defaultResizeModule(new Vector3d(4, 2, 4));

        ExtendableModelRenderer rightReverseJoint = new ExtendableModelRenderer(0, 16);
        rightReverseJoint.setPos(0.0F, 1.0F, 0);
        rightReverseJoint.setRotationPoint(new Vector3d(1.0D, 1, 1));
        //rightReverseJoint.setFixedPosAdjustment(0.0F, -1.0F, 0.0F);
        rightReverseJoint.extend(reverseJointResizer);

        ExtendableModelRenderer rightFoot = new ExtendableModelRenderer(0, 26);
        rightFoot.setPos(0.5F, 1F, 0F);
        rightFoot.setRotationPoint(new Vector3d(0.5D, 1D, 1D));
        rightFoot.extend(footResizer);

        rightReverseJoint.getChildren().get(0).addChild(rightFoot);

        addModel(BodyPartModelNames.reverseJointLeftLegModel, leftReverseJoint);
        addReference(BodyPartModelNames.reverseJointLeftLegModel, BodyPartModelNames.reverseJointLeftLegLowerModel, leftReverseJoint.getChildren().get(0));
        addReference(BodyPartModelNames.reverseJointLeftLegModel, BodyPartModelNames.reverseJointLeftFootModel, leftFoot);

        addModel(BodyPartModelNames.reverseJointRightLegModel, rightReverseJoint);
        addReference(BodyPartModelNames.reverseJointRightLegModel, BodyPartModelNames.reverseJointRightLegLowerModel, rightReverseJoint.getChildren().get(0));
        addReference(BodyPartModelNames.reverseJointRightLegModel, BodyPartModelNames.reverseJointRightFootModel, rightFoot);
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

    // Returns the specified quad
    public QuadCollection getQuadCollection(String ID)
    {
        return quads.get(ID);
    }
}
