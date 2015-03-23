package com.brunooliveira.droidnate.validator;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * Created by Bruno on 21/03/2015.
 */
public class PatternViewValidator implements ViewValidator {

    private String pattern;

    public PatternViewValidator(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean validate(View validatedView) {
        if (validatedView == null) return false;
        if (validatedView.getClass().equals(EditText.class)) {
            EditText editText = (EditText) validatedView;
            return Pattern.compile(pattern).matcher(editText.getText().toString()).matches();
        }
        if (validatedView.getClass().equals(TextView.class)) {
            TextView textView = (TextView) validatedView;
            return Pattern.compile(pattern).matcher(textView.getText().toString()).matches();
        }
        return false;
    }

}