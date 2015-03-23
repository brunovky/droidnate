package com.brunooliveira.droidnate.util;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.brunooliveira.droidnate.annotations.CPF;
import com.brunooliveira.droidnate.annotations.Email;
import com.brunooliveira.droidnate.annotations.Entity;
import com.brunooliveira.droidnate.annotations.Future;
import com.brunooliveira.droidnate.annotations.Length;
import com.brunooliveira.droidnate.annotations.Mask;
import com.brunooliveira.droidnate.annotations.NoImage;
import com.brunooliveira.droidnate.annotations.NotChecked;
import com.brunooliveira.droidnate.annotations.NotEmpty;
import com.brunooliveira.droidnate.annotations.NotNull;
import com.brunooliveira.droidnate.annotations.NotSelected;
import com.brunooliveira.droidnate.annotations.Past;
import com.brunooliveira.droidnate.annotations.Pattern;
import com.brunooliveira.droidnate.annotations.Validator;
import com.brunooliveira.droidnate.annotations.ViewValidator;
import com.brunooliveira.droidnate.exception.DroidnateException;
import com.brunooliveira.droidnate.validator.CPFValidator;
import com.brunooliveira.droidnate.validator.EmailValidator;
import com.brunooliveira.droidnate.validator.FutureValidator;
import com.brunooliveira.droidnate.validator.LengthValidator;
import com.brunooliveira.droidnate.validator.LengthViewValidator;
import com.brunooliveira.droidnate.validator.MaskValidator;
import com.brunooliveira.droidnate.validator.MaskViewValidator;
import com.brunooliveira.droidnate.validator.NoImageViewValidator;
import com.brunooliveira.droidnate.validator.NotCheckedViewValidator;
import com.brunooliveira.droidnate.validator.NotEmptyValidator;
import com.brunooliveira.droidnate.validator.NotEmptyViewValidator;
import com.brunooliveira.droidnate.validator.NotNullValidator;
import com.brunooliveira.droidnate.validator.NotSelectedViewValidator;
import com.brunooliveira.droidnate.validator.PastValidator;
import com.brunooliveira.droidnate.validator.PatternValidator;
import com.brunooliveira.droidnate.validator.PatternViewValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bruno on 21/03/2015.
 */
public final class ValidatorUtil {

    private static String errorMessage;

