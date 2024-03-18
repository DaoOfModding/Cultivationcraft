package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath;

import DaoOfModding.Cultivationcraft.Client.Particles.WaterParticle.WaterParticleData;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivatorControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class WaterBreath extends Breath
{
    public WaterBreath(Color col, ResourceLocation newElement, FlowingFluid flu, float diggingPower, float damagePower)
    {
        super(col, newElement, flu, diggingPower, damagePower);

        canExpell = true;
    }

    public ParticleOptions getParticle(Vec3 endTarget, Entity targetEntity)
    {
        return new WaterParticleData(endTarget, targetEntity);
    }

    public void onBlockDestroy(Level level, BlockPos pos)
    {
        BlockState state = fluid.getSource().defaultFluidState().createLegacyBlock();

        level.setBlock(pos, state, 2);
    }

    // Returns whether the specified block is mineable
    public boolean canBeMined(Player player, BlockPos pos, Direction direction, Technique source)
    {
        BlockState state = player.level.getBlockState(pos);

        if (state.hasProperty(BlockStateProperties.WATERLOGGED) && !state.getValue(BlockStateProperties.WATERLOGGED))
        {
            ClientPacketHandler.sendAttackToServer(player.getUUID(), HitResult.Type.BLOCK, new Vec3(pos.getX(), pos.getY(), pos.getZ()), player.getUUID(), direction, CultivatorControl.getTechnique(player, source));
            return false;
        }

        return true;
    }

    // Returns whether this block should be destroyed on hit or not
    public boolean doBlockAttack(Player player, BlockPos pos)
    {
        BlockState state = player.level.getBlockState(pos);

        if (state.hasProperty(BlockStateProperties.WATERLOGGED) && !state.getValue(BlockStateProperties.WATERLOGGED))
        {
            player.level.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 2);
            return false;
        }

        return true;
    }
}
