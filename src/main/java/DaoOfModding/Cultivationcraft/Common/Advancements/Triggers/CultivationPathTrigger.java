package DaoOfModding.Cultivationcraft.Common.Advancements.Triggers;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

public class CultivationPathTrigger extends SimpleCriterionTrigger<CultivationPathTrigger.Instance>
{
    public static final ResourceLocation ID = new ResourceLocation(Cultivationcraft.MODID, "chose_path");
    public static LootContextParam<Integer> CHOSEN_PATH = new LootContextParam<Integer>(ID);

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public CultivationPathTrigger.Instance createInstance(JsonObject json, EntityPredicate.Composite composite, DeserializationContext context)
    {
        Integer chosenPath = GsonHelper.getAsInt(json, "chosen_path");
        return new CultivationPathTrigger.Instance(composite, chosenPath);
    }

    public void trigger(ServerPlayer player, int type)
    {
        this.trigger(player, (p_61501_) -> {
            return p_61501_.matches(type);
        });
    }

    public static class Instance extends AbstractCriterionTriggerInstance
    {
        private final Integer chosenPath;

        public Instance(EntityPredicate.Composite composite, Integer chosen_path)
        {
            super(ID, composite);
            this.chosenPath = chosen_path;
        }

        public boolean matches(int type)
        {
            return (chosenPath == type);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context)
        {
            JsonObject json = super.serializeToJson(context);
            json.addProperty("chosen_path", chosenPath);
            return json;
        }
    }
}
