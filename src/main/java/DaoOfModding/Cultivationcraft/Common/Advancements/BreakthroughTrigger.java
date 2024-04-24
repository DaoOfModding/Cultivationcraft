package DaoOfModding.Cultivationcraft.Common.Advancements;

import DaoOfModding.Cultivationcraft.Common.Advancements.utils.ICriterionTriggerInstanceTester;
import DaoOfModding.Cultivationcraft.Common.Advancements.utils.LibCriteriaTrigger;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

public class BreakthroughTrigger extends LibCriteriaTrigger<BreakthroughTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(Cultivationcraft.MODID, "has_breakthrough");
    public static LootContextParam<Boolean> HAS_BROKENTROUGH = new LootContextParam<Boolean>(ID);
    public static LootContextParamSet requiredParams = LootContextParamSet.builder().required(BreakthroughTrigger.HAS_BROKENTROUGH).build();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public BreakthroughTrigger.Instance createInstance(JsonObject json, DeserializationContext context) {
        return new BreakthroughTrigger.Instance();
    }

    public static class Instance implements ICriterionTriggerInstanceTester {
        public Instance() {
        }

        @Override
        public ResourceLocation getCriterion() {
            return BreakthroughTrigger.ID;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject json = new JsonObject();
            return json;
        }

        @Override
        public boolean test(LootContext ctx) {
            return ctx.getParam(HAS_BROKENTROUGH);
        }
    }
}
