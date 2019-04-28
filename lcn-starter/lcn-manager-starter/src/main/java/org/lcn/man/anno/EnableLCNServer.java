package org.lcn.man.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.lcn.man.TxmanagerStarter;
import org.springframework.context.annotation.Import;

@Import(TxmanagerStarter.class)
@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableLCNServer {

}
