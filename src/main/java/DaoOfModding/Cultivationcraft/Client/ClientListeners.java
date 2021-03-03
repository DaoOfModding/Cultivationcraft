package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.GenericPoses;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.MultiLimbedRenderer;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.PlayerPoseHandler;
import DaoOfModding.Cultivationcraft.Client.Animations.CultivatorModelHandler;
import DaoOfModding.Cultivationcraft.Client.Animations.GenericQiPoses;
import DaoOfModding.Cultivationcraft.Client.GUI.SkillHotbarOverlay;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.PoseHandler;
import DaoOfModding.Cultivationcraft.Client.Textures.TextureManager;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientListeners
{
    public static long lastTickTime = System.nanoTime();

    public static void playerTick(TickEvent.PlayerTickEvent event)
    {
        // Add the idle pose to the players poseHandler
        PoseHandler.addPose(event.player.getUniqueID(), GenericQiPoses.Idle);

        if (event.phase == TickEvent.Phase.START)
        {
            // Tell the PoseHandler that the player is not jumping if they are on the ground or in water
            if (event.player.isOnGround() || event.player.isInWater())
                PoseHandler.setJumping(event.player.getUniqueID(), false);

            // Tick through all active cultivator techniques
            ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(event.player);

            for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
                if (techs.getTechnique(i) != null)
                {
                    if (techs.getTechnique(i).isActive())
                        techs.getTechnique(i).tickClient(event);
                    else
                        techs.getTechnique(i).tickInactiveClient(event);
                }

            // If player is moving add the walking pose to the PoseHandler
            if (Math.abs(event.player.getMotion().x) + Math.abs(event.player.getMotion().z) > 0)
            {
                PoseHandler.addPose(event.player.getUniqueID(), GenericPoses.Walking);
                PoseHandler.addPose(event.player.getUniqueID(), GenericQiPoses.Walk);
            }

            // Update the PoseHandler
            PoseHandler.updatePoses();

            // Tick through all body modifications on the player
            IBodyModifications modifications = BodyModifications.getBodyModifications(event.player);

            for (BodyPart part : modifications.getModifications().values())
                part.onClientTick(event.player);

            for (HashMap<String, BodyPartOption> parts : modifications.getModificationOptions().values())
                for (BodyPartOption part : parts.values())
                    part.onClientTick(event.player);
        }
    }

    public static void playerJump(PlayerEntity entity)
    {
        PlayerPoseHandler handler = PoseHandler.getPlayerPoseHandler(entity.getUniqueID());

        if (handler != null)
            handler.setJumping(true);
    }

    @SubscribeEvent
    public static void renderFirstPerson(RenderHandEvent event)
    {
        // Do nothing unless this is trying to render the main hand
        // Otherwise this will run twice a render
        if (event.getHand() != Hand.MAIN_HAND)
        {
            event.setCanceled(true);
            return;
        }

        CultivatorModelHandler.updateModifications(Minecraft.getInstance().player);

        // If MultiLimbedRenderer renders the player, cancel the render event
        event.setCanceled(MultiLimbedRenderer.renderFirstPerson(Minecraft.getInstance().player, event.getPartialTicks(), event.getMatrixStack(), event.getBuffers(), event.getLight()));
    }

    @SubscribeEvent
    public static void renderPlayer(RenderPlayerEvent.Pre event)
    {
        CultivatorModelHandler.updateModifications((ClientPlayerEntity)event.getPlayer());

        // If MultiLimbedRenderer renders the player, cancel the render event
        event.setCanceled(MultiLimbedRenderer.render((ClientPlayerEntity)event.getPlayer(), event.getPartialRenderTick(), event.getMatrixStack(), event.getBuffers(), event.getLight()));
    }

    @SubscribeEvent
    // Toggle off the third person boolean so that the camera will still render in first person
    public static void renderWorldLast(RenderWorldLastEvent event)
    {
        if (Minecraft.getInstance().world == null)
            return;

        MultiLimbedRenderer.fakeThirdPersonOff();
    }

    @SubscribeEvent
    // Toggle on the third person boolean in ActiveRenderInfo to allow the player model to be drawn even when in first person
    public static void cameraSetup(EntityViewRenderEvent.CameraSetup event)
    {
        if (Minecraft.getInstance().world == null)
            return;

        MultiLimbedRenderer.fakeThirdPersonOn(event.getRenderPartialTicks());
    }

    @SubscribeEvent
    public static void overlayRender(RenderGameOverlayEvent.Pre event)
    {
        // Do nothing if the player is dead
        if (!Minecraft.getInstance().player.isAlive())
            return;

        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
        {
            SkillHotbarOverlay.PreRenderSkillHotbar(event.getMatrixStack());
        }
        else if(event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            Renderer.renderTechniqueOverlays();
        }
    }

    @SubscribeEvent
    public static void overlayRender(RenderGameOverlayEvent.Post event)
    {
        // Do nothing if the player is dead
        if (!Minecraft.getInstance().player.isAlive())
            return;

        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
        {
            SkillHotbarOverlay.PostRenderSkillHotbar(event.getMatrixStack());
        }
    }

    @SubscribeEvent
    public static void gameTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            if(ClientItemControl.thisWorld != null)
            {
                // Attempt to process any ChunkQiSource packets pending
                AddChunkQiSourceToClient.processPackets();
            }

            lastTickTime = System.nanoTime();
        }
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
            if (Minecraft.getInstance().world != null)
                Renderer.render();
    }
}
