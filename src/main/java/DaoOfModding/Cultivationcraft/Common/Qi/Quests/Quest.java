package DaoOfModding.Cultivationcraft.Common.Qi.Quests;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Quest
{
    public static final ResourceLocation QI_SOURCE_MEDITATION = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.qisource");
    public static final ResourceLocation DAMAGE_TAKEN = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.damagetaken");
    public static final ResourceLocation DAMAGE_DEALT = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.damagedealt");
    public static final ResourceLocation DAMAGE_RESISTED = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.damageresisted");
    public static final ResourceLocation DAMAGE_ABSORBED = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.damageabsorbbed");
    public static final ResourceLocation EXPLOSION_DAMAGE_TAKEN = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.explosiondamagetaken");
    public static final ResourceLocation EXPLOSION_DAMAGE_DEALT = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.explosiondamagedealt");
    public static final ResourceLocation ELEMENTALY_EFFECTED = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.elementalyeffected");
    public static final ResourceLocation ELEMENTAL_EFFECT_APPLIED = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.elementaleffectapplied");
    public static final ResourceLocation TIME_ALIVE = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.alive");
    public static final ResourceLocation LIVE = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.live");
    public static final ResourceLocation BOUNCE = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.bounce");
    public static final ResourceLocation GROWPLANT = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.growplant");
    public static final ResourceLocation GROW = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.grow");
    public static final ResourceLocation FLIGHT = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.fly");
    public static final ResourceLocation WALK = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.walk");
    public static final ResourceLocation SWIM = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.swim");
    public static final ResourceLocation JUMP = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.jump");
    public static final ResourceLocation HEAL = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.heal");
    public static final ResourceLocation DRAIN_STAMINA = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.drainstamina");
    public static final ResourceLocation ENDER_TELEPORT = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.endertp");

    public final ResourceLocation mode;
    public final double complete;
    public final String extra;

    public Quest(ResourceLocation questMode, double questMax)
    {
        mode = questMode;
        complete = questMax;
        extra = null;
    }

    public Quest(ResourceLocation questMode, double questMax, String extraRequirement)
    {
        mode = questMode;
        complete = questMax;
        extra = extraRequirement;
    }

    public double progress(ResourceLocation progressMode, double amount, String extraRequirement)
    {
        if (mode.compareTo(progressMode) == 0 && ((extra == null && extraRequirement == null) || (extraRequirement != null && extra.compareTo(extraRequirement) == 0)))
            return amount;

        return 0;
    }

    public String getDescription()
    {
        String description = Component.translatable(mode.getPath()).getString();

        if (extra != null)
            description = extra + " " + description;

        return description;
    }

    public CompoundTag writeNBT()
    {
        CompoundTag nbt = new CompoundTag();

        nbt.putString("mode", mode.toString());
        nbt.putDouble("complete", complete);

        if (nbt.contains("extra"))
            nbt.putString("extra", extra);

        return nbt;
    }

    public static Quest readNBT(CompoundTag nbt)
    {
        if (nbt.contains("extra"))
            return new Quest(new ResourceLocation(nbt.getString("mode")), nbt.getDouble("complete"), nbt.getString("extra"));

        return new Quest(new ResourceLocation(nbt.getString("mode")), nbt.getDouble("complete"));
    }
}
