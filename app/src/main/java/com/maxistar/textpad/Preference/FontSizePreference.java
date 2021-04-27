package com.maxistar.textpad.Preference;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.maxistar.textpad.SettingService;
import com.maxistar.textpad.TPStrings;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class FontSizePreference extends DialogPreference
{

    SettingService settingService;

    int CurrentSelected,PreviousSelected;

    // previous selected Size of font



    // This one is an Constructor which is called by Inflator while loading Preference Since
    // Path is Given
    public FontSizePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        settingService =SettingService.Instance(context);

        // get previous selected one's

        String s=settingService.FontSize();

        if(s.equals(SettingService.SETTING_EXTRA_SMALL))
        {
            PreviousSelected=0;
        }
        if(s.equals(SettingService.SETTING_SMALL))
        {
            PreviousSelected=1;
        }
        if(s.equals(SettingService.SETTING_MEDIUM))
        {
            PreviousSelected=2;
        }
        if(s.equals(SettingService.SETTING_LARGE))
        {
            PreviousSelected=3;
        }
        if(s.equals(SettingService.SETTING_HUGE))
        {
            PreviousSelected=4;
        }

        // NOW WE KNOW WHAT IS PREVIOUS SIZE

        // If user clicked ok without Selecting any Size in adaptor
        // So at That point of time CurrentSelected is not holding anything
        // so we gonna make both Same

        CurrentSelected=PreviousSelected;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        //super.onPrepareDialogBuilder(builder);
        builder.setTitle("Set Font Size");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {


              if(CurrentSelected!=PreviousSelected)
              {
                  // edit text Size according to user Selected
                  if(CurrentSelected==0)
                  {
                      settingService.EditFontSize(SettingService.SETTING_EXTRA_SMALL,getContext());
                  }
                  if(CurrentSelected==1)
                  {
                      settingService.EditFontSize(SettingService.SETTING_SMALL,getContext());
                  }
                  if(CurrentSelected==2)
                  {
                      settingService.EditFontSize(SettingService.SETTING_MEDIUM,getContext());
                  }
                  if(CurrentSelected==3)
                  {
                      settingService.EditFontSize(SettingService.SETTING_LARGE,getContext());
                  }
                  if(CurrentSelected==4)
                  {
                      settingService.EditFontSize(SettingService.SETTING_HUGE,getContext());
                  }

                  notifyChanged();

              }


            }
        });

        builder.setNegativeButton("cancel",null);

        String arr[]=
        {
                SettingService.SETTING_EXTRA_SMALL,
                SettingService.SETTING_SMALL,
                SettingService.SETTING_MEDIUM,
                SettingService.SETTING_LARGE,
                SettingService.SETTING_HUGE
        };

        // conversion into ArrayList Since we Needed for Adaptors

        List<String> first= Arrays.asList(arr);

        //  passing data to the Adaptor
        FontAdaptor adaptor=new FontAdaptor(getContext(),android.R.layout.simple_list_item_single_choice,first);

        // Attach Adaptor to the Dialog
        builder.setSingleChoiceItems(adaptor, PreviousSelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CurrentSelected=which;
            }
        });



    }

    // this one is for adaptor

    public class FontAdaptor extends ArrayAdapter<String>
    {

        // This Thing Call the view to to Pass on the data
        public FontAdaptor( Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }


        public View getView(int position, View convertView, ViewGroup parent)
        {
            //return super.getView(position, convertView, parent);
            View v=super.getView(position, convertView, parent);
            TextView tv=(TextView) v;
            String s=tv.toString();

            // lets set the Size of Each  Selection Option Menu Accordingly to the Real Size
            if(s.equals(SettingService.SETTING_EXTRA_SMALL))
            {
                tv.setTextSize(12.0f);
            }
            if(s.equals(SettingService.SETTING_SMALL))
            {
                tv.setTextSize(16.0f);
            }
            if(s.equals(SettingService.SETTING_MEDIUM))
            {
                tv.setTextSize(20.0f);
            }
            if(s.equals(SettingService.SETTING_LARGE))
            {
                tv.setTextSize(24.0f);
            }
            if(s.equals(SettingService.SETTING_HUGE))
            {
                tv.setTextSize(28.0f);
            }


            tv.setTextColor(Color.BLACK);
            tv.setPadding(10,3,3,3);

            return v;


        }
    }
}











