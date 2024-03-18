package DaoOfModding.Cultivationcraft.Client.Particles.WaterParticle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class WaterParticle extends TextureSheetParticle
{
    Vec3 target;
    Entity entity;

    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public WaterParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int life)
    {
        super(world, x, y, z, velocityX, velocityY, velocityZ);

        this.hasPhysics = false;

        this.lifetime = life;
        this.alpha = 0.7F;
        this.setColor(0.4f, 0.6f, 1);

        xd = velocityX;
        yd = velocityY;
        zd = velocityZ;
    }

    @Override
    public void tick()
    {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime || ((int)target.x == (int)this.x && (int)target.y == (int)this.y && (int)target.z == (int)this.z) || ((entity != null) && entity.getBoundingBox().intersects(this.getBoundingBox())))
        {
            this.remove();
        }
        else
        {
            this.setBoundingBox(this.getBoundingBox().move(this.xd, this.yd, this.zd));
            this.setLocationFromBoundingbox();
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks)
    {
        super.render(buffer, renderInfo, partialTicks);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<WaterParticleData>
    {
        protected final SpriteSet sprites;

        @Override
        public Particle createParticle(WaterParticleData particleData, ClientLevel world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity)
        {
            WaterParticle particle = new WaterParticle(world, xPos, yPos, zPos, xVelocity, yVelocity, zVelocity, 10);
            particle.pickSprite(sprites);
            particle.target = particleData.pos;
            particle.entity = particleData.target;

            return particle;
        }

        public Factory(SpriteSet sprite)
        {
            this.sprites = sprite;
        }
    }
}