package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Reflection
{
    protected static Field aboveGroundTick;
    protected static Field foodStatField;
    protected static Method lightningTargetAroundMethod;

    protected static Field wasTouchingWater;
    protected static Field wasUnderwater;

    public static void setup()
    {
        // aboveGroundTickCount - H - f_9737_
        aboveGroundTick = ObfuscationReflectionHelper.findField(ServerGamePacketListenerImpl.class, "f_9737_");

        foodStatField = ObfuscationReflectionHelper.findField(Player.class,"f_36097_");

        wasTouchingWater = ObfuscationReflectionHelper.findField(Entity.class,"f_19798_");
        wasUnderwater = ObfuscationReflectionHelper.findField(Player.class,"f_36076_");

        lightningTargetAroundMethod = ObfuscationReflectionHelper.findMethod(ServerLevel.class,"m_143288_", BlockPos.class);
    }

    public static void setWasTouchingWater(Player entity, boolean on)
    {
        try
        {
            wasTouchingWater.set(entity, on);
            wasUnderwater.set(entity, on);
        }
        catch (Exception e)
        {
            Cultivationcraft.LOGGER.error("Error setting value at field " + wasTouchingWater.getName() + " in " + wasTouchingWater.toString() + ": " + e);
        }
    }

    public static BlockPos findLightningTargetAround(ServerLevel level, BlockPos pos)
    {
        try
        {
            return (BlockPos) lightningTargetAroundMethod.invoke(level, pos);
        }
        catch (Exception e)
        {
            Cultivationcraft.LOGGER.error("Error calling findLightningTargetAround method: " + e);
            return null;
        }
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
