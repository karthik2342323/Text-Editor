package com.maxistar.textpad;

/*
 Delimeter is for opening file with specific space
 so that string did not get messed

 */

public class delimeter
{
    public static final String RN = "\r\n";
    public static final String N = "\n";
    public static final String R = "\r";
    public static final String DEFAULT = "default";
    public static final String WINDOWS = "windows";
    public static final String UNIX = "unix";
    public static final String MACOS = "macos";

    public static delimeter getInstance()
    {
        return (new delimeter());
    }

    // processing function which apply at the end of string

    public String applyEndings(String value, String to) {

        if (WINDOWS.equals(to)){
            value = value.replace(RN, N);
            value = value.replace(R, N);
            value = value.replace(N, RN); //simply replace unix endings to win endings
            return value;
        }

        if (UNIX.equals(to)){ //just in case it was previously read as other encoding
            value = value.replace(RN, N);
            value = value.replace(R, N);
            return value;
        }

        if (MACOS.equals(to)){
            value = value.replace(RN, N);
            value = value.replace(R, N);
            value = value.replace(N, R); //simply replace unix endings to mac endings
            return value;
        }

        return value; //leave as is

    }
}
