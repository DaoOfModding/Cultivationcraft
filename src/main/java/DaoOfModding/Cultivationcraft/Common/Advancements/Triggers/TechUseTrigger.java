package DaoOfModding.Cultivationcraft.Common.Advancements.Triggers;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class TechUseTrigger extends SimpleCriterionTrigger<TechUseTrigger.Instance>
{
    public static final ResourceLocation ID = new ResourceLocation(Cultivationcraft.MODID, "techuse");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public TechUseTrigger.Instance createInstance(JsonObject json, EntityPredicate.Composite composite, DeserializationContext context)
    {
        String techID = GsonHelper.getAsString(json, "techid");
        int conditional = GsonHelper.getAsInt(json, "conditional");
        return new TechUseTrigger.Instance(composite, techID, conditional);
    }

    public void trigger(ServerPlayer player, String tech, int conditional)
    {
        this.trigger(player, (p_61501_) -> {
            return p_61501_.matches(tech, conditional);
        });
    }

    public static class Instance extends AbstractCriterionTriggerInstance
    {
        private final String techID;
        private final int conditional;

        public Instance(EntityPredicate.Composite composite, String tech, int conditional)
        {
            super(ID, composite);
            this.techID = tech;
            this.conditional = conditional;
        }

        public boolean matches(String ID, int condition)
        {
            return ((techID.matches(ID) || techID.matches("")) && conditional == condition);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context)
        {
            JsonObject json = super.serializeToJson(context);
            json.addProperty("techid", techID);
            return json;
        }
    }
}