    public static boolean validate(Object entity) throws DroidnateException {
        if (entity == null) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The entity can't be null.");
        if (!entity.getClass().isAnnotationPresent(Entity.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The class [" + entity.getClass().getSimpleName() + "] isn't an entity. Please, use @Entity annotation in this class.");
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                try {
                    if (annotation.getClass().equals(CPF.class)) {
                        boolean result = validateCPF(field, entity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(Email.class)) {
                        boolean result = validateEmail(field, entity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(Future.class)) {
                        boolean result = validateFuture(field, entity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(Length.class)) {
                        boolean result = validateLength(field, entity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(Mask.class)) {
                        boolean result = validateMask(field, entity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(NotEmpty.class)) {
                        boolean result = validateNotEmpty(field, entity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(NotNull.class)) {
                        boolean result = validateNotNull(field, entity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(Past.class)) {
                        boolean result = validatePast(field, entity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(Pattern.class)) {
                        boolean result = validatePattern(field, entity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(Validator.class)) {
                        boolean result = validateValidator(field, entity);
                        if (!result) return false;
                    }
                } catch (Exception e) {
                    throw new DroidnateException(ValidatorUtil.class.getSimpleName(), e);
                }
            }
        }
        return true;
    }

    public static boolean validate(Activity activity) throws DroidnateException {
        if (activity == null) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The activity can't be null.");
        Field[] fields = activity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                try {
                    if (annotation.getClass().equals(Length.class)) {
                        boolean result = validateViewLength(field, activity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(Mask.class)) {
                        boolean result = validateViewMask(field, activity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(NoImage.class)) {
                        boolean result = validateNoImage(field, activity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(NotChecked.class)) {
                        boolean result = validateNotChecked(field, activity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(NotEmpty.class)) {
                        boolean result = validateViewNotEmpty(field, activity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(NotSelected.class)) {
                        boolean result = validateNotSelected(field, activity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(Pattern.class)) {
                        boolean result = validateViewPattern(field, activity);
                        if (result) continue;
                        else return false;
                    }
                    if (annotation.getClass().equals(ViewValidator.class)) {
                        boolean result = validateViewValidator(field, activity);
                        if (!result) return false;
                    }
                } catch (Exception e) {
                    throw new DroidnateException(ValidatorUtil.class.getSimpleName(), e);
                }
            }
        }
        return true;
    }

    public static String getErrorMessage() {
        return errorMessage;
    }

    private static boolean validateCPF(Field field, Object entity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(String.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @CPF Annotation needs a field of String type.");
        CPFValidator validator = new CPFValidator();
        boolean valid = validator.validate(getAsString(field, entity));
        if (!valid) errorMessage = field.getAnnotation(CPF.class).errorMessage();
        return valid;
    }

    private static boolean validateEmail(Field field, Object entity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(String.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @Email Annotation needs a field of String type.");
        EmailValidator validator = new EmailValidator();
        boolean valid = validator.validate(getAsString(field, entity));
        if (!valid) errorMessage = field.getAnnotation(Email.class).errorMessage();
        return valid;
    }

    private static boolean validateFuture(Field field, Object entity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(Date.class) && !field.getType().equals(Calendar.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @Future Annotation needs a field of Date or Calendar type.");
        FutureValidator validator = new FutureValidator();
        boolean valid = validator.validate(field.get(entity));
        if (!valid) errorMessage = field.getAnnotation(Future.class).errorMessage().replace("{0}", field.getName());
        return valid;
    }

    private static boolean validateLength(Field field, Object entity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(String.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @Length Annotation needs a field of String type.");
        Length length = field.getAnnotation(Length.class);
        LengthValidator validator = new LengthValidator(length.min(), length.max());
        boolean valid = validator.validate(getAsString(field, entity));
        if (!valid) errorMessage = length.errorMessage().replace("{0}", field.getName())
                .replace("{1}", String.valueOf(length.min()))
                .replace("{2}", String.valueOf(length.max()));
        return valid;
    }

    private static boolean validateMask(Field field, Object entity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(String.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @Mask Annotation needs a field of String type.");
        Mask mask = field.getAnnotation(Mask.class);
        MaskValidator validator = new MaskValidator(mask.mask());
        boolean valid = validator.validate(getAsString(field, entity));
        if (!valid) errorMessage = mask.errorMessage();
        return valid;
    }

    private static boolean validateNotNull(Field field, Object entity) throws IllegalAccessException {
        NotNullValidator validator = new NotNullValidator();
        boolean valid = validator.validate(field.get(entity));
        if (!valid) errorMessage = field.getAnnotation(NotNull.class).errorMessage().replace("{0}", field.getName());
        return valid;
    }

    private static boolean validateNotEmpty(Field field, Object entity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(String.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @NotEmpty Annotation needs a field of String type.");
        NotEmptyValidator validator = new NotEmptyValidator();
        boolean valid = validator.validate(getAsString(field, entity));
        if (!valid) errorMessage = field.getAnnotation(NotEmpty.class).errorMessage().replace("{0}", field.getName());
        return valid;
    }

    private static boolean validatePast(Field field, Object entity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(String.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @Past Annotation needs a field of Date or Calendar type.");
        PastValidator validator = new PastValidator();
        boolean valid = validator.validate(field.get(entity));
        if (!valid) errorMessage = field.getAnnotation(Past.class).errorMessage().replace("{0}", field.getName());
        return valid;
    }

    private static boolean validatePattern(Field field, Object entity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(String.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @Pattern Annotation needs a field of String type.");
        Pattern pattern = field.getAnnotation(Pattern.class);
        PatternValidator validator = new PatternValidator(pattern.regex());
        boolean valid = validator.validate(getAsString(field, entity));
        if (!valid) errorMessage = pattern.errorMessage();
        return valid;
    }

    private static boolean validateValidator(Field field, Object entity) throws IllegalAccessException, InstantiationException {
        Validator validatorAnnot = field.getAnnotation(Validator.class);
        com.brunooliveira.droidnate.validator.Validator validator = validatorAnnot.validatorClass().newInstance();
        boolean valid = validator.validate(field.get(entity));
        if (!valid) errorMessage = validatorAnnot.errorMessage();
        return valid;
    }

    private static boolean validateViewLength(Field field, Activity activity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(EditText.class) && !field.getType().equals(TextView.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @Length Annotation needs a field of EditText or TextView type.");
        Length length = field.getAnnotation(Length.class);
        LengthViewValidator validator = new LengthViewValidator(length.min(), length.max());
        boolean valid = validator.validate((android.view.View) field.get(activity));
        if (!valid) errorMessage = length.errorMessage().replace("{0}", field.getName())
                .replace("{1}", String.valueOf(length.min()))
                .replace("{2}", String.valueOf(length.max()));
        return valid;
    }

    private static boolean validateViewMask(Field field, Activity activity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(EditText.class) && !field.getType().equals(TextView.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @Mask Annotation needs a field of EditText or TextView type.");
        Mask mask = field.getAnnotation(Mask.class);
        MaskViewValidator validator = new MaskViewValidator(mask.mask());
        boolean valid = validator.validate((android.view.View) field.get(activity));
        if (!valid) errorMessage = mask.errorMessage();
        return valid;
    }

    private static boolean validateNoImage(Field field, Activity activity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(ImageView.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @NoImage Annotation needs a field of ImageView type.");
        NoImageViewValidator validator = new NoImageViewValidator();
        boolean valid = validator.validate((android.view.View) field.get(activity));
        if (!valid) errorMessage = field.getAnnotation(NoImage.class).errorMessage().replace("{0}", field.getName());
        return valid;
    }

    private static boolean validateNotChecked(Field field, Activity activity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(CheckBox.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @NotChecked Annotation needs a field of CheckBox type.");
        NotCheckedViewValidator validator = new NotCheckedViewValidator();
        boolean valid = validator.validate((android.view.View) field.get(activity));
        if (!valid) errorMessage = field.getAnnotation(NotChecked.class).errorMessage().replace("{0}", field.getName());
        return valid;
    }

    private static boolean validateViewNotEmpty(Field field, Activity activity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(EditText.class) && !field.getType().equals(TextView.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @NotEmpty Annotation needs a field of EditText or TextView type.");
        NotEmptyViewValidator validator = new NotEmptyViewValidator();
        boolean valid = validator.validate((android.view.View) field.get(activity));
        if (!valid) errorMessage = field.getAnnotation(NotEmpty.class).errorMessage().replace("{0}", field.getName());
        return valid;
    }

    private static boolean validateNotSelected(Field field, Activity activity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(Spinner.class) && !field.getType().equals(RadioGroup.class) && !field.getType().equals(RadioButton.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @NotSelected Annotation needs a field of Spinner, RadioGroup or RadioButton type.");
        NotSelectedViewValidator validator = new NotSelectedViewValidator();
        boolean valid = validator.validate((android.view.View) field.get(activity));
        if (!valid) errorMessage = field.getAnnotation(NotSelected.class).errorMessage().replace("{0}", field.getName());
        return valid;
    }

    private static boolean validateViewPattern(Field field, Activity activity) throws IllegalAccessException, DroidnateException {
        if (!field.getType().equals(EditText.class) && !field.getType().equals(TextView.class)) throw new DroidnateException(ValidatorUtil.class.getSimpleName(), "The @Pattern Annotation needs a field of EditText or TextView type.");
        Pattern pattern = field.getAnnotation(Pattern.class);
        PatternViewValidator validator = new PatternViewValidator(pattern.regex());
        boolean valid = validator.validate((android.view.View) field.get(activity));
        if (!valid) errorMessage = pattern.errorMessage();
        return valid;
    }

    private static boolean validateViewValidator(Field field, Activity activity) throws IllegalAccessException, InstantiationException {
        ViewValidator validatorAnnot = field.getAnnotation(ViewValidator.class);
        com.brunooliveira.droidnate.validator.ViewValidator validator = validatorAnnot.validatorClass().newInstance();
        boolean valid = validator.validate((android.view.View) field.get(activity));
        if (!valid) errorMessage = validatorAnnot.errorMessage();
        return valid;
    }

    private static String getAsString(Field field, Object entity) throws IllegalAccessException {
        return field.get(entity) != null ? field.get(entity).toString() : null;
    }

}