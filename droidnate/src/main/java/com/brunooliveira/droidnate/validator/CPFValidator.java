package com.brunooliveira.droidnate.validator;

import java.util.InputMismatchException;

/**
 * Created by Bruno on 21/03/2015.
 */
public class CPFValidator implements Validator<String> {

    @Override
    public boolean validate(String validatedObj) {
        if (validatedObj == null || validatedObj.equals("")) return false;
        return validateCPF(validatedObj);
    }

    private boolean validateCPF(String cpf) {
        cpf = cpf.replace(".", "").replace("-", "");
        if (cpf.equals("00000000000") || cpf.equals("11111111111")
                || cpf.equals("22222222222") || cpf.equals("33333333333")
                || cpf.equals("44444444444") || cpf.equals("55555555555")
                || cpf.equals("66666666666") || cpf.equals("77777777777")
                || cpf.equals("88888888888") || cpf.equals("99999999999")
                || (cpf.length() != 11)) {
            return false;
        }
        char firstDigit, secondDigit;
        int sum, i, r, num, weight;
        try {
            sum = 0;
            weight = 10;
            for (i = 0; i < 9; i++) {
                num = (cpf.charAt(i) - 48);
                sum = sum + (num * weight);
                weight = weight - 1;
            }
            r = 11 - (sum % 11);
            if ((r == 10) || (r == 11)) firstDigit = '0';
            else firstDigit = (char) (r + 48);
            sum = 0;
            weight = 11;
            for (i = 0; i < 10; i++) {
                num = (cpf.charAt(i) - 48);
                sum = sum + (num * weight);
                weight = weight - 1;
            }
            r = 11 - (sum % 11);
            if ((r == 10) || (r == 11)) secondDigit = '0';
            else secondDigit = (char) (r + 48);
            return ((firstDigit == cpf.charAt(9)) && (secondDigit == cpf.charAt(10)));
        } catch (InputMismatchException e) {
            return false;
        }
    }

}