package DaoOfModding.Cultivationcraft.Client;

import static org.lwjgl.glfw.GLFW.*;

import DaoOfModding.Cultivationcraft.Common.Misc;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.UUID;


public class ClientItemControl
{
    public static IWorld thisWorld;

    public static KeyBinding[] keyBindings;

    public static void init(FMLClientSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(ClientItemControl.class);

        keyBindings = new KeyBinding[3];
        keyBindings[0] = new KeyBinding("Create Flying Sword", GLFW_KEY_Y, "Cultivation");
        keyBindings[1] = new KeyBinding("Flying Sword Target", GLFW_KEY_R, "Cultivation");
        keyBindings[2] = new KeyBinding("Flying Sword Recall", GLFW_KEY_O, "Cultivation");

        for (KeyBinding binding : keyBindings)
            ClientRegistry.registerKeyBinding(binding);
    }

    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event)
    {
        // Only perform if the world is loaded
        if (thisWorld != null)
        {
            if (keyBindings[0].isPressed())
            {
                ItemStack selected = Minecraft.getInstance().player.inventory.getCurrentItem();

                // If the selected stack isn't empty then send a message to the server converting this item to a flying sword
                if (!selected.isEmpty())
                    PacketHandler.sendFlyingSwordConversionToServer(Minecraft.getInstance().player.inventory.currentItem, Minecraft.getInstance().player.getUniqueID());
            }

            if (keyBindings[1].isPressed())
            {
                final RayTraceResult result = getMouseOver(100);

                RayTraceResult.Type type = result.getType();
                UUID targetID = null;

                Vector3d pos = result.getHitVec();

                if (type == RayTraceResult.Type.ENTITY)
                    targetID = Misc.getEntityAtLocation(result.getHitVec(), Minecraft.getInstance().world).getUniqueID();

                // If result is a block, move position vector inside the block
                if (type == RayTraceResult.Type.BLOCK)
                {
                    if (Misc.enableHarvest)
                    {
                        pos = pos.add(Minecraft.getInstance().player.getLookVec().scale(0.1));
                        pos = new Vector3d(Math.floor(pos.x), Math.floor(pos.y), Math.floor(pos.z));
                    }
                    else
                        type = RayTraceResult.Type.MISS;
                }

                PacketHandler.sendCultivatorTargetToServer(Minecraft.getInstance().player.getUniqueID(), type, pos, targetID);
            }

            if (keyBindings[2].isPressed())
            {
                PacketHandler.sendRecallFlyingToServer(true, Minecraft.getInstance().player.getUniqueID());
            }
        }
    }

    // Extended getMouseOver class, ripped almost word for word from minecraft's GameRenderer.getMouseOver() class
    // double d0 = the distance the raytrace should cover
    public static RayTraceResult getMouseOver(double d0)
    {
        RayTraceResult result = null;

        Entity entity = Minecraft.getInstance().getRenderViewEntity();
        if (entity != null) {
            if (Minecraft.getInstance().world != null) {
                Minecraft.getInstance().getProfiler().startSection("pick");
                Minecraft.getInstance().pointedEntity = null;
                result = entity.pick(d0, 1, false);
                Vector3d vector3d = entity.getEyePosition(1);

                int i = 3;
                double d1 = d0;

                d1 = d1 * d1;
                if (result != null) {
                    d1 = result.getHitVec().squareDistanceTo(vector3d);
                }

                Vector3d vector3d1 = entity.getLook(1.0F);
                Vector3d vector3d2 = vector3d.add(vector3d1.x * d0, vector3d1.y * d0, vector3d1.z * d0);
                float f = 1.0F;
                AxisAlignedBB axisalignedbb = entity.getBoundingBox().expand(vector3d1.scale(d0)).grow(1.0D, 1.0D, 1.0D);
                EntityRayTraceResult entityraytraceresult = ProjectileHelper.rayTraceEntities(entity, vector3d, vector3d2, axisalignedbb, (p_215312_0_) -> {
                    return !p_215312_0_.isSpectator() && p_215312_0_.canBeCollidedWith();
                }, d1);
                if (entityraytraceresult != null) {
                    Entity entity1 = entityraytraceresult.getEntity();
                    Vector3d vector3d3 = entityraytraceresult.getHitVec();
                    double d2 = vector3d.squareDistanceTo(vector3d3);
                    result = entityraytraceresult;
                }
            }
        }

        return result;
    }
}
