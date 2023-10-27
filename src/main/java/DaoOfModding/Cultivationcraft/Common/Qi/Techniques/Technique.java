package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.TechniqueStatModification;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import DaoOfModding.Cultivationcraft.StaminaHandler;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Technique
{
    public enum useType {Toggle, Channel, Tap};

    protected useType type;

    protected ResourceLocation icon;
    protected ResourceLocation overlay;

    protected String langLocation;

    protected ResourceLocation Element;
    protected boolean active = false;

    protected boolean overlayOn = false;

    protected boolean multiple = true;

    // Cooldown (in ticks)
    protected int cooldown = 5;
    protected int cooldownCount = 0;

    // Length of time until max channel in ticks
    protected int channelLength = 0;
    protected int currentChannel = 0;

    protected PlayerPose pose = new PlayerPose();

    protected ArrayList<AttributeModifier> modifiers = new ArrayList<AttributeModifier>();
    protected ArrayList<MobEffect> effects = new ArrayList<MobEffect>();

    protected PlayerStatModifications stats = new PlayerStatModifications();

    protected ResourceLocation progress = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/progressbar.png");

    public boolean toDeactivate = false;

    protected boolean elytraDisables = false;

    protected boolean legAnimationLockOff = false;
    protected int animationRecoveryTime = 20;
    protected int legAnimationCountdown = 0;

    protected boolean canBreathWhileActive = true;

    protected HashMap<ResourceLocation, Double> defaultStats = new HashMap<ResourceLocation, Double>();
    protected HashMap<ResourceLocation, TechniqueStatModification> statChangesPerLevel = new HashMap<ResourceLocation, TechniqueStatModification>();

    protected boolean canLevel = false;

    public Technique()
    {
        langLocation = "cultivationcraft.technique.example";
        Element = Elements.noElement;
        type = useType.Toggle;
        multiple = true;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/example.png");
    }

    protected void addTechniqueStat(ResourceLocation stat, double amount, TechniqueStatModification changePerLevel)
    {
        defaultStats.put(stat, amount);

        if (canLevel && changePerLevel != null)
            statChangesPerLevel.put(stat, changePerLevel);
    }

    protected void removeTechniqueStat(ResourceLocation stat)
    {
        defaultStats.remove(stat);
    }

    public TechniqueStatModification getStatChangesPerLevel(ResourceLocation stat)
    {
        return statChangesPerLevel.get(stat);
    }

    public void levelUp(Player player, double amount)
    {
        if (!canLevel || player.level.isClientSide)
            return;

        CultivatorStats.getCultivatorStats(player).getCultivation().levelTech(this, amount, player);
    }

    public boolean canLevel()
    {
        return canLevel;
    }

    public ResourceLocation getFirstStatChange()
    {
        return (ResourceLocation)statChangesPerLevel.keySet().toArray()[0];
    }

    protected void addTechniqueStat(ResourceLocation stat, double amount)
    {
        addTechniqueStat(stat, amount, null);
    }

    public String getTechniqueStatString(Player player)
    {
        String statString = "";

        for (ResourceLocation stat : getTechniqueStats())
        {
            statString += "\n" + Component.translatable(stat.getPath()).getString() + ": ";

            double value = getTechniqueStat(stat, player);

            if (value % 1.0 == 0)
                statString += (int)value;
            else
                statString += value;
        }

        return statString;
    }

    public Set<ResourceLocation> getTechniqueStats()
    {
        return defaultStats.keySet();
    }

    public boolean hasTechniqueStat(ResourceLocation stat)
    {
        return defaultStats.containsKey(stat);
    }

    public double getTechniqueStat(ResourceLocation stat, Player player)
    {
        double defaultStat = getTechniqueDefaultStat(stat);
        CultivationType cultivation = CultivatorStats.getCultivatorStats(player).getCultivation();

        // If this technique can level, then loop through the stat changes and apply them to this stat
        if (canLevel)
            for (Map.Entry<ResourceLocation, TechniqueStatModification> modification : statChangesPerLevel.entrySet())
            {
                if (modification.getValue() != null)
                {
                    int statLevel = (int) (double) cultivation.getStatLevel(this.getClass(), modification.getKey());
                    defaultStat += modification.getValue().getStatChange(stat) * statLevel;
                }
            }

        return defaultStat;
    }

    public double getTechniqueDefaultStat(ResourceLocation stat)
    {
        return defaultStats.get(stat);
    }

    // Deactivates the bounding box animation lock for legs whilst this tech is active
    // And for the specified number of ticks after it is deactivated
    protected void setLegAnimationLockOffWhileActive(int ticksWhileDeactivated)
    {
        legAnimationLockOff = true;
        animationRecoveryTime = ticksWhileDeactivated;
    }

    public useType getType()
    {
        return type;
    }

    public boolean disableBreathing()
    {
        return !canBreathWhileActive;
    }

    public String getDescription()
    {
        return Component.translatable(langLocation + ".description").getString();
    }

    public PlayerStatModifications getStats()
    {
        return stats;
    }

    // Returns whether this technique can be used by the specified player
    public boolean isValid(Player player)
    {
        return false;
    }

    public ResourceLocation getElement()
    {
        return Element;
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
        return Component.translatable(langLocation).getString();
    }

    public boolean isCalled(String name)
    {
        if (langLocation.compareTo(name) == 0)
            return true;

        return false;
    }

    // Allow multiple copies of this technique to be equipped at once
    public boolean allowMultiple()
    {
        return multiple;
    }

    // What to do when the use key for this technique is pressed
    // keyDown = true when the key is pressed down, false when the key is released
    public void useKeyPressed(boolean keyDown, Player player)
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

    public void activate(Player player)
    {
        if (elytraDisables && player.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.ELYTRA)
            return;

        active = true;

        addModifiers(player);
    }

    public void deactivate(Player player)
    {
        active = false;
        cooldownCount = cooldown;
        currentChannel = 0;

        if (player.level.isClientSide)
            toDeactivate = false;
        else
            toDeactivate = true;

        removeModifiers(player);
    }

    protected void addModifiers(Player player)
    {
        if (modifiers.size() == 0 && effects.size() == 0)
            return;

        AttributeInstance modifierInstance = player.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());

        boolean modified = false;

        for (AttributeModifier modifier : modifiers)
            if (!modifierInstance.hasModifier(modifier))
            {
                modifierInstance.addTransientModifier(modifier);
                modified = true;
            }

        for (MobEffect effect : effects)
            if (!player.hasEffect(effect))
            {
                player.addEffect(new MobEffectInstance(effect, 9999999, 0, false, false));
                modified = true;
            }

        if (modified)
            BodyPartStatControl.updateStats(player);
    }

    protected void removeModifiers(Player player)
    {
        if (modifiers.size() == 0 && effects.size() == 0)
            return;

        AttributeInstance modifierInstance = player.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());

        for (AttributeModifier modifier : modifiers)
            modifierInstance.removeModifier(modifier);

        for (MobEffect effect : effects)
            if (player.hasEffect(effect))
                player.removeEffect(effect);

        BodyPartStatControl.updateStats(player);
    }

    // Called when a fall event is called and this technique is active
    public void onFall(LivingFallEvent event)
    {

    }

    // Called when the use key is released for a channel skill
    protected void onRelease(Player player)
    {

    }

    protected void setOverlay(ResourceLocation location)
    {
        overlay = location;
        overlayOn = true;
    }

    public void writeBuffer(FriendlyByteBuf buffer)
    {
        buffer.writeNbt(writeNBT());
    }

    public static Technique readBuffer(FriendlyByteBuf buffer)
    {
        Technique newTech;
        CompoundTag nbt = buffer.readNbt();

        return readNBT(nbt);
    }

    public void setCooldown(int ticks)
    {
        cooldownCount = ticks;
    }

    // Write a techniques data to NBT
    public CompoundTag writeNBT()
    {
        CompoundTag nbt = new CompoundTag();

        String className = this.getClass().getName();

        nbt.putString("className", className);

        nbt.putBoolean("active", active);
        nbt.putInt("cooldown", cooldownCount);
        nbt.putBoolean("toDeactivate", toDeactivate);
        nbt.putInt("legAnimationCountdown", legAnimationCountdown);

        toDeactivate = false;

        return nbt;
    }

    // Read a Technique stored in NBT and create a technique from it
    public static Technique readNBT(CompoundTag nbt)
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

    public void readNBTData(CompoundTag nbt)
    {
        setActive(nbt.getBoolean("active"));
        setCooldown(nbt.getInt("cooldown"));

        legAnimationCountdown = nbt.getInt("legAnimationCountdown");
        toDeactivate = nbt.getBoolean("toDeactivate");
    }

    public void readBufferData(FriendlyByteBuf buffer)
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
        if (elytraDisables && event.player.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.ELYTRA)
        {
            deactivate(event.player);
            return;
        }

        addModifiers(event.player);

        // While the key is being held down increase the currentChannel duration
        if (type == useType.Channel)
            if (currentChannel < channelLength)
                currentChannel++;
    }

    // Ticks on client side, only called if Technique is active
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        if (elytraDisables && event.player.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.ELYTRA)
        {
            deactivate(event.player);
            return;
        }

        if (legAnimationLockOff)
        {
            PoseHandler.getPlayerPoseHandler(event.player.getUUID()).lockLegPose(null);
            legAnimationCountdown = animationRecoveryTime;
        }

        addModifiers(event.player);

        // While the key is being held down increase the currentChannel duration
        if (type == useType.Channel)
            if (currentChannel < channelLength)
                currentChannel++;

        PoseHandler.addPose(event.player.getUUID(), pose);
    }

    public void tickInactiveClient(TickEvent.PlayerTickEvent event)
    {
        if (legAnimationLockOff && legAnimationCountdown > 0)
        {
            legAnimationCountdown = legAnimationCountdown - 1;
            PoseHandler.getPlayerPoseHandler(event.player.getUUID()).lockLegPose(null);
        }

        if (toDeactivate)
            deactivate(event.player);

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

    // Called when taking damage, returns the amount of damage to be taken
    public float onDamage(QiDamageSource source, float amount, Player player)
    {
        return amount;
    }

    // Rendering as the player who owns the technique
    // Put code here for things only the person using the technique can see
    public void renderPlayerView()
    {
        if (active && type == useType.Channel && channelLength > 0)
        {
            int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            float minPercent = 0.3f;
            float maxPercent = 0.675f;
            float percent = (float)currentChannel / (float)channelLength;
            float adjustedWidth = minPercent + (maxPercent - minPercent) * percent;

            RenderSystem.setShaderTexture(0, progress);

            GlStateManager._enableBlend();

            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0f, 0.5f).endVertex();
            bufferbuilder.vertex(scaledWidth, scaledHeight, -90.0D).uv(1.0f, 0.5f).endVertex();
            bufferbuilder.vertex(scaledWidth, 0.0D, -90.0D).uv(1.0f, 0.0f).endVertex();
            bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0f, 0.0f).endVertex();
            tesselator.end();

            bufferbuilder = tesselator.getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0f, 1f).endVertex();
            bufferbuilder.vertex(adjustedWidth * scaledWidth, scaledHeight, -90.0D).uv(1.0f * adjustedWidth, 1f).endVertex();
            bufferbuilder.vertex(adjustedWidth * scaledWidth, 0.0D, -90.0D).uv(1.0f * adjustedWidth, 0.5f).endVertex();
            bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0f, 0.5f).endVertex();
            tesselator.end();
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

        RenderSystem.setShaderTexture(0, overlay);

        GlStateManager._enableBlend();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0f, 1.0f).endVertex();
        bufferbuilder.vertex(scaledWidth, scaledHeight, -90.0D).uv(1.0f, 1.0f).endVertex();
        bufferbuilder.vertex(scaledWidth, 0.0D, -90.0D).uv(1.0f, 0.0f).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0f, 0.0f).endVertex();
        tesselator.end();
    }

    // Send an int info packet to other clients
    // Client only, only if owner of technique
    public void sendInfo(int info)
    {
        ClientPacketHandler.sendTechniqueInfoToServer(genericClientFunctions.getPlayer().getUUID(), info, langLocation);
    }

    // Process a received int info packet
    public void processInfo(Player player, int info)
    {

    }
}
