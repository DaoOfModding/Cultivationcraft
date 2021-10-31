package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Textures.TextureList;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts.GlidePart;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.HashMap;

public class BodyPartNames
{
    // PARTS
    public static final String jawPart = "jaw";
    public static final String reverseJointLegPart = "rjleg";
    public static final String reinforcedBodyPart = "rbody";
    public static final String reinforcedArmPart = "rarm";
    public static final String reinforcedLegPart = "rleg";
    public static final String reinforcedHeadPart = "rhead";

    public static final String glideArmPart = "glidearm";

    // OPTIONS
    public static final String reinforceSkinPart = "reinforceSkin";
    public static final String reinforceBonePart = "reinforceBone";

    public static final String startingEyesPart = "qisight";
    public static final String flatTeethPart = "flatteeth";
    public static final String sharpTeethPart = "sharpteeth";
    public static final String wingPart = "wing";
    public static final String insectwingPart = "iwing";
    public static final String rubberSkinPart = "rubber";

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

    public static final String eyeSubPosition = "EYE";
    public static final String mouthSubPosition = "MOUTH";
    public static final String skinSubPosition = "SKIN";
    public static final String boneSubPosition = "BONE";
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
        addSubPartDisplayName(bodyPosition, backSubPosition, "cultivationcraft.gui.bodypart.back");
        addSubPartDisplayName(headPosition, eyeSubPosition, "cultivationcraft.gui.headpart.eye");
        addSubPartDisplayName(headPosition, mouthSubPosition, "cultivationcraft.gui.headpart.mouth");

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
        reinforceBones.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);

        addOption(reinforceBones);

        setupSkinOptions();
        setupBackOptions();
    }

    private static void setupSkinOptions()
    {
        BodyPartOption rubberSkin = new BodyPartOption(rubberSkinPart, bodyPosition, skinSubPosition,  "cultivationcraft.gui.bodypart.skin.rubber", 1000);
        //rubberSkin.addTextureChange(TextureList.skin, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        rubberSkin.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);
        rubberSkin.getStatChanges().setStat(StatIDs.bounceHeight, 0.5f);

        BodyPartOption reinforceSkin = new BodyPartOption(reinforceSkinPart, bodyPosition, skinSubPosition,  "cultivationcraft.gui.generic.reinforce", 1000);
        //rubberSkin.addTextureChange(TextureList.skin, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        reinforceSkin.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);

        addOption(rubberSkin);
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
        addWings.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        addWings.addNotNeededPart(BodyPartNames.glideArmPart);

        BodyPartOption addIWings = new BodyPartOption(insectwingPart, bodyPosition, backSubPosition,  "cultivationcraft.gui.bodypart.back.iwings", 1000);
        addIWings.addModel(BodyPartModelNames.rinsectWing);
        addIWings.addModel(BodyPartModelNames.linsectWing);
        addIWings.addQuad(BodyPartModelNames.insectQuads);
        addIWings.setTexture(TextureList.bone);
        addIWings.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.boneSubPosition);
        addIWings.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        addIWings.addNotNeededPart(BodyPartNames.glideArmPart);

        addOption(addWings);
        addOption(addIWings);
    }

    private static void setupBodyParts()
    {
        BodyPart reinforce = new BodyPart(reinforcedBodyPart, bodyPosition, "cultivationcraft.gui.generic.reinforce", 1000);
        reinforce.addModel(GenericLimbNames.body);
        reinforce.addNeededPart(BodyPartNames.startingEyesPart);

        addPart(reinforce);
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
        reinforce.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);

        addPart(reinforce);


        BodyPart glide = new GlidePart(glideArmPart, armPosition, "cultivationcraft.gui.armpart.glide", 1000);
        glide.addModel(GenericLimbNames.leftArm);
        glide.addModel(GenericLimbNames.rightArm);
        glide.addQuad(BodyPartModelNames.armglidequad);
        glide.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.skinSubPosition);
        glide.addNotNeededPart(BodyPartNames.wingPart);

        addPart(glide);
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

        addOption(flatTeeth);

        BodyPartOption sharpTeeth = new BodyPartOption(sharpTeethPart, headPosition, mouthSubPosition, "cultivationcraft.gui.headpart.mouth.sharpteeth", 1000);
        sharpTeeth.addModel(BodyPartModelNames.sharpToothModel);
        sharpTeeth.addModel(BodyPartModelNames.sharpToothLowerModel, BodyPartModelNames.jawModelLower);
        sharpTeeth.addModel(BodyPartModelNames.sharpToothLowerModel, BodyPartModelNames.FPjawModelLower);
        sharpTeeth.addNeededPart(BodyPartNames.jawPart);
        sharpTeeth.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.boneSubPosition);
        sharpTeeth.setTexture(TextureList.bone);

        addOption(sharpTeeth);
    }

    private static void setupLegParts()
    {
        BodyPart rjLegPart = new BodyPart(reverseJointLegPart, legPosition, "cultivationcraft.gui.legpart.reversejoint", 1000);
        rjLegPart.getStatChanges().setStat(StatIDs.jumpHeight, 5);
        rjLegPart.addModel(BodyPartModelNames.reverseJointRightLegModel);
        rjLegPart.addModel(BodyPartModelNames.reverseJointLeftLegModel);
        rjLegPart.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);

        addPart(rjLegPart);
    }

    public static String getDisplayName(String position)
    {
        return new TranslationTextComponent(displayNames.get(position)).getString();
    }

    public static String getDisplayName(String position, String subPosition)
    {
        if (subPosition.compareTo(basePosition) == 0)
            return getDisplayName(subPosition);

        return new TranslationTextComponent(subPartDisplayNames.get(position).get(subPosition)).getString();
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
