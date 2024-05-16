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
    protected String cultivationString = "";
    protected String extraString = "";

    public BreakthroughPacket(boolean down, String cult)
    {
        this(down, cult, "");
    }

    public BreakthroughPacket(boolean down, String cult, String extra)
    {
        downgrade = down;
        cultivationString = cult;
        extraString = extra;
    }


    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeBoolean(downgrade);
        buffer.writeUtf(cultivationString);
        buffer.writeUtf(extraString);
    }

    public static BreakthroughPacket decode(FriendlyByteBuf buffer)
    {
        try
        {
            // Read in the send values
            Boolean down = buffer.readBoolean();
            String cult = buffer.readUtf();
            String extra = buffer.readUtf();

            return new BreakthroughPacket(down, cult, extra);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading Breakthrough message: " + e);
            return new BreakthroughPacket(false, "");
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
            if (cultivation.hasTribulated())
            {
                if (cultivationString.length() > 0)
                    cultivation.advance(player, cultivationString);
               else if (extraString.length() > 0)
                    cultivation.advanceExtra(player, extraString);
            }
            else
            {
                if (cultivation.hasTribulation(player))
                    cultivation.startTribulation();
                else
                    cultivation.breakthrough(player, extraString);
            }

            PacketHandler.sendCultivatorStatsToClient(player);
        }
    }
}
