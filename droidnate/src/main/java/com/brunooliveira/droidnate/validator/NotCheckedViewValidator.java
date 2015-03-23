package com.brunooliveira.droidnate.validator;

import android.view.View;
import android.widget.CheckBox;

/**
 * Created by Bruno on 21/03/2015.
 */
public class NotCheckedViewValidator implements ViewValidator {

    @Override
    public boolean validate(View validatedView) {
        if (validatedView == null) return false;
        if (validatedView.getClass().equals(CheckBox.class)) {
            CheckBox checkBox = (CheckBox) validatedView;
            return checkBox.isChecked();
        }
        return false;
    }

}