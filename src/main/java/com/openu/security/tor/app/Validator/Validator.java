package com.openu.security.tor.app.Validator;

import org.apache.commons.validator.routines.UrlValidator;

public class Validator {

    public static boolean isUrlValid(String url) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }
}
