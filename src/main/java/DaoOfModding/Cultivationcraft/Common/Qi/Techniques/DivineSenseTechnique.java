package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Common.PlayerUtils;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Client.Renderer;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.TickEvent;

public class DivineSenseTechnique extends Technique
{
    public DivineSenseTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.divinesense";
        Element = Elements.noElement;

        type = useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/divinesense.png");
        //setOverlay(new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/overlays/divinesense.png"));

        stats.setStat(StatIDs.staminaDrain, 0.05f);
        addTechniqueStat(DefaultTechniqueStatIDs.qiCost, 1f);

        effects.add(MobEffects.NIGHT_VISION);
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        if (PlayerUtils.isClientPlayerCharacter(event.player))
            Renderer.QiSourcesVisible = true;

        if (event.player.getFoodData().getFoodLevel() == 0)
            this.deactivate(event.player);
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        super.tickServer(event);

        if (CultivatorStats.getCultivatorStats(event.player).getCultivationType() == CultivationTypes.QI_CONDENSER)
        {
            if (!CultivatorStats.getCultivatorStats(event.player).getCultivation().consumeQi(event.player, getTechniqueStat(DefaultTechniqueStatIDs.qiCost, event.player) / 20f))
                this.deactivate(event.player);
        }
        else if (event.player.getFoodData().getFoodLevel() == 0)
            this.deactivate(event.player);
    }

    @Override
    public void deactivate(Player player)
    {
        super.deactivate(player);

        if (PlayerUtils.isClientPlayerCharacter(player))
            Renderer.QiSourcesVisible = false;
    }

    @Override
    public boolean isValid(Player player)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() == CultivationTypes.BODY_CULTIVATOR)
        {
            // If player is a body cultivator and has cultivated Qi Sight then return true
            IBodyModifications modifications = BodyModifications.getBodyModifications(player);

            BodyPartOption eyes = BodyPartNames.getOption(BodyPartNames.startingEyesPart);

            if (!modifications.hasOption(eyes.getPosition(), eyes.getSubPosition()))
                return false;

            if (modifications.getOption(eyes.getPosition(), eyes.getSubPosition()) == eyes)
                return true;
        }
        else if (stats.getCultivationType() == CultivationTypes.QI_CONDENSER)
        {
            return true;
        }

        return false;
    }
}
