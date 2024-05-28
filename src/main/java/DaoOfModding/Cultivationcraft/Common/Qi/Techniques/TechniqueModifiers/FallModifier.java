package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Common.FlyingSwordEntity;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques.FlyingSwordFormationTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques.QiEmission;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
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

    public void onHitTaken(Player owner, float damage, ResourceLocation defensiveElement, QiDamageSource source)
    {
        if (source.msgId.compareTo(DamageSource.FALL.getMsgId()) != 0 || damage <= 0)
            return;


        // TODO: Create a shockwave dealing AOE damage  based on fall damage.
    }

    public Vec3 flyingSwordMovementOverride(FlyingSwordEntity sword, Entity target, Vec3 targetPos)
    {
        // Make the sword fall faster if it is currently above the entity
        if (Math.abs(target.position().x - sword.position().x) < 1 && Math.abs(target.position().z - sword.position().z) < 1)
        {
            sword.movement = modifyMovement(sword.movement);
            return targetPos;
        }

        // Move the sword above the entity
        return targetPos.add(0, 5, 0);
    }
}

