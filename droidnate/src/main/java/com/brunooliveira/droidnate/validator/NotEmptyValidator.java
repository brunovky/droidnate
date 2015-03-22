package com.brunooliveira.droidnate.validator;

/**
 * Created by Bruno on 21/03/2015.
 */
public class NotEmptyValidator implements Validator<String> {

    @Override
    public boolean validate(String validatedObj) {
        return validatedObj != null && !validatedObj.equals("");
    }

}