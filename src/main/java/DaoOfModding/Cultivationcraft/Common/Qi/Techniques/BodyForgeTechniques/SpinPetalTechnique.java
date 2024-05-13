package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.MovementOverrideTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Common.Reflection;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.StaminaHandler;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

public class SpinPetalTechnique extends MovementOverrideTechnique
{
    protected boolean jumpPressed = false;
    protected Vec3 spinVector = new Vec3(0, 0.15, 0);

    protected PlayerPose spinUp = new PlayerPose();

    public SpinPetalTechnique()
    {
        super();

        elytraDisables = true;

        langLocation = "cultivationcraft.technique.spinpetal";
        Element = Elements.noElement;

        type = useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/spinpetal.png");

        spinUp.addAngle(BodyPartModelNames.headFlowerModel, new Vec3(0, Math.toRadians(91), 0), 5);
        spinUp.addAngle(BodyPartModelNames.headFlowerModel, new Vec3(0, Math.toRadians(1), 0), 5);
        spinUp.addAngle(BodyPartModelNames.headFlowerModel, new Vec3(0, Math.toRadians(-89), 0), 5);
        spinUp.addAngle(BodyPartModelNames.headFlowerModel, new Vec3(0, Math.toRadians(-179), 0), 5);

        addTechniqueStat(DefaultTechniqueStatIDs.staminaCost, 0.02f);
    }

    @Override
    public boolean isValid(Player player)
    {
        // Technique is valid if the player is a body cultivator with Wings
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR &&
                BodyModifications.getBodyModifications(player).hasOption(BodyPartNames.headPosition, BodyPartNames.petalSubPosition, BodyPartNames.rotatingPetalPart))
            return true;

        return false;
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);
        tickInactiveClient(event);

        if (jumpPressed)
            spin(event.player);
    }

    public void spin(Player player)
    {
        PoseHandler.addPose(player.getUUID(), spinUp);
        player.setDeltaMovement(player.getDeltaMovement().add(spinVector.scale(BodyPartStatControl.getPlayerStatControl(player).getFlightWeightModifier())));
        QuestHandler.progressQuest(player, player.getDeltaMovement().length());
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        super.tickServer(event);
        tickInactiveServer(event);

        if (jumpPressed)
        {
            Reflection.allowFlight((ServerPlayer) event.player);

            event.player.fallDistance = 0;
            StaminaHandler.consumeStamina(event.player, (float)getTechniqueStat(DefaultTechniqueStatIDs.staminaCost, event.player));
        }
    }

    @Override
    public void onInput()
    {
        // Check if the jump key is depressed
        if (jumpPressed && !Minecraft.getInstance().options.keyJump.isDown())
        {
            sendInfo(2);
            spinDown();
        }
    }

    protected void spinUp()
    {
        jumpPressed = true;
    }

    protected void spinDown()
    {
        jumpPressed = false;
    }

    @Override
    public void processInfo(Player player, int info)
    {
        if (info == 2)
            spinDown();
        else if (info == 1)
            spinUp();
    }

    @Override
    public boolean overwriteJump()
    {
        sendInfo(1);
        spinUp();

        return true;
    }
}
