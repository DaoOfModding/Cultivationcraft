package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class FlyingSwordFormationTechnique extends Technique
{
    private final float QiCost = 20;
    private final float Damage = 1;
    private final float speed = 0.02f;
    private final float maxSpeed = 1;
    private final double turnSpeed = 0.2;
    private final float range = 10;

    public FlyingSwordFormationTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.flyingswordformation";
        Element = Elements.noElement;

        type = useType.Toggle;
        multiple = true;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/flyingsword.png");
    }

    @Override
    public boolean isValid(Player player)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() == CultivationTypes.QI_CONDENSER)
            return true;

        return false;
    }

    public float getSwordQiCost()
    {
        return QiCost;
    }

    public float getSwordControlRange()
    {
        return range;
    }

    public float getSwordDamageMultiplier()
    {
        return Damage;
    }

    public float getSwordMovementSpeed()
    {
        return speed;
    }

    public float getSwordMaxSpeed()
    {
        return maxSpeed;
    }

    public double getSwordTurnSpeed()
    {
        return turnSpeed;
    }
}
