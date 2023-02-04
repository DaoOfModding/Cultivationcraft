package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModificationsCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class QuestCancelPacket extends Packet
{
    public QuestCancelPacket()
    {
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
    }

    public static QuestCancelPacket decode(FriendlyByteBuf buffer)
    {
        return new QuestCancelPacket();
    }

    // Read the packet received over the network
    public void handle(Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        if (sideReceived.isServer())
        {
                ctx.enqueueWork(() -> processPacket(ctx.getSender().getUUID()));
        }
        else
        {
            Cultivationcraft.LOGGER.warn("Server sent quest cancel message to player");
        }
    }

    // Process received packet on the Server
    protected void processPacket(UUID player)
    {
        // Grab the player entity based on the read UUID
        Player ownerEntity = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(player);

        IBodyModifications modifications = BodyModifications.getBodyModifications(ownerEntity);

        String lastForged = modifications.getLastForged();
        modifications.setLastForged("");

        BodyPart part = BodyPartNames.getPart(lastForged);
        if (part != null)
            modifications.removeModification(part);
        else
        {
            BodyPartOption option = BodyPartNames.getOption(lastForged);
            modifications.removeOption(option);
        }

        PacketHandler.sendBodyModificationsToClient(ownerEntity);
    }
}
