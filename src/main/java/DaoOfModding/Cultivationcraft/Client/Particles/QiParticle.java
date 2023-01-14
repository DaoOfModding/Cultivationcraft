package DaoOfModding.Cultivationcraft.Client.Particles;

import DaoOfModding.Cultivationcraft.Client.Renderer;
import DaoOfModding.Cultivationcraft.Client.Renderers.QiSourceRenderer;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSourceConfig;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

public class QiParticle extends TextureSheetParticle
{
    public QiParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int lifespan, SpriteSet sprite)
    {
        super(world, x, y, z, velocityX, velocityY, velocityZ);

        this.hasPhysics = false;

        // Convert seconds into ticks
        this.lifetime = lifespan;
        this.alpha = 0.5F;

        xd = velocityX;
        yd = velocityY;
        xd = velocityZ;
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

        if (!Renderer.QiSourcesVisible)
            this.remove();
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
            float size = (particleData.source.getSize() - QiSourceConfig.MinSize) / (QiSourceConfig.MaxSize - QiSourceConfig.MinSize);
            float amount = (particleData.source.getQiCurrent() - QiSourceConfig.MinStorage) / (QiSourceConfig.MaxStorage - QiSourceConfig.MinStorage);

            double velocityModifier = 1 + size * 3;
            double lifeModifier = 30 + amount * 270;

            QiParticle particle = new QiParticle(world, xPos, yPos, zPos, xVelocity * velocityModifier, yVelocity * velocityModifier, zVelocity * velocityModifier, (int)(lifeModifier * ( 1 / QiSourceRenderer.speed)), sprites);
            particle.pickSprite(sprites);

            Color color = Elements.getElement(particleData.source.getElementID()).color;
            particle.setColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);

            return particle;
        }


        public Factory(SpriteSet sprite)
        {
            this.sprites = sprite;
        }
    }
}
