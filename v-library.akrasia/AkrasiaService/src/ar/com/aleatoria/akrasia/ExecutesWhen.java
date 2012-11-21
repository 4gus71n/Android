package ar.com.aleatoria.akrasia;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author astinx
 * This notation is used to simplify the process of message handling, without 
 * this one case statements should be used which could potentially become unreadable.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExecutesWhen {
	int what();
}


