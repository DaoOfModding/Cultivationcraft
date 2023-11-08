package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class BreakthroughPacket extends Packet
{
    protected boolean downgrade = false;

    public BreakthroughPacket(boolean down)
    {
        downgrade = down;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeBoolean(downgrade);
    }

    public static BreakthroughPacket decode(FriendlyByteBuf buffer)
    {
        try
        {
            // Read in the send values
            Boolean down = buffer.readBoolean();

            return new BreakthroughPacket(down);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading Attack message: " + e);
            return new BreakthroughPacket(false);
        }
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

        if (downgrade)
        {
            cultivation.reset(player);
            PacketHandler.sendCultivatorStatsToClient(player);
        }
        else if (cultivation.canBreakthrough(player))
        {
            if (cultivation.hasTribulation(player))
                cultivation.startTribulation();
            else
                cultivation.breakthrough(player);

            PacketHandler.sendCultivatorStatsToClient(player);
        }
    }
}
