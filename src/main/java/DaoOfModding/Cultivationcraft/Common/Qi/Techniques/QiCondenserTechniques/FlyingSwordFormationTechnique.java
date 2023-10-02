package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.AttackOverrideTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.TechniqueStatModification;
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
    public static final ResourceLocation flyingswordspeed = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.tstat.fsspeed");
    public static final ResourceLocation flyingswordmaxspeed = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.tstat.fsmaxspeed");
    public static final ResourceLocation flyingswordturnspeed = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.tstat.fsturnspeed");

    private final float QiCost = 20;

    protected Entity target = null;

    public FlyingSwordFormationTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.flyingswordformation";
        Element = Elements.noElement;

        type = useType.Toggle;
        multiple = true;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/flyingsword.png");

        canLevel = true;

        TechniqueStatModification qiCostModification = new TechniqueStatModification(DefaultTechniqueStatIDs.qiCost);
        TechniqueStatModification flyingdamageModification = new TechniqueStatModification(DefaultTechniqueStatIDs.damage);
        TechniqueStatModification flyingrangeModification = new TechniqueStatModification(DefaultTechniqueStatIDs.range);
        TechniqueStatModification flyingSwordSpeedModification = new TechniqueStatModification(flyingswordspeed);
        TechniqueStatModification flyingSwordMaxSpeedModification = new TechniqueStatModification(flyingswordmaxspeed);
        TechniqueStatModification flyingSwordTurnSpeedModification = new TechniqueStatModification(flyingswordturnspeed);

        qiCostModification.addStatChange(DefaultTechniqueStatIDs.qiCost, -0.0001);
        flyingdamageModification.addStatChange(DefaultTechniqueStatIDs.qiCost, 0.0001);
        flyingdamageModification.addStatChange(DefaultTechniqueStatIDs.damage, 0.002);
        flyingrangeModification.addStatChange(DefaultTechniqueStatIDs.range, 0.1);
        flyingSwordSpeedModification.addStatChange(flyingswordspeed, 0.01);
        flyingSwordMaxSpeedModification.addStatChange(flyingswordmaxspeed, 1);
        flyingSwordTurnSpeedModification.addStatChange(flyingswordturnspeed, 0.002);


        addTechniqueStat(DefaultTechniqueStatIDs.qiCost, 0.05, qiCostModification);
        addTechniqueStat(DefaultTechniqueStatIDs.range, 10);
        addTechniqueStat(DefaultTechniqueStatIDs.damage, 0.25, flyingdamageModification);
        addTechniqueStat(flyingswordspeed, 2, flyingSwordSpeedModification);
        addTechniqueStat(flyingswordmaxspeed, 100, flyingSwordMaxSpeedModification);
        addTechniqueStat(flyingswordturnspeed, 0.2, flyingSwordTurnSpeedModification);
    }

    @Override
    public void attack(Player player, int slot)
    {
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
    public void attackAnimation(Player player, Entity attackTarget)
    {
        target = attackTarget;

        super.attackAnimation(player, attackTarget);
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
