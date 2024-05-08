package DaoOfModding.Cultivationcraft.Common.Advancements;

import DaoOfModding.Cultivationcraft.Common.Advancements.Triggers.*;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CultivationAdvancements
{
    public static BreakthroughTrigger HAS_BROKENTROUGH;
    public static CultivationPathTrigger CULTIVATION_PATH;
    public static FlyingSwordTrigger HAS_FLYING_SWORD;
    public static FlyingSwordAttackTrigger FLYING_SWORD_ATTACk;
    public static EvolvedLimbTrigger EVOLVED_LIMB;
    public static TechUseTrigger TECH_USE;

    public static void init(IEventBus bus)
    {
        HAS_BROKENTROUGH = CriteriaTriggers.register(new BreakthroughTrigger());
        CULTIVATION_PATH = CriteriaTriggers.register(new CultivationPathTrigger());
        HAS_FLYING_SWORD = CriteriaTriggers.register(new FlyingSwordTrigger());
        FLYING_SWORD_ATTACk = CriteriaTriggers.register(new FlyingSwordAttackTrigger());
        EVOLVED_LIMB = CriteriaTriggers.register(new EvolvedLimbTrigger());
        TECH_USE = CriteriaTriggers.register(new TechUseTrigger());
    }
}
