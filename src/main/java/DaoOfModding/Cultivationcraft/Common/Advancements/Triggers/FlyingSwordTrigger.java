package DaoOfModding.Cultivationcraft.Common.Advancements.Triggers;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

public class FlyingSwordTrigger extends SimpleCriterionTrigger<FlyingSwordTrigger.Instance>
{
    public static final ResourceLocation ID = new ResourceLocation(Cultivationcraft.MODID, "has_flying_sword");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player, boolean deployed)
    {
        this.trigger(player, (p_61501_) -> {
            return p_61501_.matches(deployed);
        });
    }

    @Override
    public FlyingSwordTrigger.Instance createInstance(JsonObject json, EntityPredicate.Composite composite, DeserializationContext context)
    {
        boolean deployed = GsonHelper.getAsBoolean(json, "deployed");
        return new FlyingSwordTrigger.Instance(composite, deployed);
    }

    public static class Instance extends AbstractCriterionTriggerInstance
    {
        private final Boolean out;

        public Instance(EntityPredicate.Composite composite, boolean deployed)
        {
            super(ID, composite);

            out = deployed;
        }

        public boolean matches(boolean deployed)
        {
            return (out == deployed);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context)
        {
            JsonObject json = super.serializeToJson(context);
            json.addProperty("deployed", out);
            return json;
        }
    }
}
