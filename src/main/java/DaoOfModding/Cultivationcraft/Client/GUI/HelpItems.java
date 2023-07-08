package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.SelectableText;

import java.util.ArrayList;

public class HelpItems
{
    protected static ArrayList<SelectableText> selectText = new ArrayList<>();
    protected static ArrayList<SelectableText> selectOPText = new ArrayList<>();

    protected static SelectableText menuField = new SelectableText("cultivationcraft.gui.help.cultivationmenu");
    protected static SelectableText statsField = new SelectableText("cultivationcraft.gui.help.cultivationmenu.stats");
    protected static SelectableText elementsField = new SelectableText("cultivationcraft.gui.help.elements");

    public static void setup()
    {
        addStatsField(new SelectableText("cultivationcraft.stat.weight"));
        addStatsField(new SelectableText("cultivationcraft.stat.armor"));
        addStatsField(new SelectableText("cultivationcraft.stat.armortoughness"));
        addStatsField(new SelectableText("cultivationcraft.stat.movementspeed"));
        addStatsField(new SelectableText("cultivationcraft.stat.maxhp"));
        addStatsField(new SelectableText("cultivationcraft.stat.healthregen"));
        addStatsField(new SelectableText("cultivationcraft.stat.healthstaminaconversion"));
        addStatsField(new SelectableText("cultivationcraft.stat.maxstamina"));
        addStatsField(new SelectableText("cultivationcraft.stat.staminadrain"));
        addStatsField(new SelectableText("cultivationcraft.stat.staminause"));
        addStatsField(new SelectableText("cultivationcraft.stat.lungcapacity"));
        addStatsField(new SelectableText("cultivationcraft.stat.jumpheight"));
        addStatsField(new SelectableText("cultivationcraft.stat.fallheight"));
        addStatsField(new SelectableText("cultivationcraft.stat.bounceheight"));
        addStatsField(new SelectableText("cultivationcraft.stat.swimspeed"));
        addStatsField(new SelectableText("cultivationcraft.stat.flightspeed"));
        addStatsField(new SelectableText("cultivationcraft.stat.legweight"));
        addStatsField(new SelectableText("cultivationcraft.stat.wingweight"));
        addStatsField(new SelectableText("cultivationcraft.stat.attackrange"));
        addStatsField(new SelectableText("cultivationcraft.stat.armattackmodifier"));
        addStatsField(new SelectableText("cultivationcraft.stat.boneattackmodifier"));
        addStatsField(new SelectableText("cultivationcraft.stat.biteattackmodifier"));
        addStatsField(new SelectableText("cultivationcraft.stat.resistancemodifier"));
        addStatsField(new SelectableText("cultivationcraft.stat.width"));
        addStatsField(new SelectableText("cultivationcraft.stat.size"));
        addStatsField(new SelectableText("cultivationcraft.stat.qiabsorb"));
        addStatsField(new SelectableText("cultivationcraft.stat.qiabsorbrange"));
        addStatsField(new SelectableText("cultivationcraft.stat.qicost"));

        SelectableText qiField = new SelectableText("cultivationcraft.gui.help.qi");
        qiField.addItem(new SelectableText("cultivationcraft.gui.help.qi.source"));
        qiField.addItem(elementsField);

        SelectableText fireField = new SelectableText("cultivationcraft.gui.help.elements.fire");
        SelectableText woodField = new SelectableText("cultivationcraft.gui.help.elements.wood");
        SelectableText windField = new SelectableText("cultivationcraft.gui.help.elements.wind");
        SelectableText earthField = new SelectableText("cultivationcraft.gui.help.elements.earth");
        SelectableText waterField = new SelectableText("cultivationcraft.gui.help.elements.water");
        addElementsField(fireField);
        addElementsField(woodField);
        addElementsField(windField);
        addElementsField(earthField);
        addElementsField(waterField);

        SelectableText lightningField = new SelectableText("cultivationcraft.gui.help.elements.lightning");
        windField.addItem(lightningField);

        SelectableText iceField = new SelectableText("cultivationcraft.gui.help.elements.ice");
        waterField.addItem(iceField);

        SelectableText skillsField = new SelectableText("cultivationcraft.gui.help.cultivationmenu.skills");
        skillsField.addItem(new SelectableText("cultivationcraft.gui.help.cultivationmenu.skills.assign"));
        skillsField.addItem(new SelectableText("cultivationcraft.gui.help.cultivationmenu.skills.hotbar"));
        skillsField.addItem(new SelectableText("cultivationcraft.gui.help.cultivationmenu.skills.activation"));

        SelectableText keyskillsField = new SelectableText("cultivationcraft.gui.help.cultivationmenu.skills.keyskills");
        skillsField.addItem(keyskillsField);
        keyskillsField.addItem(new SelectableText("cultivationcraft.gui.help.cultivationmenu.skills.keyskills.meditate"));
        keyskillsField.addItem(new SelectableText("cultivationcraft.gui.help.cultivationmenu.skills.keyskills.qisight"));

        SelectableText cultField = new SelectableText("cultivationcraft.gui.help.cultivationmenu.cultivate");
        cultField.addItem(new SelectableText("cultivationcraft.gui.help.cultivationmenu.cultivate.bodyforge.select"));
        cultField.addItem(new SelectableText("cultivationcraft.gui.help.cultivationmenu.cultivate.bodyforge.progress"));
        cultField.addItem(new SelectableText("cultivationcraft.gui.help.cultivationmenu.cultivate.bodyforge.quest"));

        menuField.addItem(statsField);
        menuField.addItem(skillsField);
        menuField.addItem(cultField);

        addText(menuField);
        addText(qiField);

        SelectableText debugField = new SelectableText("cultivationcraft.gui.help.debug");
        SelectableText bfdebugField = new SelectableText("cultivationcraft.gui.help.debug.bodyforge");
        debugField.addItem(bfdebugField);
        bfdebugField.addItem(new SelectableText("cultivationcraft.gui.help.debug.bodyforge.completeForge"));
        bfdebugField.addItem(new SelectableText("cultivationcraft.gui.help.debug.bodyforge.completeQuest"));
        bfdebugField.addItem(new SelectableText("cultivationcraft.gui.help.debug.bodyforge.reset"));

        addOPText(debugField);
    }

    public static void addStatsField(SelectableText newStats)
    {
        statsField.addItem(newStats);
    }

    public static void addElementsField(SelectableText newStats)
    {
        elementsField.addItem(newStats);
    }

    public static void addText(SelectableText field)
    {
        selectText.add(field);
    }

    public static void addOPText(SelectableText field)
    {
        selectOPText.add(field);
    }

    public static ArrayList<SelectableText> getText()
    {
        return selectText;
    }

    public static ArrayList<SelectableText> getOPText()
    {
        return selectOPText;
    }
}
