package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Freeze;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.TickEvent;

public class IceAuraTechnique extends AuraTechnique
{/*
    // TODO: Temp
    protected int power = 20;

    public IceAuraTechnique()
    {
        super();

        elementID = Elements.iceElementID;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/iceaura.png");
    }

    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        Freeze.FreezeArea(event.player.getCommandSenderWorld(), event.player.blockPosition(), getRange(), power);
    }

    @Override
    public boolean isValid(Player player)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() != CultivationTypes.QI_CONDENSER)
            return false;

        return true;
    }*/
}
