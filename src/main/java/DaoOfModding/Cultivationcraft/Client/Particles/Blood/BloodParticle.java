package DaoOfModding.Cultivationcraft.Client.Particles.Blood;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.Blood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class BloodParticle extends TextureSheetParticle
{
    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100.0D);
    protected Blood blood;

    public BloodParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int lifespan, Blood source)
    {
        super(world, x, y, z, velocityX, velocityY, velocityZ);

        this.hasPhysics = true;

        this.lifetime = lifespan;
        this.alpha = 1;

        xd = velocityX;
        yd = velocityY;
        zd = velocityZ;

        this.gravity = 1;
        this.quadSize = 0.025f;
        this.setSize(0.025f, 0.025f);

        blood = source;
    }

    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick()
    {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime)
        {
            this.remove();
            return;
        }

        this.alpha -= 1.0 / this.lifetime;

        this.yd -= 0.04D * (double)this.gravity;

        // Stop movement if blood is sitting on ground
        this.move(this.xd, this.yd, this.zd);

        /*if (this.onGround)
            this.friction = 0;*/

        this.xd *= this.friction;
        this.yd *= this.friction;
        this.zd *= this.friction;

        blood.externalTick(level, x, y, z);
    }

    @Override
    public void move(double p_107246_, double p_107247_, double p_107248_)
    {
            double d0 = p_107246_;
            double d1 = p_107247_;
            double d2 = p_107248_;
            if (this.hasPhysics && (p_107246_ != 0.0D || p_107247_ != 0.0D || p_107248_ != 0.0D) && p_107246_ * p_107246_ + p_107247_ * p_107247_ + p_107248_ * p_107248_ < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
                Vec3 vec3 = Entity.collideBoundingBox(null, new Vec3(p_107246_, p_107247_, p_107248_), this.getBoundingBox(), this.level, List.of());
                p_107246_ = vec3.x;
                p_107247_ = vec3.y;
                p_107248_ = vec3.z;
            }

            if (p_107246_ != 0.0D || p_107247_ != 0.0D || p_107248_ != 0.0D) {
                this.setBoundingBox(this.getBoundingBox().move(p_107246_, p_107247_, p_107248_));
                this.setLocationFromBoundingbox();
            }

            // Stop movement if colliding with ground
            if (Math.abs(d1) >= (double)1.0E-5F && Math.abs(p_107247_) < (double)1.0E-5F)
            {
                this.xd *= 0;
                this.yd *= 0;
                this.zd *= 0;
            }
            else
            {

                this.onGround = d1 != p_107247_ && d1 < 0.0D;
                if (d0 != p_107246_)
                    this.xd = 0.0D;

                if (d2 != p_107248_)
                    this.zd = 0.0D;
            }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks)
    {
        super.render(buffer, renderInfo, partialTicks);
    }


    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<BloodParticleData>
    {
        protected final SpriteSet sprites;

        @Override
        public Particle createParticle(BloodParticleData particleData, ClientLevel world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity)
        {
            Blood type = PlayerHealthManager.getBlood(particleData.sourcePlayer);

            BloodParticle particle = new BloodParticle(world, xPos, yPos, zPos, xVelocity, yVelocity, zVelocity, type.life, type);
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
