package sia.knights;

public class DamselRescuingKnight implements Knight {

   // private RescueDamselQuest quest;
    private Quest quest;

    /**
     * 基于接口的构造注入：松耦合-不与特定的Quest实现耦合，他们的耦合关系由第三方组件也就是spring容器在创建对象的时候进行设定
     * @param quest
     */
    public DamselRescuingKnight(Quest quest) {
       // this.quest = new RescueDamselQuest();
        this.quest = quest;
    }

    public void embarkOnQuest() {
        quest.embark();
    }

}
