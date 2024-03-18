package DaoOfModding.Cultivationcraft.Common.Qi.Effects;

import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class Wind
{
    protected static HashMap<UUID, LinkedList<WindInstance>> windEffects = new HashMap<>();

    public static void tick(Entity entity)
    {
        if (!windEffects.containsKey(entity.getUUID()))
            return;

        LinkedList<WindInstance> list = windEffects.get(entity.getUUID());

        // Remove each element of the wind list that has timed out
        while (list.size() > 0 && list.getFirst().isDone())
            list.pop();

        // Tick through each wind instance remaining for this entity
        for (WindInstance wind : list)
            wind.tick(entity);
    }

    public static void addWindEffect(Entity entity, WindInstance wind)
    {
        addWindEffectClient(entity.getUUID(), wind);

        PacketHandler.sendWindInstanceToClients(wind, entity);
    }

    public static void clearWindEffect(UUID entity)
    {
        windEffects.remove(entity);
    }

    public static void addWindEffectClient(UUID entity, WindInstance wind)
    {
        if (!windEffects.containsKey(entity))
            windEffects.put(entity, new LinkedList<>());

        windEffects.get(entity).addLast(wind);
    }
}
