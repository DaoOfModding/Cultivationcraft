package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs;

import DaoOfModding.Cultivationcraft.Client.ClientListeners;
import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung.Lung;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.awt.*;

public class Lungs
{
    protected animatedTexture lungs = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/lungtube.png"));
    protected final animatedTexture lungsFilling = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/lungtubefilling.png"));

    protected Color breathingColor = Breath.AIR.color;

    Lung lung = new Lung();


    public void breath(Player player)
    {
        Breath breathing = BreathingHandler.getBreath(player);
        breathingColor = breathing.color;

        lung.breath(20, breathing, player);
    }

    public void render(int x, int y)
    {
        float time = 0.4f + (1 - ((ClientListeners.tick-1) % 20) / 20.0f) * 0.6f;

        RenderSystem.setShaderColor(breathingColor.getRed() / 255.0f, breathingColor.getGreen() / 255.0f, breathingColor.getBlue() / 255.0f, 0.3f * breathingColor.getAlpha() / 255.0f);
        lungsFilling.render(x, (int)(y + (40 * (1 - time))), 40, (int)(40 * time), time);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        lungs.render(x, y, 40, 40);

        renderLungs(x, y);
    }

    public void renderLungs(int x, int y)
    {
        lung.render(x, y, false);
        lung.render(x, y, true);
    }

    public float getCurrent(Breath breath)
    {
        if (!lung.canBreath(breath))
            return 0;

        return lung.getCurrent() * 2;
    }

    public Lungs copy(Lungs lungCopy)
    {
        Lungs newLung = new Lungs();

        newLung.lung.setCurrent(lungCopy.getCurrent(lung.getBreath()) / 2);
        newLung.breathingColor = lungCopy.breathingColor;

        return newLung;
    }
}
