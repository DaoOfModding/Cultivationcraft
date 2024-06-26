package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.UUID;

public class Physics
{
    protected static HashMap<UUID, Double> fallSpeed = new HashMap<>();

    // PoseHandlers as client only
    public static Vec3 getDelta(Player player)
    {
        Vec3 currentMotion = player.getDeltaMovement();

        if (player.level.isClientSide)
            currentMotion = PoseHandler.getPlayerPoseHandler(player.getUUID()).getDeltaMovement();

        return currentMotion;
    }

    // Increase player jump speed based on the jump height
    public static void applyJump(Player player)
    {
        PlayerStatControl stats = BodyPartStatControl.getPlayerStatControl(player);
        float jumpHeight = (stats.getStats().getStat(StatIDs.jumpHeight) - StatIDs.defaultJumpHeight) * stats.getLegWeightModifier();
        double jumpBoost = player.hasEffect(MobEffects.JUMP) ? (double)(0.1F * (float)(player.getEffect(MobEffects.JUMP).getAmplifier() + 1)) : 0.0D;

        // Why? I don't know, Minecraft is weird like that
        if (jumpBoost == 0 && jumpHeight > 0)
            jumpHeight++;

        jumpBoost += jumpHeight * 0.1f;

        Vec3 currentMotion = getDelta(player);

        // Increase not only the height jump but also multiply X and Z momentum
        player.setDeltaMovement(currentMotion.x + (currentMotion.x * jumpHeight * 0.2f), (0.42f) * getBlockJumpFactor(player) + jumpBoost, currentMotion.z + (currentMotion.z * jumpHeight * 0.2f));

        QuestHandler.progressQuest(player, Quest.JUMP, 1);
    }

    // Copied from LivingEntity, cuz it's protected for no reason
    public static float getBlockJumpFactor(Player player)
    {
        float f = player.level.getBlockState(player.blockPosition()).getBlock().getJumpFactor();
        float f1 = player.level.getBlockState(new BlockPos(player.getX(), player.getBoundingBox().minY - 0.5000001D, player.getZ())).getBlock().getJumpFactor();
        return (double)f == 1.0D ? f1 : f;
    }

    public static void Bounce(Player player)
    {
        Vec3 delta = getDelta(player);
        float bounceHeight = BodyPartStatControl.getStats(player).getStat(StatIDs.bounceHeight) * getBlockJumpFactor(player);

        // Do nothing if the player is in water
        if (player.isInWater())
        {
            fallSpeed.remove(player.getUUID());
            return;
        }

        if (bounceHeight > 0 && player.isOnGround())
            Bounce(player, bounceHeight);

        // If the player is falling place their fall speed into the fallSpeed hashmap
        double fall = delta.y;

        if (fall < 0)
            fallSpeed.put(player.getUUID(), fall);
        else
            fallSpeed.remove(player.getUUID());
    }

    public static void Bounce(Player player, float bounceHeight)
    {
        Vec3 delta = getDelta(player);

        // If the player is on the ground then bounce if they have been falling, otherwise do nothing
        if (fallSpeed.containsKey(player.getUUID()))
        {
            double bounce = fallSpeed.get(player.getUUID()) * -bounceHeight;

            // Only bounce if above a certain threshold, to stop infinite micro-bounces
            if (bounce > 0.25)
            {
                QuestHandler.progressQuest(player, Quest.BOUNCE, bounce);
                player.setDeltaMovement(delta.x, bounce, delta.z);
            }

            fallSpeed.remove(player.getUUID());
        }
    }

    // Increase the distance you can fall without taking damage by the fall height
    public static float reduceFallDistance(Player player, float distance)
    {
        PlayerStatControl stats = BodyPartStatControl.getPlayerStatControl(player);

        distance -= (stats.getStats().getStat(StatIDs.fallHeight) - 1) * stats.getLegWeightModifier();

        // Adjust the vanilla fall distance of 1 by the legWeightModifier
        distance += 1 * (1 - stats.getLegWeightModifier());

        if (distance < 0)
            distance = 0;

        return distance;
    }

    public static void swim(Player player)
    {
        // TODO: Maybe need to adjust vertical swim speed here?
        /*
        float swimSpeed = BodyPartStatControl.getStats(player).getStat(StatIDs.swimSpeed);

            double d3 = player.getLookAngle().y;
            double d4 = d3 < -0.2D ? 0.085D : 0.06D;
            if (d3 <= 0.0D ||  !player.level.getBlockState(new BlockPos(player.getX(), player.getY() + 1.0D - 0.1D, player.getZ())).getFluidState().isEmpty())
            {
                Vec3 vec31 = player.getDeltaMovement();
                player.setDeltaMovement(vec31.add(0.0D, (d3 - vec31.y) * d4, 0.0D));
            }*/
    }
}
