package com.projetjavaopc.api.tools.password;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@Component
public class PasswordUtil {

    @Value("${site.passkey.first}")
    String passkeyone;

    @Value("${site.passkey.second}")
    String passkeytwo;

    @Value("${site.passkey.third}")
    String passkeythree;

    @Value("${site.passkey.fourth}")
    String passkeyfour;

    //private static final Logger log = LoggerFactory.getLogger(PasswordUtil.class);

    
    public String encryptPassword(String password) {
        
        if (password == null || password.isEmpty()) {
            return null;
        }

        String passwordWithKey = passkeythree + password + passkeyfour;
        String hashedPassword = BCrypt.hashpw(passwordWithKey, BCrypt.gensalt());

        String fullEncodedPassword = passkeytwo + hashedPassword + passkeyone;

        return fullEncodedPassword;
    }

    public boolean checkPassword(String hashedPassword, String password) {
        
        if (password == null || password.isEmpty()) {
            return false;
        }
        
        return BCrypt.checkpw(password, hashedPassword);
    }

    public String removeKeys(String password) {
        String passwordWithoutKey = password.replace(passkeyone, "").replace(passkeytwo, "");

        return passwordWithoutKey;
    }

    public String addKeys(String password) {
        String passwordWithKeys = passkeythree + password + passkeyfour;

        return passwordWithKeys;
    }
    
}
