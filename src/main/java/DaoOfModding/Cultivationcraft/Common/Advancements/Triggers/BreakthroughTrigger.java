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

public class BreakthroughTrigger extends LibCriteriaTrigger<BreakthroughTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(Cultivationcraft.MODID, "has_brokenthrough");
    public static LootContextParam<String> REALM_ID = new LootContextParam<String>(ID);
    public static LootContextParam<Integer> REALM_STAGE = new LootContextParam<Integer>(ID);
    public static LootContextParamSet requiredParams = LootContextParamSet.builder().required(BreakthroughTrigger.REALM_ID).required(BreakthroughTrigger.REALM_STAGE).build();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public BreakthroughTrigger.Instance createInstance(JsonObject json, DeserializationContext context) {
        String realmID = GsonHelper.getAsString(json, "realm_id");
        Integer currentStage = GsonHelper.getAsInt(json, "current_stage");
        System.out.println("Trigger fired realmID : " + realmID + " - currentStage : " + currentStage);
        return new BreakthroughTrigger.Instance(realmID, currentStage);
    }

    public static class Instance implements ICriterionTriggerInstanceTester {
        private final String realmID;
        private final Integer currentStage;

        public Instance(String realm_id, Integer current_stage) {
            this.realmID = realm_id;
            this.currentStage = current_stage;
        }

        @Override
        public ResourceLocation getCriterion() {
            return BreakthroughTrigger.ID;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject json = new JsonObject();
            json.addProperty("realm_id", realmID);
            json.addProperty("current_stage", currentStage);
            return json;
        }

        @Override
        public boolean test(LootContext ctx) {
            if (ctx.getParam(REALM_ID).equals(realmID) && ctx.getParam(REALM_STAGE).equals(currentStage)) {
                return true;
            }
            return false;
        }
    }
}
