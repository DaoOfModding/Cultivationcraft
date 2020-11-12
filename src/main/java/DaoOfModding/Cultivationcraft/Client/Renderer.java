package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.Renderers.QiSourceRenderer;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class Renderer
{
    public static boolean QiSourcesVisable = false;

    public static void render()
    {
        if (QiSourcesVisable)
            QiSourceRenderer.renderQiSources();

        renderTechniques();
    }

    public static void renderTechniques()
    {
        // Loop through all players in the world
        for (PlayerEntity player : Minecraft.getInstance().world.getPlayers())
        {
            // Grab the players technique list and try to render them
            ICultivatorTechniques techniques = CultivatorTechniques.getCultivatorTechniques(player);

            for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
                if (techniques.getTechnique(i) != null && techniques.getTechnique(i).isActive())
                    techniques.getTechnique(i).render();
        }

        // Grab the player characters techniques and render them from the players view
        if (Minecraft.getInstance().player != null)
        {
            ICultivatorTechniques techniques = CultivatorTechniques.getCultivatorTechniques(Minecraft.getInstance().player);

            for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
                if (techniques.getTechnique(i) != null && techniques.getTechnique(i).isActive())
                    techniques.getTechnique(i).renderPlayerView();
        }
    }

    public static void renderTechniqueOverlays()
    {
        // Grab the player characters techniques and render their overlays
        if (Minecraft.getInstance().player != null)
        {
            ICultivatorTechniques techniques = CultivatorTechniques.getCultivatorTechniques(Minecraft.getInstance().player);

            for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
                if (techniques.getTechnique(i) != null && techniques.getTechnique(i).isActive())
                    techniques.getTechnique(i).renderOverlay();
        }
    }
}
