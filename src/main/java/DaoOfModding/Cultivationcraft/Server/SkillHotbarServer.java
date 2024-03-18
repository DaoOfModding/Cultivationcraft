package DaoOfModding.Cultivationcraft.Server;

import java.util.HashMap;
import java.util.UUID;

public class SkillHotbarServer
{
    static HashMap<UUID, Boolean> skillHotbarActive = new HashMap<UUID, Boolean>();

    public static void addPlayer(UUID player)
    {
        skillHotbarActive.put(player, false);
    }

    public static void removePlayer(UUID player)
    {
        skillHotbarActive.remove(player);
    }

    public static boolean isActive(UUID player)
    {
        if (skillHotbarActive.containsKey(player))
            return skillHotbarActive.get(player);

        return false;
    }

    public static void switchActive(UUID player)
    {
        boolean isActive = skillHotbarActive.get(player);

        skillHotbarActive.replace(player, !isActive);
    }
}
