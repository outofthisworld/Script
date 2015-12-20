/*
 * Copyright (c) 2015. Dale Appleby
 */

package Util;

import java.lang.annotation.*;

/**
 * Created by Unknown on 20/12/2015.
 */

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Documented
@Retention(RetentionPolicy.CLASS)
public @interface NotNull {
}
