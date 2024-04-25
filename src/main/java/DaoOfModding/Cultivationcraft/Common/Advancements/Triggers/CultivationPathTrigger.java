package DaoOfModding.Cultivationcraft.Common.Advancements.Triggers;

import DaoOfModding.Cultivationcraft.Common.Advancements.utils.ICriterionTriggerInstanceTester;
import DaoOfModding.Cultivationcraft.Common.Advancements.utils.LibCriteriaTrigger;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

public class CultivationPathTrigger extends LibCriteriaTrigger<CultivationPathTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(Cultivationcraft.MODID, "chose_path");
    public static LootContextParam<Integer> CHOSEN_PATH = new LootContextParam<Integer>(ID);
    public static LootContextParamSet requiredParams = LootContextParamSet.builder().required(CultivationPathTrigger.CHOSEN_PATH).build();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public CultivationPathTrigger.Instance createInstance(JsonObject json, DeserializationContext context) {
        Integer chosenPath = GsonHelper.getAsInt(json, "chosen_path");
        return new CultivationPathTrigger.Instance(chosenPath);
    }

    public static class Instance implements ICriterionTriggerInstanceTester {
        private final Integer chosenPath;

        public Instance(Integer chosen_path) {
            this.chosenPath = chosen_path;
        }

        @Override
        public ResourceLocation getCriterion() {
            return CultivationPathTrigger.ID;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject json = new JsonObject();
            json.addProperty("chosen_path", chosenPath);
            return json;
        }

        @Override
        public boolean test(LootContext ctx) {
            return ctx.getParam(CHOSEN_PATH) == chosenPath;
        }
    }
}
