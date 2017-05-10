package soundsystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CDPlayer implements MediaPlayer {
  private CompactDisc cd;

  /**
   * 自动装配就是让Spring自动满足bean依赖的一种方法， 在满足依赖的过程中， 会
   * 在Spring应用上下文中寻找匹配某个bean需求的其他bean
   * @Autowired 注解不仅能够用在构造器上， 还能用在属性的Setter方法上
   * @Autowired 注解可以用在类的任何方法上
   * 不管是构造器、 Setter方法还是其他的方法， Spring都会尝试满足方法参数上所声明的依赖
   * 假如有且只有一个bean匹配依赖需求的话， 那么这个bean将会被装配进来
   * 如果没有匹配的bean， 那么在应用上下文创建的时候， Spring会抛出一个异常。 为了避免异常的出现， 你可以将@Autowired的required属性设置为false
   * 果有多个bean都能满足依赖关系的话， Spring将会抛出一个异常， 表明没有明确指定要选择哪个bean进行自动装配
   * @param cd
   */
  @Autowired
  public CDPlayer(CompactDisc cd) {
    this.cd = cd;
  }

  public void play() {
    cd.play();
  }

}
