package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Client.KeybindingControl;
import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Physics;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Poses.GenericPoses;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SingleLegPart extends MovementOverridePart
{
    final ResourceLocation HOP = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.hop");

    PlayerPose Idle = new PlayerPose();

    public SingleLegPart(String partID, String position, String displayNamePos)
    {
        super(partID, position, displayNamePos);

        Idle.addAngle(BodyPartModelNames.singleLegModel, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.jumpLegPriority + 1, 5f, -1);
        Idle.addAngle(BodyPartModelNames.singleLegLowerModel, new Vec3(Math.toRadians(60), 0, 0), GenericPoses.jumpLegPriority + 1, 5f, -1);

        setQuest(new Quest(HOP, 10000));
    }

    @Override
    public void onClientTick(Player player)
    {
        Vec3 delta = Physics.getDelta(player);

        // "Retract" leg whilst falling
        if (!player.isOnGround() && !genericClientFunctions.getPlayer().isInWater() && delta.y < 0)
            PoseHandler.addPose(player.getUUID(), Idle);
    }

    @Override
    public boolean overwriteForward()
    {
        Player player = genericClientFunctions.getPlayer();
        // If the player presses forward and is on the ground, then hop forward
        if (player.isOnGround() && !player.isInWater())
        {
            Vec3 direction = player.getForward().normalize();
            float weight = BodyPartStatControl.getPlayerStatControl(player.getUUID()).getLegWeightModifier();
            float speed = player.getSpeed() * weight;

            // Move player forward based on their movement speed whilst jumping 1 block high
            Vec3 movement = new Vec3(direction.x * 0.4f * speed * 10, 0.52f * Physics.getBlockJumpFactor(player) * weight, direction.z * 0.4f * speed * 10);

            player.setDeltaMovement(movement);

            QuestHandler.progressQuest(player, HOP, 1);

            return true;
        }

        return false;
    }

    @Override
    public boolean overwriteLeft()
    {
        if (genericClientFunctions.getPlayer().isOnGround() && !genericClientFunctions.getPlayer().isInWater())
            return true;

        return false;
    }

    @Override
    public boolean overwriteRight()
    {
        if (genericClientFunctions.getPlayer().isOnGround() && !genericClientFunctions.getPlayer().isInWater())
            return true;

        return false;
    }

    @Override
    public boolean overwriteBackward()
    {
        if (genericClientFunctions.getPlayer().isOnGround() && !genericClientFunctions.getPlayer().isInWater())
            return true;

        return false;
    }
}
