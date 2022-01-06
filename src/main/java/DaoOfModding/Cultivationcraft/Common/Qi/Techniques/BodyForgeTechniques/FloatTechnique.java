package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;

public class FloatTechnique extends Technique
{
    private boolean jumpPressed = false;

    private PlayerPose floating = new PlayerPose();

    public FloatTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.float";
        elementID = Elements.noElementID;

        type = useType.Toggle;
        multiple = false;

        cooldown = 0;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/float.png");

        floating.addSize(GenericLimbNames.body, new Vector3d(3, 2, 4), 9999, 0.1f);
        floating.addAngle(GenericLimbNames.body, new Vector3d(0, 0, Math.toRadians(-15)), GenericPoses.jumpArmPriority+2, 40f, -1);
        floating.addAngle(GenericLimbNames.body, new Vector3d(0, 0, Math.toRadians(15)), GenericPoses.jumpArmPriority+2, 40f, -1);

        floating.addAngle(GenericLimbNames.leftArm, new Vector3d(Math.toRadians(0), 0, Math.toRadians(-130)), GenericPoses.jumpArmPriority + 5, 5f,-1);
        floating.addAngle(GenericLimbNames.leftArm, new Vector3d(Math.toRadians(0), 0, Math.toRadians(0)), GenericPoses.jumpArmPriority + 5, 5f, -1);
        floating.addAngle(GenericLimbNames.rightArm, new Vector3d(Math.toRadians(0), 0, Math.toRadians(130)), GenericPoses.jumpArmPriority + 5, 5f, -1);
        floating.addAngle(GenericLimbNames.rightArm, new Vector3d(Math.toRadians(0), 0, Math.toRadians(0)), GenericPoses.jumpArmPriority + 5, 5f, -1);

    }

    @Override
    public boolean isValid(PlayerEntity player)
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
        super.tickServer(event);
        tickInactiveServer(event);
    }

    public void floatUp(PlayerEntity player)
    {
        PoseHandler.getPlayerPoseHandler(player.getUUID()).addPose(floating);

        Vector3d movement = PoseHandler.getPlayerPoseHandler(player.getUUID()).getDeltaMovement();

        // TODO: Calc this based off weight
        float floating = 0.1f;

        player.setDeltaMovement(movement.x, floating, movement.z);
    }

    @Override
    public void onInput()
    {
        // Check if the jump key is depressed
        boolean newJump = Minecraft.getInstance().options.keyJump.getKeyBinding().isDown();

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
    public void processInfo(PlayerEntity player, int info)
    {
        if (info == 2)
            jumpPressed = false;
        else if (info == 1)
            jumpPressed = true;
    }
}
