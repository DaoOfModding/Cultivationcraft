package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.DefaultPlayerBodyPartWeights;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.StaminaHandler;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

public class ExpandingStomachTechnique extends PassiveTechnique
{
    protected final float maxXSize = 3;
    protected final float maxYSize = 2;
    protected final float maxZSize = 4;

    protected int oldStamina = -1;
    protected float currentStamina = -1;

    protected float currentPercent;

    protected boolean update = false;

    public ExpandingStomachTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.expandingstomach";
    }

    @Override
    public boolean isValid(Player player)
    {
        // Technique is valid if the player is a body cultivator with appropriate teeth
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR &&
                (BodyModifications.getBodyModifications(player).hasModification(BodyPartNames.bodyPosition, BodyPartNames.expandingBodyPart)))
            return true;

        return false;
    }

    // Ticks on client side
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        currentStamina = StaminaHandler.getStamina(event.player);

        updateSizeStats(event.player);

        if (update)
            updateExpandingPose();

        super.tickClient(event);
    }

    // Ticks on Server side
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        updateSizeStats(event.player);

        super.tickServer(event);
    }

    protected void updateSizeStats(Player player)
    {
        float foodPercent = currentStamina / StaminaHandler.getMaxStamina(player);

        // If the hunger percent has changed, update the player's weight stat to reflect this change
        if (currentPercent != foodPercent)
        {
            currentPercent = foodPercent;

            // Maybe multiply other than add here
            float weight = DefaultPlayerBodyPartWeights.bodyWeight * (((maxXSize-1) * currentPercent) + ((maxYSize-1) * currentPercent) + ((maxZSize-1) * currentPercent));

            stats.setStat(StatIDs.weight, weight);
            BodyPartStatControl.updateStats(player);

            update = true;

            if (oldStamina != (int)currentStamina)
            {
                if (player.level.isClientSide && Minecraft.getInstance().player == player)
                    sendInfo((int)currentStamina);

                oldStamina = (int) currentStamina;
            }
        }
    }

    @Override
    // Process a received int info packet
    public void processInfo(Player player, int info)
    {
        currentStamina = info;
        oldStamina = info;
    }

    protected void updateExpandingPose()
    {
        pose = new PlayerPose();
        pose.addSize(GenericLimbNames.body, new Vec3(1 + (maxXSize - 1) * currentPercent, 1 + (maxYSize - 1) * currentPercent, 1 + (maxZSize- 1) * currentPercent), 99, 5);
    }
}
