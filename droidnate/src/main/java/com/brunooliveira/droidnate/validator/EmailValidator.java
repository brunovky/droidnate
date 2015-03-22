package com.brunooliveira.droidnate.validator;

import java.util.regex.Pattern;

/**
 * Created by Bruno on 21/03/2015.
 */
public class EmailValidator implements Validator<String> {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean validate(String validatedObj) {
        if (validatedObj == null || validatedObj.equals("")) return false;
        return Pattern.compile(EMAIL_PATTERN).matcher(validatedObj).matches();
    }

}