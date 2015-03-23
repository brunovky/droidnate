package com.brunooliveira.droidnate.validator;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by Bruno on 21/03/2015.
 */
public class NoImageViewValidator implements ViewValidator {

    @Override
    public boolean validate(View validatedView) {
        if (validatedView == null) return false;
        if (validatedView.getClass().equals(ImageView.class)) {
            ImageView imageView = (ImageView) validatedView;
            return imageView.getDrawingCache() != null;
        }
        return false;
    }

}