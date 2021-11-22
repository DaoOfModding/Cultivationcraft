package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivatorControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.AttackOverrideTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.AttackTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class AttackPacket extends Packet
{
    private UUID player = null;
    private RayTraceResult.Type targetType = RayTraceResult.Type.MISS;
    private Vector3d targetPos = null;
    private UUID target = null;
    private int techSlot = 0;

    public AttackPacket(UUID playerID, RayTraceResult.Type type, Vector3d pos, UUID targetUUID, int slot)
    {
        player = playerID;
        targetType = type;
        targetPos = pos;
        target = targetUUID;
        techSlot = slot;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        if (player != null)
        {
            buffer.writeUUID(player);
            buffer.writeEnum(targetType);
            buffer.writeInt(techSlot);

            if (targetPos != null) {
                buffer.writeDouble(targetPos.x);
                buffer.writeDouble(targetPos.y);
                buffer.writeDouble(targetPos.z);

                if (targetType == RayTraceResult.Type.ENTITY)
                    buffer.writeUUID(target);
            }
        }
    }

    public static AttackPacket decode(PacketBuffer buffer)
    {
        AttackPacket returnValue = new AttackPacket(null, RayTraceResult.Type.MISS, null, null, 0);

        try
        {
            // Read in the send values
            UUID readingPlayer = buffer.readUUID();
            RayTraceResult.Type readingType = buffer.readEnum(RayTraceResult.Type.class);
            int slot = buffer.readInt();

            Vector3d readingPos = null;
            UUID readingTargetID = null;

            // Only read the target position if there is a target
            if (readingType != RayTraceResult.Type.MISS) {
                readingPos = new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());

                // Only read the target ID if target is an entity
                if (readingType == RayTraceResult.Type.ENTITY)
                    readingTargetID = buffer.readUUID();
            }

            return new AttackPacket(readingPlayer, readingType, readingPos, readingTargetID, slot);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading Attack message: " + e);
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
        {
            if (ctx.getSender().getUUID().compareTo(player) != 0)
                Cultivationcraft.LOGGER.warn("Client sent attack message for other player");
            else
                ctx.enqueueWork(() -> processServerPacket());
        }
        else
            ctx.enqueueWork(() -> processClientPacket());
    }


    // Process received packet on the Server
    private void processServerPacket()
    {
        // Grab the player entity based on the read UUID
        PlayerEntity ownerEntity = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(player);

        // Do nothing if the specified player doesn't exist
        if (ownerEntity == null)
            return;

        Technique tech = CultivatorTechniques.getCultivatorTechniques(ownerEntity).getTechnique(techSlot);

        // Do nothing if specified technique isn't an attack technique
        if (!(tech instanceof AttackTechnique))
            return;

        // Send packet to all clients
        PacketHandler.sendAttackToClient(player, targetType, targetPos, target, techSlot);

        if (targetType == RayTraceResult.Type.ENTITY)
        {
            // Create a large bounding box at the specified position then search for a list of entities at that location
            AxisAlignedBB scan = new AxisAlignedBB(targetPos.x - 10, targetPos.y - 10, targetPos.z - 10, targetPos.x + 10, targetPos.y + 10, targetPos.z + 10);

            List<Entity> entities = ownerEntity.getCommandSenderWorld().getLoadedEntitiesOfClass(Entity.class, scan);

            // If entities have been found, search through and try to find one with the targetID
            if (!entities.isEmpty())
                for (Entity testEntity : entities)
                    if (testEntity.getUUID().equals(target))
                    {
                        ((AttackTechnique)tech).attackEntity(ownerEntity, testEntity);
                        return;
                    }
        }
        else if (targetType == RayTraceResult.Type.BLOCK)
        {
            BlockPos pos = new BlockPos((int)targetPos.x, (int)targetPos.y, (int)targetPos.z);
            BlockState block = ownerEntity.getCommandSenderWorld().getBlockState(pos);

            if (block != null)
                ((AttackTechnique)tech).attackBlock(ownerEntity, block, pos);
        }
        else
            ((AttackTechnique)tech).attackNothing(ownerEntity);
    }

    private void processClientPacket()
    {
        // Do nothing if this is a packet for the current player
        if (player.compareTo(Minecraft.getInstance().player.getUUID()) == 0)
            return;

        PlayerEntity pEntity = Minecraft.getInstance().level.getPlayerByUUID(player);

        // Do nothing if the player entity is not found
        if (pEntity == null)
            return;

        Technique tech = CultivatorTechniques.getCultivatorTechniques(pEntity).getTechnique(techSlot);

        // Do nothing if specified technique isn't an attack technique
        if (!(tech instanceof AttackTechnique))
            return;

        // Play attack animation for the specified player
        ((AttackTechnique)tech).attackAnimation(pEntity);
    }
}
