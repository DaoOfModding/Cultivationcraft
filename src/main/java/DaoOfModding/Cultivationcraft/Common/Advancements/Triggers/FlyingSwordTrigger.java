package DaoOfModding.Cultivationcraft.Common.Advancements.Triggers;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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

    public void trigger(ServerPlayer player)
    {
        this.trigger(player, (p_61501_) -> {
            return true;
        });
    }

    @Override
    public FlyingSwordTrigger.Instance createInstance(JsonObject json, EntityPredicate.Composite composite, DeserializationContext context)
    {
        return new FlyingSwordTrigger.Instance(composite);
    }

    public static class Instance extends AbstractCriterionTriggerInstance
    {
        public Instance(EntityPredicate.Composite composite)
        {
            super(ID, composite);
        }
    }
}
