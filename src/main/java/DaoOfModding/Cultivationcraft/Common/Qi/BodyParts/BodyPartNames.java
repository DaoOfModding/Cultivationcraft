package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartLocation;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Textures.TextureList;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts.*;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.CarnivoreFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.HerbivoreFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.DefaultPlayerBodyPartWeights;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;

public class BodyPartNames
{
    // PARTS
    public static final String jawPart = "jaw";
    public static final String longNeckPart = "longneck";

    public static final String reinforcedBodyPart = "rbody";
    public static final String reinforcedArmPart = "rarm";
    public static final String reinforcedLegPart = "rleg";
    public static final String reinforcedHeadPart = "rhead";

    public static final String expandingBodyPart = "expandingbody";
    public static final String shortBodyPart = "shortbody";

    public static final String glideArmPart = "glidearm";

    // ARMS
    public static final String shortArmPart = "shortarm";
    public static final String flipperArmPart = "flipperarm";

    public static final String headArmsPart = "headarms";

    // LEGS
    public static final String feetPart = "feet";
    public static final String largeLegPart = "largeleg";
    public static final String reverseJointLegPart = "rjleg";
    public static final String hexaLegPart = "hexaleg";
    public static final String singleLegPart = "oneleg";
    public static final String jetLegPart = "jetleg";

    // OPTIONS
    public static final String reinforceSkinPart = "reinforceSkin";
    public static final String reinforceBonePart = "reinforceBone";

    public static final String startingEyesPart = "qisight";

    public static final String flatTeethPart = "flatteeth";
    public static final String sharpTeethPart = "sharpteeth";

    public static final String wingPart = "wing";
    public static final String insectwingPart = "iwing";
    public static final String jetPart = "jet";

    public static final String rubberSkinPart = "rubber";
    public static final String stretchySkinPart = "stretchy";

    public static final String expandingStomachPart = "expandingStomach";
    public static final String carnivorousStomachPart = "carnivorousStomach";
    public static final String herbivorousStomachPart = "herbivorousStomachPart";


    // LOCATIONS
    public static final String headFrontPart = "headfront";

    // DEFAULTS
    public static final String DefaultLeftArm = "armleft";
    public static final String DefaultRightArm = "armright";
    public static final String DefaultRightLeg = "legright";
    public static final String DefaultLeftLeg = "legleft";
    public static final String DefaultHead = "head";
    public static final String DefaultBody = "body";

    // POSITIONS
    public static final String headPosition = "HEAD";
    public static final String bodyPosition = "BODY";
    public static final String armPosition = "ARM";
    public static final String legPosition = "LEG";

    // SUB-POSITIONS
    public static final String basePosition = "BASE";

    public static final String locationSubPosition = "LOCATION";

    public static final String eyeSubPosition = "EYE";
    public static final String mouthSubPosition = "MOUTH";
    public static final String skinSubPosition = "SKIN";
    public static final String boneSubPosition = "BONE";
    public static final String stomachSubPosition = "STOMACH";
    public static final String bloodSubPosition = "BLOOD";
    public static final String backSubPosition = "BACK";


    private static ArrayList<BodyPart> parts = new ArrayList<BodyPart>();
    private static ArrayList<BodyPartOption> options = new ArrayList<BodyPartOption>();
    private static HashMap<String, String> displayNames = new HashMap<String, String>();
    private static HashMap<String, HashMap<String, String>> subPartDisplayNames = new HashMap<String, HashMap<String, String>>();

