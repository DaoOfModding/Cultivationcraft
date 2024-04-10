package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques;

import DaoOfModding.Cultivationcraft.Client.Animations.GenericQiPoses;
import DaoOfModding.Cultivationcraft.Client.Renderer;
import DaoOfModding.Cultivationcraft.Client.Renderers.QiGlowRenderer;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.TechniqueStatModification;
import DaoOfModding.Cultivationcraft.Common.Reflection;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;

public class FlightTechnique extends Technique
{
    public FlightTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.flight";
        Element = Elements.noElement;

        type = useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/flight.png");

        canLevel = true;

        addMinTechniqueStat(DefaultTechniqueStatIDs.qiCost, 0.5);

        TechniqueStatModification qiCostModification = new TechniqueStatModification(DefaultTechniqueStatIDs.qiCost);
        TechniqueStatModification movementModification = new TechniqueStatModification(DefaultTechniqueStatIDs.movementSpeed);

        qiCostModification.addStatChange(DefaultTechniqueStatIDs.qiCost, -0.001);
        movementModification.addStatChange(DefaultTechniqueStatIDs.qiCost, 0.05);
        movementModification.addStatChange(DefaultTechniqueStatIDs.movementSpeed, 0.00005);


        addTechniqueStat(DefaultTechniqueStatIDs.qiCost, 5, qiCostModification);
        addTechniqueStat(DefaultTechniqueStatIDs.movementSpeed, 0.01, movementModification);
    }

    @Override
    public boolean isValid(Player player)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() == CultivationTypes.QI_CONDENSER)
            return true;

        return false;
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);

        if (!event.player.isOnGround())
        {
            if (!CultivatorStats.getCultivatorStats(event.player).getCultivation().consumeQi(event.player, getTechniqueStat(DefaultTechniqueStatIDs.qiCost, event.player) / 20f)) {
                deactivate(event.player);
                return;
            }

            event.player.getAbilities().setFlyingSpeed((float) getTechniqueStat(DefaultTechniqueStatIDs.movementSpeed, event.player));
            event.player.getAbilities().flying = true;

            PoseHandler.addPose(event.player.getUUID(), GenericQiPoses.HandsBehind);

            QiGlowRenderer.setQiVisible(event.player, Elements.getElement(getElement()));
        }
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        if (!event.player.isOnGround())
        {
            if (!CultivatorStats.getCultivatorStats(event.player).getCultivation().consumeQi(event.player, getTechniqueStat(DefaultTechniqueStatIDs.qiCost, event.player) / 20f)) {
                deactivate(event.player);
                return;
            }

            levelUp(event.player, 0.05);

            event.player.getAbilities().setFlyingSpeed((float) getTechniqueStat(DefaultTechniqueStatIDs.movementSpeed, event.player));
            event.player.getAbilities().flying = true;
            Reflection.allowFlight((ServerPlayer) event.player);
        }

        tickTechniqueModifiers(event.player, event.player.position(), getElement());

        super.tickServer(event);
    }

    @Override
    public void deactivate(Player player)
    {
        player.getAbilities().setFlyingSpeed(0.05f);
        player.getAbilities().flying = false;

        super.deactivate(player);
    }
}
