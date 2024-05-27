package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens.CultivationTypeScreen;
import DaoOfModding.Cultivationcraft.Client.GUI.Screens.GenericTabScreen;
import DaoOfModding.Cultivationcraft.Common.Advancements.CultivationAdvancements;
import DaoOfModding.Cultivationcraft.Common.Advancements.Triggers.BreakthroughTrigger;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.ExternalCultivationHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.PassiveTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.TechniqueModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultCultivationStatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.StaminaHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CultivationType {
    protected ResourceLocation ID = new ResourceLocation(Cultivationcraft.MODID, "cultivation.default");

    protected int techLevel = 0;
    protected int maxedTechsToBreakthrough = 0;

    protected int stage = 1;
    protected int maxStage = 1;

    protected CultivationType previousCultivation = null;
    protected Tribulation tribulation = new Tribulation(maxStage, 100, 1);

    protected boolean isTribulating = false;
    protected boolean hasTribulated = false;

    protected HashMap<String, HashMap<ResourceLocation, Double>> statLevels = new HashMap<>();

    protected ArrayList<TechniqueModifier> modifiers = new ArrayList<>();

    protected PassiveTechnique passive = new PassiveTechnique();

    protected CultivationTypeScreen screen = new CultivationTypeScreen();

    protected ArrayList<CultivationType> advancements = new ArrayList<>();

    protected boolean statLeveling = true;
    protected double qiLevel = 0;

    protected Quest quest;
    protected double questProgress = 0;

    protected CultivationType advancingCultivation = null;

    public CultivationType(int currentStage) {
        stage = currentStage;
        stageCalculations();
    }

    // Put anything here that changes based on the stage
    public void stageCalculations() {

    }

    public Quest getQuest()
    {
        return quest;
    }

    public void setQuest(Quest newQuest)
    {
        quest = newQuest;
    }

    public ArrayList<ResourceLocation> getElements()
    {
        ArrayList<ResourceLocation> ElementList = new ArrayList<>();

        for (TechniqueModifier mod : getModifiers())
            if (mod.hasElement())
                ElementList.add(mod.getElement());

        if (ElementList.size() == 0)
            ElementList.add(Elements.noElement);

        return ElementList;
    }

    public boolean progressQuest(double amount)
    {
        if (questProgress == quest.complete || amount == 0)
            return false;

        questProgress += amount;

        if (questProgress >= quest.complete)
        {
            questProgress = quest.complete;

            // TODO: Cultivation quest has been completed
        }

        return true;
    }

    public double getQuestProgress()
    {
        return questProgress;
    }

    public boolean statsCanLevel()
    {
        return statLeveling;
    }

    public String getName() {
        return Component.translatable(ID.toLanguageKey()).getString();
    }

    public ResourceLocation getID() {
        return ID;
    }

    public boolean canCultivate(ResourceLocation element) {
        return true;
    }

    public boolean consumeQi(Player player, double qiToConsume) {
        return StaminaHandler.consumeStamina(player, (float) qiToConsume);
    }

    public ArrayList<TechniqueModifier> getModifiers() {
        ArrayList<TechniqueModifier> totalModifiers = new ArrayList<>();

        for (TechniqueModifier mod : modifiers)
            totalModifiers.add(mod);

        if (getPreviousCultivation() != null) {
            for (TechniqueModifier mod : getPreviousCultivation().getModifiers())
                totalModifiers.add(mod);
        }

        return totalModifiers;
    }

    public boolean canBreakthrough(Player player)
    {
        if (qiLevel >= techLevel || getTechLevelProgressWithoutPrevious(passive.getClass().toString()) >= techLevel)
            if (getMaxedTechs() >= maxedTechsToBreakthrough)
                if (getQuest() == null || getQuestProgress() >= getQuest().complete)
                    return true;

        return false;
    }

    public ArrayList<CultivationType> getAdvancements(Player player)
    {
        ArrayList<CultivationType> playerAdvancements = new ArrayList<>();

        for (CultivationType testAdvance : advancements)
            if (testAdvance.canCultivate(player))
                playerAdvancements.add(testAdvance);

        return playerAdvancements;
    }

    // Returns whether the specified player can cultivate this cultivationType
    public boolean canCultivate(Player player) {
        return true;
    }

    public void advanceExtra(Player player, String extra)
    {
        if (advancingCultivation == null)
            return;

        CultivationType advancing = advancingCultivation;
        advancingCultivation = null;

        advancing.setPreviousCultivation(this);
        if(!advancing.doPostAdvancementActions(player, extra))
        {
            Cultivationcraft.LOGGER.error("Error progressing cultivation to " + advancingCultivation.getID() + " with " + extra);
            return;
        }
        CultivatorStats.getCultivatorStats(player).setCultivation(advancing);
    }

    public void advance(Player player, String advancement)
    {
        CultivationType advanceTo = null;

        for (CultivationType advance : advancements)
            if (advance.getID().toString().compareTo(advancement) == 0)
                advanceTo = advance;

        if (advanceTo == null) {
            Cultivationcraft.LOGGER.error("Tried to advance to unavailable cultivation type " + advancement);
            return;
        }

        advanceTo.doPreAdvancementActions(this, player);

        if (!advanceTo.hasAdvancementOptions())
        {
            advanceTo.setPreviousCultivation(this);
            CultivatorStats.getCultivatorStats(player).setCultivation(advanceTo);
        }
        else
            advancingCultivation = advanceTo;

        tribulation.reset();
    }

    public void doPreAdvancementActions(CultivationType advancingFrom, Player player)
    {

    }

    public boolean doPostAdvancementActions(Player player, String extra)
    {
        return true;
    }

    public CultivationType getAdvancingCultivation()
    {
        return advancingCultivation;
    }

    public GenericTabScreen getAdvancmentOptionScreen()
    {
        return null;
    }

    public boolean hasAdvancementOptions()
    {
        return false;
    }

    public boolean hasTribulated() {
        return hasTribulated;
    }

    public boolean hasTribulation(Player player) {
        if (stage == maxStage && canBreakthrough(player))
            return true;

        return false;
    }

    public void tick(Player player) {
        ((QiFoodStats) player.getFoodData()).setFoodLevel(((QiFoodStats) player.getFoodData()).getTrueFoodLevel() + (float) (getCultivationStat(player, DefaultCultivationStatIDs.qiPassiveSpeed) / 20.0));

        if (player.level.isClientSide)
            return;

        if (isTribulating() && player.isAlive())
            if (tribulation.tick(player)) {
                isTribulating = false;
                tribulationComplete(player);
                PacketHandler.sendCultivatorStatsToClient(player);
            }
    }

    public int getStage() {
        return stage;
    }

    public int getMaxStage() {
        return maxStage;
    }

    public void startTribulation() {
        isTribulating = true;
    }

    public boolean isTribulating() {
        return isTribulating;
    }

    // Calls on client side before breakingthrough
    // Returns wether to breakthrough or not
    public boolean clientPreBreakthrough(Player player)
    {
        return true;
    }

    public void breakthrough(Player player, String conditionals)
    {
    }

    public void tribulationComplete(Player player)
    {
        hasTribulated = true;

        if (player instanceof ServerPlayer)
            CultivationAdvancements.HAS_BROKENTROUGH.trigger((ServerPlayer) player, true);
    }

    public CultivationTypeScreen getScreen() {
        return screen;
    }

    public String breakthroughProgress(Player player)
    {
        String progress = Component.translatable("cultivationcraft.gui.cultivationprogress").getString();

        if (statsCanLevel())
            progress = progress + ": " + getTechLevelProgressWithoutPrevious(passive.getClass().toString()) + "/" + techLevel;
        else
            progress = progress + ": " + getQiLevelProgress() + "/" + techLevel;

        if (maxedTechsToBreakthrough > 0)
            progress += "\n" + Component.translatable("cultivationcraft.gui.completedtech").getString() + ": " + getMaxedTechs() + "/" + maxedTechsToBreakthrough;

        if (hasTribulation(player))
            progress += "\n" + Component.translatable("cultivationcraft.gui.tribulationpending").getString();

        if (getQuest() != null)
            progress += "\n" + (int)questProgress + "/" + (int)getQuest().complete + " " + getQuest().getDescription();

        return progress;
    }

    public int getMaxedTechs() {
        int i = 0;

        for (String tech : statLevels.keySet())
            if (passive.getClass().toString().compareTo(tech) != 0 && getTechLevelProgress(tech) >= getMaxTechLevel())
                i++;

        return i;
    }

    public PassiveTechnique getPassive() {
        return passive;
    }

    public double getCultivationStat(Player player, ResourceLocation stat) {
        double amount = 0;

        if (getPassive().hasTechniqueStat(stat))
            amount += getPassive().getTechniqueStat(stat, player);

        return amount;
    }

    public void reset(Player player) {
        statLevels = new HashMap<>();
    }

    public double absorbFromQiSource(double amount, Player player)
    {
        List<QiSource> sources = ChunkQiSources.getQiSourcesInRange(player.level, player.position(), (int) getCultivationStat(player, DefaultCultivationStatIDs.qiAbsorbRange));

        double remaining = amount;
        float toAbsorb = 0;

        // Draw Qi from each Qi source available
        for (QiSource source : sources) {
            // Only absorb from QiSources of the correct element
            if (canCultivate(source.getElement()))
                if (remaining > 0) {
                    double absorbed = source.absorbQi(remaining, player);
                    remaining -= absorbed;

                    toAbsorb += absorbed;
                }
        }

        if (toAbsorb > amount)
            return amount;

        if (toAbsorb < 0)
            return 0;

        return toAbsorb;
    }

    public double absorbFromQiSource(int amount, Player player, QiSource source) {
        double toAbsorb = 0;

        toAbsorb = 0;

        // Only absorb from QiSources of the correct element
        if (canCultivate(source.getElement()))
            toAbsorb = source.absorbQi(amount, player);

        if (toAbsorb > amount)
            return amount;

        if (toAbsorb < 0)
            return 0;

        return toAbsorb;
    }

    public float progressCultivation(Player player, float Qi, ResourceLocation element)
    {
        if (!canCultivate(element))
            return Qi;

        if (Qi < 0)
            return 0;

        int currentLevel = (int)qiLevel;

        if (statsCanLevel())
            currentLevel = getTechLevelProgressWithoutPrevious(getPassive().getClass().toString());

        // Don't cultivate if already at the max
        if (currentLevel >= techLevel)
            return Qi;

        float remains = (currentLevel + Qi) - techLevel;

        // If cultivating will go over the max, cultivate to the max instead
        if (remains > 0) {
            increaseCultivationLevel(player, remains);
            return Qi - remains;
        }

        // Cultivate all the supplied Qi
        increaseCultivationLevel(player, Qi);
        return 0;
    }

    public void increaseCultivationLevel(Player player, float amount)
    {
        if (statsCanLevel())
            getPassive().levelUp(player, amount);
        else
        {
            qiLevel += amount;

            if (qiLevel > getMaxTechLevel())
                qiLevel = getMaxTechLevel();
        }
    }

    public CultivationType getPreviousCultivation() {
        return previousCultivation;
    }

    public Double getStatLevelWithoutPrevious(Class tech, ResourceLocation stat) {
        Double level = 0.0;

        if (statLevels.containsKey(tech.toString()) && statLevels.get(tech.toString()).containsKey(stat))
            level += statLevels.get(tech.toString()).get(stat);

        return level;
    }

    public Double getStatLevel(Class tech, ResourceLocation stat) {
        if (tech.toString().compareTo(passive.getClass().toString()) == 0)
            return getPassiveStatLevel(stat);

        Double level = 0.0;

        if (statLevels.containsKey(tech.toString()) && statLevels.get(tech.toString()).containsKey(stat))
            level += statLevels.get(tech.toString()).get(stat);

        if (previousCultivation != null)
            level += previousCultivation.getStatLevel(tech, stat);

        return level;
    }

    public Double getPassiveStatLevel(ResourceLocation stat)
    {
        Double level = 0.0;

        if (statsCanLevel())
        {
            String passiveString = getPassive().getClass().toString();

            if (statLevels.containsKey(passiveString) && statLevels.get(passiveString).containsKey(stat))
                level += statLevels.get(passiveString).get(stat);
        }
        /*else
            level += qiLevel;*/

        if (previousCultivation != null)
            level += previousCultivation.getPassiveStatLevel(stat);

        return level;
    }

    public int getMaxTechLevelWithoutPrevious() {
        return techLevel;
    }

    public int getMaxTechLevel()
    {
        int level = 0;

        if (statsCanLevel())
            level = techLevel;

        if (previousCultivation != null)
            level += getPreviousCultivation().getMaxTechLevel();

        return level;
    }

    public int getTechLevelProgress(Class tech) {
        return getTechLevelProgress(tech.toString());
    }

    public int getQiLevelProgress()
    {
        if (qiLevel >= techLevel)
            return techLevel;

        return (int)qiLevel;
    }

    public int getTechLevelProgressWithoutPrevious(String tech) {
        int progress = 0;

        if (statLevels.containsKey(tech)) {
            for (double value : statLevels.get(tech).values())
                progress += (int) value;
        }

        return progress;
    }

    public int getTechLevelProgress(String tech) {
        if (tech.compareTo(passive.getClass().toString()) == 0)
            return getPassiveTechLevelProgress();

        int progress = 0;

        if (statLevels.containsKey(tech)) {
            for (double value : statLevels.get(tech).values())
                progress += (int) value;
        }

        if (previousCultivation != null)
            progress += previousCultivation.getTechLevelProgress(tech);

        return progress;
    }

    public int getPassiveTechLevelProgress()
    {
        int progress = 0;

        String tech = getPassive().getClass().toString();

        if (statLevels.containsKey(tech)) {
            for (double value : statLevels.get(tech).values())
                progress += (int) value;
        }

        if (previousCultivation != null)
            progress += previousCultivation.getPassiveTechLevelProgress();

        return progress;
    }

    public void levelTech(Technique tech, double amount, Player player)
    {
        int max = getMaxTechLevel();
        int current = getTechLevelProgress(tech.getClass());

        if (max == current)
        {
            // If this technique is maxed then trigger the appropriate achievements
            if (player instanceof ServerPlayer)
                CultivationAdvancements.TECH_USE.trigger((ServerPlayer) player, tech.getClass().getName(), 2);

            return;
        }

        double amountToLevel = max - current;

        if (amount > amountToLevel)
        {
            amount = amountToLevel;

            if (player instanceof ServerPlayer)
                CultivationAdvancements.TECH_USE.trigger((ServerPlayer) player, tech.getClass().getName(), 2);
        }

        ResourceLocation toLevel = CultivatorStats.getCultivatorStats(player).getTechniqueFocus(tech.getClass());

        if (toLevel == null)
            toLevel = (ResourceLocation) tech.getTechniqueStats().toArray()[0];

        double currentLevel = amount;

        if (!statLevels.containsKey(tech.getClass().toString()))
            statLevels.put(tech.getClass().toString(), new HashMap<>());

        if (statLevels.get(tech.getClass().toString()).containsKey(toLevel))
            currentLevel += statLevels.get(tech.getClass().toString()).get(toLevel);

        statLevels.get(tech.getClass().toString()).put(toLevel, currentLevel);

        PacketHandler.sendCultivatorStatsToClient(player);
    }

    public void setPreviousCultivation(CultivationType previous) {
        previousCultivation = previous;
    }

    public CompoundTag writeNBT()
    {
        CompoundTag nbt = new CompoundTag();

        if (getPreviousCultivation() != null) {
            nbt.putString("PREVIOUSCULTNAME", getPreviousCultivation().getClass().toString());
            nbt.put("PREVIOUSCULT", getPreviousCultivation().writeNBT());
        }

        nbt.putInt("STAGE", getStage());
        nbt.putBoolean("TRIBULATING", isTribulating);
        nbt.putBoolean("TRIBULATED", hasTribulated);

        int i = 0;

        for (TechniqueModifier mod : modifiers) {
            nbt.putString("MODIFIER" + i, mod.getID().toString());
            i++;
        }

        if (quest != null)
            nbt.put("QUEST", quest.writeNBT());

        i = 0;

        for (Map.Entry<String, HashMap<ResourceLocation, Double>> tech : statLevels.entrySet()) {
            nbt.putString("TECH" + i + "CLASS", tech.getKey());

            int j = 0;

            for (Map.Entry<ResourceLocation, Double> techEntry : tech.getValue().entrySet()) {
                nbt.putString("TECH" + i + "ENTRY" + j + "NAME", techEntry.getKey().toString());
                nbt.putDouble("TECH" + i + "ENTRY" + j + "VALUE", techEntry.getValue());

                j++;
            }

            i++;
        }

        nbt.putDouble("QUESTPROGRESS", questProgress);
        nbt.putDouble("QIPROGRESS", qiLevel);

        if (advancingCultivation != null)
        {
            nbt.putString("ADVANCINGNAME", advancingCultivation.getClass().toString());
            nbt.put("ADVANCING", advancingCultivation.writeNBT());
        }

        return nbt;
    }

    public void readNBT(CompoundTag nbt)
    {
        if (nbt.contains("PREVIOUSCULTNAME"))
        {
            CultivationType newCultivation = ExternalCultivationHandler.getCultivation(nbt.getString("PREVIOUSCULTNAME"));
            newCultivation.readNBT(nbt.getCompound("PREVIOUSCULT"));

            previousCultivation = newCultivation;
        }

        stage = nbt.getInt("STAGE");
        stageCalculations();

        isTribulating = nbt.getBoolean("TRIBULATING");
        hasTribulated = nbt.getBoolean("TRIBULATED");

        HashMap<String, HashMap<ResourceLocation, Double>> newTechLevels = new HashMap<>();

        int i = 0;

        modifiers = new ArrayList<>();

        while (nbt.contains("MODIFIER" + i)) {
            modifiers.add(ExternalCultivationHandler.getModifier(new ResourceLocation(nbt.getString("MODIFIER" + i))));
            i++;
        }

        if (nbt.contains("QUEST"))
        quest = Quest.readNBT(nbt.getCompound("QUEST"));

        i = 0;

        while (nbt.contains("TECH" + i + "CLASS")) {
            int j = 0;

            HashMap<ResourceLocation, Double> values = new HashMap<>();

            while (nbt.contains("TECH" + i + "ENTRY" + j + "NAME")) {
                values.put(new ResourceLocation(nbt.getString("TECH" + i + "ENTRY" + j + "NAME")), nbt.getDouble("TECH" + i + "ENTRY" + j + "VALUE"));
                j++;
            }

            newTechLevels.put(nbt.getString("TECH" + i + "CLASS"), values);

            i++;
        }

        statLevels = newTechLevels;

        questProgress = nbt.getDouble("QUESTPROGRESS");
        qiLevel = nbt.getDouble("QIPROGRESS");

        if (nbt.contains("ADVANCING"))
        {
            CultivationType newCultivation = ExternalCultivationHandler.getCultivation(nbt.getString("ADVANCINGNAME"));
            newCultivation.readNBT(nbt.getCompound("ADVANCING"));

            advancingCultivation = newCultivation;
        }
    }
}
