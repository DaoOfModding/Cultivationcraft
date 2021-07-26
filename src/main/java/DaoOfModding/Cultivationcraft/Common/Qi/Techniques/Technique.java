package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;

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

    public Technique()
    {
        name = "Example name";
        elementID = Elements.noElementID;
        type = useType.Toggle;
        multiple = true;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/example.png");
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
                deactivate();
            else if (keyDown && !active)
                activate();
        }
        else if (type == useType.Toggle)
        {
            // Toggle skill when key pressed
            if (!keyDown)
            {
                if (active)
                    deactivate();
                else
                    activate();
            }
        }
        // Skill is turned on when the key is pressed (custom code to deactivate)
        else if (type == useType.Tap)
            if (keyDown)
                activate();
    }

    public void activate()
    {
        active = true;
    }

    public void deactivate()
    {
        active = false;
        cooldownCount = cooldown;
        currentChannel = 0;
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
        buffer.writeCompoundTag(writeNBT());
    }

    public static Technique readBuffer(PacketBuffer buffer)
    {
        Technique newTech;
        CompoundNBT nbt = buffer.readCompoundTag();

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

    // Ticks on server side, only called if Technique is active and owned by the player
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        // While the key is being held down increase the currentChannel duration
        if (type == useType.Channel)
            if (currentChannel < channelLength)
                currentChannel++;
    }

    // Ticks on client side, only called if Technique is active
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        // While the key is being held down increase the currentChannel duration
        if (type == useType.Channel)
            if (currentChannel < channelLength)
                currentChannel++;

        PoseHandler.addPose(event.player.getUniqueID(), pose);
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

            int scaledWidth = Minecraft.getInstance().getMainWindow().getScaledWidth();
            int scaledHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();

            float minPercent = 0.3f;
            float maxPercent = 0.675f;
            float percent = (float)currentChannel / (float)channelLength;
            float adjustedWidth = minPercent + (maxPercent - minPercent) * percent;


            Minecraft.getInstance().getTextureManager().bindTexture(progress);

            GlStateManager.enableBlend();

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(0.0D, scaledHeight, -90.0D).tex(0.0f, 0.5f).endVertex();
            bufferbuilder.pos(scaledWidth, scaledHeight, -90.0D).tex(1.0f, 0.5f).endVertex();
            bufferbuilder.pos(scaledWidth, 0.0D, -90.0D).tex(1.0f, 0.0f).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0f, 0.0f).endVertex();
            tessellator.draw();

            bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(0.0D, scaledHeight, -90.0D).tex(0.0f, 1f).endVertex();
            bufferbuilder.pos(adjustedWidth * scaledWidth, scaledHeight, -90.0D).tex(1.0f * adjustedWidth, 1f).endVertex();
            bufferbuilder.pos(adjustedWidth * scaledWidth, 0.0D, -90.0D).tex(1.0f * adjustedWidth, 0.5f).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0f, 0.5f).endVertex();
            tessellator.draw();
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

        int scaledWidth = Minecraft.getInstance().getMainWindow().getScaledWidth();
        int scaledHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();

        Minecraft.getInstance().getTextureManager().bindTexture(overlay);

        GlStateManager.enableBlend();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, scaledHeight, -90.0D).tex(0.0f, 1.0f).endVertex();
        bufferbuilder.pos(scaledWidth, scaledHeight, -90.0D).tex(1.0f, 1.0f).endVertex();
        bufferbuilder.pos(scaledWidth, 0.0D, -90.0D).tex(1.0f, 0.0f).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0f, 0.0f).endVertex();
        tessellator.draw();
    }
}
