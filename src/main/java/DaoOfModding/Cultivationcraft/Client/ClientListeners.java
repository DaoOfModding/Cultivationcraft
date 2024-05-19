package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.Animations.CultivatorModelHandler;
import DaoOfModding.Cultivationcraft.Client.GUI.SkillHotbarOverlay;
import DaoOfModding.Cultivationcraft.Client.Renderers.QiSourceRenderer;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.CommonListeners;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.PassiveTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Common.Reflection;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.StaminaHandler;
import DaoOfModding.mlmanimator.Client.MultiLimbedRenderer;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPoseHandler;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.level.material.LavaFluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientListeners
{
    public static long lastTickTime = System.nanoTime();
    public static int tick = 0;

    @SubscribeEvent
    public static void MovementInputUpdate(MovementInputUpdateEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            CommonListeners.clearStatus(event.getEntity());

            // Required to enable swimming in lava
            if (event.getEntity().isInLava())
                Reflection.setWasTouchingWater(event.getEntity(), true);
        }
    }

    public static void playerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.START)
        {
            if (event.player == genericClientFunctions.getPlayer())
                KeybindingControl.handleMovementOverrides();

            if (event.player.isSwimming() && !event.player.isPassenger())
                Physics.swim(event.player);

            PlayerPoseHandler pose = PoseHandler.getPlayerPoseHandler(event.player.getUUID());

            // Do nothing if the pose handler hasn't yet loaded
            if (pose == null)
                return;

            // Update the cultivator model if needed
            CultivatorModelHandler.updateModifications((AbstractClientPlayer) event.player);

            Physics.Bounce(event.player);

            if (!Minecraft.getInstance().hasSingleplayerServer())
                // Breath once every second
                if (tick % 20 == 0)
                    PlayerHealthManager.getLungs(event.player).breath(event.player);

            // Tick through all active cultivator techniques
            ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(event.player);

            for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
            {
                Technique tech = techs.getTechnique(i);

                if (tech != null) {
                    if (tech.isValid(event.player))
                    {
                        if (tech.isActive())
                            tech.tickClient(event);
                        else
                            tech.tickInactiveClient(event);
                    } else {
                        techs.setTechnique(i, null);
                    }
                }
            }

            for (PassiveTechnique passive : techs.getPassives())
            {
                if (passive.isValid(event.player))
                {
                    if (passive.isActive())
                        passive.tickClient(event);
                    else
                        passive.tickInactiveClient(event);
                }
            }

            // Tick through all body modifications on the player
            IBodyModifications modifications = BodyModifications.getBodyModifications(event.player);

            for (BodyPart part : modifications.getModifications().values())
                part.onClientTick(event.player);

            for (HashMap<String, BodyPartOption> parts : modifications.getModificationOptions().values())
                for (BodyPartOption part : parts.values())
                    part.onClientTick(event.player);


            // Disabled water touching in lava here to stop constant splashing sounds
            if (event.player.isInLava())
                Reflection.setWasTouchingWater(event.player, false);

            if (CultivatorStats.getCultivatorStats(event.player).getCultivationType() == CultivationTypes.QI_CONDENSER)
                CultivatorStats.getCultivatorStats(event.player).getCultivation().tick(event.player);
        }
    }

    /*@SubscribeEvent
    public static void overlayRender(RenderBlockScreenEffectEvent event)
    {
        if (event.getBlockState() == Blocks.FIRE.defaultBlockState())
            event.setCanceled(true);
    }*/

    // Allow vision in lava
    @SubscribeEvent
    public static void fogDensityEvent(ViewportEvent.RenderFog event)
    {
        if (event.getType() == FogType.LAVA)
        {
            event.setNearPlaneDistance(-8.0f);

            // Adjust player vision in lava based on fire resistance
            PlayerStatModifications stats = BodyPartStatControl.getStats(Minecraft.getInstance().player);
            float fireResistance = stats.getElementalStat(StatIDs.resistanceModifier, Elements.fireElement) / 100f;
            fireResistance = Math.min(1, fireResistance);

            event.setFarPlaneDistance(Math.max(0, 24f * fireResistance));

            // This... is not CANCELING the event, it is allowing the changes to be saved :/
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void overlayRender(RenderGuiOverlayEvent.Pre event)
    {
        // Do nothing if the player is dead
        if (genericClientFunctions.getPlayer() != null && !genericClientFunctions.getPlayer().isAlive())
            return;

        if (event.getOverlay().id() == VanillaGuiOverlay.HOTBAR.id())
        {
            // Do not render the skill hotbar if the player has no cultivation
            if (CultivatorStats.getCultivatorStats(genericClientFunctions.getPlayer()).getCultivationType() != CultivationTypes.NO_CULTIVATION)
            {
                SkillHotbarOverlay.PreRenderSkillHotbar(event.getPoseStack());
                Renderer.renderTechniqueOverlays();
            }
        }
        else if (event.getOverlay().id() == VanillaGuiOverlay.FOOD_LEVEL.id())
        {
            // Render HP here instead of in PLAYER_HEALTH as boss bars somehow bork up PLAYER_HEALTH rendering :/
            if (!Minecraft.getInstance().player.isCreative())
            {
                Renderer.renderStamina();
                Renderer.renderHP();
            }

            event.setCanceled(true);
        }
        else if (event.getOverlay().id() == VanillaGuiOverlay.PLAYER_HEALTH.id())
        {
            event.setCanceled(true);
        }
        else if(event.getOverlay().id() == VanillaGuiOverlay.ARMOR_LEVEL.id())
        {
            // Move the armor/XP/air bar up slightly
            event.getPoseStack().pushPose();
            event.getPoseStack().translate(0, -10, 0);
        }
        else if(event.getOverlay().id() == VanillaGuiOverlay.AIR_LEVEL.id())
        {
            // Cancel the vanilla air level rendering
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void overlayRenderPost(RenderGuiOverlayEvent.Post event)
    {
        // Do nothing if the player is dead
        if (genericClientFunctions.getPlayer() != null && !genericClientFunctions.getPlayer().isAlive())
            return;

        if (event.getOverlay().id() == VanillaGuiOverlay.EXPERIENCE_BAR.id())
        {
            // Remove the upward translation
            event.getPoseStack().popPose();
        }

        if (event.getOverlay().id() == VanillaGuiOverlay.HOTBAR.id())
        {
            SkillHotbarOverlay.PostRenderSkillHotbar(event.getPoseStack());
        }
    }

    @SubscribeEvent
    public static void gameTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            tick = (tick + 1) % 14400;

            if(ClientItemControl.hasLoaded)
            {
                // Attempt to process any ChunkQiSource packets pending
                AddChunkQiSourceToClient.processPackets();
            }

            lastTickTime = System.nanoTime();
        }
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
            if (Minecraft.getInstance().level != null)
                Renderer.render(event.renderTickTime);
    }

    @SubscribeEvent
    public static void playerDisconnects(PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (event.getEntity().getUUID().compareTo(Minecraft.getInstance().player.getUUID()) == 0)
        {
            SkillHotbarOverlay.reset();
            CultivatorModelHandler.reset();
        }
    }
}
