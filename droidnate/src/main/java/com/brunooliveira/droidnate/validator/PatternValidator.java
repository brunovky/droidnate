package com.brunooliveira.droidnate.validator;

import java.util.regex.Pattern;

/**
 * Created by Bruno on 21/03/2015.
 */
public class PatternValidator implements Validator<String> {

    private String pattern;

    public PatternValidator(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean validate(String validatedObj) {
        if (validatedObj == null) return false;
        return Pattern.compile(pattern).matcher(validatedObj).matches();
    }

}