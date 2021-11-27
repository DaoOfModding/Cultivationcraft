package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.mlmanimator.Client.Poses.GenericPoses;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;

public class DartTechnique extends Technique
{
    private PlayerPose flap = new PlayerPose();
    private float doubledCooldown;
    private boolean firstDash = false;

    public DartTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.dart";
        elementID = Elements.noElementID;

        type = useType.Toggle;
        multiple = false;

        cooldown = 20;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/dart.png");


        flap.addAngle(BodyPartModelNames.linsectWing, new Vector3d(0, Math.toRadians(0), Math.toRadians(-60)), 5, 3f, -1);
        flap.addAngle(BodyPartModelNames.rinsectWing, new Vector3d(0, Math.toRadians(0), Math.toRadians(60)), 5, 3f, -1);
        flap.addAngle(BodyPartModelNames.linsectWing, new Vector3d(0, Math.toRadians(0), Math.toRadians(-10)), 5, 3f, -1);
        flap.addAngle(BodyPartModelNames.rinsectWing, new Vector3d(0, Math.toRadians(0), Math.toRadians(10)), 5, 3f, -1);

        flap.addAngle(GenericLimbNames.leftArm, new Vector3d(0, 0, 0), GenericPoses.jumpArmPriority + 1);
        flap.addAngle(GenericLimbNames.rightArm, new Vector3d(0, 0, 0), GenericPoses.jumpArmPriority + 1);
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        // Technique is valid if the player is a body cultivator with Insect Wings
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR &&
                BodyModifications.getBodyModifications(player).hasOption(BodyPartNames.bodyPosition, BodyPartNames.backSubPosition, BodyPartNames.insectwingPart))
            return true;

        return false;
    }


    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);
        tickInactiveClient(event);

        if (doubledCooldown > 0)
            doubledCooldown = doubledCooldown - 1;

        // Do nothing if move is no longer on cooldown
        if (doubledCooldown == 0)
            return;

        // Don't play animation or slow movement if on ground
        if (event.player.isOnGround() || event.player.isInWater())
            return;

        PlayerPose flapping = flap.clone();

        double yLook = 1 - event.player.getLookAngle().y;

        flapping.addAngle(GenericLimbNames.body, new Vector3d(Math.toRadians(75 * yLook), 0, 0), 10, 5f, -1);

        PoseHandler.addPose(event.player.getUUID(), flapping);

        // Don't slow player speed on the first tick
        if (firstDash)
        {
            firstDash = false;
            return;
        }

        float weightModifier = BodyPartStatControl.getPlayerStatControl(event.player.getUUID()).getFlightWeightModifier();
        float slowAmount = ((doubledCooldown * weightModifier) / (cooldown * 2));

        if (slowAmount > 1)
            slowAmount = 1;
        else if (slowAmount < 0)
            slowAmount = 0;

        // Slow the player speed based on the amount of cooldown remaining
        Vector3d slowedSpeed = event.player.getDeltaMovement().scale(1 - slowAmount);
        event.player.setDeltaMovement(slowedSpeed.x, slowedSpeed.y, slowedSpeed.z);
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        super.tickServer(event);
        tickInactiveServer(event);
    }

    @Override
    public void onInput()
    {
        if (cooldownCount > 0)
            return;

        // Check if the jump key is depressed
        if (Minecraft.getInstance().options.keyJump.getKeyBinding().isDown())
        {
            sendInfo(1);
            dart(Minecraft.getInstance().player);
        }
    }

    @Override
    public void processInfo(PlayerEntity player, int info)
    {
        if (!isValid(player))
        {
            Cultivationcraft.LOGGER.error("Player " + player.getUUID() + " tried to dart when this is not a valid technique for them");
            return;
        }

        if (info == 1)
            dart(player);
    }

    public void dart(PlayerEntity player)
    {
        float weightModifier = BodyPartStatControl.getPlayerStatControl(player.getUUID()).getFlightWeightModifier();

        float speed = 20 * weightModifier;

        System.out.println(speed);

        Vector3d dash = player.getLookAngle().normalize().scale(speed);
        player.setDeltaMovement(dash.x, dash.y, dash.z);

        cooldownCount = cooldown;
        doubledCooldown = cooldown*2;

        firstDash = true;
    }
}
