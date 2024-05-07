package DaoOfModding.Cultivationcraft.Common.Advancements.Triggers;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

public class EvolvedLimbTrigger extends SimpleCriterionTrigger<EvolvedLimbTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(Cultivationcraft.MODID, "evolved_limb");
    public static LootContextParam<Boolean> EVOLVED_LIMB = new LootContextParam<Boolean>(ID);
    public static LootContextParamSet requiredParams = LootContextParamSet.builder().required(EvolvedLimbTrigger.EVOLVED_LIMB).build();

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
    public EvolvedLimbTrigger.Instance createInstance(JsonObject json, EntityPredicate.Composite composite, DeserializationContext context) {
        return new EvolvedLimbTrigger.Instance(composite);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        public Instance(EntityPredicate.Composite composite)
        {
            super(ID, composite);
        }
    }
}
