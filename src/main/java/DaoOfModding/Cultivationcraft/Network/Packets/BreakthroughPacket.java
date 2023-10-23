package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.function.Supplier;

public class BreakthroughPacket extends Packet
{
    public BreakthroughPacket()
    {
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
    }

    public static BreakthroughPacket decode(FriendlyByteBuf buffer)
    {
        return new BreakthroughPacket();
    }

    // Read the packet received over the network
    public void handle(Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        if (sideReceived.isServer())
        {
            ctx.enqueueWork(() -> processPacket(ctx.getSender()));
        }
        else
        {
            Cultivationcraft.LOGGER.warn("Server sent Breakthrough packet message");
        }
    }

    // Process received packet on the Server
    protected void processPacket(ServerPlayer player)
    {
        CultivationType cultivation = CultivatorStats.getCultivatorStats(player).getCultivation();

        if (cultivation.canBreakthrough(player))
        {
            cultivation.breakthrough(player);
            PacketHandler.sendCultivatorStatsToClient(player);
        }
    }
}
