package com.brunooliveira.droidnate.validator;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

/**
 * Created by Bruno on 21/03/2015.
 */
public class NotSelectedViewValidator implements ViewValidator {

    @Override
    public boolean validate(View validatedView) {
        if (validatedView == null) return false;
        if (validatedView.getClass().equals(Spinner.class)) {
            Spinner spinner = (Spinner) validatedView;
            return spinner.getSelectedItem() != null;
        }
        if (validatedView.getClass().equals(RadioGroup.class)) {
            RadioGroup radioGroup = (RadioGroup) validatedView;
            return radioGroup.getCheckedRadioButtonId() > 0;
        }
        if (validatedView.getClass().equals(RadioButton.class)) {
            RadioButton radioButton = (RadioButton) validatedView;
            return radioButton.isChecked();
        }
        return false;
    }

}