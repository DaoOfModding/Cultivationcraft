package DaoOfModding.Cultivationcraft.Client.Particles.Blood;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.Blood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GaseousBloodParticle extends BloodParticle
{
    public GaseousBloodParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int lifespan, Blood source)
    {
        super(world, x, y, z, velocityX, velocityY, velocityZ, lifespan, source);

        this.gravity = 0;
        this.friction = 0.95f;
        this.hasPhysics = false;
    }

    @Override
    public void move(double p_107246_, double p_107247_, double p_107248_)
    {
        if (p_107246_ != 0.0D || p_107247_ != 0.0D || p_107248_ != 0.0D)
        {
            this.setBoundingBox(this.getBoundingBox().move(p_107246_, p_107247_, p_107248_));
            this.setLocationFromBoundingbox();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<GaseousBloodParticleData>
    {
        protected final SpriteSet sprites;

        @Override
        public Particle createParticle(GaseousBloodParticleData particleData, ClientLevel world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity)
        {
            Blood type = PlayerHealthManager.getBlood(particleData.sourcePlayer);

            GaseousBloodParticle particle = new GaseousBloodParticle(world, xPos, yPos, zPos, xVelocity, yVelocity, zVelocity, type.life, type);
            particle.pickSprite(sprites);

            particle.setColor(type.getColour().x(), type.getColour().y(), type.getColour().z());

            return particle;
        }


        public Factory(SpriteSet sprite)
        {
            this.sprites = sprite;
        }
    }
}
