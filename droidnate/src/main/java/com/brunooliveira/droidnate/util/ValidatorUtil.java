package com.brunooliveira.droidnate.util;

import com.brunooliveira.droidnate.annotations.CPF;
import com.brunooliveira.droidnate.annotations.Email;
import com.brunooliveira.droidnate.annotations.Entity;
import com.brunooliveira.droidnate.annotations.Future;
import com.brunooliveira.droidnate.annotations.Length;
import com.brunooliveira.droidnate.annotations.Mask;
import com.brunooliveira.droidnate.annotations.NotEmpty;
import com.brunooliveira.droidnate.annotations.NotNull;
import com.brunooliveira.droidnate.annotations.Past;
import com.brunooliveira.droidnate.annotations.Pattern;
import com.brunooliveira.droidnate.annotations.Validator;
import com.brunooliveira.droidnate.exception.DroidnateException;
import com.brunooliveira.droidnate.validator.CPFValidator;
import com.brunooliveira.droidnate.validator.EmailValidator;
import com.brunooliveira.droidnate.validator.FutureValidator;
import com.brunooliveira.droidnate.validator.LengthValidator;
import com.brunooliveira.droidnate.validator.MaskValidator;
import com.brunooliveira.droidnate.validator.NotEmptyValidator;
import com.brunooliveira.droidnate.validator.NotNullValidator;
import com.brunooliveira.droidnate.validator.PastValidator;
import com.brunooliveira.droidnate.validator.PatternValidator;

import java.lang.reflect.Field;

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
            try {
                if (field.isAnnotationPresent(CPF.class)) {
                    boolean result = validateCPF(field, entity);
                    if (result) continue;
                    else return false;
                }
                if (field.isAnnotationPresent(Email.class)) {
                    boolean result = validateEmail(field, entity);
                    if (result) continue;
                    else return false;
                }
                if (field.isAnnotationPresent(Future.class)) {
                    boolean result = validateFuture(field, entity);
                    if (result) continue;
                    else return false;
                }
                if (field.isAnnotationPresent(Length.class)) {
                    boolean result = validateLength(field, entity);
                    if (result) continue;
                    else return false;
                }
                if (field.isAnnotationPresent(Mask.class)) {
                    boolean result = validateMask(field, entity);
                    if (result) continue;
                    else return false;
                }
                if (field.isAnnotationPresent(NotEmpty.class)) {
                    boolean result = validateNotEmpty(field, entity);
                    if (result) continue;
                    else return false;
                }
                if (field.isAnnotationPresent(NotNull.class)) {
                    boolean result = validateNotNull(field, entity);
                    if (result) continue;
                    else return false;
                }
                if (field.isAnnotationPresent(Past.class)) {
                    boolean result = validatePast(field, entity);
                    if (result) continue;
                    else return false;
                }
                if (field.isAnnotationPresent(Pattern.class)) {
                    boolean result = validatePattern(field, entity);
                    if (result) continue;
                    else return false;
                }
                if (field.isAnnotationPresent(Validator.class)) {
                    boolean result = validateValidator(field, entity);
                    if (!result) return false;
                }
            } catch (Exception e) {
                throw new DroidnateException(ValidatorUtil.class.getSimpleName(), e);
            }
        }
        return true;
    }

    public static String getErrorMessage() {
        return errorMessage;
    }

    private static boolean validateCPF(Field field, Object entity) throws IllegalAccessException {
        CPFValidator validator = new CPFValidator();
        boolean valid = validator.validate(getAsString(field, entity));
        if (!valid) errorMessage = field.getAnnotation(CPF.class).errorMessage();
        return valid;
    }

    private static boolean validateEmail(Field field, Object entity) throws IllegalAccessException {
        EmailValidator validator = new EmailValidator();
        boolean valid = validator.validate(getAsString(field, entity));
        if (!valid) errorMessage = field.getAnnotation(Email.class).errorMessage();
        return valid;
    }

    private static boolean validateFuture(Field field, Object entity) throws IllegalAccessException {
        FutureValidator validator = new FutureValidator();
        boolean valid = validator.validate(field.get(entity));
        if (!valid) errorMessage = field.getAnnotation(Future.class).errorMessage().replace("{0}", field.getName());
        return valid;
    }

    private static boolean validateLength(Field field, Object entity) throws IllegalAccessException {
        Length length = field.getAnnotation(Length.class);
        LengthValidator validator = new LengthValidator(length.min(), length.max());
        boolean valid = validator.validate(getAsString(field, entity));
        if (!valid) errorMessage = length.errorMessage().replace("{0}", field.getName())
                .replace("{1}", String.valueOf(length.min()))
                .replace("{2}", String.valueOf(length.max()));
        return valid;
    }

    private static boolean validateMask(Field field, Object entity) throws IllegalAccessException {
        Mask mask = field.getAnnotation(Mask.class);
        MaskValidator validator = new MaskValidator(mask.mask());
        boolean valid = validator.validate(getAsString(field, entity));
        if (!valid) errorMessage = mask.errorMessage();
        return valid;
    }

    private static boolean validateNotNull(Field field, Object entity) throws IllegalAccessException {
        NotNullValidator validator = new NotNullValidator();
        boolean valid = validator.validate(getAsString(field, entity));
        if (!valid) errorMessage = field.getAnnotation(NotNull.class).errorMessage().replace("{0}", field.getName());
        return valid;
    }

    private static boolean validateNotEmpty(Field field, Object entity) throws IllegalAccessException {
        NotEmptyValidator validator = new NotEmptyValidator();
        boolean valid = validator.validate(getAsString(field, entity));
        if (!valid) errorMessage = field.getAnnotation(NotEmpty.class).errorMessage().replace("{0}", field.getName());
        return valid;
    }

    private static boolean validatePast(Field field, Object entity) throws IllegalAccessException {
        PastValidator validator = new PastValidator();
        boolean valid = validator.validate(field.get(entity));
        if (!valid) errorMessage = field.getAnnotation(Past.class).errorMessage().replace("{0}", field.getName());
        return valid;
    }

    private static boolean validatePattern(Field field, Object entity) throws IllegalAccessException {
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

    private static String getAsString(Field field, Object entity) throws IllegalAccessException {
        return field.get(entity) != null ? field.get(entity).toString() : null;
    }

}