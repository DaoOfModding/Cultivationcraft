package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Animations.GenericQiPoses;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.AttackOverrideTechnique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

public class BiteTechnique extends AttackOverrideTechnique
{
    public BiteTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.bite";
        Element = null;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/bite.png");

        attackSound = SoundEvents.FOX_BITE;
        missSound = SoundEvents.FOX_BITE;

        damage = 2;
        range = 3.5;

        staminaUse = 0.05f;

        pose.addAngle(BodyPartModelNames.jawModelLower, new Vec3(Math.toRadians(40), 0, 0), GenericQiPoses.attackPriority-1, 5f, -1);
        attack.addAngle(BodyPartModelNames.jawModelLower, new Vec3(Math.toRadians(20), 0, 0), GenericQiPoses.attackPriority, 0f, -1);

        pose.addAngle(BodyPartModelNames.FPjawModel, new Vec3(Math.toRadians(-40), 0, 0), GenericQiPoses.attackPriority-1, 5f, -1);
        pose.addAngle(BodyPartModelNames.FPjawModelLower, new Vec3(Math.toRadians(60), 0, 0), GenericQiPoses.attackPriority-1, 5f, -1);
        attack.addAngle(BodyPartModelNames.FPjawModel, new Vec3(Math.toRadians(-10), 0, 0), GenericQiPoses.attackPriority, 0f, -1);
        attack.addAngle(BodyPartModelNames.FPjawModelLower, new Vec3(Math.toRadians(30), 0, 0), GenericQiPoses.attackPriority, 0f, -1);
    }

    @Override
    public boolean isValid(Player player)
    {
        // Technique is valid if the player is a body cultivator with appropriate teeth
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR &&
            (BodyModifications.getBodyModifications(player).hasOption(BodyPartNames.headPosition, BodyPartNames.mouthSubPosition, BodyPartNames.flatTeethPart) ||
            BodyModifications.getBodyModifications(player).hasOption(BodyPartNames.headPosition, BodyPartNames.mouthSubPosition, BodyPartNames.sharpTeethPart)))
                return true;

        return false;
    }

    // Ticks on server side, only called if Technique is active and owned by the player
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        // Set the element of the bite attack to be the same as the bone's element
        if (Element == null)
            Element = BodyModifications.getBodyModifications(event.player).getOption(BodyPartNames.bodyPosition, BodyPartNames.boneSubPosition).getElement();

        super.tickServer(event);
    }

    // Ticks on client side, only called if Technique is active
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        // Set the element of the bite attack to be the same as the bone's element
        if (Element == null)
            Element = BodyModifications.getBodyModifications(event.player).getOption(BodyPartNames.bodyPosition, BodyPartNames.boneSubPosition).getElement();

        super.tickClient(event);
    }

    @Override
    public float getAttack(Player player)
    {
        PlayerStatModifications stats = BodyPartStatControl.getPlayerStatControl(player.getUUID()).getStats();

        return damage * stats.getStat(StatIDs.boneAttackModifier) * stats.getStat(StatIDs.biteAttackModifier);
    }

    @Override
    public void attackBlock(Player player, BlockState block, BlockPos pos)
    {
        int nutrition = ((QiFoodStats) player.getFoodData()).getNutrition(block);

        if (nutrition > 0 && BodyModifications.getBodyModifications(player).hasOption(BodyPartNames.headPosition, BodyPartNames.mouthSubPosition, BodyPartNames.flatTeethPart))
        {
            player.level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EAT, player.getSoundSource(), 1.0F, 1.0F);

            // If the block is grass, destroy the grass but leave the dirt
            if (block.getMaterial() == Material.GRASS)
            {
                player.level.levelEvent(2001, pos, Block.getId(block));
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
    protected void onKill(Player player, LivingEntity entity)
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

            ((ServerLevel) player.level).sendParticles(ParticleTypes.SMOKE, entity.getX(), entity.getY(), entity.getZ(), 20, 1D, 1D, 1D, 1D);
            player.level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EAT, player.getSoundSource(), 1.0F, 1.0F);
        }
    }
}
