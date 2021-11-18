package com.be88be.example.common.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Scope(SCOPE_PROTOTYPE)
public @interface MainVerticle {
}
