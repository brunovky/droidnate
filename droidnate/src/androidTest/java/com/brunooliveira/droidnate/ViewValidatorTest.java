package com.brunooliveira.droidnate;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.brunooliveira.droidnate.annotations.Length;
import com.brunooliveira.droidnate.exception.DroidnateException;
import com.brunooliveira.droidnate.util.ValidatorUtil;

/**
 * Created by Bruno on 22/03/2015.
 */
public class ViewValidatorTest extends ActivityInstrumentationTestCase2<TestActivity> {

    @Length(min = 5, max = 100)
    private EditText etName;

    public ViewValidatorTest() {
        super(TestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        etName = new EditText(getActivity());
    }

    public void testValidateNullActivity() {
        try {
            ValidatorUtil.validate(null);
            fail("Should return DroidnateException");
        } catch (DroidnateException e) {
            assertEquals("The activity can't be null.", e.getMessage());
        }
    }

    public void testValidateLength() {
        try {
            etName.setText("Test");
            assertFalse(ValidatorUtil.validate(getActivity()));
            assertEquals("etName length can't be lower than 5 and greater than 100", ValidatorUtil.getErrorMessage());
        } catch (DroidnateException e) {
            fail(e.getMessage());
        }
    }

}