    public static void init()
    {
        addDisplayName(headPosition, "cultivationcraft.gui.headpart");
        addDisplayName(bodyPosition, "cultivationcraft.gui.bodypart");
        addDisplayName(armPosition, "cultivationcraft.gui.armpart");
        addDisplayName(legPosition, "cultivationcraft.gui.legpart");

        addDisplayName(basePosition, "cultivationcraft.gui.generic.base");

        addSubPartDisplayName(bodyPosition, skinSubPosition, "cultivationcraft.gui.bodypart.skin");
        addSubPartDisplayName(bodyPosition, boneSubPosition, "cultivationcraft.gui.bodypart.bone");
        addSubPartDisplayName(bodyPosition, stomachSubPosition, "cultivationcraft.gui.bodypart.stomach");
        addSubPartDisplayName(bodyPosition, bloodSubPosition, "cultivationcraft.gui.bodypart.blood");
        addSubPartDisplayName(bodyPosition, backSubPosition, "cultivationcraft.gui.bodypart.back");
        addSubPartDisplayName(headPosition, eyeSubPosition, "cultivationcraft.gui.headpart.eye");
        addSubPartDisplayName(headPosition, mouthSubPosition, "cultivationcraft.gui.headpart.mouth");
        addSubPartDisplayName(bodyPosition, locationSubPosition, "cultivationcraft.gui.generic.location");
        addSubPartDisplayName(armPosition, locationSubPosition, "cultivationcraft.gui.generic.location");
        addSubPartDisplayName(headPosition, locationSubPosition, "cultivationcraft.gui.generic.location");


        setupBodyParts();
        setupBodyOptions();

        setupHeadParts();
        setupHeadOptions();

        setupArmParts();

        setupLegParts();
    }

    private static void setupBodyOptions()
    {
        BodyPartOption reinforceBones = new BodyPartOption(reinforceBonePart, bodyPosition, boneSubPosition,  "cultivationcraft.gui.generic.reinforce", 1000);
        reinforceBones.addTextureChange(TextureList.bone, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        reinforceBones.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        reinforceBones.getStatChanges().setStat(StatIDs.boneAttackModifier, 1);

        addOption(reinforceBones);

        setupSkinOptions();
        setupStomachOptions();
        setupBackOptions();


        BodyPartOption longneck = new BodyPartOption(longNeckPart, bodyPosition, locationSubPosition, "cultivationcraft.gui.headpart.longneck", 1000);
        longneck.addModel(BodyPartModelNames.longNeckModel);

        BodyPartLocation longneckLocation = new BodyPartLocation(headPosition, basePosition, BodyPartModelNames.longNeckModelEnd);
        longneckLocation.adjustDepth(9);
        longneck.setConnection(longneckLocation);

        longneck.addNeededPosition(BodyPartNames.headPosition, BodyPartNames.basePosition);
        longneck.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.headWeight * 0.5f);

        addOption(longneck);
    }

    private static void setupStomachOptions()
    {
        ExpandingStomachPart expandingStomach = new ExpandingStomachPart(expandingStomachPart, bodyPosition, stomachSubPosition,  "cultivationcraft.gui.bodypart.stomach.expanding", 1000);
        expandingStomach.addUniqueTag(BodyPartTags.hunger);
        expandingStomach.addUniqueTag(BodyPartTags.expanding);
        expandingStomach.addNeededPart(expandingBodyPart);
        expandingStomach.getStatChanges().setStat(StatIDs.maxStamina, 80);

        StomachPart carnivorousStomach = new StomachPart(carnivorousStomachPart, bodyPosition, stomachSubPosition,  "cultivationcraft.gui.bodypart.stomach.carnivorous", 1000);
        carnivorousStomach.addUniqueTag(BodyPartTags.hunger);
        carnivorousStomach.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        carnivorousStomach.getStatChanges().setStat(StatIDs.maxStamina, 20);
        carnivorousStomach.setFoodStats(new CarnivoreFoodStats());

        StomachPart herbivoreStomach = new StomachPart(herbivorousStomachPart, bodyPosition, stomachSubPosition,  "cultivationcraft.gui.bodypart.stomach.herbivorous", 1000);
        herbivoreStomach.addUniqueTag(BodyPartTags.hunger);
        herbivoreStomach.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        herbivoreStomach.getStatChanges().setStat(StatIDs.maxStamina, 20);
        herbivoreStomach.setFoodStats(new HerbivoreFoodStats());

        addOption(expandingStomach);
        addOption(carnivorousStomach);
        addOption(herbivoreStomach);
    }

