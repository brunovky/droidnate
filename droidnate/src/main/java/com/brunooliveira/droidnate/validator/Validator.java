package com.brunooliveira.droidnate.validator;

/**
 * Created by Bruno on 21/03/2015.
 */
public interface Validator<T> {

    boolean validate(T validatedObj);

}