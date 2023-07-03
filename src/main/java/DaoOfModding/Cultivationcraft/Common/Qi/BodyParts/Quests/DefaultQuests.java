package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques.BiteTechnique;

public class DefaultQuests
{
    public static Quest defaultSkinQuest = new Quest(Quest.TIME_ALIVE, 10);
    public static Quest defaultLiveQuest = new Quest(Quest.LIVE, 30);
    public static Quest defaultBodyQuest = new Quest(Quest.DAMAGE_TAKEN, 300);
    public static Quest defaultBoneQuest = new Quest(Quest.DAMAGE_RESISTED, 100);
    public static Quest defaultLegQuest = new Quest(Quest.WALK, 10000);
    public static Quest defaultSwimQuest = new Quest(Quest.SWIM, 10000);
    public static Quest defaultFlightQuest = new Quest(Quest.FLIGHT, 10000);
    public static Quest defaultHealQuest = new Quest(Quest.HEAL, 500);
    public static Quest defaultStaminaQuest = new Quest(Quest.DRAIN_STAMINA, 1000);

    public static void init()
    {
        QuestHandler.addDamageQuest("cultivationcraft.technique.bite", BiteTechnique.biteQuest);
    }
}
