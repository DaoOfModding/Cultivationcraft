package DaoOfModding.Cultivationcraft.Common.Advancements.utils;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.world.level.storage.loot.LootContext;

// Shamelessly stolen from https://github.com/Draco18s/ReasonableRealism/blob/1.19/src/main/java/com/draco18s/hardlib/api/advancement/CriterionListener.java
public interface ICriterionTriggerInstanceTester extends CriterionTriggerInstance {
    // I can just hijack the lootcontext and lootcontextparams to pass the relevant values
    // Then the generic class doesn't need to know about them
    boolean test(LootContext ctx);
}