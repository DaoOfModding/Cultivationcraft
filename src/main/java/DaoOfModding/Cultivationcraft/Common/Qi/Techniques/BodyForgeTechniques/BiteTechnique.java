package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Animations.GenericQiPoses;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.AttackTechnique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;

public class BiteTechnique extends AttackTechnique
{
    public BiteTechnique()
    {
        super();

        name = new TranslationTextComponent("cultivationcraft.technique.bite").getString();
        elementID = Elements.noElementID;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/bite.png");

        pose.addAngle(BodyPartModelNames.jawModelLower, new Vector3d(Math.toRadians(40), 0, 0), GenericQiPoses.attackPriority, 5f, -1);
        attack.addAngle(BodyPartModelNames.jawModelLower, new Vector3d(Math.toRadians(20), 0, 0), GenericQiPoses.attackPriority, 0f, -1);
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        // Technique is valid if the player is a body cultivator with appropriate teeth
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR &&
            (BodyModifications.getBodyModifications(player).hasOption(BodyPartNames.headPosition, BodyPartNames.mouthSubPosition, BodyPartNames.flatTeethPart) ||
            BodyModifications.getBodyModifications(player).hasOption(BodyPartNames.headPosition, BodyPartNames.mouthSubPosition, BodyPartNames.sharpTeethPart)))
            return true;

        return false;
    }
}
