package DaoOfModding.Cultivationcraft.Client.GUI;

import java.util.ArrayList;
import java.util.HashMap;

public class BodyPartGUIs
{
    static HashMap<String, ArrayList<BodyPartGUI>> guis = new HashMap<String, ArrayList<BodyPartGUI>>();

    public static void addGUI(String ID, BodyPartGUI gui)
    {
        if (!guis.containsKey(ID))
            guis.put(ID, new ArrayList<BodyPartGUI>());

        guis.get(ID).add(gui);
    }

    public static ArrayList<BodyPartGUI> getGUI(String ID)
    {
        return guis.get(ID);
    }
}
