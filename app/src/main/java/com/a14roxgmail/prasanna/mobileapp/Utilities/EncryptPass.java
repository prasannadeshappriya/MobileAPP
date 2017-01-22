package com.a14roxgmail.prasanna.mobileapp.Utilities;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class EncryptPass {
    private static final String key = "UNBREAKABLE";

    public static String decrypt(String value){
        StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
        decryptor.setPassword(key);
        return decryptor.decrypt(value);
    }

    public static String encrypt(String value){
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(key);
        String encryptedPassword = encryptor.encrypt(value);
        return encryptedPassword;
    }
}
