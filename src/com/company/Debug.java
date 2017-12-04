package com.company;

import java.util.Objects;

/**
 * Created by Recreational on 11/27/2017.
 */
public class Debug {
    /////////////////////
    //Variables
    ////////////////////


    /////////////////////
    //Enums
    ////////////////////
    public enum Level{
        everyone,
        user,
        info,
        warning,
        error
    }


    /////////////////////
    //Functions and methods
    ////////////////////
    public static void Log(String message){
        Log(message, Level.everyone);
    }

    public static void Log(String message, Object callingClass){
        Log(message, Level.everyone, callingClass);
    }
    public static void Log(String message, Level debugLevel){
        System.out.println(message);
    }

    public static void Log(String message, Level debugLevel, Object callingClass){
        String newString = "[" + callingClass.getClass().getSimpleName() + "] : " + message;
        Log(newString, debugLevel);
    }
}
