package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Client.CultivatorAttackLogicClient;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivatorControl;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

public class ChanneledAttackTechnique extends AttackTechnique
{
    protected float ticksSinceHit = 0;
    protected Entity targetEntity = null;

    protected BlockPos target = null;
    protected float progress = 0;
    protected boolean destroyed = false;

    // The number of ticks between each damage hit
    float hitInterval = 10;

    public ChanneledAttackTechnique()
    {
        super();
        type = useType.Channel;
    }

    // Ticks on client side, only called if Technique is active
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);

        attack(event.player, CultivatorControl.getTechnique(event.player, this));

        if (targetEntity == null)
        {
            BlockHitResult result = CultivatorAttackLogicClient.tryAttackBlock(getRange(event.player));

            if (result != null)
                mine(event.player, result.getBlockPos(), result.getDirection());
        }
    }

    public void deactivate(Player player)
    {
        super.deactivate(player);

        if (player.level.isClientSide)
            unmine(player);

        targetEntity = null;
    }

    // Try to attack with specified player, client only
    public void attack(Player player, int slot)
    {
        targetEntity = CultivatorAttackLogicClient.tryAttackEntity(getRange(player));

        // Do nothing if not entity is found
        if (targetEntity == null)
            return;

        if (ticksSinceHit == 0)
        {
            ClientPacketHandler.sendAttackToServer(player.getUUID(), HitResult.Type.ENTITY, targetEntity.position(), targetEntity.getUUID(), null, slot);
        }
        else
        {
            ticksSinceHit = (ticksSinceHit + 1) % hitInterval;
        }

        target = null;
        unmine(player);

        return;
    }

    // Attack specified entity with specified player, server only
    public void attackEntity(Player player, Entity toAttack)
    {
        super.attackEntity(player, toAttack);

        ticksSinceHit = hitInterval * 1.25f;
    }

    public void attackBlock(Player player, BlockState block, BlockPos pos)
    {
        player.level.destroyBlock(pos, true);
    }

    protected void mine(Player player, BlockPos pos, Direction direction)
    {
        if (target == null || pos == null || pos.compareTo(target) != 0)
        {
            progress = 0;
            destroyed = false;
        }

        target = pos;

        if (target == null)
        {
            unmine(player);
            return;
        }

        float destroySpeed = player.level.getBlockState(pos).getDestroySpeed(player.level, pos);

        if (destroySpeed == -1)
        {
            progress = 0;
            return;
        }

        progress += getMinePower(player.level, target) / destroySpeed / 3.0f;

        if (progress < 10)
            player.level.destroyBlockProgress(player.getId(), pos, (int)progress);
        else if (destroyed)
        {
            player.level.destroyBlockProgress(player.getId(), pos, 9);
        }
        else if (player.level.isClientSide)
        {
            ClientPacketHandler.sendAttackToServer(player.getUUID(), HitResult.Type.BLOCK, new Vec3(pos.getX(), pos.getY(), pos.getZ()), player.getUUID(), direction, CultivatorControl.getTechnique(player, this));
        }
    }

    protected void unmine(Player player)
    {
        player.level.destroyBlockProgress(player.getId(), null, -1);

        target = null;
    }
}
