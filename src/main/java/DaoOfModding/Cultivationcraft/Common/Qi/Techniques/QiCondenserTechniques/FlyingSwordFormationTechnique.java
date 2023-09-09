package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.AttackOverrideTechnique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;

public class FlyingSwordFormationTechnique extends AttackOverrideTechnique
{
    private final float QiCost = 20;
    private final float Damage = 1;
    private final float speed = 0.02f;
    private final float maxSpeed = 1;
    private final double turnSpeed = 0.2;
    private final float flyingSwordRange = 10;

    protected Entity target = null;

    public FlyingSwordFormationTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.flyingswordformation";
        Element = Elements.noElement;

        type = useType.Toggle;
        multiple = true;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/flyingsword.png");
    }

    @Override
    public void attack(Player player, int slot)
    {
        range = getSwordControlRange();

        super.attack(player, slot);
    }

    @Override
    public void attackEntity(Player player, Entity toAttack)
    {
        target = toAttack;

        if (player.level.isClientSide)
            PoseHandler.getPlayerPoseHandler(player.getUUID()).cancelNextAttackAnimation();
    }

    @Override
    public void attackNothing(Player player)
    {
        target = null;

        if (player.level.isClientSide)
            PoseHandler.getPlayerPoseHandler(player.getUUID()).cancelNextAttackAnimation();
    }

    @Override
    public void attackBlock(Player player, BlockState block, BlockPos pos, Direction direction)
    {
        attackNothing(player);
    }

    @Override
    public boolean isValid(Player player)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() == CultivationTypes.QI_CONDENSER)
            return true;

        return false;
    }

    public Entity getTarget()
    {
        return target;
    }

    public float getSwordQiCost()
    {
        return QiCost;
    }

    public float getSwordControlRange()
    {
        return flyingSwordRange;
    }

    public float getSwordDamageMultiplier()
    {
        return Damage;
    }

    public float getSwordMovementSpeed()
    {
        return speed;
    }

    public float getSwordMaxSpeed()
    {
        return maxSpeed;
    }

    public double getSwordTurnSpeed()
    {
        return turnSpeed;
    }


    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        if (target != null && !target.isAlive())
            target = null;

        super.tickClient(event);
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        if (target != null && !target.isAlive())
            target = null;

        super.tickServer(event);
    }
}
