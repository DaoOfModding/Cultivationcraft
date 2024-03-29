package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class BodyModificationsPacket extends Packet
{
    protected UUID owner;
    protected IBodyModifications mods;

    public BodyModificationsPacket(UUID ownerID, IBodyModifications modifications)
    {
        owner = ownerID;

        mods = modifications;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeUUID(owner);
        buffer.writeNbt(mods.write());
    }

    public static BodyModificationsPacket decode(FriendlyByteBuf buffer)
    {
        BodyModificationsPacket returnValue = new BodyModificationsPacket(null, null);

        try
        {
            // Read in the sent values
            UUID readingOwner = buffer.readUUID();

            IBodyModifications modifications = new BodyModifications();

            modifications.read(buffer.readNbt());

            return new BodyModificationsPacket(readingOwner, modifications);
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading BodyModifications message: " + e);
            return returnValue;
        }
    }

    // Read the packet received over the network
    public void handle(Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        // Check to ensure that the packet has valid data values
        if (owner == null)
        {
            Cultivationcraft.LOGGER.warn("BodyModifications Packet was invalid: " + this.toString());
            return;
        }
        if (sideReceived.isClient())
            ctx.enqueueWork(() -> processPacket());
        else
            Cultivationcraft.LOGGER.warn("BodyModifications Packet was received by server, THIS SHOULD NOT HAPPEN: " + this.toString());
    }

    // Process received packet on client
    protected void processPacket()
    {
        Player player = Minecraft.getInstance().level.getPlayerByUUID(owner);

        // Get the modifications for the specified player
        IBodyModifications modifications = BodyModifications.getBodyModifications(player);

        modifications.copy(mods);

        CultivatorTechniques.getCultivatorTechniques(player).determinePassives(player);
        BodyPartStatControl.updateStats(player);
    }
}
