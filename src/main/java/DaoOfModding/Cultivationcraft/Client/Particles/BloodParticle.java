package DaoOfModding.Cultivationcraft.Client.Particles;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.Blood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BloodParticle extends TextureSheetParticle
{
    public BloodParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int lifespan)
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
        if (!this.onGround)
            this.move(this.xd, this.yd, this.zd);

        this.xd *= this.friction;
        this.yd *= this.friction;
        this.zd *= this.friction;
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

            BloodParticle particle = new BloodParticle(world, xPos, yPos, zPos, xVelocity, yVelocity, zVelocity, type.life);
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
