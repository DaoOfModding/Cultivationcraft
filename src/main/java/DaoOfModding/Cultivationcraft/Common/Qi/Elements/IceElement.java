package DaoOfModding.Cultivationcraft.Common.Qi.Elements;


import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.awt.*;

public class IceElement extends ElementVariant
{
    public IceElement(ResourceLocation Element, Color elementColor, double newDensity, double chance)
    {
        super(Element, elementColor, newDensity, chance);

        effectTickChance = 1.0 / 20.0;
    }

    @Override
    public void effectBlock(Level level, BlockPos pos)
    {
        BlockState block = level.getBlockState(pos);

        if (block.is(Blocks.WATER))
        {
            level.setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
            level.gameEvent(null, GameEvent.BLOCK_PLACE, pos);
        }
    }

    public float resistanceModifier(Player player)
    {
        return (int)(BodyPartStatControl.getStats(player.getUUID()).getElementalStat(StatIDs.resistanceModifier, Elements.waterElement) * -0.5f);
    }
}
