package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.SelectableText;

public class HelpItems
{
    protected static SelectableTextField selectText = new SelectableTextField();

    public static void setup()
    {
        SelectableText elementsField = new SelectableText("cultivationcraft.gui.help.elements");
        SelectableText fireField = new SelectableText("cultivationcraft.gui.help.elements.fire");
        SelectableText woodField = new SelectableText("cultivationcraft.gui.help.elements.wood");
        SelectableText windField = new SelectableText("cultivationcraft.gui.help.elements.wind");
        SelectableText earthField = new SelectableText("cultivationcraft.gui.help.elements.earth");
        SelectableText waterField = new SelectableText("cultivationcraft.gui.help.elements.water");
        elementsField.addItem(fireField);
        elementsField.addItem(woodField);
        elementsField.addItem(windField);
        elementsField.addItem(earthField);
        elementsField.addItem(waterField);

        SelectableText lightningField = new SelectableText("cultivationcraft.gui.help.elements.lightning");
        windField.addItem(lightningField);

        SelectableText iceField = new SelectableText("cultivationcraft.gui.help.elements.ice");
        waterField.addItem(iceField);

        addText(elementsField);
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
