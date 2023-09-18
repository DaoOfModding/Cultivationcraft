package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.multiplayer.ClientLevel;

import java.util.HashMap;
import java.util.UUID;

public interface ICultivatorStats
{
    public int getCultivationType();
    public void setCultivationType(int newType);
    public CultivationType getCultivation();
    public void setCultivation(CultivationType newCultivation);
    public ResourceLocation getTechniqueFocus(Class tech);
    public void setTechniqueFocus(String tech, ResourceLocation focus);

    public void reset();

    public void setDisconnected(boolean value);
    public boolean isDisconnected();

    public CompoundTag writeNBT();
    public void readNBT(CompoundTag nbt);
}
