package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Client.Renderer;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class DivineSenseTechnique extends Technique
{
    public DivineSenseTechnique()
    {
        name = new TranslationTextComponent("cultivationcraft.technique.divinesense").getString();
        elementID = Elements.noElementID;

        type = useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/divinesense.png");
        setOverlay(new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/overlays/divinesense.png"));
    }

    @Override
    public void useKeyPressed(boolean keyDown)
    {
        super.useKeyPressed(keyDown);

        // Only do this on the client
        if (ClientItemControl.thisWorld != null)
            Renderer.QiSourcesVisable = active;
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        return true;
    }
}
