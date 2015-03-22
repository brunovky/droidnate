package com.brunooliveira.droidnate.validator;

/**
 * Created by Bruno on 21/03/2015.
 */
public class NotNullValidator implements Validator {

    @Override
    public boolean validate(Object validatedObj) {
        return validatedObj != null;
    }

}