package DaoOfModding.Cultivationcraft.Common.Advancements;

import DaoOfModding.Cultivationcraft.Common.Advancements.Triggers.BreakthroughTrigger;
import DaoOfModding.Cultivationcraft.Common.Advancements.Triggers.CultivationPathTrigger;
import DaoOfModding.Cultivationcraft.Common.Advancements.Triggers.EvolvedLimbTrigger;
import DaoOfModding.Cultivationcraft.Common.Advancements.Triggers.FlyingSwordTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CultivationAdvancements {
    private static Method CriterionRegister;

    /**
     * Json details:<br>
     * cultivationcraft:has_brokenthrough {<br>
     * &nbsp;&nbsp; realm_id:string <br>
     * &nbsp;&nbsp; current_stage:int<br>
     * }<br>
     */
    public static BreakthroughTrigger HAS_BROKENTROUGH;
    /**
     * Json details:<br>
     * cultivationcraft:chose_external_path {<br>
     * &nbsp;&nbsp; chosen_path:integer <br>
     * }<br>
     * where &nbsp;0 = external Cultivation <br>
     * and &nbsp;&nbsp;&nbsp;&nbsp; 1 = internal Cultivation
     */
    public static CultivationPathTrigger CULTIVATION_PATH;
    /**
     * Json details:<br>
     * cultivationcraft:has_flying_sword {<br>
     * <br>
     * }<br>
     */
    public static FlyingSwordTrigger HAS_FLYING_SWORD;
    /**
     * Json details:<br>
     * cultivationcraft:evolved_limb {<br>
     * <br>
     * }<br>
     */
    public static EvolvedLimbTrigger EVOLVED_LIMB;

    public static void init(IEventBus bus) {
        HAS_BROKENTROUGH = (BreakthroughTrigger) registerAdvancementTrigger(new BreakthroughTrigger());
        CULTIVATION_PATH = (CultivationPathTrigger) registerAdvancementTrigger(new CultivationPathTrigger());
        HAS_FLYING_SWORD = (FlyingSwordTrigger) registerAdvancementTrigger(new FlyingSwordTrigger());
        EVOLVED_LIMB = (EvolvedLimbTrigger) registerAdvancementTrigger(new EvolvedLimbTrigger());
    }


    @SuppressWarnings("unchecked")
    public static <T extends CriterionTrigger<?>> T registerAdvancementTrigger(T trigger) {
        if (CriterionRegister == null) {
            //Class<?> persistentClass = trigger.getClass().getGenericSuperclass().getClass();
            CriterionRegister = ObfuscationReflectionHelper.findMethod(CriteriaTriggers.class, "register", CriterionTrigger.class);
            CriterionRegister.setAccessible(true);
        }
        try {
            trigger = (T) CriterionRegister.invoke(null, trigger);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            System.out.println("Failed to register trigger " + trigger.getId() + "! ");
            e.printStackTrace();
        }
        return trigger;
    }
}
