package DaoOfModding.Cultivationcraft.Network;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Network.Packets.*;
import DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats.CultivatorTargetPacket;
import DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats.RecallFlyingSwordPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

public class ClientPacketHandler
{
    public static void sendKeypressToServer(Register.keyPresses keyPress)
    {
        keypressPacket pack = new keypressPacket(keyPress);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendTechniqueUseToServer(int slot, boolean keyDown)
    {
        TechniqueUsePacket pack = new TechniqueUsePacket(slot, keyDown);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendFlyingSwordConversionToServer(int heldItemID)
    {
        ConvertToFlyingPacket pack = new ConvertToFlyingPacket(heldItemID);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendAttackToServer(UUID playerID, RayTraceResult.Type type, Vector3d pos, UUID targetID, int slot)
    {
        AttackPacket pack = new AttackPacket(playerID, type, pos, targetID, slot);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendRecallFlyingToServer(boolean recall, UUID playerID)
    {
        RecallFlyingSwordPacket pack = new RecallFlyingSwordPacket(recall, playerID);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendCultivatorTargetToServer(UUID playerID, RayTraceResult.Type type, Vector3d pos, UUID targetID)
    {
        CultivatorTargetPacket pack = new CultivatorTargetPacket(playerID, type, pos, targetID);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendTechniqueInfoToServer(UUID playerID, int info, int slot)
    {
        TechniqueInfoPacket packet = new TechniqueInfoPacket(slot, info, playerID);

        PacketHandler.channel.sendToServer(packet);
    }

    public static void sendPartInfoToServer(UUID playerID, int info, String partID, String limbID)
    {
        PartInfoPacket packet = new PartInfoPacket(partID, limbID, info, playerID);

        PacketHandler.channel.sendToServer(packet);
    }

    public static void sendCultivatorTechniquesToServer()
    {
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(Minecraft.getInstance().player);

        CultivatorTechniquesPacket pack = new CultivatorTechniquesPacket(Minecraft.getInstance().player.getUUID(), techs);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendBodyForgeSelectionToServer(String selection)
    {
        BodyForgeSelectionPacket pack = new BodyForgeSelectionPacket(selection);
        PacketHandler.channel.sendToServer(pack);
    }
}
