package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.Animations.CultivatorModelHandler;
import DaoOfModding.Cultivationcraft.Client.GUI.SkillHotbarOverlay;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
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
            if (event.player == genericClientFunctions.getPlayer())
                KeybindingControl.handleMovementOverrides();

            // Do nothing if the pose handler hasn't yet loaded
            if (PoseHandler.getPlayerPoseHandler(event.player.getUUID()) == null)
                return;

            // Update the cultivator model if needed
            CultivatorModelHandler.updateModifications((AbstractClientPlayer)event.player);

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
                part.onClientTick(event.player);

            for (HashMap<String, BodyPartOption> parts : modifications.getModificationOptions().values())
                for (BodyPartOption part : parts.values())
                    part.onClientTick(event.player);
        }
    }


    @SubscribeEvent
    public static void overlayRender(RenderGuiOverlayEvent.Pre event)
    {
        // Do nothing if the player is dead
        if (!genericClientFunctions.getPlayer().isAlive())
            return;

        if (event.getOverlay().id() == VanillaGuiOverlay.HOTBAR.id())
        {
            SkillHotbarOverlay.PreRenderSkillHotbar(event.getPoseStack());
        }
        else if (event.getOverlay().id() == VanillaGuiOverlay.FOOD_LEVEL.id())
        {
            // Render stamina and health the other way around, so health renders ontop of stamina
            Renderer.renderStamina();
            event.setCanceled(true);
        }
        else if (event.getOverlay().id() == VanillaGuiOverlay.PLAYER_HEALTH.id())
        {
            Renderer.renderHP();
            event.setCanceled(true);
        }
        else if (event.getOverlay().id() == VanillaGuiOverlay.EXPERIENCE_BAR.id())
        {
            // Move the experience bar up slightly
            event.getPoseStack().pushPose();
            event.getPoseStack().translate(0, -10, 0);
        }
        else if(event.getOverlay().id() == VanillaGuiOverlay.VIGNETTE.id())
        {
            Renderer.renderTechniqueOverlays();
        }
    }

    @SubscribeEvent
    public static void overlayRenderPost(RenderGuiOverlayEvent.Post event)
    {
        // Do nothing if the player is dead
        if (!genericClientFunctions.getPlayer().isAlive())
            return;

        if (event.getOverlay().id() == VanillaGuiOverlay.EXPERIENCE_BAR.id())
        {
            // Remove the upward translation
            event.getPoseStack().popPose();
        }

        if (event.getOverlay().id() == VanillaGuiOverlay.HOTBAR.id())
        {
            SkillHotbarOverlay.PostRenderSkillHotbar(event.getPoseStack());
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
