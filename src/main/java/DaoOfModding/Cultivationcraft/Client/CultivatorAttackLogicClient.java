package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Common.Misc;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class CultivatorAttackLogicClient
{
    // Try attacking entity as player client
    public static Entity tryAttackEntity(double range)
    {
        // If the mouse is over an entity in range, attack that entity
        HitResult result = KeybindingControl.getMouseOver(range);

        if (result.getType() == HitResult.Type.ENTITY)
            return Misc.getEntityAtLocation(result.getLocation(), Minecraft.getInstance().level);

        return null;
    }

    public static BlockHitResult tryAttackBlock(double range)
    {
        HitResult result = KeybindingControl.getMouseOver(range);

        if (result != null && result.getType() == HitResult.Type.BLOCK)
            return ((BlockHitResult) result);

        return null;
    }
}
