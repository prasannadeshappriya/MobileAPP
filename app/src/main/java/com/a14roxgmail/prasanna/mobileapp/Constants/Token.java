package com.a14roxgmail.prasanna.mobileapp.Constants;

/**
 * Created by Prasanna Deshappriya on 1/16/2017.
 */
public class Token {
    //Store the token
    private static String token;

    public static void setToken(String token){
        Token.token = token;
    }

    public static String getToken(){
        return Token.token;
    }
}
