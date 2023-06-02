package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.gameevent.GameEvent;

import java.awt.*;

public class FireElement extends Element
{
    public FireElement(ResourceLocation resourcelocation, Color elementColor, double newDensity)
    {
        super(resourcelocation, elementColor, newDensity);

        effectTickChance = 1.0;
    }

    public void applyStatusEffect(Entity target, float damageAmount)
    {
        target.setSecondsOnFire((int)damageAmount);
    }

    @Override
    public void effectBlock(Level level, BlockPos pos)
    {
        if (BaseFireBlock.canBePlacedAt(level, pos, Direction.DOWN))
        {
            level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
            level.gameEvent(null, GameEvent.BLOCK_PLACE, pos);

            if (level.isClientSide)
                ClientPacketHandler.sendElementalEffectToServer(name, level.dimension(), pos);
        }
    }
}
