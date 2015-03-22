package com.brunooliveira.droidnate.validator;

/**
 * Created by Bruno on 21/03/2015.
 */
public class LengthValidator implements Validator<String> {

    private int min;
    private int max;

    public LengthValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean validate(String validatedObj) {
        if (validatedObj == null) return false;
        return validatedObj.length() >= min && validatedObj.length() <= max;
    }

}