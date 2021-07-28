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

        this.hasPhysics = false;

        // Convert seconds into ticks
        this.lifetime = lifespan;
        this.alpha = 0.5F;
        this.sprites = sprite;

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

    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
    public void render(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks)
    {
        GlStateManager._disableDepthTest();
        super.render(buffer, renderInfo, partialTicks);
    }


    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<QiParticleData>
    {
        private final IAnimatedSprite sprites;

        @Override
        public Particle createParticle(QiParticleData particleData, ClientWorld world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity)
        {
            QiParticle particle = new QiParticle(world, xPos, yPos, zPos, xVelocity, yVelocity, zVelocity, (int)(particleData.source.getSize() * ( 1 / QiSourceRenderer.speed)), sprites);
            particle.pickSprite(sprites);

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
