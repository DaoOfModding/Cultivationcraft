package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Client.Renderers.QiGlowRenderer;
import DaoOfModding.Cultivationcraft.Client.Renderers.QiSourceRenderer;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.Blood;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;

import java.awt.*;

public class Renderer
{
    protected static final animatedTexture healthOrb = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/healthorb.png"));

    public static boolean QiSourcesVisible = false;

    public static void render(float partialTick)
    {
        if (!Minecraft.getInstance().isPaused())
                QiSourceRenderer.renderQiSources(partialTick);

        if (Renderer.QiSourcesVisible)
            QiGlowRenderer.render();

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

    public static void renderHP()
    {
        float health = genericClientFunctions.getPlayer().getHealth();
        float maxHealth = BodyPartStatControl.getStats(genericClientFunctions.getPlayer()).getStat(StatIDs.maxHP);
        float healthPercent = health/maxHealth;

        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        Blood blood = PlayerHealthManager.getBlood(genericClientFunctions.getPlayer());
        Vector3f colour = blood.getColour();

        RenderSystem.setShaderColor(colour.x(), colour.y(), colour.z(), 0.7f);
        blood.getOrbFilling().render((int)(scaledWidth * 0.1), (int)(scaledHeight - (10 + 40 * healthPercent)), 40, (int)(40 * healthPercent), healthPercent);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        healthOrb.render((int)(scaledWidth * 0.1), scaledHeight - 50, 40, 40);

        PlayerHealthManager.getLungs(genericClientFunctions.getPlayer()).render((int)(scaledWidth * 0.9) - 40, scaledHeight - 50);
    }

    public static void renderStamina()
    {
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        float stamina = genericClientFunctions.getPlayer().getFoodData().getFoodLevel();
        float maxStamina = BodyPartStatControl.getStats(genericClientFunctions.getPlayer()).getStat(StatIDs.maxStamina);

        if (genericClientFunctions.getPlayer().getFoodData() instanceof QiFoodStats)
        {
            stamina = ((QiFoodStats) genericClientFunctions.getPlayer().getFoodData()).getTrueFoodLevel();
            maxStamina = ((QiFoodStats) genericClientFunctions.getPlayer().getFoodData()).getMaxFood();
        }

        float staminaPercent = stamina/maxStamina;
        float saturationPercent =  genericClientFunctions.getPlayer().getFoodData().getSaturationLevel()/maxStamina;

        QiFoodStats stats = new QiFoodStats();
        if (genericClientFunctions.getPlayer().getFoodData() instanceof QiFoodStats)
            stats = (QiFoodStats)genericClientFunctions.getPlayer().getFoodData();

        RenderSystem.setShaderColor(stats.getColor().x(), stats.getColor().y(), stats.getColor().z(), 0.7f);
        stats.getOrb().render((int)(scaledWidth * 0.1 - 5), (int)(scaledHeight - (5 + 50 * staminaPercent)), 50, (int)(50 * staminaPercent), staminaPercent);

        RenderSystem.setShaderColor(stats.getSaturationColor().x(), stats.getSaturationColor().y(), stats.getSaturationColor().z(), 0.5f);
        stats.getOrb().render((int)(scaledWidth * 0.1 - 5), (int)(scaledHeight - (5 + 50 * saturationPercent)), 50, (int)(50 * saturationPercent), saturationPercent);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        healthOrb.render((int)(scaledWidth * 0.1 - 5), scaledHeight - 55, 50, 50);
    }
}
