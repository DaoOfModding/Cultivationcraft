package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;

public class Technique
{
    protected enum useType {Toggle, Channel, Tap};

    protected useType type;

    protected ResourceLocation icon;
    protected ResourceLocation overlay;

    protected String name;

    protected int elementID;
    protected boolean active = false;

    protected boolean overlayOn = false;

    protected boolean multiple = true;

    // Cooldown (in ticks)
    protected int cooldown = 5;
    protected int cooldownCount = 0;

    // Length of time until max channel in ticks
    protected int channelLength = 1;
    protected int currentChannel = 0;

    protected PlayerPose pose = new PlayerPose();

    protected ArrayList<AttributeModifier> modifiers = new ArrayList<AttributeModifier>();
    protected ArrayList<Effect> effects = new ArrayList<Effect>();

    protected int slot;

    protected PlayerStatModifications stats;


    public Technique()
    {
        name = "Example name";
        elementID = Elements.noElementID;
        type = useType.Toggle;
        multiple = true;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/example.png");
    }

    public PlayerStatModifications getStats()
    {
        return stats;
    }

    public void setSlot(int newSlot)
    {
        slot = newSlot;
    }

    // Returns whether this technique can be used by the specified player
    public boolean isValid(PlayerEntity player)
    {
        return false;
    }

    public int getElementID()
    {
        return elementID;
    }

