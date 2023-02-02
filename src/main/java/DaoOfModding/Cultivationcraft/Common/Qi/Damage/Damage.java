package DaoOfModding.Cultivationcraft.Common.Qi.Damage;

import DaoOfModding.Cultivationcraft.Client.Renderers.BloodRenderer;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
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

        progressDamageQuest((Player)event.getEntity(), event.getAmount());

        return resistedDamage;
    }

    public static boolean shouldCancel(LivingAttackEvent event)
    {
        QiDamageSource source = damageSourceToQiDamageSource(event.getSource());
        float damage = event.getAmount();

        PlayerStatModifications stats = BodyPartStatControl.getStats(event.getEntity().getUUID());
        float resistedDamage = damage * (1 - (stats.getElementalStat(StatIDs.resistanceModifier, source.damageElement)  / 100.0f));

        // TODO: Resist damage quest

        if (resistedDamage <= 0)
            return true;

        Vec3 position = source.getSourcePosition();

        if (position == null && source.getEntity() != null)
            position = source.getEntity().position();

        PacketHandler.sendBloodSpawnToClient(event.getEntity().getUUID(), position, event.getAmount());

        return false;
    }

    public static void progressDamageQuest(Player player, double amount)
    {
        QuestHandler.progressQuest(player, Quest.DAMAGE_TAKEN, amount);
    }

    protected static QiDamageSource damageSourceToQiDamageSource(DamageSource damage)
    {
        if (damage instanceof QiDamageSource)
            return (QiDamageSource)damage;

        return new QiDamageSource(damage);
    }
}
