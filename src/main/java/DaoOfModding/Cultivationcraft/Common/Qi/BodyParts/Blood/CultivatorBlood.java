package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood;

import DaoOfModding.Cultivationcraft.Client.Particles.Blood.BloodParticleData;
import DaoOfModding.Cultivationcraft.Client.Renderers.BloodRenderer;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec3;

public class CultivatorBlood extends Blood
{
    protected int maxBloodSpawn = 200;
    protected double maxSpeed = 1;

    public CultivatorBlood()
    {
        staminaHealingModifier = 4;
    }

    @Override
    public void naturalHealing(Player player)
    {
        boolean flag = player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        QiFoodStats food = (QiFoodStats)player.getFoodData();

        // If the player has stamina and is hurt, then heal
        if (flag && food.getFoodLevel() > 0 && player.isHurt())
        {
            // Get player regen, divided by 20 to convert seconds into ticks
            float regen = BodyPartStatControl.getStats(player).getStat(StatIDs.healthRegen) / 20;

            if (!player.level.isClientSide)
                QuestHandler.progressQuest(player, Quest.HEAL, regen);

            player.heal(regen);

            // Exhaust the player by the amount regenerated multiplied by their healthStaminaConversion modifier
            food.addExhaustion(regen * BodyPartStatControl.getStats(player).getStat(StatIDs.healthStaminaConversion) * 4);
        }
    }

    // Called when player takes damage
    public void onHit(Player player, Vec3 source, double amount)
    {
        // Send a block spawn packet to all clients if called on server
        if (!player.level.isClientSide)
        {
            PacketHandler.sendBloodSpawnToClient(player.getUUID(), source, amount);
            return;
        }

        ParticleOptions particle = getParticle(player);

        // Do nothing if this blood has no particle
        if (particle == null)
            return;

        double percent = amount / player.getMaxHealth();
        int toSpawn =  (int)(percent * maxBloodSpawn);

        for (int i = 0; i <toSpawn; i++)
        {
            Vec3 direction = BloodRenderer.getBloodDirection(player, source);
            double speed = (percent * maxSpeed) * (Math.random() * 0.5 + 0.5);
            direction = direction.scale(speed);

            float height = PoseHandler.getPlayerPoseHandler(player.getUUID()).getPlayerModel().getHeightAdjustment();

            player.level.addParticle(particle, player.getX(), player.getY() + height, player.getZ(), direction.x, direction.y, direction.z);
        }
    }

    public ParticleOptions getParticle(Player player)
    {
        return new BloodParticleData(player);
    }
}
