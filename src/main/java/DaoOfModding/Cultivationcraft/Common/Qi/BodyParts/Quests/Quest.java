package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class Quest
{
    public static final ResourceLocation QI_SOURCE_MEDITATION = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.qisource");
    public static final ResourceLocation DAMAGE_TAKEN = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.damagetaken");
    public static final ResourceLocation DAMAGE_RESISTED = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.damageresisted");
    public static final ResourceLocation DAMAGE_ABSORBED = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.damageabsorbbed");
    public static final ResourceLocation TIME_ALIVE = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.alive");
    public static final ResourceLocation LIVE = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.live");
    public static final ResourceLocation BOUNCE = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.bounce");
    public static final ResourceLocation FLIGHT = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.fly");
    public static final ResourceLocation WALK = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.walk");
    public static final ResourceLocation SWIM = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.swim");
    public static final ResourceLocation JUMP = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.jump");
    public static final ResourceLocation HEAL = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.heal");
    public static final ResourceLocation DRAIN_STAMINA = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.drainstamina");

    public final ResourceLocation mode;
    public final double complete;

    public Quest(ResourceLocation questMode, double questMax)
    {
        mode = questMode;
        complete = questMax;
    }

    public double progress(ResourceLocation progressMode, double amount)
    {
        if (mode.compareTo(progressMode) == 0)
            return amount;

        return 0;
    }

    public String getDescription()
    {
        return Component.translatable(mode.getPath()).getString();
    }
}
