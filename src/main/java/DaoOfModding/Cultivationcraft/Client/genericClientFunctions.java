package DaoOfModding.Cultivationcraft.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.awt.*;

public class genericClientFunctions
{
    public static Player getPlayer()
    {
        return Minecraft.getInstance().player;
    }

    // Move supplied color towards white
    // 0 = no change, 1 = fully white
    public static Color saturate(Color toSaturate, float amount)
    {
        float f = 1.0F - amount;

        float r = toSaturate.getRed() * f + 255f * amount;
        float g = toSaturate.getGreen() * f + 255f * amount;
        float b = toSaturate.getBlue() * f + 255f * amount;

        return new Color(r/255f, g/255f, b/255f);
    }
}
