package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.CultivationPassives.FoundationPassive;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;

public class FoundationEstablishmentCultivation extends CultivationType
{
    public static final ResourceLocation ID = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.cultivation.foundation");

    public FoundationEstablishmentCultivation()
    {
        super();

        passive = new FoundationPassive();
    }

}
