package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Quest
{
    public static final ResourceLocation QI_SOURCE_MEDITATION = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.qisource");

    public final ResourceLocation mode;
    public final double complete;

    public Quest(ResourceLocation questMode, double questMax)
    {
        mode = questMode;
        complete = questMax;
    }

    public double progress(ResourceLocation progressMode, double amount)
    {
        if (progressMode.compareTo(progressMode) == 0)
            return amount;

        return 0;
    }

    public String getDescription()
    {
        return Component.translatable(mode.getPath()).getString();
    }
}
