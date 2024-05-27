package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques.FlyingSwordFormationTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques.QiEmission;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;

public class FallModifier extends TechniqueModifier
{
    public FallModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.fall");
        CATEGORY = MODIFIER_CATEGORY;

        unlockQuest = new Quest(Quest.DAMAGE_TAKEN, 1, DamageSource.FALL.msgId);
        stabiliseQuest = new Quest(Quest.DAMAGE_DEALT, 10000);

        allowSameCategory = true;

        damageMult = 2;
    }

    public float getDamageMultiplier(Technique tech)
    {
        // Only apply damage multipliers to techniques that can fall
        if (tech instanceof QiEmission || tech instanceof FlyingSwordFormationTechnique)
            return super.getDamageMultiplier(tech);

        return 1;
    }

    public Vec3 modifyMovement(Vec3 move)
    {
        if (move.y > -1);
            move = move.add(0, -0.02, 0);

        return move;
    }

    // TODO: Modify flying sword movement
    // Function to assign location relative to target flying sword should move to before attacking?

    // TODO: Flight modification
    // Deal AOE damage on landing based on... height? Speed? something.
}

