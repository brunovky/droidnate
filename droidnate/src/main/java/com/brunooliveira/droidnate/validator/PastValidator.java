package com.brunooliveira.droidnate.validator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Bruno on 21/03/2015.
 */
public class PastValidator implements Validator {

    @Override
    public boolean validate(Object validatedObj) {
        if (validatedObj == null) return false;
        if (validatedObj.getClass().equals(GregorianCalendar.class)) {
            Calendar calendar = (Calendar) validatedObj;
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            return calendar.before(now);
        }
        if (validatedObj.getClass().equals(Date.class)) {
            Date date = (Date) validatedObj;
            Date now = new Date();
            return date.before(now);
        }
        return false;
    }

}