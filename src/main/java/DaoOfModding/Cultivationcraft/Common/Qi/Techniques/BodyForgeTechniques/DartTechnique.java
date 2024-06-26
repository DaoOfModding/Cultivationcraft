package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Physics;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Common.Reflection;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.StaminaHandler;
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

public class DartTechnique extends Technique
{
    protected PlayerPose flap = new PlayerPose();
    protected float doubledCooldown;
    protected boolean firstDash = false;

    public DartTechnique()
    {
        super();

        elytraDisables = true;

        langLocation = "cultivationcraft.technique.dart";
        Element = Elements.noElement;

        type = useType.Toggle;
        multiple = false;

        cooldown = 20;

        addTechniqueStat(DefaultTechniqueStatIDs.staminaCost, 0.2);

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/dart.png");


        flap.addAngle(BodyPartModelNames.linsectWing, new Vec3(0, Math.toRadians(0), Math.toRadians(-60)), 5, 3f, -1);
        flap.addAngle(BodyPartModelNames.rinsectWing, new Vec3(0, Math.toRadians(0), Math.toRadians(60)), 5, 3f, -1);
        flap.addAngle(BodyPartModelNames.linsectWing, new Vec3(0, Math.toRadians(0), Math.toRadians(-10)), 5, 3f, -1);
        flap.addAngle(BodyPartModelNames.rinsectWing, new Vec3(0, Math.toRadians(0), Math.toRadians(10)), 5, 3f, -1);

        flap.addAngle(GenericLimbNames.leftArm, new Vec3(0, 0, 0), GenericPoses.jumpArmPriority + 1);
        flap.addAngle(GenericLimbNames.rightArm, new Vec3(0, 0, 0), GenericPoses.jumpArmPriority + 1);
    }

    @Override
    public boolean isValid(Player player)
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
        event.player.fallDistance = 0;

        double yLook = 1 - event.player.getLookAngle().y;

        flapping.addAngle(GenericLimbNames.body, new Vec3(Math.toRadians(75 * yLook), 0, 0), 10, 5f, -1);

        PoseHandler.addPose(event.player.getUUID(), flapping);

        // Don't slow player speed on the first tick
        if (firstDash)
        {
            firstDash = false;
            return;
        }

        float weightModifier = BodyPartStatControl.getPlayerStatControl(event.player).getFlightWeightModifier();
        float slowAmount = ((doubledCooldown * weightModifier) / (cooldown * 2));

        if (slowAmount > 1)
            slowAmount = 1;
        else if (slowAmount < 0)
            slowAmount = 0;

        // Slow the player speed based on the amount of cooldown remaining
        Vec3 slowedSpeed = Physics.getDelta(event.player).scale(1 - slowAmount);
        event.player.setDeltaMovement(slowedSpeed.x, slowedSpeed.y, slowedSpeed.z);

        QuestHandler.progressQuest(event.player, new Vec3(event.player.getDeltaMovement().x, 0, event.player.getDeltaMovement().z).length());
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        Reflection.allowFlight((ServerPlayer) event.player);

        super.tickServer(event);
        tickInactiveServer(event);

        event.player.fallDistance = 0;
    }

    @Override
    public void onInput()
    {
        if (cooldownCount > 0)
            return;

        // Check if the jump key is depressed
        if (Minecraft.getInstance().options.keyJump.isDown())
        {
            sendInfo(1);
            dart(genericClientFunctions.getPlayer());
        }
    }

    @Override
    public void processInfo(Player player, int info)
    {
        if (!isValid(player))
        {
            Cultivationcraft.LOGGER.error("Player " + player.getUUID() + " tried to dart when this is not a valid technique for them");
            return;
        }

        if (info == 1)
            dart(player);
    }

    public void dart(Player player)
    {
        // Do nothing if player is out of stamina
        if (!StaminaHandler.consumeStamina(player, (float)getTechniqueStat(DefaultTechniqueStatIDs.staminaCost, player)))
            return;

        float weightModifier = BodyPartStatControl.getPlayerStatControl(player).getFlightWeightModifier();

        float speed = 20 * weightModifier;

        Vec3 dash = player.getLookAngle().normalize().scale(speed);
        player.setDeltaMovement(dash.x, dash.y, dash.z);

        QuestHandler.progressQuest(player, dash.length());

        cooldownCount = cooldown;
        doubledCooldown = cooldown*2;

        firstDash = true;
    }
}
