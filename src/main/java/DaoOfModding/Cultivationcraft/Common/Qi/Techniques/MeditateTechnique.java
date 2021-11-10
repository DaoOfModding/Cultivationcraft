package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Client.Animations.GenericQiPoses;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Server.BodyPartControl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;

public class MeditateTechnique extends MovementOverrideTechnique
{
    public MeditateTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.meditate";
        elementID = Elements.noElementID;

        type = Technique.useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/meditate.png");

        pose = GenericQiPoses.CrossLegs.clone();
    }

    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        increaseProgress(event);
        BodyPartControl.checkForgeProgress(event.player);

        super.tickServer(event);
    }

    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        // We can increase the progress on the client, as it will just be overridden later
        increaseProgress(event);

        super.tickClient(event);
    }

    // Increase the bodyforge progress
    private void increaseProgress(TickEvent.PlayerTickEvent event)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(event.player);

        // TODO: Get QI from QI sources
        int toAdd = 5;

        if (stats.getCultivationType() == CultivationTypes.BODY_CULTIVATOR)
        {
            IBodyModifications modifications = BodyModifications.getBodyModifications(event.player);

            // Only progress if a part has been selected
            if (modifications.getSelection().compareTo("") != 0)
                modifications.addProgress(toAdd);
        }
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() == CultivationTypes.BODY_CULTIVATOR || stats.getCultivationType() == CultivationTypes.QI_CONDENSER)
            return true;

        return false;
    }

    @Override
    public boolean overwriteForward()
    {
        return true;
    }

    @Override
    public boolean overwriteLeft()
    {
        return true;
    }

    @Override
    public boolean overwriteRight()
    {
        return true;
    }

    @Override
    public boolean overwriteBackward()
    {
        return true;
    }

    @Override
    public boolean overwriteJump()
    {
        return true;
    }
}
