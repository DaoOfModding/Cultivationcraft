package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath.Breath;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class Lung
{
    protected animatedTexture lung = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/lung.png"));
    protected final animatedTexture lungFilling = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/lungfilling.png"));

    Breath canBreath = Breath.AIR;

    protected float capacity = 150;
    protected float current = 150;

    public float breath(float amount, Breath breath, Player player)
    {
        current = (float)player.getAirSupply() / (float)player.getMaxAirSupply() * capacity;

        return 0;
    }

    // Tries to drain the specified amount of breath from the lungs
    // Returns the amount that couldn't be drained
    public float drain(Breath type, float amount)
    {
        if (type != canBreath)
            return amount;

        if (current < amount)
        {
            amount -= current;
            current = 0;

            return amount;
        }

        current -= amount;

        return 0;
    }

    public void setCurrent(float amount)
    {
        current = amount;

        if (current > capacity)
            current = capacity;
    }

    public void setCapacity(float amount)
    {
        capacity = amount;

        if (current > capacity)
            current = capacity;
    }

    public Breath getBreath()
    {
        return canBreath;
    }

    public boolean canBreath(Breath breath)
    {
        return (canBreath == breath);
    }

    public float getCurrent()
    {
        return current;
    }

    public float getFullness()
    {
        return current / capacity;
    }

    public void render(int x, int y, boolean mirror)
    {
        float breathPercent = getFullness();

        RenderSystem.setShaderColor(canBreath.getColor().getRed() / 255.0f, canBreath.getColor().getGreen() / 255.0f,canBreath.getColor().getBlue() / 255.0f, 0.3f);
        lungFilling.render(x, (int)(y + (40 * (1 - breathPercent))), 40, (int)(40 * breathPercent), breathPercent, mirror);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        lung.render(x, y, 40, 40, mirror);
    }
}
