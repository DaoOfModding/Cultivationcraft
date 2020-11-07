package DaoOfModding.Cultivationcraft.Client.Renderers;

import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleTypes;

public class QiSourceRenderer
{
    public static void render(QiSource source)
    {
        double x = source.getPos().getX();
        double y = source.getPos().getY();
        double z = source.getPos().getZ();

        Minecraft.getInstance().world.addParticle(ParticleTypes.HEART, x, y, z, 0, 0, 0);
    }
}
