package com.brunooliveira.droidnate;

import com.brunooliveira.droidnate.validator.Validator;

/**
 * Created by Bruno on 22/03/2015.
 */
public class NotZeroValidator implements Validator<Integer> {

    @Override
    public boolean validate(Integer validatedObj) {
        return validatedObj > 0;
    }

}