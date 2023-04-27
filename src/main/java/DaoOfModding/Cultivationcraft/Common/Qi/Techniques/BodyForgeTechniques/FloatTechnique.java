package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.Physics;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Common.Reflection;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.mlmanimator.Client.Poses.GenericPoses;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

public class FloatTechnique extends Technique
{
    protected boolean jumpPressed = false;

    protected PlayerPose floating = new PlayerPose();

    public FloatTechnique()
    {
        super();

        elytraDisables = true;

        langLocation = "cultivationcraft.technique.float";
        Element = Elements.noElement;

        type = useType.Toggle;
        multiple = false;

        cooldown = 0;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/float.png");

        floating.addSize(GenericLimbNames.body, new Vec3(3, 2, 4), 9999, 0.1f);
        floating.addAngle(GenericLimbNames.body, new Vec3(0, 0, Math.toRadians(-15)), GenericPoses.jumpArmPriority+2, 40f, -1);
        floating.addAngle(GenericLimbNames.body, new Vec3(0, 0, Math.toRadians(15)), GenericPoses.jumpArmPriority+2, 40f, -1);

        floating.addAngle(GenericLimbNames.leftArm, new Vec3(Math.toRadians(0), 0, Math.toRadians(-130)), GenericPoses.jumpArmPriority + 5, 5f,-1);
        floating.addAngle(GenericLimbNames.leftArm, new Vec3(Math.toRadians(0), 0, Math.toRadians(0)), GenericPoses.jumpArmPriority + 5, 5f, -1);
        floating.addAngle(GenericLimbNames.rightArm, new Vec3(Math.toRadians(0), 0, Math.toRadians(130)), GenericPoses.jumpArmPriority + 5, 5f, -1);
        floating.addAngle(GenericLimbNames.rightArm, new Vec3(Math.toRadians(0), 0, Math.toRadians(0)), GenericPoses.jumpArmPriority + 5, 5f, -1);

    }

    @Override
    public boolean isValid(Player player)
    {
        // TODO: add body parts for this technique
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR)
            return true;

        return false;
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);
        tickInactiveClient(event);

        // Reset flapping if player is on the ground
        if (jumpPressed)
            floatUp(event.player);
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        Reflection.allowFlight((ServerPlayer) event.player);

        super.tickServer(event);
        tickInactiveServer(event);
    }

    public void floatUp(Player player)
    {
        PoseHandler.getPlayerPoseHandler(player.getUUID()).addPose(floating);

        Vec3 movement = Physics.getDelta(player);

        // TODO: Calc this based off weight
        float floating = 0.1f;

        player.setDeltaMovement(movement.x, floating, movement.z);
    }

    @Override
    public void onInput()
    {
        // Check if the jump key is depressed
        boolean newJump = Minecraft.getInstance().options.keyJump.isDown();

        if (jumpPressed != newJump)
        {
            jumpPressed = newJump;

            if (newJump)
                sendInfo(1);
            else
                sendInfo(2);
        }
    }

    @Override
    public void processInfo(Player player, int info)
    {
        if (info == 2)
            jumpPressed = false;
        else if (info == 1)
            jumpPressed = true;
    }
}
