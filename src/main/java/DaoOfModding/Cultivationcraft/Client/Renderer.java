package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.Renderers.QiSourceRenderer;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class Renderer
{
    protected static final ResourceLocation healthOrb = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/healthorb.png");
    protected static final ResourceLocation orbFilling = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/orbfilling.png");
    protected static final ResourceLocation outerfilling = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/outerfilling.png");

    public static boolean QiSourcesVisible = false;

    public static void render()
    {
        if (QiSourcesVisible)
            QiSourceRenderer.renderQiSources();

        renderTechniques();
    }

    public static void renderTechniques()
    {
        // Loop through all players in the world
        for (PlayerEntity player : Minecraft.getInstance().level.players())
        {
            if (player.isAlive())
            {
                // Grab the players technique list and try to render them
                ICultivatorTechniques techniques = CultivatorTechniques.getCultivatorTechniques(player);

                for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
                    if (techniques.getTechnique(i) != null && techniques.getTechnique(i).isActive())
                        techniques.getTechnique(i).render();
            }
        }

        // Grab the player characters techniques and render them from the players view
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isAlive())
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

    public static double getAspectRatio()
    {
        return (double)Minecraft.getInstance().getWindow().getGuiScaledHeight() / (double)Minecraft.getInstance().getWindow().getGuiScaledWidth();
    }

    public static void renderTexture(ResourceLocation texture, double x, double y, double width, double height, float texHeight)
    {
        width += x;
        height += y;

        Minecraft.getInstance().getTextureManager().bind(texture);
        GlStateManager._enableBlend();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(x, height, -90.0D).uv(0.0f, 1f).endVertex();
        bufferbuilder.vertex(width, height, -90.0D).uv(1.0f, 1f).endVertex();
        bufferbuilder.vertex(width, y, -90.0D).uv(1.0f, 1 - texHeight).endVertex();
        bufferbuilder.vertex(x, y, -90.0D).uv(0.0f, 1 - texHeight).endVertex();
        tessellator.end();
    }

    public static void renderHP()
    {
        float health = Minecraft.getInstance().player.getHealth();
        float maxHealth = BodyPartStatControl.getStats(Minecraft.getInstance().player.getUUID()).getStat(StatIDs.maxHP);
        float healthPercent = health/maxHealth;

        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        Vector3f colour = PlayerHealthManager.getBlood(Minecraft.getInstance().player).getColour();

        //GlStateManager._blendColor(1, 0, 0, 1);
        GlStateManager._color4f(colour.x(), colour.y(), colour.z(), 0.7f);
        renderTexture(orbFilling, scaledWidth * 0.1, scaledHeight - (10 + 40 * healthPercent), 40, 40 * healthPercent, healthPercent);

        GlStateManager._color4f(1, 1, 1, 1f);
        renderTexture(healthOrb, scaledWidth * 0.1, scaledHeight - 50, 40, 40, 1);
    }

    public static void renderStamina()
    {
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        float stamina = Minecraft.getInstance().player.getFoodData().getFoodLevel();

        if (Minecraft.getInstance().player.getFoodData() instanceof QiFoodStats)
            stamina = ((QiFoodStats)Minecraft.getInstance().player.getFoodData()).getTrueFoodLevel();

        float maxStamina = BodyPartStatControl.getStats(Minecraft.getInstance().player.getUUID()).getStat(StatIDs.maxStamina);

        float staminaPercent = stamina/maxStamina;


        GlStateManager._color4f(1, 0.5f, 0, 0.7f);
        renderTexture(outerfilling, scaledWidth * 0.1 - 5, scaledHeight - (5 + 50 * staminaPercent), 50, 50 * staminaPercent, staminaPercent);

        GlStateManager._color4f(1, 1, 1, 1f);
        renderTexture(healthOrb, scaledWidth * 0.1 - 5, scaledHeight - 55, 50, 50, 1);
    }
}
