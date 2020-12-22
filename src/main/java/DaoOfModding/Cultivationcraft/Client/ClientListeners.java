package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.GenericPoses;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.MultiLimbedRenderer;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.PlayerPoseHandler;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartList;
import DaoOfModding.Cultivationcraft.Client.Animations.CultivatorModelHandler;
import DaoOfModding.Cultivationcraft.Client.GUI.SkillHotbarOverlay;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.PoseHandler;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientListeners
{
    public static long lastTickTime = System.nanoTime();

    public static void playerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(event.player);

            for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
                if (techs.getTechnique(i) != null && techs.getTechnique(i).isActive())
                    techs.getTechnique(i).tickClient(event);

            if (Math.abs(event.player.getMotion().x) + Math.abs(event.player.getMotion().z) > 0)
                PoseHandler.addPose(event.player.getUniqueID(), GenericPoses.Walking);

            PoseHandler.updatePoses();
        }
    }

    @SubscribeEvent
    public static void renderPlayer(RenderPlayerEvent.Pre event)
    {
        // Generate BodyPart list for this player if it doesn't already exist
        if (!BodyPartList.playerExists(event.getPlayer().getUniqueID()))
            BodyPartList.addPlayer(event.getPlayer().getUniqueID(), event.getRenderer().getEntityModel());

        IBodyModifications modifications = BodyModifications.getBodyModifications(event.getPlayer());

        if (!modifications.hasUpdated())
            CultivatorModelHandler.updateModel(event.getRenderer(), event.getPlayer(), modifications);


        // If MultiLimbedRenderer returns true, cancel the render event
        event.setCanceled(MultiLimbedRenderer.render(event.getRenderer(), (ClientPlayerEntity)event.getPlayer(), event.getPartialRenderTick(), event.getMatrixStack(), event.getBuffers(), event.getLight()));
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
