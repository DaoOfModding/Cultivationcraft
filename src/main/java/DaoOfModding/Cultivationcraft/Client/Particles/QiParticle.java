package DaoOfModding.Cultivationcraft.Client.Particles;

import DaoOfModding.Cultivationcraft.Client.Renderer;
import DaoOfModding.Cultivationcraft.Client.Renderers.QiSourceRenderer;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

public class QiParticle extends SpriteTexturedParticle
{
    private final IAnimatedSprite sprites;

    public QiParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int lifespan, IAnimatedSprite sprite)
    {
        super(world, x, y, z, velocityX, velocityY, velocityZ);

        this.canCollide = false;

        // Convert seconds into ticks
        this.maxAge = lifespan;
        this.particleAlpha = 0.5F;
        this.sprites = sprite;

        motionX = velocityX;
        motionY = velocityY;
        motionZ = velocityZ;
    }

    @Override
    protected int getBrightnessForRender(float partialTick)
    {
        final int BLOCK_LIGHT = 15;  // maximum brightness
        final int SKY_LIGHT = 15;    // maximum brightness
        final int FULL_BRIGHTNESS_VALUE = LightTexture.packLight(BLOCK_LIGHT, SKY_LIGHT);
        return FULL_BRIGHTNESS_VALUE;
    }

    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        move(motionX, motionY, motionZ);

        if (onGround)
        {  // onGround is only true if the particle collides while it is moving downwards...
            this.setExpired();
        }

        if (prevPosY == posY && motionY > 0)
        {  // detect a collision while moving upwards (can't move up at all)
            this.setExpired();
        }

        if (this.age++ >= this.maxAge)
        {
            this.setExpired();
        }

        if (!Renderer.QiSourcesVisible)
            this.setExpired();
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks)
    {
        GlStateManager.disableDepthTest();
        super.renderParticle(buffer, renderInfo, partialTicks);
    }


    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<QiParticleData>
    {
        private final IAnimatedSprite sprites;

        @Override
        public Particle makeParticle(QiParticleData particleData, ClientWorld world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity)
        {
            QiParticle particle = new QiParticle(world, xPos, yPos, zPos, xVelocity, yVelocity, zVelocity, (int)(particleData.source.getSize() * ( 1 / QiSourceRenderer.speed)), sprites);
            particle.selectSpriteRandomly(sprites);

            Color color = Elements.getElement(particleData.source.getElementID()).color;
            particle.setColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);

            return particle;
        }


        public Factory(IAnimatedSprite sprite)
        {
            this.sprites = sprite;
        }
    }
}
