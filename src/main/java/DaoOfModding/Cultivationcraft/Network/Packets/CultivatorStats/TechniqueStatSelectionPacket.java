package DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import DaoOfModding.Cultivationcraft.Network.Packets.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class TechniqueStatSelectionPacket extends Packet
{
    protected ResourceLocation selectedStat;
    protected String selectedTech;

    public TechniqueStatSelectionPacket(String tech, ResourceLocation selection)
    {
        selectedTech = tech;
        selectedStat = selection;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeUtf(selectedTech);
        buffer.writeUtf(selectedStat.toString());
    }

    public static TechniqueStatSelectionPacket decode(FriendlyByteBuf buffer)
    {
        TechniqueStatSelectionPacket returnValue = new TechniqueStatSelectionPacket("", null);

        try
        {
            // Read in the send values
            return new TechniqueStatSelectionPacket(buffer.readUtf(), new ResourceLocation(buffer.readUtf()));

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading TechniqueStatSelectionPacket message: " + e);
            return returnValue;
        }
    }

    // Read the packet received over the network
    public void handle(Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        if (sideReceived.isServer())
            ctx.enqueueWork(() -> processServerPacket(ctx.getSender()));
    }


    // Process received packet on the Server
    protected void processServerPacket(Player player)
    {
        CultivatorStats.getCultivatorStats(player).setTechniqueFocus(selectedTech, selectedStat);

        // Send the new player stats to the clients
        PacketHandler.sendCultivatorStatsToClient(player);
    }
}
