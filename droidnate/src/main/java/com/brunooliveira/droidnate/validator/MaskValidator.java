package com.brunooliveira.droidnate.validator;

/**
 * Created by Bruno on 21/03/2015.
 */
public class MaskValidator implements Validator<String> {

    private String mask;

    public MaskValidator(String mask) {
        this.mask = mask;
    }

    @Override
    public boolean validate(String validatedObj) {
        if (validatedObj == null) return false;
        StringBuilder validatedMask = new StringBuilder();
        for (char c : validatedObj.toCharArray()) {
            if (Character.isLetterOrDigit(c)) validatedMask.append("#");
            else validatedMask.append(c);
        }
        return validatedMask.toString().equals(mask);
    }

}