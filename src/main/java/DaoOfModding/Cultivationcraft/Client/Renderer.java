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
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;

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
        for (Player player : Minecraft.getInstance().level.players())
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
        if (genericClientFunctions.getPlayer() != null && genericClientFunctions.getPlayer().isAlive())
        {
            ICultivatorTechniques techniques = CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer());

            for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
                if (techniques.getTechnique(i) != null && techniques.getTechnique(i).isActive())
                    techniques.getTechnique(i).renderPlayerView();
        }
    }

    public static void renderTechniqueOverlays()
    {
        // Grab the player characters techniques and render their overlays
        if (genericClientFunctions.getPlayer() != null)
        {
            ICultivatorTechniques techniques = CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer());

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

        RenderSystem.setShaderTexture(0, texture);
        GlStateManager._enableBlend();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(x, height, -90.0D).uv(0.0f, 1f).endVertex();
        bufferbuilder.vertex(width, height, -90.0D).uv(1.0f, 1f).endVertex();
        bufferbuilder.vertex(width, y, -90.0D).uv(1.0f, 1 - texHeight).endVertex();
        bufferbuilder.vertex(x, y, -90.0D).uv(0.0f, 1 - texHeight).endVertex();
        tesselator.end();
    }

    public static void renderHP()
    {
        float health = genericClientFunctions.getPlayer().getHealth();
        float maxHealth = BodyPartStatControl.getStats(genericClientFunctions.getPlayer().getUUID()).getStat(StatIDs.maxHP);
        float healthPercent = health/maxHealth;

        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        Vector3f colour = PlayerHealthManager.getBlood(genericClientFunctions.getPlayer()).getColour();

        //GlStateManager._blendColor(1, 0, 0, 1);
        RenderSystem.setShaderColor(colour.x(), colour.y(), colour.z(), 0.7f);
        renderTexture(orbFilling, scaledWidth * 0.1, scaledHeight - (10 + 40 * healthPercent), 40, 40 * healthPercent, healthPercent);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderTexture(healthOrb, scaledWidth * 0.1, scaledHeight - 50, 40, 40, 1);
    }

    public static void renderStamina()
    {
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        float stamina = genericClientFunctions.getPlayer().getFoodData().getFoodLevel();

        if (genericClientFunctions.getPlayer().getFoodData() instanceof QiFoodStats)
            stamina = ((QiFoodStats)genericClientFunctions.getPlayer().getFoodData()).getTrueFoodLevel();

        float maxStamina = BodyPartStatControl.getStats(genericClientFunctions.getPlayer().getUUID()).getStat(StatIDs.maxStamina);

        float staminaPercent = stamina/maxStamina;


        RenderSystem.setShaderColor(1.0F, 0.5F, 0.0F, 0.7F);
        renderTexture(outerfilling, scaledWidth * 0.1 - 5, scaledHeight - (5 + 50 * staminaPercent), 50, 50 * staminaPercent, staminaPercent);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderTexture(healthOrb, scaledWidth * 0.1 - 5, scaledHeight - 55, 50, 50, 1);
    }
}
