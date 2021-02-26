package net.tislib.stateprocessor.spring.base;

import net.tislib.stateprocessor.model.ResourceSpecification;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface ProcessorComponent {
    Class<? extends ResourceSpecification> specClass();

    String kind();
}
