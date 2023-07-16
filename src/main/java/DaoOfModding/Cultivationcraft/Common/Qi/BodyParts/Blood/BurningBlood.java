package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Server.ServerListeners;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class BurningBlood extends CultivatorBlood
{
    int tick = 0;

    public BurningBlood()
    {
        colour = new Vector3f(0.7f, 0 ,0);
        orbFilling = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/orbfillingfire.png"), 32);
        life = 20*10;
    }

    @Override
    public void regen(Player player)
    {
        // Only apply fire damage every 20 ticks
        if (ServerListeners.tick % 20 != 0)
            return;

        QiDamageSource damage = new QiDamageSource("burningBlood", Elements.fireElement, false);
        damage.setInternal();
        player.hurt(damage, 1f);
    }

    @Override
    public boolean externalTick(Level level, double x, double y, double z, boolean onGround)
    {
        tick++;

        // Only run this once every 20 ticks
        if (tick < 20)
            return false;

        tick = 0;

        BlockPos pos = new BlockPos(Mth.floor(x), Mth.floor(y), Mth.floor(z));

        Elements.getElement(Elements.fireElement).effectBlock(level, pos);

        return false;
    }

    @Override
    public boolean canHeal(ResourceLocation element, @Nullable Player player)
    {
        if (element == Elements.fireElement && (player == null || player.hurtTime == 0))
            return true;

        return false;
    }
}
