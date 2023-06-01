package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Server.ServerListeners;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class BurningBlood extends Blood
{
    public BurningBlood()
    {
        orbFilling = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/orbfillingfire.png"), 32);
    }

    @Override
    public void regen(Player player)
    {
        // Only apply fire damage every 20 ticks
        if (ServerListeners.tick % 20 != 0)
            return;

        QiDamageSource damage = new QiDamageSource("burningBlood", Elements.fireElement);
        player.hurt(damage, 1f);
    }

    @Override
    public boolean canHeal(ResourceLocation element)
    {
        if (element == Elements.fireElement)
            return true;

        return false;
    }
}
