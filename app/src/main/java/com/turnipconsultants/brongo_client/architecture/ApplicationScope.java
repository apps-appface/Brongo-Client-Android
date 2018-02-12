package com.turnipconsultants.brongo_client.architecture;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by mohit on 25-01-2018.
 */

/*Create scope for global objcet. Same functionality as singleton but more expressive name*/
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationScope {
}
