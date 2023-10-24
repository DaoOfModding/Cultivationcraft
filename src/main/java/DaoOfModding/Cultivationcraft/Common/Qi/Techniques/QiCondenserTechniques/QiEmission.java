package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.PlayerUtils;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.QiProjectile;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.TechniqueStatModification;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class QiEmission extends Technique
{
    public QiEmission()
    {
        super();

        langLocation = "cultivationcraft.technique.emission";
        Element = Elements.noElement;

        type = useType.Tap;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/emission.png");

        canLevel = true;

        TechniqueStatModification qiCostModification = new TechniqueStatModification(DefaultTechniqueStatIDs.qiCost);
        TechniqueStatModification qiDamageModification = new TechniqueStatModification(DefaultTechniqueStatIDs.damage);
        TechniqueStatModification qiSpeedModification = new TechniqueStatModification(DefaultTechniqueStatIDs.movementSpeed);

        qiCostModification.addStatChange(DefaultTechniqueStatIDs.qiCost, -0.01);
        qiDamageModification.addStatChange(DefaultTechniqueStatIDs.damage, 0.01);
        qiDamageModification.addStatChange(DefaultTechniqueStatIDs.qiCost, 0.01);
        qiSpeedModification.addStatChange(DefaultTechniqueStatIDs.movementSpeed, 0.01);
        qiSpeedModification.addStatChange(DefaultTechniqueStatIDs.qiCost, 0.01);

        addTechniqueStat(DefaultTechniqueStatIDs.qiCost, 10, qiCostModification);
        addTechniqueStat(DefaultTechniqueStatIDs.damage, 5, qiDamageModification);
        addTechniqueStat(DefaultTechniqueStatIDs.movementSpeed, 1, qiSpeedModification);
    }

    public boolean isValid(Player player)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() == CultivationTypes.QI_CONDENSER)
            return true;

        return false;
    }

    public void activate(Player player)
    {
        super.activate(player);

        if (!player.level.isClientSide && CultivatorStats.getCultivatorStats(player).getCultivation().consumeQi(player, getTechniqueStat(DefaultTechniqueStatIDs.qiCost, player)))
        {
            Vec3 pos = PlayerUtils.getPosition(player);
            QiProjectile projectile = new QiProjectile(player.level, player, pos.x, pos.y, pos.z, Element, (int)getTechniqueStat(DefaultTechniqueStatIDs.damage, player), (float)getTechniqueStat(DefaultTechniqueStatIDs.movementSpeed, player));
            projectile.setDirection(player.getLookAngle());
            player.level.addFreshEntity(projectile);

            this.levelUp(player, 1);
        }

        deactivate(player);
    }
}
