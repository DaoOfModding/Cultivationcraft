package DaoOfModding.Cultivationcraft.Common.Advancements;

import DaoOfModding.Cultivationcraft.Common.Advancements.Triggers.BreakthroughTrigger;
import DaoOfModding.Cultivationcraft.Common.Advancements.Triggers.ExternalCultivationPathTrigger;
import DaoOfModding.Cultivationcraft.Common.Advancements.Triggers.InternalCultivationPathTrigger;
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
     * <br>
     * }<br>
     */
    public static ExternalCultivationPathTrigger EXTERNAL_CULTIVATION;
    /**
     * Json details:<br>
     * cultivationcraft:chose_internal_path {<br>
     * <br>
     * }<br>
     */
    public static InternalCultivationPathTrigger INTERNAL_CULTIVATION;

    public static void init(IEventBus bus) {
        HAS_BROKENTROUGH = (BreakthroughTrigger) registerAdvancementTrigger(new BreakthroughTrigger());
        EXTERNAL_CULTIVATION = (ExternalCultivationPathTrigger) registerAdvancementTrigger(new ExternalCultivationPathTrigger());
        INTERNAL_CULTIVATION = (InternalCultivationPathTrigger) registerAdvancementTrigger(new InternalCultivationPathTrigger());
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
            System.out.println("Failed to register trigger " + trigger.getId() + "!");
            e.printStackTrace();
        }
        return trigger;
    }
}
