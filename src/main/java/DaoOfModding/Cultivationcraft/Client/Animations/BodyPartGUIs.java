package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Client.GUI.BodyPartGUI;

import java.util.HashMap;

public class BodyPartGUIs
{
    static HashMap<String, BodyPartGUI> guis = new HashMap<String, BodyPartGUI>();

    public static void addGUI(String ID, BodyPartGUI gui)
    {
        guis.put(ID, gui);
    }

    public static BodyPartGUI getGUI(String ID)
    {
        return guis.get(ID);
    }
}
