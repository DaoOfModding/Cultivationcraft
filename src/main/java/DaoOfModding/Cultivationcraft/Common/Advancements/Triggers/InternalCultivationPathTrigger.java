package DaoOfModding.Cultivationcraft.Common.Advancements.Triggers;

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

public class InternalCultivationPathTrigger extends LibCriteriaTrigger<InternalCultivationPathTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(Cultivationcraft.MODID, "chose_internal_path");
    public static LootContextParam<Integer> CHOSEN_INTERNAL_PATH = new LootContextParam<Integer>(ID);
    public static LootContextParamSet requiredParams = LootContextParamSet.builder().required(InternalCultivationPathTrigger.CHOSEN_INTERNAL_PATH).build();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public InternalCultivationPathTrigger.Instance createInstance(JsonObject json, DeserializationContext context) {
        return new InternalCultivationPathTrigger.Instance();
    }

    public static class Instance implements ICriterionTriggerInstanceTester {
        public Instance() {
        }

        @Override
        public ResourceLocation getCriterion() {
            return InternalCultivationPathTrigger.ID;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject json = new JsonObject();
            return json;
        }

        @Override
        public boolean test(LootContext ctx) {
            return ctx.getParam(CHOSEN_INTERNAL_PATH) == 1;
        }
    }
}
