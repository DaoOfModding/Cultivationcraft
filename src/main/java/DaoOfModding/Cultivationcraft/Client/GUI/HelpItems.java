package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.SelectableText;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

public class HelpItems
{
    protected static ArrayList<SelectableText> selectText = new ArrayList<>();
    protected static ArrayList<SelectableText> selectOPText = new ArrayList<>();

    protected static SelectableText menuField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu"));
    protected static SelectableText qimenuField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu"));
    protected static SelectableText statsField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.stats"));
    protected static SelectableText elementsField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.elements"));
    protected static SelectableText qiField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.qi"));

    public static void setup()
    {
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.weight")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.armor")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.armortoughness")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.movementspeed")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.maxhp")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.healthregen")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.healthstaminaconversion")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.maxstamina")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.staminadrain")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.staminause")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.lungcapacity")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.jumpheight")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.fallheight")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.bounceheight")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.swimspeed")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.flightspeed")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.legweight")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.wingweight")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.attackrange")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.armattackmodifier")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.boneattackmodifier")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.biteattackmodifier")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.resistancemodifier")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.width")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.size")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.stepheightmodifier")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.qiabsorb")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.qiabsorbrange")));
        addStatsField(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "stat.qicost")));

        qiField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.qi"));
        qiField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.qi.source")));
        qiField.addItem(elementsField);

        SelectableText fireField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.elements.fire"));
        SelectableText woodField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.elements.wood"));
        SelectableText windField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.elements.wind"));
        SelectableText earthField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.elements.earth"));
        SelectableText waterField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.elements.water"));
        addElementsField(fireField);
        addElementsField(woodField);
        addElementsField(windField);
        addElementsField(earthField);
        addElementsField(waterField);

        SelectableText lightningField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.elements.lightning"));
        windField.addItem(lightningField);

        SelectableText iceField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.elements.ice"));
        waterField.addItem(iceField);

        SelectableText skillsField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.skills"));
        skillsField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.skills.assign")));
        skillsField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.skills.hotbar")));
        skillsField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.skills.activation")));
        skillsField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.skills.level")));

        SelectableText keyskillsField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.skills.keyskills"));
        skillsField.addItem(keyskillsField);
        keyskillsField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.skills.keyskills.meditate")));
        keyskillsField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.skills.keyskills.qisight")));

        SelectableText cultField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.cultivate"));
        cultField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.cultivate.bodyforge.select")));
        cultField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.cultivate.bodyforge.progress")));
        cultField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.cultivate.bodyforge.quest")));

        SelectableText qicultField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.cultivate"));
        qicultField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.cultivate.qicondenser.breakthrough")));
        qicultField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.cultivate.qicondenser.stats")));
        qicultField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.cultivate.qicondenser.reset")));
        qicultField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.cultivate.qicondenser.tribulation")));

        SelectableText bindField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.cultivationmenu.bind"));

        menuField.addItem(statsField);
        menuField.addItem(skillsField);
        menuField.addItem(cultField);

        qimenuField.addItem(qicultField);
        qimenuField.addItem(skillsField);
        qimenuField.addItem(bindField);

        SelectableText debugField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.debug"));
        SelectableText bfdebugField = new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.debug.bodyforge"));
        debugField.addItem(bfdebugField);
        bfdebugField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.debug.bodyforge.completecultivation")));
        bfdebugField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.debug.bodyforge.completequest")));
        bfdebugField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.debug.bodyforge.levelskills")));
        bfdebugField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.debug.bodyforge.reset")));
        bfdebugField.addItem(new SelectableText(new ResourceLocation(Cultivationcraft.MODID, "gui.help.debug.bodyforge.fillqi")));

        addOPText(debugField);
    }

    public static void updateText()
    {
        selectText = new ArrayList<>();

        int cultivationType = CultivatorStats.getCultivatorStats(Minecraft.getInstance().player).getCultivationType();

        if (cultivationType == CultivationTypes.QI_CONDENSER)
        {
            addText(qimenuField);
            addText(qiField);
        }
        else if (cultivationType == CultivationTypes.BODY_CULTIVATOR)
        {
            addText(menuField);
            addText(qiField);
        }
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
