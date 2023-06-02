package DaoOfModding.Cultivationcraft.Client.Renderers;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.Blood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class BloodRenderer
{
    protected static final int maxBloodSpawn = 200;
    protected static final double maxSpeed = 1;

    public static void spawnBlood(Player player, Vec3 source, double amount)
    {
        Blood playerBlood = PlayerHealthManager.getBlood(player);

        // Do nothing if this blood has no particle
        if (playerBlood.getParticle(player) == null)
            return;

        double percent = amount / player.getMaxHealth();
        int toSpawn =  (int)(percent * maxBloodSpawn);

        for (int i = 0; i <toSpawn; i++)
        {
            Vec3 direction = getBloodDirection(player, source);
            double speed = (percent * maxSpeed) * (Math.random() * 0.5 + 0.5);
            direction = direction.scale(speed);

            ParticleOptions particle = playerBlood.getParticle(player);

            float height = PoseHandler.getPlayerPoseHandler(player.getUUID()).getPlayerModel().getHeightAdjustment();

            player.level.addParticle(particle, player.getX(), player.getY() + height, player.getZ(), direction.x, direction.y, direction.z);
        }
    }

    protected static Vec3 getBloodDirection(Player player, Vec3 source)
    {
        Vec3 direction;

        if (source != null)
        {
            double x = player.getX() - source.x();
            double z = player.getZ() - source.z();

            direction = new Vec3(x, 0 , z);
        }
        else
            direction = new Vec3(Math.random()* 2 - 1, 0, Math.random() * 2 - 1);

        double rand = Math.random() * 0.3 - 0.15;

        direction = direction.normalize();
        return direction.add(rand, 0, -rand);
    }
}
