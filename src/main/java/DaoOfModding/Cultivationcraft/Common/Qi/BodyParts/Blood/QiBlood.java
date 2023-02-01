package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSourceConfig;
import com.mojang.math.Vector3f;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class QiBlood extends Blood
{
    ResourceLocation element = null;

    public QiBlood()
    {
        colour = new Vector3f(1, 1 ,1);
    }

    @Override
    public void regen(Player player)
    {

    }

    @Override
    public int meditation(int QiRemaining, List<QiSource> sources, Player player)
    {
        float toHeal = player.getMaxHealth() - player.getHealth();

        if (toHeal == 0)
            return QiRemaining;

        float heal = 0;

        // Draw Qi from each Qi source available
        for (QiSource source : sources)
        {
            // Only absorb from QiSources of the correct element if an element is set to the body part
            if (element == null || element.compareTo(source.getElement()) == 0)
                // Do not absorb more qi than the players max absorb speed
                if (QiRemaining > 0 && heal < toHeal)
                {
                    int absorbed = source.absorbQi(QiRemaining, player);
                    QiRemaining -= absorbed;

                    heal += (float)absorbed / (QiSourceConfig.MaxStorage / 100000.0f);
                }
        }

        if (heal > toHeal)
            heal = toHeal;

        if (!player.level.isClientSide)
            QuestHandler.progressQuest(player, Quest.HEAL, heal);

        player.heal(heal);

        return QiRemaining;
    }
}
