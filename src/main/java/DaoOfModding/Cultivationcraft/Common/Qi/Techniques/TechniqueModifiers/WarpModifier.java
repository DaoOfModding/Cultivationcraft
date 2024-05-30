package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordEntity;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class WarpModifier extends TechniqueModifier
{
    int range = 6;

    public WarpModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.warp");
        CATEGORY = MODIFIER_CATEGORY;

        unlockQuest = new Quest(Quest.ENDER_TELEPORT, 1);
        stabiliseQuest = new Quest(Quest.ENDER_TELEPORT, 1000);

        coreTexture = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/cores/warp.png"), 20, 4);

        allowSameCategory = true;
    }

    @Override
    public boolean flyingSwordMovementOverride(FlyingSwordEntity sword, Vec3 targetPos)
    {
        // Do nothing if the sword is already in range of the targetPos
        if (sword.level.isClientSide() || sword.position().subtract(targetPos).length() < range || sword.getOwningPlayer() == null)
            return false;

        return teleport(sword.getOwningPlayer(), sword, randomRangeValue(targetPos, range/2));
    }

    public void onHitTaken(Player owner, float damage, ResourceLocation defensiveElement, QiDamageSource source)
    {
        if (damage <= 0 || source.isInternal() || source.msgId.compareTo(DamageSource.FALL.getMsgId()) == 0)
            return;

        float rand = (float)Math.random();
        float modifier = 1;
        float negmodifier = 1;

        Vec3 destination = new Vec3(owner.position().x + rand * damage / 2f, (int)owner.position().y, owner.position().z + (1 - rand) * damage / 2f);
        Vec3 originalDestination = destination;

        // Change the Y position until there is an air block on and above the position, but not below
        while (!owner.level.getBlockState(new BlockPos(destination)).isAir() || !owner.level.getBlockState(new BlockPos(destination.x, destination.y + 1, destination.z)).isAir() || owner.level.getBlockState(new BlockPos(destination.x, destination.y - 1, destination.z)).isAir())
        {
            destination = originalDestination.add(0, (int)modifier * negmodifier, 0);

            negmodifier *= -1;
            modifier += 0.5f;
        }

        teleport(owner, owner, destination);
    }

    public Entity QiProjectileSpawnOverride(Player owner, Entity inputProjectile)
    {
        // TODO: Teleport inputProjectile here

        return inputProjectile;
    }

    public Vec3 randomRangeValue(Vec3 input, float amount)
    {
        return new Vec3(randomRangeValue(amount) + input.x, randomRangeValue(amount) + input.y,randomRangeValue(amount) + input.z);
    }

    public float randomRangeValue(float amount)
    {
        return (float)(Math.random() - 0.5f) * amount;
    }

    public boolean teleport(Player owner, Entity entity, Vec3 pos)
    {
        if (owner == null)
            return false;

        // Spend Qi equal to the distance teleported * 10
        double distance = entity.position().subtract(pos).length();
        if (!CultivatorStats.getCultivatorStats(owner).getCultivation().consumeQi(owner, distance * 10))
            return false;

        QuestHandler.progressQuest(owner, Quest.ENDER_TELEPORT, 1);

        for(int i = 0; i < 32; ++i)
            ((ServerLevel)entity.level).sendParticles(ParticleTypes.PORTAL, entity.position().x, entity.position().y + owner.getRandom().nextDouble() * 2.0D, entity.position().z, 1, owner.getRandom().nextGaussian(), 0.0D, owner.getRandom().nextGaussian(), 0);

        entity.level.playSound(null, entity, SoundEvents.ENDERMAN_TELEPORT, entity.getSoundSource(), 1, 1);

        entity.teleportTo(pos.x, pos.y, pos.z);
        entity.resetFallDistance();

        return true;
    }

    // TODO: Modify flight to teleport instead
}