    private static void setupSkinOptions()
    {
        BodyPartOption rubberSkin = new BodyPartOption(rubberSkinPart, bodyPosition, skinSubPosition,  "cultivationcraft.gui.bodypart.skin.rubber", 1000);
        //rubberSkin.addTextureChange(TextureList.skin, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        rubberSkin.getStatChanges().setStat(StatIDs.bounceHeight, 0.5f);
        rubberSkin.addNeededPart(BodyPartNames.startingEyesPart);
        rubberSkin.addUniqueTag(BodyPartTags.stretchy);
        rubberSkin.getStatChanges().setStat(StatIDs.fallHeight, 99);

        BodyPartOption stretchySkin = new BodyPartOption(stretchySkinPart, bodyPosition, skinSubPosition,  "cultivationcraft.gui.bodypart.skin.stretchy", 1000);
        //stretchySkin.addTextureChange(TextureList.skin, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        stretchySkin.addUniqueTag(BodyPartTags.stretchy);
        stretchySkin.addNeededPart(BodyPartNames.startingEyesPart);

        BodyPartOption reinforceSkin = new BodyPartOption(reinforceSkinPart, bodyPosition, skinSubPosition,  "cultivationcraft.gui.generic.reinforce", 1000);
        //rubberSkin.addTextureChange(TextureList.skin, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        reinforceSkin.addNeededPart(BodyPartNames.startingEyesPart);

        addOption(rubberSkin);
        addOption(stretchySkin);
        addOption(reinforceSkin);
    }


    private static void setupBackOptions()
    {
        BodyPartOption addWings = new BodyPartOption(wingPart, bodyPosition, backSubPosition,  "cultivationcraft.gui.bodypart.back.wings", 1000);
        addWings.addModel(BodyPartModelNames.rwingUpperArmModel);
        addWings.addModel(BodyPartModelNames.lwingUpperArmModel);
        addWings.addQuad(BodyPartModelNames.wingquad);
        addWings.setTexture(TextureList.bone);
        addWings.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.boneSubPosition);
        addWings.addUniqueTag(BodyPartTags.flight);
        addWings.getStatChanges().setStat(StatIDs.weight, 0.2f);
        addWings.getStatChanges().setStat(StatIDs.wingSupport, 2f);

