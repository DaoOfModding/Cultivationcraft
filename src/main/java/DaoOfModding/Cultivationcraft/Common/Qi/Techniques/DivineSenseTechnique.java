package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

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
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class DivineSenseTechnique extends Technique
{
    public DivineSenseTechnique()
    {
        super();

        name = new TranslationTextComponent("cultivationcraft.technique.divinesense").getString();
        elementID = Elements.noElementID;

        type = useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/divinesense.png");
        //setOverlay(new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/overlays/divinesense.png"));
    }

    @Override
    public void useKeyPressed(boolean keyDown, PlayerEntity player)
    {
        super.useKeyPressed(keyDown, player);

        // Only do this on the client
        if (ClientItemControl.thisWorld != null)
            Renderer.QiSourcesVisible = active;
    }

    @Override
    public boolean isValid(PlayerEntity player)
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
