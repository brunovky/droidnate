package com.brunooliveira.droidnate.validator;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Bruno on 21/03/2015.
 */
public class NotEmptyViewValidator implements ViewValidator {

    @Override
    public boolean validate(View validatedView) {
        if (validatedView == null) return false;
        if (validatedView.getClass().equals(EditText.class)) {
            EditText editText = (EditText) validatedView;
            return !editText.getText().toString().equals("");
        }
        if (validatedView.getClass().equals(TextView.class)) {
            TextView textView = (TextView) validatedView;
            return !textView.getText().toString().equals("");
        }
        return false;
    }

}