package DaoOfModding.Cultivationcraft.Client.Renderers;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.Blood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class BloodRenderer
{
    public static void spawnBlood(Player player, Vec3 source, double amount)
    {
        PlayerHealthManager.getBlood(player).onHit(player, source, amount);
    }

    public static Vec3 getBloodDirection(Player player, Vec3 source)
    {
        Vec3 direction;

        if (source != null)
        {
            double x = player.getX() - source.x();
            double y = player.getY() - source.y();
            double z = player.getZ() - source.z();

            direction = new Vec3(x, y , z);
        }
        else
            direction = new Vec3(Math.random()* 2 - 1, 0, Math.random() * 2 - 1);

        double rand = Math.random() * 0.3 - 0.15;

        direction = direction.normalize();
        return direction.add(rand, Math.random() * 0.3 - 0.15, -rand);
    }
}
