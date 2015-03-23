package com.brunooliveira.droidnate.validator;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Bruno on 21/03/2015.
 */
public class MaskViewValidator implements ViewValidator {

    private String mask;

    public MaskViewValidator(String mask) {
        this.mask = mask;
    }

    @Override
    public boolean validate(View validatedView) {
        if (validatedView == null) return false;
        StringBuilder validatedMask = new StringBuilder();
        if (validatedView.getClass().equals(EditText.class)) {
            EditText editText = (EditText) validatedView;
            for (char c : editText.getText().toString().toCharArray()) {
                if (Character.isLetterOrDigit(c)) validatedMask.append("#");
                else validatedMask.append(c);
            }
            return validatedMask.toString().equals(mask);
        }
        if (validatedView.getClass().equals(TextView.class)) {
            TextView textView = (TextView) validatedView;
            for (char c : textView.getText().toString().toCharArray()) {
                if (Character.isLetterOrDigit(c)) validatedMask.append("#");
                else validatedMask.append(c);
            }
            return validatedMask.toString().equals(mask);
        }
        return false;
    }

}