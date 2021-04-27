package com.maxistar.textpad;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingService
{
    // List of Keys

    public static final String SETTING_FONT = "font";
    public static final String SETTING_LAST_FILENAME = "last_filename";
    public static final String SETTING_AUTO_SAVE_CURRENT_FILE = "auto_save_current_file";
    public static final String SETTING_OPEN_LAST_FILE = "open_last_file";
    public static final String SETTING_DELIMITERS = "delimeters";
    public static final String SETTING_FILE_ENCODING = "encoding";
    public static final String SETTING_AUTOSAVE = "autosave";
    public static final String SETTING_FONT_SIZE = "fontsize";
    public static final String SETTING_BG_COLOR = "bgcolor";
    public static final String SETTING_FONT_COLOR = "fontcolor";
    public static final String SETTING_LANGUAGE = "language";

    public static final String SETTING_MEDIUM = "Medium";
    public static final String SETTING_EXTRA_SMALL = "Extra Small";
    public static final String SETTING_SMALL = "Small";
    public static final String SETTING_LARGE = "Large";
    public static final String SETTING_HUGE = "Huge";

    // for selection of encoding

    public static final String Setting_Select_Encoding="Setting_select_encoding";

    // for selection of encoding


    // variable


    private boolean open_last_file = true;
    private boolean autosave = true;

    private String file_encoding = "";
    private String last_filename = "";
    private String delimiters;
    private String font;
    private String font_size;
    private String language;
    private int bgcolor;
    private int fontcolor;

    private int Select_Encoding;

    private static boolean languageWasChanged = false;

    // for data loading when anything is changed from Preference
    private void LoadData(Context context)
    {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);

        // auto save functionality is not there
        autosave=preferences.getBoolean(SETTING_AUTOSAVE,false);

        // open last file
        open_last_file=preferences.getBoolean(SETTING_OPEN_LAST_FILE,false);

        //last file name
        last_filename=preferences.getString(SETTING_LAST_FILENAME,TPStrings.EMPTY);

        // file encoding
        file_encoding=preferences.getString(SETTING_FILE_ENCODING,TPStrings.UTF_8);

        // Delimeters
        // Now we had used string arrays so it is predefined
        // so according to entry and its value it gonna alter by itself
        // so we can fetch the data from key which is mentioned in preference
        delimiters=preferences.getString(SETTING_DELIMITERS,TPStrings.EMPTY);

        //Font
        font=preferences.getString(SETTING_FONT,TPStrings.FONT_SANS_SERIF);

        // Font Size
        font_size=preferences.getString(SETTING_FONT_SIZE,SETTING_MEDIUM);

        // BG COLOR
        bgcolor=preferences.getInt(SETTING_BG_COLOR,0xFFCCCCCC);

        //Font Color
        fontcolor=preferences.getInt(SETTING_FONT_COLOR,0xFF000000);

        // LANGUAGE
        language=preferences.getString(SETTING_LANGUAGE,TPStrings.EMPTY);

        // selection for Encoding
        Select_Encoding=preferences.getInt(Setting_Select_Encoding,0);

    }

    // reload the Preference

    public void read(Context context)
    {
        LoadData(context);
    }

    // constructor
    public SettingService(Context context)
    {
        LoadData(context);
    }

    // At an Instance Loading Data
    public static SettingService Instance(Context context)
    {
        return (new SettingService(context));
    }


    //-----  return the default values ----------

    // for selecting Encoding

    public int Selection()
    {
       return Select_Encoding;
    }


    // Auto Save
    public Boolean Auto_Save()
    {
        return autosave;
    }

    // Open Last File
    public Boolean Last_File()
    {
        return open_last_file;
    }

    // Last File Name
    public String Last_Name()
    {
        return last_filename;
    }

    // File Encoding
    public String Encoding()
    {
        return file_encoding;
    }

    // Delimeter
    public String Delimeter()
    {
        return delimiters;
    }

    // font
    public String Font()
    {
        return font;
    }

    //Font Size
    public String FontSize()
    {
        return font_size;
    }

    // Font BG Color
    public int BgColor()
    {
        return bgcolor;
    }

    // Font Color

    public int FontColor()
    {
        return fontcolor;
    }

    //Language
    public String Language()
    {
        return language;
    }

    // ---------  set and edit -----------------

    // set

    public void EditDelimeter(String delimeter,Context context)
    {
        setValue(SETTING_DELIMITERS,delimeter,context);
    }


    public void EditBgColor(int Color,Context context)
    {
        setValue(SETTING_BG_COLOR,Color,context);
    }

    public void EditFontColor(int Color,Context context)
    {
        setValue(SETTING_FONT_COLOR,Color,context);
    }

    public void EditFontSize(String Size,Context context)
    {
        setValue(SETTING_FONT_SIZE,Size,context);
    }

    public void EditLastFileName(String Path,Context context)
    {
        setValue(SETTING_LAST_FILENAME,Path,context);
    }

    public void EditFont(String name,Context context)
    {
        setValue(SETTING_FONT,name,context);
    }

    public void EditEncoding(String name,Context context)
    {
        setValue(SETTING_FILE_ENCODING,name,context);
    }

    public void EditSelection(int select,Context context)
    {
        setValue(Setting_Select_Encoding,select,context);
    }


    // Now We are not Gonna making the Function of Last file to open or not
    // Since the nature of Checkbox is When It gets Clecked then Key holding it
    // directly set to True and not checked then false



    //  edit
    private void setValue(String key,String value,Context context)
    {
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    private void setValue(String key,int value,Context context)
    {
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }
    private void setValue(String key,Boolean value,Context context)
    {
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    // Locate in Java for Language Setting is Remaining Lets prefer User System Default Language i.e English


}


















