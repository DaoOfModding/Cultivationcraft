package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood;

import DaoOfModding.Cultivationcraft.Client.Particles.Blood.GaseousBloodParticleData;
import DaoOfModding.Cultivationcraft.Client.Renderers.BloodRenderer;
import DaoOfModding.Cultivationcraft.Common.Qi.Effects.Wind;
import DaoOfModding.Cultivationcraft.Common.Qi.Effects.WindInstance;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import com.mojang.math.Vector3f;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class GaseousBlood extends CultivatorBlood
{
    public GaseousBlood()
    {
        colour = new Vector3f(0.6f, 1f ,0.8f);
        life = 20;

        staminaHealingModifier = 2;
        maxSpeed = 3;
        maxBloodSpawn = 50;
    }

    @Override
    public void regen(Player player)
    {
        // Only heal if player is in the air
        if (!player.isInWater() && !player.isOnGround())
            if (!staminaHealing(player))
                naturalHealing(player);
    }

    // Called when player takes damage
    public void onHit(Player player, Vec3 source, double amount)
    {
        // Send a block spawn packet to all clients if called on server
        if (!player.level.isClientSide)
        {
            PacketHandler.sendBloodSpawnToClient(player.getUUID(), source, amount);

            if (source != null)
            {
                Vec3 direction = player.position().subtract(source).normalize();
                Wind.addWindEffect(player, new WindInstance(direction, (float)amount * 0.025f, (float)amount / 5f));
            }

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
            Vec3 direction = BloodRenderer.getBloodDirection(player, source).scale(-1);
            double speed = (percent * maxSpeed) * (Math.random() * 0.5 + 0.5);
            direction = direction.scale(speed);

            float height = PoseHandler.getPlayerPoseHandler(player.getUUID()).getPlayerModel().getHeightAdjustment();

            player.level.addParticle(particle, player.getX(), player.getY() + height, player.getZ(), direction.x, direction.y, direction.z);
        }
    }

    @Override
    public boolean canHeal(ResourceLocation element, @Nullable Player player)
    {
        if (element == Elements.windElement && (player == null || player.hurtTime == 0))
            return true;

        return false;
    }

    @Override
    public ParticleOptions getParticle(Player player)
    {
        return new GaseousBloodParticleData(player);
    }
}
