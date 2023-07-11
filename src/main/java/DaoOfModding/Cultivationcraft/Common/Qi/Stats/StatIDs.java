package DaoOfModding.Cultivationcraft.Common.Qi.Stats;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;

public class StatIDs
{
    public static final ResourceLocation weight = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.weight");
    public static final ResourceLocation movementSpeed = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.movementspeed");
    public static final ResourceLocation swimSpeed = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.swimspeed");
    public static final ResourceLocation maxHP = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.maxhp");
    public static final ResourceLocation maxStamina = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.maxstamina");
    public static final ResourceLocation lungCapacity = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.lungcapacity");
    public static final ResourceLocation jumpHeight = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.jumpheight");
    public static final ResourceLocation fallHeight = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.fallheight");
    public static final ResourceLocation bounceHeight = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.bounceheight");

    public static final ResourceLocation staminaDrain = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.staminadrain");
    public static final ResourceLocation staminaUse = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.staminause");

    public static final ResourceLocation healthRegen = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.healthregen");
    public static final ResourceLocation healthStaminaConversion = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.healthstaminaconversion");

    public static final ResourceLocation qiAbsorb = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.qiabsorb");
    public static final ResourceLocation qiAbsorbRange = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.qiabsorbrange");
    public static final ResourceLocation qiCost = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.qicost");

    public static final ResourceLocation armor = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.armor");
    public static final ResourceLocation armorToughness = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.armortoughness");

    public static final ResourceLocation width = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.width");
    public static final ResourceLocation size = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.size");

    // TODO: Make this do something
    public static final ResourceLocation attackRange = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.attackrange");

    // TODO: Make this do something
    public static final ResourceLocation armAttackModifier = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.armattackmodifier");

    public static final ResourceLocation legSupport = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.legweight");
    public static final ResourceLocation wingSupport = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.wingweight");

    public static final ResourceLocation flightSpeed = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.flightspeed");

    public static final ResourceLocation boneAttackModifier = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.boneattackmodifier");
    public static final ResourceLocation biteAttackModifier = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.biteattackmodifier");

    public static final ResourceLocation resistanceModifier = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.stat.resistancemodifier");

    public static final int defaultMaxHP = 20;
    public static final int defaultMaxStamina = 20;
    public static final int defaultLungCapacity = 300;
    public static final float defaultMovementSpeed = 0.10000000149011612f;

    public static final float defaultSwimSpeed = 1;

    public static final int defaultStaminaUse = 1;
    public static final int defaultWeight = 1;
    public static final float defaultLegSupport = 1.5f;
    public static final int defaultJumpHeight = 1;
    public static final float defaultFallHeight = 1.5f;
    public static final int defaulthealthStaminaConversion = 1;
    public static final float defaultHealthRegen = 0.1f;

    public static final float defaultAttackRange = 4.5f;
    public static final float defaultAttackModifier = 1;

    public static final float defaultQiAbsorbRange = 1;

    public static final float defaultLightningResist = -100;
}
