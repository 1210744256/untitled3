package com.example.a20;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
//运行期间生效
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {
}
