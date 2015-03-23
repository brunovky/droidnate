package com.brunooliveira.droidnate.validator;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Bruno on 21/03/2015.
 */
public class LengthViewValidator implements ViewValidator {

    private int min;
    private int max;

    public LengthViewValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean validate(View validatedView) {
        if (validatedView == null) return false;
        if (validatedView.getClass().equals(EditText.class)) {
            EditText editText = (EditText) validatedView;
            return editText.getText().toString().length() >= min && editText.getText().toString().length() <= max;
        }
        if (validatedView.getClass().equals(TextView.class)) {
            TextView textView = (TextView) validatedView;
            return textView.getText().toString().length() >= min && textView.getText().toString().length() <= max;
        }
        return false;
    }

}