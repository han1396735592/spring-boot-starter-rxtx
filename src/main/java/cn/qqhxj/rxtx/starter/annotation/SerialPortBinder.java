package cn.qqhxj.rxtx.starter.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SerialPortBinder {

    @AliasFor("alias")
    String value() default "";

    @AliasFor("value")
    String alias() default "";
}
