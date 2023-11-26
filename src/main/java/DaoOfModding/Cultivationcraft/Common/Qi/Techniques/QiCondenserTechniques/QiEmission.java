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
    public static final ResourceLocation amount = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.tstat.amount");

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
        TechniqueStatModification amountModification = new TechniqueStatModification(amount);

        qiCostModification.addStatChange(DefaultTechniqueStatIDs.qiCost, -0.01);
        qiDamageModification.addStatChange(DefaultTechniqueStatIDs.damage, 0.01);
        qiDamageModification.addStatChange(DefaultTechniqueStatIDs.qiCost, 0.02);
        qiSpeedModification.addStatChange(DefaultTechniqueStatIDs.movementSpeed, 0.01);
        qiSpeedModification.addStatChange(DefaultTechniqueStatIDs.qiCost, 0.01);
        amountModification.addStatChange(amount, 0.005);
        amountModification.addStatChange(DefaultTechniqueStatIDs.qiCost, 0.01);

        addTechniqueStat(DefaultTechniqueStatIDs.qiCost, 10, qiCostModification);
        addTechniqueStat(DefaultTechniqueStatIDs.damage, 5, qiDamageModification);
        addTechniqueStat(DefaultTechniqueStatIDs.movementSpeed, 0.25, qiSpeedModification);
        addTechniqueStat(amount, 1, amountModification);
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

        if (!player.level.isClientSide)
        {
            int number = (int)getTechniqueStat(amount, player);

            if (number == 0)
                spawnProjectile(player, new Vec3(0, 0, 0));
            else
                spawnInCircle(player, number);
        }

        deactivate(player);
    }

    // Spawn projectiles evenly in a circle infront of the player
    protected void spawnInCircle(Player player, int number)
    {
        float angleInterval = 360f / number;
        float distanceBetween = 0.5f + number / 10;

        for (int i = 0; i < number; i++)
        {
            float x = (float)(distanceBetween * Math.cos(Math.toRadians(angleInterval * i)));
            float y = (float)(distanceBetween * Math.sin(Math.toRadians(angleInterval * i)));

            spawnProjectile(player, new Vec3(x, y, 0).yRot(player.getYRot()));
        }
    }

    protected void spawnProjectile(Player player, Vec3 positionModifier)
    {
        // Try to consume Qi for this projectile, doing nothing if there is not enough Qi to do so
        // Happens once per projectile spawned
        if (!CultivatorStats.getCultivatorStats(player).getCultivation().consumeQi(player, getTechniqueStat(DefaultTechniqueStatIDs.qiCost, player)))
            return;

        Vec3 pos = PlayerUtils.getPosition(player).add(positionModifier);
        QiProjectile projectile = new QiProjectile(player.level, player, pos.x, pos.y, pos.z, Element, (int)getTechniqueStat(DefaultTechniqueStatIDs.damage, player), (float)getTechniqueStat(DefaultTechniqueStatIDs.movementSpeed, player), this);
        projectile.setDirection(player.getLookAngle());
        player.level.addFreshEntity(projectile);
    }
}
