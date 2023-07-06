package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartLocation;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Textures.TextureList;
import DaoOfModding.Cultivationcraft.Common.Misc;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.BurningBlood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.CultivatorBlood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.QiBlood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts.*;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.CarnivoreFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.HerbivoreFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung.FireLung;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung.QiLung;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung.WaterLung;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.LungConnection.LeftLungConnection;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.LungConnection.LungConnection;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.LungConnection.RightLungConnection;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.MixedLungs;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.QiLungs;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.DefaultQuests;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.DefaultPlayerBodyPartWeights;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques.BiteTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques.JetTechnique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.mlmanimator.Client.Poses.Arm;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
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
    public static final String dfinPart = "dfin";

    public static final String rubberSkinPart = "rubber";
    public static final String stretchySkinPart = "stretchy";
    public static final String scaleSkinPart = "scale";

    public static final String elementalBonePart = "elementBone";
    public static final String bigBonePart = "bigBone";
    public static final String sharpBonePart = "sharpBone";

    public static final String expandingStomachPart = "expandingStomach";
    public static final String carnivorousStomachPart = "carnivorousStomach";
    public static final String herbivorousStomachPart = "herbivorousStomachPart";

    public static final String hungerBloodPart = "hungerBlood";
    public static final String qiBloodPart = "qiBlood";
    public static final String burningBloodPart = "burningBlood";

    public static final String reinforcedLungPart = "reinforcedLung";
    public static final String highPressureLungPart = "highpressureLung";
    public static final String floatingLungPart = "floatingLung";
    public static final String aquaticLungPart = "aquaticLung";
    public static final String fireLungPart = "fireLung";
    public static final String mixedLungPart = "mixedLung";

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
    public static final String lungSubPosition = "LUNG";
    public static final String backSubPosition = "BACK";


    protected static ArrayList<BodyPart> parts = new ArrayList<BodyPart>();
    protected static ArrayList<BodyPartOption> options = new ArrayList<BodyPartOption>();
    protected static HashMap<String, String> displayNames = new HashMap<String, String>();
    protected static HashMap<String, HashMap<String, String>> subPartDisplayNames = new HashMap<String, HashMap<String, String>>();

    protected static ArrayList<ResourceLocation> lungLocations = new ArrayList<ResourceLocation>();

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
        addSubPartDisplayName(bodyPosition, lungSubPosition, "cultivationcraft.gui.bodypart.lung");
        addSubPartDisplayName(bodyPosition, backSubPosition, "cultivationcraft.gui.bodypart.back");
        addSubPartDisplayName(headPosition, eyeSubPosition, "cultivationcraft.gui.headpart.eye");
        addSubPartDisplayName(headPosition, mouthSubPosition, "cultivationcraft.gui.headpart.mouth");
        addSubPartDisplayName(bodyPosition, locationSubPosition, "cultivationcraft.gui.generic.location");
        addSubPartDisplayName(armPosition, locationSubPosition, "cultivationcraft.gui.generic.location");
        addSubPartDisplayName(headPosition, locationSubPosition, "cultivationcraft.gui.generic.location");

        addSubPartDisplayName(bodyPosition, LungConnection.location.toString(), "cultivationcraft.gui.bodypart.lung.default");
        addSubPartDisplayName(bodyPosition, LeftLungConnection.location.toString(), "cultivationcraft.gui.bodypart.lung.left");
        addSubPartDisplayName(bodyPosition, RightLungConnection.location.toString(), "cultivationcraft.gui.bodypart.lung.right");


        setupBodyParts();
        setupBodyOptions();

        setupHeadParts();
        setupHeadOptions();

        setupArmParts();

        setupLegParts();
    }

    public static void registerLungLocations()
    {
        addLungLocation(LungConnection.location);
        addLungLocation(LeftLungConnection.location);
        addLungLocation(RightLungConnection.location);
    }

    public static void addLungLocation(ResourceLocation location)
    {
        lungLocations.add(location);
    }

    public static ArrayList<ResourceLocation> getLungLocations()
    {
        return lungLocations;
    }

    protected static void setupBodyOptions()
    {
        BodyPartOption reinforceBones = new BodyPartOption(reinforceBonePart, bodyPosition, boneSubPosition,  "cultivationcraft.gui.generic.reinforce");
        reinforceBones.addTextureChange(TextureList.bone, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        reinforceBones.addNeededPart(BodyPartNames.startingEyesPart);
        reinforceBones.getStatChanges().setStat(StatIDs.boneAttackModifier, 2);
        reinforceBones.getStatChanges().setStat(StatIDs.armor, 6);
        reinforceBones.setQuest(DefaultQuests.defaultBoneQuest);

        BodyPartOption sharpBones = new BodyPartOption(sharpBonePart, bodyPosition, boneSubPosition,  "cultivationcraft.gui.bodypart.bone.sharp");
        sharpBones.addTextureChange(TextureList.bone, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        sharpBones.addNeededPart(BodyPartNames.startingEyesPart);
        sharpBones.getStatChanges().setStat(StatIDs.boneAttackModifier, 8);
        sharpBones.addTextureColorChange(TextureList.bone, new Color(0.8f, 0.8f, 0.8f));
        sharpBones.setQuest(DefaultQuests.defaultBodyQuest);

        BodyPartOption bigBones = new BodyPartOption(bigBonePart, bodyPosition, boneSubPosition,  "cultivationcraft.gui.bodypart.bone.big");
        bigBones.addTextureChange(TextureList.bone, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        bigBones.addNeededPart(BodyPartNames.startingEyesPart);
        bigBones.getStatChanges().setStat(StatIDs.weight, 0.2f);
        bigBones.getStatChanges().setStat(StatIDs.armor, 12);
        bigBones.getStatChanges().setStat(StatIDs.armorToughness, 2);
        bigBones.getStatChanges().setStat(StatIDs.width, 0.2f);
        bigBones.getStatChanges().setStat(StatIDs.boneAttackModifier, 3f);
        bigBones.setQuest(DefaultQuests.defaultBoneQuest);

        BodyPartOption fireBones = new BodyPartOption(elementalBonePart + Elements.fireElement, bodyPosition, boneSubPosition,  "cultivationcraft.gui.bodypart.bone.fire");
        fireBones.setElement(Elements.fireElement);
        fireBones.addTextureChange(TextureList.bone, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        fireBones.addTextureColorChange(TextureList.bone, Misc.saturate(Elements.getElement(Elements.fireElement).color, 0.75f));
        fireBones.addNeededPart(BodyPartNames.startingEyesPart);
        fireBones.getStatChanges().setStat(StatIDs.boneAttackModifier, 2);
        fireBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.fireElement, 50);
        fireBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.waterElement, -50);
        fireBones.getStatChanges().setStat(StatIDs.armor, 4);
        fireBones.setQuest(DefaultQuests.defaultBoneQuest);

        BodyPartOption waterBones = new BodyPartOption(elementalBonePart + Elements.waterElement, bodyPosition, boneSubPosition,  "cultivationcraft.gui.bodypart.bone.water");
        waterBones.setElement(Elements.waterElement);
        waterBones.addTextureChange(TextureList.bone, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        waterBones.addTextureColorChange(TextureList.bone, Misc.saturate(Elements.getElement(Elements.waterElement).color, 0.75f));
        waterBones.addNeededPart(BodyPartNames.startingEyesPart);
        waterBones.getStatChanges().setStat(StatIDs.boneAttackModifier, 2);
        waterBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.waterElement, 50);
        waterBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.windElement, -50);
        waterBones.getStatChanges().setStat(StatIDs.armor, 4);
        waterBones.setQuest(DefaultQuests.defaultBoneQuest);

        BodyPartOption windBones = new BodyPartOption(elementalBonePart + Elements.windElement, bodyPosition, boneSubPosition,  "cultivationcraft.gui.bodypart.bone.wind");
        windBones.setElement(Elements.windElement);
        windBones.addTextureChange(TextureList.bone, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        windBones.addTextureColorChange(TextureList.bone, Misc.saturate(Elements.getElement(Elements.windElement).color, 0.75f));
        windBones.addNeededPart(BodyPartNames.startingEyesPart);
        windBones.getStatChanges().setStat(StatIDs.boneAttackModifier, 2);
        windBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.windElement, 50);
        windBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.earthElement, -50);
        windBones.getStatChanges().setStat(StatIDs.armor, 4);
        windBones.setQuest(DefaultQuests.defaultBoneQuest);

        BodyPartOption earthBones = new BodyPartOption(elementalBonePart + Elements.earthElement, bodyPosition, boneSubPosition,  "cultivationcraft.gui.bodypart.bone.earth");
        earthBones.setElement(Elements.earthElement);
        earthBones.addTextureChange(TextureList.bone, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        earthBones.addTextureColorChange(TextureList.bone, Misc.saturate(Elements.getElement(Elements.earthElement).color, 0.75f));
        earthBones.addNeededPart(BodyPartNames.startingEyesPart);
        earthBones.getStatChanges().setStat(StatIDs.boneAttackModifier, 2);
        earthBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.earthElement, 50);
        earthBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.woodElement, -50);
        earthBones.getStatChanges().setStat(StatIDs.armor, 4);
        earthBones.setQuest(DefaultQuests.defaultBoneQuest);

        BodyPartOption woodBones = new BodyPartOption(elementalBonePart + Elements.woodElement, bodyPosition, boneSubPosition,  "cultivationcraft.gui.bodypart.bone.wood");
        woodBones.setElement(Elements.woodElement);
        woodBones.addTextureChange(TextureList.bone, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        woodBones.addTextureColorChange(TextureList.bone, Misc.saturate(Elements.getElement(Elements.woodElement).color, 0.75f));
        woodBones.addNeededPart(BodyPartNames.startingEyesPart);
        woodBones.getStatChanges().setStat(StatIDs.boneAttackModifier, 2);
        woodBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.woodElement, 50);
        woodBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.fireElement, -50);
        woodBones.getStatChanges().setStat(StatIDs.armor, 4);
        woodBones.setQuest(DefaultQuests.defaultBoneQuest);

        BodyPartOption lightningBones = new BodyPartOption(elementalBonePart + Elements.lightningElement, bodyPosition, boneSubPosition,  "cultivationcraft.gui.bodypart.bone.lightning");
        lightningBones.setElement(Elements.lightningElement);
        lightningBones.addTextureChange(TextureList.bone, new ResourceLocation(Cultivationcraft.MODID, "textures/models/bone/bone.png"));
        lightningBones.addTextureColorChange(TextureList.bone, Misc.saturate(Elements.getElement(Elements.lightningElement).color, 0.85f));
        lightningBones.addNeededPart(BodyPartNames.startingEyesPart);
        lightningBones.getStatChanges().setStat(StatIDs.boneAttackModifier, 6);
        lightningBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.lightningElement, 75);
        lightningBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.windElement, -25);
        lightningBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.fireElement, -25);
        lightningBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.earthElement, -25);
        lightningBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.waterElement, -25);
        lightningBones.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.windElement, -25);
        lightningBones.setQuest(DefaultQuests.defaultBodyQuest);

        addOption(reinforceBones);
        addOption(sharpBones);
        addOption(bigBones);
        addOption(fireBones);
        addOption(waterBones);
        addOption(windBones);
        addOption(earthBones);
        addOption(woodBones);
        addOption(lightningBones);

        setupSkinOptions();
        setupStomachOptions();
        setupBackOptions();
        setupBloodOptions();
        setupLungOptions();


        // TODO - Disabled longneck for the moment as it's buggy and has no point
        /*BodyPartOption longneck = new BodyPartOption(longNeckPart, bodyPosition, locationSubPosition, "cultivationcraft.gui.headpart.longneck", 1000);
        longneck.addModel(BodyPartModelNames.longNeckModel);

        BodyPartLocation longneckLocation = new BodyPartLocation(headPosition, basePosition, BodyPartModelNames.longNeckModelEnd);
        longneckLocation.adjustDepth(9);
        longneck.setConnection(longneckLocation);

        longneck.addNeededPosition(BodyPartNames.headPosition, BodyPartNames.basePosition);
        longneck.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.headWeight * 0.5f);

        addOption(longneck);*/
    }

    protected static void setupBloodOptions()
    {
        BloodPart hungerBlood = new BloodPart(hungerBloodPart, bodyPosition, bloodSubPosition, "cultivationcraft.gui.bodypart.blood.hunger");
        hungerBlood.addUniqueTag(BodyPartTags.blood);
        hungerBlood.addNeededPart(BodyPartNames.startingEyesPart);
        hungerBlood.setBloodType(new CultivatorBlood());
        hungerBlood.setQuest(DefaultQuests.defaultHealQuest);

        BloodPart qiBlood = new BloodPart(qiBloodPart, bodyPosition, bloodSubPosition, "cultivationcraft.gui.bodypart.blood.qi");
        qiBlood.addUniqueTag(BodyPartTags.blood);
        qiBlood.addNeededPart(BodyPartNames.startingEyesPart);
        qiBlood.setBloodType(new QiBlood());
        qiBlood.setQuest(DefaultQuests.defaultHealQuest);

        BloodPart burningBlood = new BloodPart(burningBloodPart, bodyPosition, bloodSubPosition, "cultivationcraft.gui.bodypart.blood.burning");
        burningBlood.addUniqueTag(BodyPartTags.blood);
        burningBlood.addNeededPart(BodyPartNames.startingEyesPart);
        burningBlood.setBloodType(new BurningBlood());
        burningBlood.setElement(Elements.fireElement);
        burningBlood.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.fireElement, 25);
        burningBlood.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.waterElement, -25);
        burningBlood.setQuest(DefaultQuests.defaultLiveQuest);

        addOption(hungerBlood);
        addOption(qiBlood);
        addOption(burningBlood);
    }

    protected static void setupLungOptions()
    {
        LungPart reinforcedLungs = new LungPart(reinforcedLungPart, bodyPosition, lungSubPosition, "cultivationcraft.gui.bodypart.lung.reinforce");
        reinforcedLungs.addUniqueTag(BodyPartTags.lung);
        reinforcedLungs.addNeededPart(BodyPartNames.startingEyesPart);
        reinforcedLungs.setLungType(new QiLungs());
        reinforcedLungs.getStatChanges().setStat(StatIDs.lungCapacity, 300);
        reinforcedLungs.setQuest(DefaultQuests.defaultLiveQuest);

        LungPart highPressureLungs = new LungPart(highPressureLungPart, bodyPosition, lungSubPosition, "cultivationcraft.gui.bodypart.lung.highpressure");
        highPressureLungs.addUniqueTag(BodyPartTags.lung);
        highPressureLungs.addNeededPart(BodyPartNames.startingEyesPart);
        highPressureLungs.setLungType(new QiLungs());
        highPressureLungs.getStatChanges().setStat(StatIDs.lungCapacity, 150);
        highPressureLungs.setQuest(DefaultQuests.defaultLiveQuest);

        LungPart floatingLungs = new LungPart(floatingLungPart, bodyPosition, lungSubPosition, "cultivationcraft.gui.bodypart.lung.floating");
        floatingLungs.addUniqueTag(BodyPartTags.lung);
        floatingLungs.addUniqueTag(BodyPartTags.flight);
        floatingLungs.addNeededPart(BodyPartNames.expandingBodyPart);
        floatingLungs.setLungType(new QiLungs());
        floatingLungs.setQuest(DefaultQuests.defaultFlightQuest);
        floatingLungs.getStatChanges().setStat(StatIDs.lungCapacity, -100);

        LungPart mixedLungs = new LungPart(mixedLungPart, bodyPosition, lungSubPosition, "cultivationcraft.gui.bodypart.lung.mixed");
        mixedLungs.addUniqueTag(BodyPartTags.lung);
        mixedLungs.addNeededPart(BodyPartNames.startingEyesPart);
        mixedLungs.setLungType(new MixedLungs());
        mixedLungs.setQuest(DefaultQuests.defaultLiveQuest);

        addOption(reinforcedLungs);
        addOption(highPressureLungs);
        addOption(floatingLungs);
        addOption(mixedLungs);

        for (ResourceLocation location : getLungLocations())
            setupLungConnectionOptions(location);
    }

    protected static void setupLungConnectionOptions(ResourceLocation location)
    {
        LungPart defaultLungs = new LungPart(reinforcedLungPart + location, bodyPosition, location.toString(), "cultivationcraft.gui.bodypart.lung.reinforce");
        defaultLungs.addNeededTags(BodyPartTags.lung);
        defaultLungs.setQuest(DefaultQuests.defaultLiveQuest);
        defaultLungs.getStatChanges().setStat(StatIDs.maxHP, 2);
        defaultLungs.setLung(location, new QiLung());
        defaultLungs.setNeededLungLocation(location);

        LungPart fireLungs = new LungPart(fireLungPart + location, bodyPosition, location.toString(), "cultivationcraft.gui.bodypart.lung.fire");
        fireLungs.addUniqueTag(BodyPartTags.flame);
        fireLungs.addNeededTags(BodyPartTags.lung);
        fireLungs.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.fireElement, 25);
        fireLungs.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.waterElement, -25);
        fireLungs.setQuest(DefaultQuests.defaultLiveQuest);
        fireLungs.setLung(location, new FireLung());
        fireLungs.setNeededLungLocation(location);
        fireLungs.setElement(Elements.fireElement);

        LungPart aquaticLungs = new LungPart(aquaticLungPart + location, bodyPosition, location.toString(), "cultivationcraft.gui.bodypart.lung.aquatic");
        aquaticLungs.addNeededTags(BodyPartTags.lung);
        aquaticLungs.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.waterElement, 25);
        aquaticLungs.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.windElement, -25);
        aquaticLungs.setQuest(DefaultQuests.defaultLiveQuest);
        aquaticLungs.setLung(location, new WaterLung());
        aquaticLungs.setNeededLungLocation(location);
        aquaticLungs.setElement(Elements.waterElement);

        addOption(defaultLungs);
        addOption(fireLungs);
        addOption(aquaticLungs);
    }

    protected static void setupStomachOptions()
    {
        StomachPart expandingStomach = new StomachPart(expandingStomachPart, bodyPosition, stomachSubPosition,  "cultivationcraft.gui.bodypart.stomach.expanding");
        expandingStomach.addUniqueTag(BodyPartTags.hunger);
        expandingStomach.addUniqueTag(BodyPartTags.expanding);
        expandingStomach.addNeededPart(expandingBodyPart);
        expandingStomach.getStatChanges().setStat(StatIDs.maxStamina, 80);
        expandingStomach.setQuest(DefaultQuests.defaultStaminaQuest);

        StomachPart carnivorousStomach = new StomachPart(carnivorousStomachPart, bodyPosition, stomachSubPosition,  "cultivationcraft.gui.bodypart.stomach.carnivorous");
        carnivorousStomach.addUniqueTag(BodyPartTags.hunger);
        carnivorousStomach.addNeededPart(BodyPartNames.startingEyesPart);
        carnivorousStomach.getStatChanges().setStat(StatIDs.maxStamina, 20);
        carnivorousStomach.setFoodStats(new CarnivoreFoodStats());
        carnivorousStomach.setQuest(DefaultQuests.defaultStaminaQuest);

        StomachPart herbivoreStomach = new StomachPart(herbivorousStomachPart, bodyPosition, stomachSubPosition,  "cultivationcraft.gui.bodypart.stomach.herbivorous");
        herbivoreStomach.addUniqueTag(BodyPartTags.hunger);
        herbivoreStomach.addNeededPart(BodyPartNames.startingEyesPart);
        herbivoreStomach.getStatChanges().setStat(StatIDs.maxStamina, 20);
        herbivoreStomach.setFoodStats(new HerbivoreFoodStats());
        herbivoreStomach.setQuest(DefaultQuests.defaultStaminaQuest);

        addOption(expandingStomach);
        addOption(carnivorousStomach);
        addOption(herbivoreStomach);
    }

    protected static void setupSkinOptions()
    {
        BodyPartOption rubberSkin = new BodyPartOption(rubberSkinPart, bodyPosition, skinSubPosition,  "cultivationcraft.gui.bodypart.skin.rubber");
        rubberSkin.getStatChanges().setStat(StatIDs.bounceHeight, 0.5f);
        rubberSkin.addNeededPart(BodyPartNames.startingEyesPart);
        rubberSkin.addUniqueTag(BodyPartTags.stretchy);
        rubberSkin.getStatChanges().setStat(StatIDs.fallHeight, 99);
        rubberSkin.setQuest(new Quest(Quest.BOUNCE, 100));

        BodyPartOption stretchySkin = new BodyPartOption(stretchySkinPart, bodyPosition, skinSubPosition,  "cultivationcraft.gui.bodypart.skin.stretchy");
        stretchySkin.addUniqueTag(BodyPartTags.stretchy);
        stretchySkin.addNeededPart(BodyPartNames.startingEyesPart);
        stretchySkin.setQuest(DefaultQuests.defaultSkinQuest);

        BodyPartOption reinforceSkin = new BodyPartOption(reinforceSkinPart, bodyPosition, skinSubPosition,  "cultivationcraft.gui.generic.reinforce");
        reinforceSkin.addNeededPart(BodyPartNames.startingEyesPart);
        reinforceSkin.setQuest(DefaultQuests.defaultSkinQuest);

        BodyPartOption fireScaleSkin = new BodyPartOption(scaleSkinPart+Elements.fireElement, bodyPosition, skinSubPosition,  "cultivationcraft.gui.bodypart.skin.scale.fire");
        fireScaleSkin.addTextureChange(TextureList.skin, new ResourceLocation(Cultivationcraft.MODID, "textures/models/skin/scales.png"));
        fireScaleSkin.addTextureColorChange(TextureList.skin, Elements.getElement(Elements.fireElement).color.darker().darker());
        fireScaleSkin.addTextureColorChange(TextureList.elementalColored, Elements.getElement(Elements.fireElement).color.darker().darker());
        fireScaleSkin.addNeededPart(BodyPartNames.startingEyesPart);
        fireScaleSkin.setQuest(DefaultQuests.defaultSkinQuest);
        fireScaleSkin.setElement(Elements.fireElement);
        fireScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.fireElement, 50);
        fireScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.waterElement, -50);

        BodyPartOption earthScaleSkin = new BodyPartOption(scaleSkinPart+Elements.earthElement, bodyPosition, skinSubPosition,  "cultivationcraft.gui.bodypart.skin.scale.earth");
        earthScaleSkin.addTextureChange(TextureList.skin, new ResourceLocation(Cultivationcraft.MODID, "textures/models/skin/scales.png"));
        earthScaleSkin.addTextureColorChange(TextureList.skin, Elements.getElement(Elements.earthElement).color.darker().darker());
        earthScaleSkin.addTextureColorChange(TextureList.elementalColored, Elements.getElement(Elements.earthElement).color.darker().darker());
        earthScaleSkin.addNeededPart(BodyPartNames.startingEyesPart);
        earthScaleSkin.setQuest(DefaultQuests.defaultSkinQuest);
        earthScaleSkin.setElement(Elements.earthElement);
        earthScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.earthElement, 50);
        earthScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.windElement, -50);

        BodyPartOption waterScaleSkin = new BodyPartOption(scaleSkinPart+Elements.waterElement, bodyPosition, skinSubPosition,  "cultivationcraft.gui.bodypart.skin.scale.water");
        waterScaleSkin.addTextureChange(TextureList.skin, new ResourceLocation(Cultivationcraft.MODID, "textures/models/skin/scales.png"));
        waterScaleSkin.addTextureColorChange(TextureList.skin, Elements.getElement(Elements.waterElement).color.darker().darker());
        waterScaleSkin.addTextureColorChange(TextureList.elementalColored, Elements.getElement(Elements.waterElement).color.darker().darker());
        waterScaleSkin.addNeededPart(BodyPartNames.startingEyesPart);
        waterScaleSkin.setQuest(DefaultQuests.defaultSkinQuest);
        waterScaleSkin.setElement(Elements.waterElement);
        waterScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.waterElement, 50);
        waterScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.earthElement, -50);

        BodyPartOption windScaleSkin = new BodyPartOption(scaleSkinPart+Elements.windElement, bodyPosition, skinSubPosition,  "cultivationcraft.gui.bodypart.skin.scale.wind");
        windScaleSkin.addTextureChange(TextureList.skin, new ResourceLocation(Cultivationcraft.MODID, "textures/models/skin/scales.png"));
        windScaleSkin.addTextureColorChange(TextureList.skin, Elements.getElement(Elements.windElement).color.darker().darker());
        windScaleSkin.addTextureColorChange(TextureList.elementalColored, Elements.getElement(Elements.windElement).color.darker().darker());
        windScaleSkin.addNeededPart(BodyPartNames.startingEyesPart);
        windScaleSkin.setQuest(DefaultQuests.defaultSkinQuest);
        windScaleSkin.setElement(Elements.windElement);
        windScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.windElement, 50);
        windScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.woodElement, -50);

        BodyPartOption woodScaleSkin = new BodyPartOption(scaleSkinPart+Elements.woodElement, bodyPosition, skinSubPosition,  "cultivationcraft.gui.bodypart.skin.scale.wood");
        woodScaleSkin.addTextureChange(TextureList.skin, new ResourceLocation(Cultivationcraft.MODID, "textures/models/skin/scales.png"));
        woodScaleSkin.addTextureColorChange(TextureList.skin, Elements.getElement(Elements.woodElement).color.darker().darker());
        woodScaleSkin.addTextureColorChange(TextureList.elementalColored, Elements.getElement(Elements.woodElement).color.darker().darker());
        woodScaleSkin.addNeededPart(BodyPartNames.startingEyesPart);
        woodScaleSkin.setQuest(DefaultQuests.defaultSkinQuest);
        woodScaleSkin.setElement(Elements.woodElement);
        woodScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.woodElement, 50);
        woodScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.fireElement, -50);


        BodyPartOption lightningScaleSkin = new BodyPartOption(scaleSkinPart+Elements.lightningElement, bodyPosition, skinSubPosition,  "cultivationcraft.gui.bodypart.skin.scale.lightning");
        lightningScaleSkin.addTextureChange(TextureList.skin, new ResourceLocation(Cultivationcraft.MODID, "textures/models/skin/scales.png"));
        lightningScaleSkin.addTextureColorChange(TextureList.skin, Elements.getElement(Elements.lightningElement).color.darker());
        lightningScaleSkin.addTextureColorChange(TextureList.elementalColored, Elements.getElement(Elements.lightningElement).color);
        lightningScaleSkin.addNeededPart(BodyPartNames.startingEyesPart);
        lightningScaleSkin.setQuest(DefaultQuests.defaultSkinQuest);
        lightningScaleSkin.setElement(Elements.lightningElement);
        lightningScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.lightningElement, 75);
        lightningScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.windElement, -25);
        lightningScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.fireElement, -25);
        lightningScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.waterElement, -25);
        lightningScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.woodElement, -25);
        lightningScaleSkin.getStatChanges().setElementalStat(StatIDs.resistanceModifier, Elements.earthElement, -25);

        addOption(rubberSkin);
        addOption(stretchySkin);
        addOption(reinforceSkin);
        addOption(fireScaleSkin);
        addOption(earthScaleSkin);
        addOption(waterScaleSkin);
        addOption(windScaleSkin);
        addOption(woodScaleSkin);
        addOption(lightningScaleSkin);
    }


    protected static void setupBackOptions()
    {
        BodyPartOption addWings = new BodyPartOption(wingPart, bodyPosition, backSubPosition,  "cultivationcraft.gui.bodypart.back.wings");
        addWings.addModel(BodyPartModelNames.rwingUpperArmModel);
        addWings.addModel(BodyPartModelNames.lwingUpperArmModel);
        addWings.addQuad(BodyPartModelNames.wingquad);
        addWings.setTexture(TextureList.bone);
        addWings.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);
        addWings.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.boneSubPosition);
        addWings.addUniqueTag(BodyPartTags.flight);
        addWings.getStatChanges().setStat(StatIDs.weight, 0.2f);
        addWings.getStatChanges().setStat(StatIDs.wingSupport, 2f);
        addWings.setQuest(DefaultQuests.defaultFlightQuest);

        BodyPartOption addIWings = new BodyPartOption(insectwingPart, bodyPosition, backSubPosition,  "cultivationcraft.gui.bodypart.back.iwings");
        addIWings.addModel(BodyPartModelNames.rinsectWing);
        addIWings.addModel(BodyPartModelNames.linsectWing);
        addIWings.addQuad(BodyPartModelNames.insectQuads);
        addIWings.setTexture(TextureList.bone);
        addWings.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);
        addIWings.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.boneSubPosition);
        addIWings.addUniqueTag(BodyPartTags.flight);
        addIWings.getStatChanges().setStat(StatIDs.weight, 0.01f);
        addIWings.getStatChanges().setStat(StatIDs.wingSupport, 0.75f);
        addIWings.setQuest(DefaultQuests.defaultFlightQuest);


        BodyPartOption jets = new BodyPartOption(jetPart, bodyPosition, backSubPosition,  "cultivationcraft.gui.bodypart.back.jet");
        jets.addModel(BodyPartModelNames.jetLeft);
        jets.addModel(BodyPartModelNames.jetRight);
        jets.addNeededTags(BodyPartTags.flame);
        jets.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);
        jets.getStatChanges().setStat(StatIDs.weight, 0.04f);
        jets.setQuest(new Quest(JetTechnique.jetQuest, 10000));

        BodyPartOption dfin = new BodyPartOption(dfinPart, bodyPosition, backSubPosition, "cultivationcraft.gui.bodypart.back.dfin");
        dfin.addQuad(BodyPartModelNames.dorsalFinQuad);
        dfin.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);
        dfin.getStatChanges().setStat(StatIDs.weight, 0.01f);
        dfin.getStatChanges().setStat(StatIDs.swimSpeed, 1f);
        dfin.setQuest(DefaultQuests.defaultSwimQuest);

        addOption(addWings);
        addOption(addIWings);
        addOption(jets);
        addOption(dfin);
    }

    protected static void setupBodyParts()
    {
        BodyPart reinforce = new BodyPart(reinforcedBodyPart, bodyPosition, "cultivationcraft.gui.generic.reinforce");
        reinforce.addModel(GenericLimbNames.body);
        reinforce.addNeededPart(BodyPartNames.startingEyesPart);
        reinforce.getStatChanges().setStat(StatIDs.maxHP, StatIDs.defaultMaxHP);
        reinforce.setQuest(DefaultQuests.defaultBodyQuest);

        BodyPart expanding = new BodyPart(expandingBodyPart, bodyPosition, "cultivationcraft.gui.bodypart.expanding");
        expanding.addModel(GenericLimbNames.body);
        expanding.addNeededTags(BodyPartTags.stretchy);
        expanding.addNeededPart(BodyPartNames.startingEyesPart);
        expanding.getStatChanges().setStat(StatIDs.maxHP, StatIDs.defaultMaxHP);
        expanding.setQuest(DefaultQuests.defaultBodyQuest);

        BodyPart shortBody = new BodyPart(shortBodyPart, bodyPosition, "cultivationcraft.gui.bodypart.short");
        shortBody.addModel(BodyPartModelNames.shortBody);
        shortBody.addNeededPart(BodyPartNames.startingEyesPart);
        shortBody.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.bodyWeight / -2);
        shortBody.setQuest(DefaultQuests.defaultBodyQuest);

        addPart(reinforce);
        addPart(expanding);
        addPart(shortBody);
    }

    protected static void setupHeadParts()
    {
        BodyPart jaw = new BodyPart(jawPart, headPosition, "cultivationcraft.gui.headpart.jaw");
        jaw.addModel(BodyPartModelNames.jawModel);
        jaw.addFirstPersonModel(BodyPartModelNames.FPjawModel);
        jaw.setViewPoint(BodyPartModelNames.jawModel);
        jaw.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);
        jaw.setQuest(DefaultQuests.defaultBodyQuest);

        addPart(jaw);
    }

    protected static void setupArmParts()
    {
        BodyPart reinforce = new BodyPart(reinforcedArmPart, armPosition, "cultivationcraft.gui.generic.reinforce");
        reinforce.addModel(GenericLimbNames.leftArm);
        reinforce.addModel(GenericLimbNames.rightArm);
        reinforce.addArm(new Arm(InteractionHand.OFF_HAND, GenericLimbNames.leftArm, GenericLimbNames.lowerLeftArm, true));
        reinforce.addArm(new Arm(InteractionHand.MAIN_HAND, GenericLimbNames.rightArm, GenericLimbNames.lowerRightArm, false));
        reinforce.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);
        reinforce.setQuest(DefaultQuests.defaultBodyQuest);

        addPart(reinforce);


        BodyPart shortArm = new BodyPart(shortArmPart, armPosition, "cultivationcraft.gui.armpart.short");
        shortArm.addModel(BodyPartModelNames.shortArmLeftModel);
        shortArm.addModel(BodyPartModelNames.shortArmRightModel);
        shortArm.addArm(new Arm(InteractionHand.OFF_HAND, BodyPartModelNames.shortArmLeftModel, BodyPartModelNames.shortArmLeftModel, true));
        shortArm.addArm(new Arm(InteractionHand.MAIN_HAND, BodyPartModelNames.shortArmRightModel, BodyPartModelNames.shortArmRightModel, false));
        shortArm.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);
        shortArm.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.armWeight * -1);
        shortArm.getStatChanges().setStat(StatIDs.attackRange, StatIDs.defaultAttackRange * -0.5f);
        shortArm.setQuest(DefaultQuests.defaultBodyQuest);

        addPart(shortArm);


        BodyPart fArm = new BodyPart(flipperArmPart, armPosition, "cultivationcraft.gui.armpart.flipper");
        fArm.addModel(BodyPartModelNames.flipperLeftModel);
        fArm.addModel(BodyPartModelNames.flipperRightModel);
        fArm.addArm(new Arm(InteractionHand.OFF_HAND, BodyPartModelNames.flipperLeftModel, BodyPartModelNames.flipperLowerLeftModel, true));
        fArm.addArm(new Arm(InteractionHand.MAIN_HAND, BodyPartModelNames.flipperRightModel, BodyPartModelNames.flipperLowerRightModel, false));
        fArm.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);
        fArm.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.armWeight * -1.8f);
        fArm.getStatChanges().setStat(StatIDs.swimSpeed, 1f);
        fArm.getStatChanges().setStat(StatIDs.armAttackModifier, -0.5f);
        fArm.setQuest(DefaultQuests.defaultSwimQuest);

        addPart(fArm);


        BodyPart glide = new BodyPart(glideArmPart, armPosition, "cultivationcraft.gui.armpart.glide");
        glide.addModel(GenericLimbNames.leftArm);
        glide.addModel(GenericLimbNames.rightArm);
        glide.addArm(new Arm(InteractionHand.OFF_HAND, GenericLimbNames.leftArm, GenericLimbNames.lowerLeftArm, true));
        glide.addArm(new Arm(InteractionHand.MAIN_HAND, GenericLimbNames.rightArm, GenericLimbNames.lowerRightArm, false));
        glide.addQuad(BodyPartModelNames.armglidequad);
        glide.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.basePosition);
        glide.addUniqueTag(BodyPartTags.flight);
        glide.getStatChanges().setStat(StatIDs.weight, 0.01f);
        glide.getStatChanges().setStat(StatIDs.wingSupport, 2f);
        glide.getStatChanges().setStat(StatIDs.fallHeight, 99);
        glide.setQuest(DefaultQuests.defaultFlightQuest);

        addPart(glide);


        BodyPartOption headArms = new BodyPartOption(headArmsPart, armPosition, locationSubPosition, "cultivationcraft.gui.armpart.headarms");
        headArms.setConnection(new BodyPartLocation(armPosition, basePosition, headPosition, basePosition));
        headArms.addNeededPosition(BodyPartNames.armPosition, BodyPartNames.basePosition);
        headArms.addNeededPosition(BodyPartNames.headPosition, BodyPartNames.basePosition);
        headArms.addNotNeededPart(BodyPartNames.glideArmPart);
        headArms.setQuest(DefaultQuests.defaultBodyQuest);

        addOption(headArms);
    }

    protected static void setupHeadOptions()
    {
        BodyPartOption qiSight = new BodyPartOption(startingEyesPart, headPosition, eyeSubPosition, "cultivationcraft.gui.headpart.eye.qisight");
        qiSight.setQuest(new Quest(Quest.QI_SOURCE_MEDITATION, 10));
        addOption(qiSight);


        BodyPartOption flatTeeth = new BodyPartOption(flatTeethPart, headPosition, mouthSubPosition, "cultivationcraft.gui.headpart.mouth.flatteeth");
        flatTeeth.addModel(BodyPartModelNames.flatToothModel);
        flatTeeth.addModel(BodyPartModelNames.flatToothLowerModel, BodyPartModelNames.jawModelLower);
        flatTeeth.addModel(BodyPartModelNames.FPflatToothModel, BodyPartModelNames.FPjawModel);
        flatTeeth.addModel(BodyPartModelNames.FPflatToothLowerModel, BodyPartModelNames.FPjawModelLower);
        flatTeeth.addNeededPart(BodyPartNames.jawPart);
        flatTeeth.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.boneSubPosition);
        flatTeeth.setTexture(TextureList.bone);
        flatTeeth.getStatChanges().setStat(StatIDs.weight, 0.02f);
        flatTeeth.getStatChanges().setStat(StatIDs.biteAttackModifier, 0.75f);
        flatTeeth.setQuest(new Quest(BiteTechnique.biteQuest, 750));

        addOption(flatTeeth);

        BodyPartOption sharpTeeth = new BodyPartOption(sharpTeethPart, headPosition, mouthSubPosition, "cultivationcraft.gui.headpart.mouth.sharpteeth");
        sharpTeeth.addModel(BodyPartModelNames.sharpToothModel);
        sharpTeeth.addModel(BodyPartModelNames.sharpToothLowerModel, BodyPartModelNames.jawModelLower);
        sharpTeeth.addModel(BodyPartModelNames.FPsharpToothModel, BodyPartModelNames.FPjawModel);
        sharpTeeth.addModel(BodyPartModelNames.FPsharpToothLowerModel, BodyPartModelNames.FPjawModelLower);
        sharpTeeth.addNeededPart(BodyPartNames.jawPart);
        sharpTeeth.addNeededPosition(BodyPartNames.bodyPosition, BodyPartNames.boneSubPosition);
        sharpTeeth.setTexture(TextureList.bone);
        sharpTeeth.getStatChanges().setStat(StatIDs.weight, 0.02f);
        sharpTeeth.getStatChanges().setStat(StatIDs.biteAttackModifier, 1.5f);
        sharpTeeth.setQuest(new Quest(BiteTechnique.biteQuest, 1000));

        addOption(sharpTeeth);


        BodyPartOption frontHead = new BodyPartOption(headFrontPart, headPosition, locationSubPosition, "cultivationcraft.gui.headpart.location.front");
        BodyPartLocation frontHeadConnection = new BodyPartLocation(headPosition, basePosition, bodyPosition, basePosition);
        frontHeadConnection.adjustPos(new Vec3(0.5, 0.5, 0));
        frontHeadConnection.adjustRotationPoint(new Vec3(0.5, 0.5, 0));
        frontHead.setConnection(frontHeadConnection);
        frontHead.addNeededPosition(BodyPartNames.headPosition, BodyPartNames.basePosition);
        frontHead.setQuest(DefaultQuests.defaultBodyQuest);

        addOption(frontHead);
    }

    protected static void setupLegParts()
    {
        BodyPart feetpart = new BodyPart(feetPart, legPosition, "cultivationcraft.gui.legpart.feet");
        feetpart.addModel(BodyPartModelNames.footLeftModel);
        feetpart.addModel(BodyPartModelNames.footRightModel);
        feetpart.addNeededPart(BodyPartNames.startingEyesPart);
        feetpart.getStatChanges().setStat(StatIDs.movementSpeed, StatIDs.defaultMovementSpeed * -0.25f);
        feetpart.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.legWeight * -1.6f);
        feetpart.getStatChanges().setStat(StatIDs.legSupport, 2.5f);
        feetpart.setQuest(DefaultQuests.defaultLegQuest);

        addPart(feetpart);

        BodyPart largelegpart = new BodyPart(largeLegPart, legPosition, "cultivationcraft.gui.legpart.large");
        largelegpart.addModel(BodyPartModelNames.largeLegLeftModel);
        largelegpart.addModel(BodyPartModelNames.largeLegRightModel);
        largelegpart.addNeededPosition(BodyPartNames.armPosition, BodyPartNames.locationSubPosition);
        largelegpart.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.legWeight * 2f);
        largelegpart.getStatChanges().setStat(StatIDs.legSupport, 10);
        largelegpart.setQuest(DefaultQuests.defaultLegQuest);

        addPart(largelegpart);

        BodyPart rjLegPart = new BodyPart(reverseJointLegPart, legPosition, "cultivationcraft.gui.legpart.reversejoint");
        rjLegPart.addModel(BodyPartModelNames.reverseJointRightLegModel);
        rjLegPart.addModel(BodyPartModelNames.reverseJointLeftLegModel);
        rjLegPart.addNeededPart(BodyPartNames.startingEyesPart);
        rjLegPart.getStatChanges().setStat(StatIDs.jumpHeight, 4);
        rjLegPart.getStatChanges().setStat(StatIDs.fallHeight, 4);
        rjLegPart.setQuest(new Quest(Quest.JUMP, 10000));

        addPart(rjLegPart);

        SingleLegPart oneLegPart = new SingleLegPart(singleLegPart, legPosition, "cultivationcraft.gui.legpart.singleLeg");
        oneLegPart.getStatChanges().setStat(StatIDs.jumpHeight, 7);
        oneLegPart.getStatChanges().setStat(StatIDs.fallHeight, 7);
        oneLegPart.addModel(BodyPartModelNames.singleLegModel);
        oneLegPart.addNeededPart(BodyPartNames.startingEyesPart);
        oneLegPart.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.legWeight * -1);
        oneLegPart.getStatChanges().setStat(StatIDs.legSupport, StatIDs.defaultLegSupport * -0.25f);

        addPart(oneLegPart);

        BodyPart sixLegPart = new BodyPart(hexaLegPart, legPosition, "cultivationcraft.gui.legpart.hexaLeg");
        sixLegPart.addModel(BodyPartModelNames.hexaLeftLegModel);
        sixLegPart.addModel(BodyPartModelNames.hexaLeftLegModelTwo);
        sixLegPart.addModel(BodyPartModelNames.hexaLeftLegModelThree);
        sixLegPart.addModel(BodyPartModelNames.hexaRightLegModel);
        sixLegPart.addModel(BodyPartModelNames.hexaRightLegModelTwo);
        sixLegPart.addModel(BodyPartModelNames.hexaRightLegModelThree);
        sixLegPart.addNeededPart(BodyPartNames.startingEyesPart);
        sixLegPart.getStatChanges().setStat(StatIDs.jumpHeight, 4);
        sixLegPart.getStatChanges().setStat(StatIDs.fallHeight, 4);
        sixLegPart.getStatChanges().setStat(StatIDs.movementSpeed, 0.1f);
        sixLegPart.getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.legWeight * -1.5f);
        sixLegPart.getStatChanges().setStat(StatIDs.legSupport, StatIDs.defaultLegSupport * -0.5f);
        sixLegPart.setQuest(DefaultQuests.defaultLegQuest);

        addPart(sixLegPart);

        BodyPart jetLeg = new BodyPart(jetLegPart, legPosition, "cultivationcraft.gui.legpart.jet");
        jetLeg.addModel(BodyPartModelNames.jetLegLeftModel);
        jetLeg.addModel(BodyPartModelNames.jetLegRightModel);
        jetLeg.addNeededPart(BodyPartNames.startingEyesPart);
        jetLeg.addNeededTags(BodyPartTags.flame);
        jetLeg.getStatChanges().setStat(StatIDs.wingSupport, 10);
        jetLeg.getStatChanges().setStat(StatIDs.flightSpeed, 0.5f);
        jetLeg.getStatChanges().setStat(StatIDs.fallHeight, 4f);
        jetLeg.addUniqueTag(BodyPartTags.flight);
        jetLeg.setQuest(DefaultQuests.defaultFlightQuest);

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

    public static BodyPart getPartOrOption(String part)
    {
        BodyPart partFind = getPart(part);

        if (partFind == null)
            partFind = getOption(part);

        return partFind;
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
