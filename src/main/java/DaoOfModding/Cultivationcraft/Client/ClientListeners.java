package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.GUI.SkillHotbarOverlay;
import DaoOfModding.Cultivationcraft.Common.Misc;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class ClientListeners
{
    public static long lastTickTime = System.nanoTime();

    @SubscribeEvent
    public static void overlayRender(RenderGameOverlayEvent.Pre event)
    {
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
