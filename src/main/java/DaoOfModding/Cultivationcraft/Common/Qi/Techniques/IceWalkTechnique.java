package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.mlmanimator.Client.Poses.GenericPoses;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.Cultivationcraft.Client.Animations.GenericQiPoses;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.PlayerUtils;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Freeze;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;

public class IceWalkTechnique extends Technique
{
    // TODO: This is temporary
    private int power = 20;

    public IceWalkTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.icewalk";
        elementID = Elements.iceElementID;

        type = useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/icewalk.png");

        pose = GenericQiPoses.HandsBehind.clone();

        pose.addAngle(GenericLimbNames.leftLeg, new Vector3d(Math.toRadians(-30), 0, Math.toRadians(-10)), GenericPoses.walkLegPriority + 2);
        pose.addAngle(GenericLimbNames.rightLeg, new Vector3d(Math.toRadians(30), 0, Math.toRadians(10)), GenericPoses.walkLegPriority + 2);
        pose.addAngle(GenericLimbNames.lowerLeftLeg, new Vector3d(Math.toRadians(0), 0, Math.toRadians(0)), GenericPoses.walkLegPriority + 2);
        pose.addAngle(GenericLimbNames.lowerRightLeg, new Vector3d(Math.toRadians(0), 0, Math.toRadians(0)), GenericPoses.walkLegPriority + 2);
    }

    public boolean calculateStairOrNot(PlayerEntity player)
    {
        if (PlayerUtils.lookingUp(player) || PlayerUtils.lookingDown(player))
            return true;

        return false;
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        super.tickServer(event);

        BlockPos pos = event.player.blockPosition().below();

        // If the block is already frozen, just update the freeze and do nothing more
        if(Freeze.tryUpdateFreeze(event.player.getCommandSenderWorld(), pos, power))
            return;

        // If moving up or down set the block to either be half or spawn one block above or below the player
        Direction dir = Direction.DOWN;

        if (calculateStairOrNot(event.player))
            dir = PlayerUtils.movementDirection(event.player);

        // If moving up add 1 onto the block position
        if (PlayerUtils.lookingUp(event.player))
        {
            dir = PlayerUtils.invertDirection(dir);
            pos = pos.offset(0, 1, 0);

            // If the block is already frozen, just update the freeze and do nothing more
            if(Freeze.tryUpdateFreeze(event.player.getCommandSenderWorld(), pos, power))
                return;

            //float y = pos.getY() + 0.5f;
            //event.player.setPosition(event.player.getPosX(), y, event.player.getPosZ());
        }

        // Freeze the ground directly below the player
        Freeze.FreezeAir(event.player.getCommandSenderWorld(), pos, power, dir);
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);

        // TODO: Better way of sliding
        Vector3d test = PoseHandler.getPlayerPoseHandler(event.player.getUUID()).getDeltaMovement().scale(0.04);
        event.player.push(test.x, 0, test.z);

        BlockPos pos = event.player.blockPosition().below();

        // If the block at the specified position is a frozen block do nothing
        if (Freeze.isFrozen(event.player.level, pos))
            return;

        Direction dir = Direction.DOWN;

        if (calculateStairOrNot(event.player))
            dir = PlayerUtils.movementDirection(event.player);

        // If moving up add 1 onto the block position and 0.5 onto the player position
        if (PlayerUtils.lookingUp(event.player))
        {
            dir = PlayerUtils.invertDirection(dir);
            pos = pos.offset(0, 1, 0);

            // If the block at the specified position is a frozen block do nothing
            if (Freeze.isFrozen(event.player.level, pos))
                return;

            float y = pos.getY() + 0.5f;
            event.player.setPos(event.player.getX(), y, event.player.getZ());
        }

        // Freeze the ground directly below the player
        Freeze.FreezeAirAsClient(event.player.getCommandSenderWorld(), pos, power, dir);
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() != CultivationTypes.QI_CONDENSER)
            return false;

        return true;
    }
}
