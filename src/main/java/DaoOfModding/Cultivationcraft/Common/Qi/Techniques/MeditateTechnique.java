package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.GenericPoses;
import DaoOfModding.Cultivationcraft.Client.Animations.GenericQiPoses;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;

public class MeditateTechnique extends Technique
{
    public MeditateTechnique()
    {
        name = new TranslationTextComponent("cultivationcraft.technique.meditate").getString();
        elementID = Elements.noElementID;

        type = Technique.useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/meditate.png");

        pose = GenericQiPoses.CrossLegs.clone();
    }

    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        super.tickServer(event);
    }

    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        return true;
    }
}
