package kr.co.aim.messolution.extended.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**详见：everynote：自定义注解
 * 自定义注解
 * @Target 说明了Annotation所修饰的对象范围 FIELD:用于描述域
 * @Retention 定义了该Annotation被保留的时间长短 RUNTIME:在运行时有效（即运行时保留）
 * @Inherited 元注解是一个标记注解，@Inherited阐述了某个被标注的类型是被继承的。如果一个使用了@Inherited修饰的annotation类型被用于一个class，则这个annotation将被用于该class的子类
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CTORMTemplate
{
    String seq();
    String name();
    String type();
    String dataType();
    String initial();
    String history();
}
