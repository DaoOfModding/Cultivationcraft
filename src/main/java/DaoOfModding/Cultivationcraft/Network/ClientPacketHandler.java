package DaoOfModding.Cultivationcraft.Network;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Network.Packets.ConvertToFlyingPacket;
import DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats.CultivatorTargetPacket;
import DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats.RecallFlyingSwordPacket;
import DaoOfModding.Cultivationcraft.Network.Packets.CultivatorTechniquesPacket;
import DaoOfModding.Cultivationcraft.Network.Packets.TechniqueUsePacket;
import DaoOfModding.Cultivationcraft.Network.Packets.keypressPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

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

    public static void sendCultivatorTechniquesToServer()
    {
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(Minecraft.getInstance().player);

        CultivatorTechniquesPacket pack = new CultivatorTechniquesPacket(Minecraft.getInstance().player.getUniqueID(), techs);
        PacketHandler.channel.sendToServer(pack);
    }
}
