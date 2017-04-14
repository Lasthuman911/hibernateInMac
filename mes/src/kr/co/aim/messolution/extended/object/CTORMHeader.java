package kr.co.aim.messolution.extended.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TYPE:用于描述类、接口(包括注解类型) 或enum声明
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CTORMHeader
{
    String tag();
    String divider();
}

