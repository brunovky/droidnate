package com.brunooliveira.droidnate.validator;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bruno on 21/03/2015.
 */
public class FutureValidator implements Validator {

    @Override
    public boolean validate(Object validatedObj) {
        if (validatedObj == null) return false;
        if (validatedObj.getClass().equals(Calendar.class)) {
            Calendar calendar = (Calendar) validatedObj;
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            return calendar.after(now);
        }
        if (validatedObj.getClass().equals(Date.class)) {
            Date date = (Date) validatedObj;
            Date now = new Date();
            return date.after(now);
        }
        return false;
    }

}