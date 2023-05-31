package DaoOfModding.Cultivationcraft.Common.Qi.Damage;

import DaoOfModding.Cultivationcraft.Client.Renderers.BloodRenderer;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class Damage
{
    public static float damage(LivingHurtEvent event)
    {
        QiDamageSource source = damageSourceToQiDamageSource(event.getSource());

        float damage = event.getAmount();

        PlayerStatModifications stats = BodyPartStatControl.getStats(event.getEntity().getUUID());
        float resistedDamage = damage * (1 - (stats.getElementalStat(StatIDs.resistanceModifier, source.damageElement)  / 100.0f));

        System.out.println(" deals " + resistedDamage + " (out of " + event.getAmount() + ") " + Component.translatable(source.damageElement.getPath()).getString() + " damage to " + event.getEntity().getName());

        return resistedDamage;
    }

    public static void applyStatusEffects(LivingHurtEvent event)
    {
        QiDamageSource source = damageSourceToQiDamageSource(event.getSource());

        Elements.getElement(source.getElement()).applyStatusEffect(event.getEntity(), event.getAmount());
    }

    // TODO: Mob resistances
    public static float damageEntity(LivingHurtEvent event)
    {
        QiDamageSource source = damageSourceToQiDamageSource(event.getSource());
        float damage = event.getAmount();

        return damage;
    }

    public static boolean shouldCancel(LivingAttackEvent event)
    {
        QiDamageSource source = damageSourceToQiDamageSource(event.getSource());
        float damage = armorAbsorption((Player)event.getEntity(), source, event.getAmount());

        PlayerStatModifications stats = BodyPartStatControl.getStats(event.getEntity().getUUID());
        float resistedDamage = damage * (1 - (stats.getElementalStat(StatIDs.resistanceModifier, source.damageElement)  / 100.0f));

        QuestHandler.progressQuest((Player)event.getEntity(), Quest.DAMAGE_TAKEN, event.getAmount());

        if (resistedDamage <= 0)
        {
            //TODO: Damage absorb quest
            return true;
        }

        QuestHandler.progressQuest((Player) event.getEntity(), Quest.DAMAGE_RESISTED, event.getAmount() - resistedDamage);

        Vec3 position = source.getSourcePosition();

        if (position == null && source.getEntity() != null)
            position = source.getEntity().position();

        PacketHandler.sendBloodSpawnToClient(event.getEntity().getUUID(), position, event.getAmount());

        return false;
    }

    protected static float armorAbsorption(Player player, QiDamageSource source, float amount)
    {
        if (!source.isBypassArmor())
            amount = CombatRules.getDamageAfterAbsorb(amount, (float)player.getArmorValue(), (float)player.getAttributeValue(Attributes.ARMOR_TOUGHNESS));

        if (!source.isBypassMagic())
        {
            if (player.hasEffect(MobEffects.DAMAGE_RESISTANCE) && source != DamageSource.OUT_OF_WORLD)
            {
                int i = (player.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = amount * (float)j;
                float f1 = amount;
                amount = Math.max(f / 25.0F, 0.0F);
                float f2 = f1 - amount;
            }

            if (!source.isBypassEnchantments())
            {
                int k = EnchantmentHelper.getDamageProtection(player.getArmorSlots(), source);
                if (k > 0)
                    amount = CombatRules.getDamageAfterMagicAbsorb(amount, (float)k);
            }
        }

        if (amount < 0)
            amount = 0;

        return Math.max(amount - player.getAbsorptionAmount(), 0.0F);
    }

    protected static QiDamageSource damageSourceToQiDamageSource(DamageSource damage)
    {
        if (damage instanceof QiDamageSource)
            return (QiDamageSource)damage;

        return new QiDamageSource(damage);
    }
}
