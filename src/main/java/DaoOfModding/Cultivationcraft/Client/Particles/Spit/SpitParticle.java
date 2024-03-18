package DaoOfModding.Cultivationcraft.Client.Particles.Spit;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath.Breath;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SpitParticle extends TextureSheetParticle
{
    Breath type;

    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public SpitParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Breath spitType)
    {
        super(world, x, y, z, velocityX, velocityY, velocityZ);

        this.hasPhysics = true;
        type = spitType;

        this.lifetime = 80;
        this.alpha = 0.7F;
        this.friction = 0.98f;

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

        this.yd -= 0.01f;

        if (this.age++ >= this.lifetime)
        {
            this.remove();
        }
        else
        {
            this.setBoundingBox(this.getBoundingBox().move(this.xd, this.yd, this.zd));
            this.setLocationFromBoundingbox();

            BlockPos pos = new BlockPos(this.x, this.y, this.z);

            BlockState state = this.level.getBlockState(pos);

            /*if (state.getBlock() == type.getFluid().getSource().defaultFluidState().createLegacyBlock().getBlock())
            {
                this.remove();
            }*/
            if (state.getBlock() != Blocks.AIR)
            {
                pos = new BlockPos(this.x - this.xd, this.y - this.yd, this.z - this.xd);

                this.level.setBlockAndUpdate(pos, type.getFluid().getSource().defaultFluidState().createLegacyBlock());

                this.remove();
            }

            this.xd *= friction;
            this.yd *= friction;
            this.zd *= friction;
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks)
    {
        super.render(buffer, renderInfo, partialTicks);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SpitParticleData>
    {
        protected final SpriteSet sprites;

        @Override
        public Particle createParticle(SpitParticleData particleData, ClientLevel world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity)
        {
            SpitParticle particle = new SpitParticle(world, xPos, yPos, zPos, xVelocity, yVelocity, zVelocity, particleData.type);
            particle.pickSprite(sprites);

            particle.setColor(particleData.type.getColor().getRed() / 255f, particleData.type.getColor().getGreen() / 255f, particleData.type.getColor().getBlue() / 255f);

            return particle;
        }

        public Factory(SpriteSet sprite)
        {
            this.sprites = sprite;
        }
    }
}
