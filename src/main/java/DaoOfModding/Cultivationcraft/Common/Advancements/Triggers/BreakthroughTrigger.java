package DaoOfModding.Cultivationcraft.Common.Advancements.Triggers;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

public class BreakthroughTrigger extends SimpleCriterionTrigger<BreakthroughTrigger.Instance>
{
    public static final ResourceLocation ID = new ResourceLocation(Cultivationcraft.MODID, "has_brokenthrough");
    public static LootContextParam<String> REALM_ID = new LootContextParam<String>(ID);
    public static LootContextParam<Integer> REALM_STAGE = new LootContextParam<Integer>(ID);
    public static LootContextParamSet requiredParams = LootContextParamSet.builder().required(BreakthroughTrigger.REALM_ID).required(BreakthroughTrigger.REALM_STAGE).build();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public BreakthroughTrigger.Instance createInstance(JsonObject json, EntityPredicate.Composite composite, DeserializationContext context)
    {
        String realmID = GsonHelper.getAsString(json, "realm_id");
        Integer currentStage = GsonHelper.getAsInt(json, "current_stage");
        return new BreakthroughTrigger.Instance(composite, realmID, currentStage);
    }

    public void trigger(ServerPlayer player, String realm, int stage)
    {
        this.trigger(player, (p_61501_) -> {
            return p_61501_.matches(realm, stage);
        });
    }

    public static class Instance extends AbstractCriterionTriggerInstance
    {
        private final String realmID;
        private final Integer currentStage;

        public Instance(EntityPredicate.Composite composite, String realm_id, Integer current_stage)
        {
            super(ID, composite);
            this.realmID = realm_id;
            this.currentStage = current_stage;
        }

        public boolean matches(String ID, int stage)
        {
            return (realmID.matches(ID) && stage == currentStage);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context)
        {
            JsonObject json = super.serializeToJson(context);
            json.addProperty("realm_id", realmID);
            json.addProperty("current_stage", currentStage);
            return json;
        }
    }
}