        BodyPartOption addIWings = new BodyPartOption(insectwingPart, bodyPosition, backSubPosition,  "cultivationcraft.gui.bodypart.back.iwings", 1000);
        addIWings.addModel(BodyPartModelNames.rinsectWing);
        addIWings.addModel(BodyPartModelNames.linsectWing);
        addIWings.addQuad(BodyPartModelNames.insectQuads);
        addIWings.setTexture(TextureList.bone);
        addIWings.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.boneSubPosition);
        addIWings.addUniqueTag(BodyPartTags.flight);
        addIWings.getStatChanges().setStat(StatIDs.weight, 0.01f);
        addIWings.getStatChanges().setStat(StatIDs.wingSupport, 0.75f);


        JetPart jets = new JetPart(jetPart, bodyPosition, backSubPosition,  "cultivationcraft.gui.bodypart.back.jet", 1000);
        jets.addModel(BodyPartModelNames.jetLeft);
        jets.addModel(BodyPartModelNames.jetRight);
        jets.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        jets.getStatChanges().setStat(StatIDs.weight, 0.04f);

        addOption(addWings);
        addOption(addIWings);
        addOption(jets);
    }

    private static void setupBodyParts()
    {
        BodyPart reinforce = new BodyPart(reinforcedBodyPart, bodyPosition, "cultivationcraft.gui.generic.reinforce", 1000);
        reinforce.addModel(GenericLimbNames.body);
        reinforce.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        reinforce.getStatChanges().setStat(StatIDs.maxHP, StatIDs.defaultMaxHP);

        BodyPart expanding = new BodyPart(expandingBodyPart, bodyPosition, "cultivationcraft.gui.bodypart.expanding", 1000);
        expanding.addModel(GenericLimbNames.body);
        expanding.addNeededTags(BodyPartTags.stretchy);
        expanding.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        expanding.getStatChanges().setStat(StatIDs.maxHP, StatIDs.defaultMaxHP);

        BodyPart shortBody = new BodyPart(shortBodyPart, bodyPosition, "cultivationcraft.gui.bodypart.short", 1000);
        shortBody.addModel(BodyPartModelNames.shortBody);
        shortBody.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        shortBody.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.bodyWeight / -2);

        addPart(reinforce);
        addPart(expanding);
        addPart(shortBody);
    }

    private static void setupHeadParts()
    {
        BodyPart jaw = new BodyPart(jawPart, headPosition, "cultivationcraft.gui.headpart.jaw", 1000);
        jaw.addModel(BodyPartModelNames.jawModel);
        jaw.addFirstPersonModel(BodyPartModelNames.FPjawModel);
        jaw.setViewPoint(BodyPartModelNames.jawModel);
        jaw.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);

        addPart(jaw);
    }

    private static void setupArmParts()
    {
        BodyPart reinforce = new BodyPart(reinforcedArmPart, armPosition, "cultivationcraft.gui.generic.reinforce", 1000);
        reinforce.addModel(GenericLimbNames.leftArm);
        reinforce.addModel(GenericLimbNames.rightArm);
        reinforce.addHand(GenericLimbNames.leftArm, 1);
        reinforce.addHand(GenericLimbNames.rightArm, 0);
        reinforce.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);

        addPart(reinforce);


        BodyPart shortArm = new BodyPart(shortArmPart, armPosition, "cultivationcraft.gui.armpart.short", 1000);
        shortArm.addModel(BodyPartModelNames.shortArmLeftModel);
        shortArm.addModel(BodyPartModelNames.shortArmRightModel);
        // TODO: Hands not working here
        shortArm.addHand(BodyPartModelNames.shortArmLeftModel, 1);
        shortArm.addHand(BodyPartModelNames.shortArmRightModel, 0);
        shortArm.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);
        shortArm.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.armWeight * -1);
        shortArm.getStatChanges().setStat(StatIDs.attackRange, StatIDs.defaultAttackRange * -0.5f);

        addPart(shortArm);


        BodyPart fArm = new BodyPart(flipperArmPart, armPosition, "cultivationcraft.gui.armpart.flipper", 1000);
        fArm.addModel(BodyPartModelNames.flipperLeftModel);
        fArm.addModel(BodyPartModelNames.flipperRightModel);
        fArm.addHand(BodyPartModelNames.flipperLeftModel, 1);
        fArm.addHand(BodyPartModelNames.flipperRightModel, 0);
        fArm.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);
        fArm.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.armWeight * -1.8f);
        fArm.getStatChanges().setStat(StatIDs.swimSpeed, 1);
        fArm.getStatChanges().setStat(StatIDs.armAttackModifier, -0.5f);

        addPart(fArm);


        BodyPart glide = new GlidePart(glideArmPart, armPosition, "cultivationcraft.gui.armpart.glide", 1000);
        glide.addModel(GenericLimbNames.leftArm);
        glide.addModel(GenericLimbNames.rightArm);
        glide.addHand(GenericLimbNames.leftArm, 1);
        glide.addHand(GenericLimbNames.rightArm, 0);
        glide.addQuad(BodyPartModelNames.armglidequad);
        glide.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);
        glide.addUniqueTag(BodyPartTags.flight);
        glide.getStatChanges().setStat(StatIDs.weight, 0.01f);
        glide.getStatChanges().setStat(StatIDs.wingSupport, 2f);
        glide.getStatChanges().setStat(StatIDs.fallHeight, 99);

        addPart(glide);


        BodyPartOption headArms = new BodyPartOption(headArmsPart, armPosition, locationSubPosition, "cultivationcraft.gui.armpart.headarms", 1000);
        headArms.setConnection(new BodyPartLocation(armPosition, basePosition, headPosition, basePosition));
        headArms.addNeededPosition(BodyPartNames.armPosition, BodyPartNames.basePosition);

        addOption(headArms);
    }

    private static void setupHeadOptions()
    {
        addOption(new BodyPartOption(startingEyesPart, headPosition, eyeSubPosition, "cultivationcraft.gui.headpart.eye.qisight", 1000));


        BodyPartOption flatTeeth = new BodyPartOption(flatTeethPart, headPosition, mouthSubPosition, "cultivationcraft.gui.headpart.mouth.flatteeth", 1000);
        flatTeeth.addModel(BodyPartModelNames.flatToothModel);
        flatTeeth.addModel(BodyPartModelNames.flatToothLowerModel, BodyPartModelNames.jawModelLower);
        flatTeeth.addModel(BodyPartModelNames.flatToothLowerModel, BodyPartModelNames.FPjawModelLower);
        flatTeeth.addNeededPart(BodyPartNames.jawPart);
        flatTeeth.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.boneSubPosition);
        flatTeeth.setTexture(TextureList.bone);
        flatTeeth.getStatChanges().setStat(StatIDs.weight, 0.02f);
        flatTeeth.getStatChanges().setStat(StatIDs.biteAttackModifier, 0.75f);

        addOption(flatTeeth);

        BodyPartOption sharpTeeth = new BodyPartOption(sharpTeethPart, headPosition, mouthSubPosition, "cultivationcraft.gui.headpart.mouth.sharpteeth", 1000);
        sharpTeeth.addModel(BodyPartModelNames.sharpToothModel);
        sharpTeeth.addModel(BodyPartModelNames.sharpToothLowerModel, BodyPartModelNames.jawModelLower);
        sharpTeeth.addModel(BodyPartModelNames.sharpToothLowerModel, BodyPartModelNames.FPjawModelLower);
        sharpTeeth.addNeededPart(BodyPartNames.jawPart);
        sharpTeeth.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.boneSubPosition);
        sharpTeeth.setTexture(TextureList.bone);
        sharpTeeth.getStatChanges().setStat(StatIDs.weight, 0.02f);
        sharpTeeth.getStatChanges().setStat(StatIDs.biteAttackModifier, 1.5f);

        addOption(sharpTeeth);


        BodyPartOption frontHead = new BodyPartOption(headFrontPart, headPosition, locationSubPosition, "cultivationcraft.gui.headpart.location.front", 1000);
        BodyPartLocation frontHeadConnection = new BodyPartLocation(headPosition, basePosition, bodyPosition, basePosition);
        frontHeadConnection.adjustPos(new Vec3(0.5, 0.5, 0));
        frontHeadConnection.adjustRotationPoint(new Vec3(0.5, 0.5, 0));
        frontHead.setConnection(frontHeadConnection);
        frontHead.addNeededPosition(BodyPartNames.headPosition, BodyPartNames.basePosition);

        addOption(frontHead);
    }

    private static void setupLegParts()
    {
        BodyPart feetpart = new BodyPart(feetPart, legPosition, "cultivationcraft.gui.legpart.feet", 1000);
        feetpart.addModel(BodyPartModelNames.footLeftModel);
        feetpart.addModel(BodyPartModelNames.footRightModel);
        feetpart.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        feetpart.getStatChanges().setStat(StatIDs.movementSpeed, StatIDs.defaultMovementSpeed * -0.25f);
        feetpart.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.legWeight * -1.6f);
        feetpart.getStatChanges().setStat(StatIDs.legSupport, 2.5f);

        addPart(feetpart);

        BodyPart largelegpart = new BodyPart(largeLegPart, legPosition, "cultivationcraft.gui.legpart.large", 1000);
        largelegpart.addModel(BodyPartModelNames.largeLegLeftModel);
        largelegpart.addModel(BodyPartModelNames.largeLegRightModel);
        largelegpart.addNeededPosition(BodyPartNames.armPosition, BodyPartNames.locationSubPosition);
        feetpart.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.legWeight * 2f);
        feetpart.getStatChanges().setStat(StatIDs.legSupport, 10);

        addPart(largelegpart);

        BodyPart rjLegPart = new BodyPart(reverseJointLegPart, legPosition, "cultivationcraft.gui.legpart.reversejoint", 1000);
        rjLegPart.addModel(BodyPartModelNames.reverseJointRightLegModel);
        rjLegPart.addModel(BodyPartModelNames.reverseJointLeftLegModel);
        rjLegPart.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        rjLegPart.getStatChanges().setStat(StatIDs.jumpHeight, 4);
        rjLegPart.getStatChanges().setStat(StatIDs.fallHeight, 4);

        addPart(rjLegPart);

        SingleLegPart oneLegPart = new SingleLegPart(singleLegPart, legPosition, "cultivationcraft.gui.legpart.singleLeg", 1000);
        oneLegPart.getStatChanges().setStat(StatIDs.jumpHeight, 7);
        oneLegPart.getStatChanges().setStat(StatIDs.fallHeight, 7);
        oneLegPart.addModel(BodyPartModelNames.singleLegModel);
        oneLegPart.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        oneLegPart.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.legWeight * -1);
        oneLegPart.getStatChanges().setStat(StatIDs.legSupport, StatIDs.defaultLegSupport * -0.25f);

        addPart(oneLegPart);

        BodyPart sixLegPart = new BodyPart(hexaLegPart, legPosition, "cultivationcraft.gui.legpart.hexaLeg", 1000);
        sixLegPart.addModel(BodyPartModelNames.hexaLeftLegModel);
        sixLegPart.addModel(BodyPartModelNames.hexaLeftLegModelTwo);
        sixLegPart.addModel(BodyPartModelNames.hexaLeftLegModelThree);
        sixLegPart.addModel(BodyPartModelNames.hexaRightLegModel);
        sixLegPart.addModel(BodyPartModelNames.hexaRightLegModelTwo);
        sixLegPart.addModel(BodyPartModelNames.hexaRightLegModelThree);
        sixLegPart.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        sixLegPart.getStatChanges().setStat(StatIDs.jumpHeight, 4);
        sixLegPart.getStatChanges().setStat(StatIDs.fallHeight, 4);
        sixLegPart.getStatChanges().setStat(StatIDs.movementSpeed, 0.1f);
        sixLegPart.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.legWeight * -1.5f);
        sixLegPart.getStatChanges().setStat(StatIDs.legSupport, StatIDs.defaultLegSupport * -0.5f);

        addPart(sixLegPart);

        BodyPart jetLeg = new BodyPart(jetLegPart, legPosition, "cultivationcraft.gui.legpart.jet", 1000);
        jetLeg.addModel(BodyPartModelNames.jetLegLeftModel);
        jetLeg.addModel(BodyPartModelNames.jetLegRightModel);
        jetLeg.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        jetLeg.getStatChanges().setStat(StatIDs.wingSupport, 10);
        jetLeg.getStatChanges().setStat(StatIDs.flightSpeed, 0.4f);
        jetLeg.getStatChanges().setStat(StatIDs.fallHeight, 4f);

        // TODO: Add flame generating body part
        //jetLeg.addNeededTags(BodyPartTags.flame);

        addPart(jetLeg);
    }

    public static String getDisplayName(String position)
    {
        return Component.translatable(displayNames.get(position)).getString();
    }

    public static String getDisplayName(String position, String subPosition)
    {
        if (subPosition.compareTo(basePosition) == 0)
            return getDisplayName(subPosition);

        return Component.translatable(subPartDisplayNames.get(position).get(subPosition)).getString();
    }

    public static void addDisplayName(String position, String name)
    {
        displayNames.put(position, name);
    }

    public static void addSubPartDisplayName(String position, String subPosition, String name)
    {
        if (!subPartDisplayNames.containsKey(position))
            subPartDisplayNames.put(position, new HashMap<String, String>());

        subPartDisplayNames.get(position).put(subPosition, name);
    }

    public static void addPart(BodyPart part)
    {
        parts.add(part);
    }

    public static void addOption(BodyPartOption part)
    {
        options.add(part);
    }

    public static BodyPart getPart(String part)
    {
        for (BodyPart searchPart : parts)
            if (searchPart.getID().compareTo(part) == 0)
                return searchPart;

        return null;
    }

    public static BodyPartOption getOption(String part)
    {
        for (BodyPartOption searchPart : options)
            if (searchPart.getID().compareTo(part) == 0)
                return searchPart;

        return null;
    }

    public static ArrayList<BodyPart> getParts()
    {
        return parts;
    }

    public static ArrayList<BodyPartOption> getOptions()
    {
        return options;
    }
}
