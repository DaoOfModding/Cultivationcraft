package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.SelectableText;

public class HelpItems
{
    protected static SelectableTextField selectText = new SelectableTextField();

    protected static SelectableText statsField = new SelectableText("cultivationcraft.gui.help.stats");
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

        addText(elementsField);
        addText(statsField);
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
        selectText.addSelectable(field);
    }

    public static SelectableTextField getText()
    {
        return selectText;
    }
}
