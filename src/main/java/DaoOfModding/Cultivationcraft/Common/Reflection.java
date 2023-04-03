package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class Reflection
{
    protected static Field aboveGroundTick;

    public static void setup()
    {
        // aboveGroundTickCount - H - f_9737_
        aboveGroundTick = ObfuscationReflectionHelper.findField(ServerGamePacketListenerImpl.class, "f_9737_");
    }

    public static void allowFlight(ServerPlayer entity)
    {
        try
        {
            aboveGroundTick.set(entity.connection, 0);
        }
        catch (Exception e)
        {
            Cultivationcraft.LOGGER.error("Error setting value at field " + aboveGroundTick.getName() + " in " + aboveGroundTick.toString() + ": " + e);
        }
    }
}
