package DaoOfModding.Cultivationcraft.Common.Advancements.Triggers;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class FlyingSwordAttackTrigger extends SimpleCriterionTrigger<FlyingSwordAttackTrigger.Instance>
{
    public static final ResourceLocation ID = new ResourceLocation(Cultivationcraft.MODID, "flying_sword_attack_advancement");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player, boolean dead)
    {
        this.trigger(player, (p_61501_) -> {
            return p_61501_.matches(dead);
        });
    }

    @Override
    public FlyingSwordAttackTrigger.Instance createInstance(JsonObject json, EntityPredicate.Composite composite, DeserializationContext context)
    {
        boolean dead = GsonHelper.getAsBoolean(json, "killed");
        return new FlyingSwordAttackTrigger.Instance(composite, dead);
    }

    public static class Instance extends AbstractCriterionTriggerInstance
    {
        private final Boolean killed;

        public Instance(EntityPredicate.Composite composite, boolean dead)
        {
            super(ID, composite);

            killed = dead;
        }

        public boolean matches(boolean dead)
        {
            return (killed == dead);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context)
        {
            JsonObject json = super.serializeToJson(context);
            json.addProperty("killed", killed);
            return json;
        }
    }
}