    // Returns whether this technique is currently active or not
    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean isActive)
    {
        active = isActive;
    }

    // Returns the icon for this technique
    public ResourceLocation getIcon()
    {
        return icon;
    }

    // Returns the name of this technique
    public String getName()
    {
        return name;
    }

    // Allow multiple copies of this technique to be equipped at once
    public boolean allowMultiple()
    {
        return multiple;
    }

    // What to do when the use key for this technique is pressed
    // keyDown = true when the key is pressed down, false when the key is released
    public void useKeyPressed(boolean keyDown, PlayerEntity player)
    {
        // Do nothing if this skill is on cooldown
        if (cooldownCount > 0)
            return;

        // Skill is turned on while the key is held down, turned off when key is released
        if (type == useType.Channel)
        {
            if (active && !keyDown)
                onRelease(player);

            if (!keyDown)
                deactivate(player);
            else if (keyDown && !active)
                activate(player);
        }
        else if (type == useType.Toggle)
        {
            // Toggle skill when key pressed
            if (!keyDown)
            {
                if (active)
                    deactivate(player);
                else
                    activate(player);
            }
        }
        // Skill is turned on when the key is pressed (custom code to deactivate)
        else if (type == useType.Tap)
            if (keyDown)
                activate(player);
    }

    public void activate(PlayerEntity player)
    {
        active = true;

        addModifiers(player);
    }

    public void deactivate(PlayerEntity player)
    {
        active = false;
        cooldownCount = cooldown;
        currentChannel = 0;

        removeModifiers(player);
    }

    private void addModifiers(PlayerEntity player)
    {
        ModifiableAttributeInstance modifierInstance = player.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());

        for (AttributeModifier modifier : modifiers)
            if (!modifierInstance.hasModifier(modifier))
                modifierInstance.addTransientModifier(modifier);

        for (Effect effect : effects)
            if (!player.hasEffect(effect))
                player.addEffect(new EffectInstance(effect, 9999999));

        if (stats != null)
            BodyPartStatControl.updateStats(player);
    }

    private void removeModifiers(PlayerEntity player)
    {
        ModifiableAttributeInstance modifierInstance = player.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());

        for (AttributeModifier modifier : modifiers)
            modifierInstance.removeModifier(modifier);

        for (Effect effect : effects)
            if (player.hasEffect(effect))
                player.removeEffect(effect);

        if (stats != null)
            BodyPartStatControl.updateStats(player);
    }

    // Called when the use key is released for a channel skill
    protected void onRelease(PlayerEntity player)
    {

    }

    protected void setOverlay(ResourceLocation location)
    {
        overlay = location;
        overlayOn = true;
    }

    public void writeBuffer(PacketBuffer buffer)
    {
        buffer.writeNbt(writeNBT());
    }

    public static Technique readBuffer(PacketBuffer buffer)
    {
        Technique newTech;
        CompoundNBT nbt = buffer.readNbt();

        return readNBT(nbt);
    }

    public void setCooldown(int ticks)
    {
        cooldownCount = ticks;
    }

    // Write a techniques data to NBT
    public CompoundNBT writeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();

        String className = this.getClass().getName();

        nbt.putString("className", className);

        nbt.putBoolean("active", active);
        nbt.putInt("cooldown", cooldownCount);

        return nbt;
    }

    // Read a Technique stored in NBT and create a technique from it
    public static Technique readNBT(CompoundNBT nbt)
    {
        String className = nbt.getString("className");
        Technique newTech;

        try
        {
            Class test = Class.forName(className);
            newTech = (Technique)test.newInstance();
        }
        catch (Exception e)
        {
            Cultivationcraft.LOGGER.error(className + " not found when loading Technique");
            return null;
        }

        newTech.readNBTData(nbt);

        return newTech;
    }

    public void readNBTData(CompoundNBT nbt)
    {
        setActive(nbt.getBoolean("active"));
        setCooldown(nbt.getInt("cooldown"));
    }

    public void readBufferData(PacketBuffer buffer)
    {
        setActive(buffer.readBoolean());
    }

    // Triggers clients side only
    // Only triggers if the technique is active and set by the player
    public void onInput()
    {
    }

    // Ticks on server side, only called if Technique is active and owned by the player
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        addModifiers(event.player);

        // While the key is being held down increase the currentChannel duration
        if (type == useType.Channel)
            if (currentChannel < channelLength)
                currentChannel++;
    }

    // Ticks on client side, only called if Technique is active
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        addModifiers(event.player);

        // While the key is being held down increase the currentChannel duration
        if (type == useType.Channel)
            if (currentChannel < channelLength)
                currentChannel++;

        PoseHandler.addPose(event.player.getUUID(), pose);
    }

    public void tickInactiveClient(TickEvent.PlayerTickEvent event)
    {
        if (cooldownCount > 0)
            cooldownCount = cooldownCount - 1;
    }

    public void tickInactiveServer(TickEvent.PlayerTickEvent event)
    {
        if (cooldownCount > 0)
            cooldownCount = cooldownCount - 1;
    }

    public int getCooldown()
    {
        return cooldownCount;
    }

    // Rendering as the player who owns the technique
    // Put code here for things only the person using the technique can see
    public void renderPlayerView()
    {
        if (active && type == useType.Channel)
        {
            ResourceLocation progress = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/progressbar.png");

            int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            float minPercent = 0.3f;
            float maxPercent = 0.675f;
            float percent = (float)currentChannel / (float)channelLength;
            float adjustedWidth = minPercent + (maxPercent - minPercent) * percent;


            Minecraft.getInstance().getTextureManager().bind(progress);

            GlStateManager._enableBlend();

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuilder();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0f, 0.5f).endVertex();
            bufferbuilder.vertex(scaledWidth, scaledHeight, -90.0D).uv(1.0f, 0.5f).endVertex();
            bufferbuilder.vertex(scaledWidth, 0.0D, -90.0D).uv(1.0f, 0.0f).endVertex();
            bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0f, 0.0f).endVertex();
            tessellator.end();

            bufferbuilder = tessellator.getBuilder();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0f, 1f).endVertex();
            bufferbuilder.vertex(adjustedWidth * scaledWidth, scaledHeight, -90.0D).uv(1.0f * adjustedWidth, 1f).endVertex();
            bufferbuilder.vertex(adjustedWidth * scaledWidth, 0.0D, -90.0D).uv(1.0f * adjustedWidth, 0.5f).endVertex();
            bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0f, 0.5f).endVertex();
            tessellator.end();
        }
    }

    // Generic rendering for all players
    // Put code here for things everyone can see when looking at the player using the technique
    public void render() {}

    // Render overlay if it has been enabled
    public void renderOverlay()
    {
        // Do nothing if the overlay isn't activated
        if (!overlayOn)
            return;

        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        Minecraft.getInstance().getTextureManager().bind(overlay);

        GlStateManager._enableBlend();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0f, 1.0f).endVertex();
        bufferbuilder.vertex(scaledWidth, scaledHeight, -90.0D).uv(1.0f, 1.0f).endVertex();
        bufferbuilder.vertex(scaledWidth, 0.0D, -90.0D).uv(1.0f, 0.0f).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0f, 0.0f).endVertex();
        tessellator.end();
    }

    // Send an int info packet to other clients
    // Client only, only if owner of technique
    public void sendInfo(int info)
    {
        ClientPacketHandler.sendTechniqueInfoToServer(Minecraft.getInstance().player.getUUID(), info, slot);
    }

    // Process a received int info packet
    public void processInfo(PlayerEntity player, int info)
    {

    }
}
