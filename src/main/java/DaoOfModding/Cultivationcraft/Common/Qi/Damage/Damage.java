package DaoOfModding.Cultivationcraft.Common.Qi.Damage;

import DaoOfModding.Cultivationcraft.Client.Renderers.BloodRenderer;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

        return resistDamage(event.getAmount(), source.damageElement, (Player)event.getEntity());
    }

    public static float resistDamage(float damage, ResourceLocation element, Player player)
    {
        PlayerStatModifications stats = BodyPartStatControl.getStats(player.getUUID());

        float multiplier = (1 - (stats.getElementalStat(StatIDs.resistanceModifier, element)  / 100.0f));

        if (multiplier < 0 && !PlayerHealthManager.getBlood(player).canHeal(element))
            multiplier = 0;

        return damage * multiplier;
    }

    public static void applyStatusEffects(LivingHurtEvent event)
    {
        QiDamageSource source = damageSourceToQiDamageSource(event.getSource());

        if (source.getElement() != null)
            Elements.getElement(source.getElement()).applyStatusEffect(event.getEntity(), event.getAmount());
    }

    // TODO: Mob resistances
    public static float damageEntity(LivingHurtEvent event)
    {
        float damage = event.getAmount();

        if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof ServerPlayer)
        {
            QuestHandler.damageProgress((Player)event.getSource().getEntity(), damageSourceToQiDamageSource(event.getSource()), damage);
        }

        return damage;
    }

    public static boolean shouldCancel(LivingAttackEvent event)
    {
        QiDamageSource source = damageSourceToQiDamageSource(event.getSource());
        float damage = armorAbsorption((Player)event.getEntity(), source, event.getAmount());
        float resistedDamage = resistDamage(damage, source.damageElement, (Player)event.getEntity());

        if (resistedDamage <= 0)
        {
            QuestHandler.progressQuest((Player) event.getEntity(), Quest.DAMAGE_RESISTED, event.getAmount());

            // If taking negative damage then heal that amount
            event.getEntity().heal(resistedDamage * -1);

            return true;
        }

        QuestHandler.progressQuest((Player)event.getEntity(), Quest.DAMAGE_TAKEN, resistedDamage);
        QuestHandler.progressQuest((Player)event.getEntity(), Quest.DAMAGE_RESISTED, event.getAmount() - resistedDamage);

        if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof ServerPlayer)
            QuestHandler.damageProgress((Player)event.getSource().getEntity(), damageSourceToQiDamageSource(event.getSource()), resistedDamage);

        Vec3 position = source.getSourcePosition();

        if (position == null && source.getEntity() != null)
            position = source.getEntity().position();

        // Do not spawn blood from internal damage
        if (!source.isInternal())
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
