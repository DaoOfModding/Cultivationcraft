package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Client.Renderers.PoseHandler;
import DaoOfModding.Cultivationcraft.Common.PlayerPose;
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
    enum useType {Toggle, Channel};

    useType type;

    protected ResourceLocation icon;
    protected ResourceLocation overlay;

    protected String name;

    protected int elementID;
    protected boolean active = false;

    protected boolean overlayOn = false;

    protected boolean multiple = true;

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

    // Write a techniques data to NBT
    public CompoundNBT writeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();

        String className = this.getClass().getName();

        nbt.putString("className", className);

        nbt.putBoolean("active", active);

        return nbt;
    }

    // Allow multiple copies of this technique to be equipped at once
    public boolean allowMultiple()
    {
        return multiple;
    }

    // What to do when the use key for this technique is pressed
    // keyDown = true when the key is pressed down, false when the key is released
    public void useKeyPressed(boolean keyDown)
    {
        // Skill is turned on while the key is held down, turned off when key is released
        if (type == useType.Channel)
            active = keyDown;
        else if (type == useType.Toggle)
        {
            // Toggle skill when key pressed
            if (!keyDown) {
                active = !active;
            }
        }
    }

    protected void setOverlay(ResourceLocation location)
    {
        overlay = location;
        overlayOn = true;
    }

    public void writeBuffer(PacketBuffer buffer)
    {
        buffer.writeString(this.getClass().getName());
        buffer.writeBoolean(active);
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
    }

    public static Technique readBuffer(PacketBuffer buffer)
    {
        Technique newTech;
        String className = buffer.readString(256);

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
        newTech.readBufferData(buffer);

        return newTech;
    }

    public void readBufferData(PacketBuffer buffer)
    {
        setActive(buffer.readBoolean());
    }

    // Ticks on server side, only called if Technique is active and owned by the player
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
    }

    // Ticks on client side, only called if Technique is active
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        PoseHandler.addPose(event.player.getUniqueID(), pose);
    }

    // Rendering as the player who owns the technique
    // Put code here for things only the person using the technique can see
    public void renderPlayerView() {}

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
