package DaoOfModding.Cultivationcraft.Common.Qi.Stats;

import DaoOfModding.Cultivationcraft.Client.GUI.ScreenTabControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatModifications
{
    private HashMap<String, Float> stats = new HashMap<String, Float>();

    public float getStat(String ID)
    {
        if (stats.containsKey(ID))
            return stats.get(ID);

        return 0;
    }

    public void setStat(String ID, float value)
    {
        stats.put(ID, value);
    }

    public HashMap<String, Float> getStats()
    {
        return stats;
    }

    // Combine the stats of the specified stat modifier with this stat modifier
    public void combine(PlayerStatModifications newStats)
    {
        for (Map.Entry<String, Float> stat : newStats.getStats().entrySet())
            setStat(stat.getKey(), stat.getValue() + getStat(stat.getKey()));
    }

    public String toString()
    {
        String statString = "";

        for (Map.Entry<String, Float> stat : stats.entrySet())
            statString += "\n" + stat.getKey() + ": " + stat.getValue();

        return statString;
    }
}
