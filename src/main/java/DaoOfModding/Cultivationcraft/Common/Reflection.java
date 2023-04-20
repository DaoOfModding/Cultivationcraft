package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class Reflection
{
    protected static Field aboveGroundTick;
    static Field foodStatField;

    public static void setup()
    {
        // aboveGroundTickCount - H - f_9737_
        aboveGroundTick = ObfuscationReflectionHelper.findField(ServerGamePacketListenerImpl.class, "f_9737_");

        foodStatField = ObfuscationReflectionHelper.findField(Player.class,"f_36097_");
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

    public static void setFoodStats(Player player, QiFoodStats newFood)
    {
        try
        {
            foodStatField.set(player, newFood);
        }
        catch (Exception e)
        {
            Cultivationcraft.LOGGER.error("Error setting food stats: " + e);
        }
    }
}
