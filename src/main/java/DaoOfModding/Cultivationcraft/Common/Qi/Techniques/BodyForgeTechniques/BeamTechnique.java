package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath.Breath;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.ChanneledAttackTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPoseHandler;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

public class BeamTechnique extends ChanneledAttackTechnique
{
    Breath breath;

    public BeamTechnique()
    {
        super();

        type = useType.Channel;
        multiple = false;

        langLocation = "cultivationcraft.technique.beam";
        Element = Elements.noElement;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/beam.png");

        addTechniqueStat(DefaultTechniqueStatIDs.range, 10);
        addTechniqueStat(DefaultTechniqueStatIDs.breathCost, 1);
    }

    @Override
    public boolean isValid(Player player)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() == CultivationTypes.BODY_CULTIVATOR &&
                (BodyModifications.getBodyModifications(player).hasOption(BodyPartNames.bodyPosition, BodyPartNames.lungSubPosition, BodyPartNames.highPressureLungPart)))
        {
            breath = PlayerHealthManager.getLungs(player).getConnection(0).getLung().getBreath();

            return breath.canExpell();
        }

        return false;
    }


    @Override
    // Attack specified entity with specified player, server only
    public void attackEntity(Player player, Entity toAttack)
    {
        if (breath.tryAttack(player, toAttack))
            super.attackEntity(player, toAttack);
    }

    // Ticks on client side, only called if Technique is active
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        if (!PlayerHealthManager.getLungs(event.player).drainBreath(breath, (float)getTechniqueStat(DefaultTechniqueStatIDs.breathCost, event.player)))
            return;

        // This was doing nothing
        //if (ClientListeners.tick % 10 == 0)
        //    event.player.level.playSound((Player) null, event.player.getX(), event.player.getY(), event.player.getZ(), SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, event.player.getSoundSource(), 1.0F, 1.0F);

        super.tickClient(event);

        spawnParticles(event.player);
    }

    // Ticks on client side, only called if Technique is active
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        if (!PlayerHealthManager.getLungs(event.player).drainBreath(breath, (float)getTechniqueStat(DefaultTechniqueStatIDs.breathCost, event.player)))
            return;

        breath.tick(event.player);

        super.tickServer(event);
    }

    protected float getMinePower(BlockGetter p_60801_, BlockPos p_60802_)
    {
        return breath.getDigPower(p_60801_, p_60802_);
    }

    public void spawnParticles(Player player)
    {
        PlayerPoseHandler handler = PoseHandler.getPlayerPoseHandler(player.getUUID());

        Vec3 look = player.getLookAngle();

        for (int i = 0; i < 10; i++)
        {
            Vec3 random = new Vec3(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);

            Vec3 endTarget;
            if (target == null)
                endTarget = random.add((int)(look.x * 10), (int)(look.y * 10), (int)(look.z * 10));
            else
                endTarget = random.add(target.getX(), target.getY(), target.getZ());

            ParticleOptions particle = breath.getParticle(endTarget, targetEntity);

            if (particle == null)
                return;

            Minecraft.getInstance().level.addParticle(particle,
                    player.getX() + random.x + look.x, player.getY() - handler.getPlayerModel().getEyeHeight() + random.y + look.y, player.getZ() + random.z + look.z,
                    look.x * 2, look.y * 2, look.z * 2);
        }
    }

    @Override
    protected void mine(Player player, BlockPos pos, Direction direction)
    {
        if (pos == null)
            return;

        if (breath.canBeMined(player, pos, direction,this))
            super.mine(player, pos, direction);
    }

    @Override
    public void attackBlock(Player player, BlockState block, BlockPos pos, Direction direction)
    {
        if (breath.doBlockAttack(player, pos, direction))
        {
            player.level.destroyBlock(pos, true);
            breath.onBlockDestroy(player.level, pos);
        }
    }

    public float getAttack(Player player)
    {
        addTechniqueStat(DefaultTechniqueStatIDs.damage, breath.getDamage());

        // Default attack damage
        return breath.getDamage();
    }
}
