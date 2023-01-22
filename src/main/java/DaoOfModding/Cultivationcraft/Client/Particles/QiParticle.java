package DaoOfModding.Cultivationcraft.Client.Particles;

import DaoOfModding.Cultivationcraft.Client.Renderer;
import DaoOfModding.Cultivationcraft.Client.Renderers.QiSourceRenderer;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSourceConfig;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

public class QiParticle extends TextureSheetParticle
{
    Player target;

    // How fast the Qi Particles should move a tick

    public QiParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int lifespan, SpriteSet sprite)
    {
        super(world, x, y, z, velocityX, velocityY, velocityZ);

        this.hasPhysics = false;

        // Convert seconds into ticks
        this.lifetime = lifespan;
        this.alpha = 0.5F;

        xd = velocityX;
        yd = velocityY;
        zd = velocityZ;
    }

    @Override
    protected int getLightColor(float partialTick)
    {
        // TODO : this may be wrong

        final int BLOCK_LIGHT = 15;  // maximum brightness
        final int SKY_LIGHT = 15;    // maximum brightness
        final int FULL_BRIGHTNESS_VALUE = LightTexture.pack(BLOCK_LIGHT, SKY_LIGHT);

        return FULL_BRIGHTNESS_VALUE;
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

        move(this.xd, this.yd, this.zd);

        if (!Renderer.QiSourcesVisible)
            this.remove();

        if (onGround)
        {  // onGround is only true if the particle collides while it is moving downwards...
            this.remove();
        }

        if (this.yo == this.y && this.yd > 0)
        {  // detect a collision while moving upwards (can't move up at all)
            this.remove();
        }

        if (this.age++ >= this.lifetime)
        {
            this.remove();
        }

        // If particle collides with absorbing player
        if (target != null && target.getBoundingBox().contains(this.x, this.y, this.z))
        {
            this.remove();
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks)
    {
        GlStateManager._disableDepthTest();
        super.render(buffer, renderInfo, partialTicks);
    }


    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<QiParticleData>
    {
        protected final SpriteSet sprites;

        @Override
        public Particle createParticle(QiParticleData particleData, ClientLevel world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity)
        {
            // The distance particles should reach
            float distance = particleData.source.getSize();

            // Value from 0-1 signifying how dense this QiSource currently is
            double density = Math.sqrt(particleData.source.getQiCurrent()) / Math.sqrt((float)QiSourceConfig.MaxStorage);

            int life;
            Vec3 direction = new Vec3(xVelocity, yVelocity, zVelocity);

            if (particleData.target == null)
            {
                // How long the particles last is calculated based on the QiSource density
                life = 5 + (int)(density * 2999);

                // Determine the velocity of the particles based on their lifespan and the distance they need to travel
                direction = direction.scale(distance/(float)life);
            }
            else
            {
                life = (int)distance * 2;
                direction = direction.scale(0.5);
            }

            QiParticle particle = new QiParticle(world, xPos, yPos, zPos, direction.x, direction.y, direction.z, life, sprites);
            particle.pickSprite(sprites);

            Color color = Elements.getElement(particleData.source.getElement()).color;
            particle.setColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);

            particle.target = particleData.target;

            return particle;
        }


        public Factory(SpriteSet sprite)
        {
            this.sprites = sprite;
        }
    }
}
