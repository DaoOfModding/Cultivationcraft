package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Animations.CultivatorModelHandler;
import DaoOfModding.Cultivationcraft.Client.GUI.SkillHotbarOverlay;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Models.ExtendableModelRenderer;
import DaoOfModding.mlmanimator.Client.Poses.GenericPoses;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPoseHandler;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientListeners
{
    public static long lastTickTime = System.nanoTime();

    public static void playerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.START)
        {
            if (event.player == Minecraft.getInstance().player)
                KeybindingControl.handleMovementOverrides();

            // Update the cultivator model if needed
            CultivatorModelHandler.updateModifications((AbstractClientPlayerEntity)event.player);

            Physics.Bounce(event.player);

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

            // Tick through all body modifications on the player
            IBodyModifications modifications = BodyModifications.getBodyModifications(event.player);

            for (BodyPart part : modifications.getModifications().values())
                part.onClientTick((ClientPlayerEntity)event.player);

            for (HashMap<String, BodyPartOption> parts : modifications.getModificationOptions().values())
                for (BodyPartOption part : parts.values())
                    part.onClientTick((ClientPlayerEntity)event.player);
        }
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
        else if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH)
        {
            // Render stamina and health the other way around, so health renders ontop of stamina
            Renderer.renderStamina();
            event.setCanceled(true);
        }
        else if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD)
        {
            Renderer.renderHP();
            event.setCanceled(true);
        }
        else if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE)
        {
            // Move the experience bar up slightly
            event.getMatrixStack().pushPose();
            event.getMatrixStack().translate(0, -10, 0);
        }
        else if(event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            Renderer.renderTechniqueOverlays();
        }
    }

    @SubscribeEvent
    public static void overlayRenderPost(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE)
        {
            // Remove the upward translation
            event.getMatrixStack().popPose();
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
            if (Minecraft.getInstance().level != null)
                Renderer.render();
    }
}
