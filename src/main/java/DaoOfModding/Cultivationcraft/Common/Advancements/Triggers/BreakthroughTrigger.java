package DaoOfModding.Cultivationcraft.Common.Advancements.Triggers;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTrigger;
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

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public BreakthroughTrigger.Instance createInstance(JsonObject json, EntityPredicate.Composite composite, DeserializationContext context)
    {
        boolean tribulation = GsonHelper.getAsBoolean(json, "tribulation");
        return new BreakthroughTrigger.Instance(composite, tribulation);
    }

    public void trigger(ServerPlayer player, boolean tribulation)
    {
        this.trigger(player, (p_61501_) -> {
            return p_61501_.matches(tribulation);
        });
    }

    public static class Instance extends AbstractCriterionTriggerInstance
    {
        private final Boolean tribulation;

        public Instance(EntityPredicate.Composite composite, boolean trib)
        {
            super(ID, composite);
            this.tribulation = trib;
        }

        public boolean matches(boolean trib)
        {
            return (tribulation == trib);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context)
        {
            JsonObject json = super.serializeToJson(context);
            json.addProperty("tribulation", tribulation);
            return json;
        }
    }
}
