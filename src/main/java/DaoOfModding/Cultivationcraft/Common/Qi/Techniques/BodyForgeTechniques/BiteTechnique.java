package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Animations.GenericQiPoses;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.AttackOverrideTechnique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.GameData;

public class BiteTechnique extends AttackOverrideTechnique
{
    public BiteTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.bite";
        elementID = Elements.noElementID;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/bite.png");

        attackSound = SoundEvents.FOX_BITE;
        missSound = SoundEvents.FOX_BITE;

        damage = 2;
        range = 3.5;

        pose.addAngle(BodyPartModelNames.jawModelLower, new Vector3d(Math.toRadians(40), 0, 0), GenericQiPoses.attackPriority-1, 5f, -1);
        attack.addAngle(BodyPartModelNames.jawModelLower, new Vector3d(Math.toRadians(20), 0, 0), GenericQiPoses.attackPriority, 0f, -1);

        pose.addAngle(BodyPartModelNames.FPjawModel, new Vector3d(Math.toRadians(-50), 0, 0), GenericQiPoses.attackPriority-1, 5f, -1);
        pose.addAngle(BodyPartModelNames.FPjawModelLower, new Vector3d(Math.toRadians(70), 0, 0), GenericQiPoses.attackPriority-1, 5f, -1);
        attack.addAngle(BodyPartModelNames.FPjawModel, new Vector3d(Math.toRadians(0), 0, 0), GenericQiPoses.attackPriority, 0f, -1);
        attack.addAngle(BodyPartModelNames.FPjawModelLower, new Vector3d(Math.toRadians(20), 0, 0), GenericQiPoses.attackPriority, 0f, -1);
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

    @Override
    public float getAttack(PlayerEntity player)
    {
        PlayerStatModifications stats = BodyPartStatControl.getPlayerStatControl(player.getUUID()).getStats();

        return damage * stats.getStat(StatIDs.boneAttackModifier) * stats.getStat(StatIDs.biteAttackModifier);
    }

    @Override
    public void attackBlock(PlayerEntity player, BlockState block, BlockPos pos)
    {
        int nutrition = ((QiFoodStats) player.getFoodData()).getNutrition(block);

        if (nutrition > 0 && BodyModifications.getBodyModifications(player).hasOption(BodyPartNames.headPosition, BodyPartNames.mouthSubPosition, BodyPartNames.flatTeethPart))
        {
            player.level.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EAT, player.getSoundSource(), 1.0F, 1.0F);

            // If the block is grass, destroy the grass but leave the dirt
            if (block.getMaterial() == Material.GRASS)
            {
                player.level.levelEvent(2001, pos, Block.getId(block.getBlockState()));
                player.level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
            }
            // Otherwise destroy the block
            else
            {
                player.level.destroyBlock(pos, false);
            }

            player.getFoodData().eat(nutrition, nutrition);
        }
        else
            super.attackBlock(player, block, pos);
    }

    @Override
    protected void onKill(PlayerEntity player, LivingEntity entity)
    {
        super.onKill(player, entity);

        if (BodyModifications.getBodyModifications(player).hasOption(BodyPartNames.headPosition, BodyPartNames.mouthSubPosition, BodyPartNames.sharpTeethPart) &&
                ((QiFoodStats)player.getFoodData()).canEatMeat())
        {
            // Stamina gain equal to entity max health / 2
            float nutrition = entity.getMaxHealth() / 2;

            player.getFoodData().eat((int)nutrition, nutrition);

            // Make entity disappear once eaten, so it leaves no corpse
            entity.setInvisible(true);

            ((ServerWorld) player.level).sendParticles(ParticleTypes.SMOKE, entity.getX(), entity.getY(), entity.getZ(), 20, 1D, 1D, 1D, 1D);
            player.level.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EAT, player.getSoundSource(), 1.0F, 1.0F);
        }
    }
}
