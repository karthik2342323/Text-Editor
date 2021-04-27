package com.maxistar.textpad.Preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.DialogPreference;
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

public class FontTypePreference extends DialogPreference
{
    SettingService settingService;
    String s;
    int CurrentSelected,PreviousSelected;

    // This one is an Constructor which is called by Inflator while loading Preference Since
    // Path is Given
    public FontTypePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        // get previous selected ones
        settingService=SettingService.Instance(context);
        s=settingService.Font();

        if(s.equals(TPStrings.FONT_MONOSPACE))
        {
            PreviousSelected=0;
        }
        if(s.equals(TPStrings.FONT_SANS_SERIF))
        {
            PreviousSelected=1;
        }
        if(s.equals(TPStrings.FONT_SERIF))
        {
            PreviousSelected=2;
        }

        // Now if user has not selected but still press ok Then we dont Have any Value for var Current selected
        // So At This point We gonna make both same for that condition

        CurrentSelected=PreviousSelected;

    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        //super.onPrepareDialogBuilder(builder);
        builder.setTitle(" Select Font ");

        // creating button
        builder.setPositiveButton(" ok ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

              if(CurrentSelected!=PreviousSelected)
              {
                  if (CurrentSelected == 0) {
                      settingService.EditFont(TPStrings.FONT_MONOSPACE, getContext());
                  }
                  if (CurrentSelected == 1) {
                      settingService.EditFont(TPStrings.FONT_SANS_SERIF, getContext());
                  }
                  if (CurrentSelected == 2) {
                      settingService.EditFont(TPStrings.FONT_SERIF, getContext());
                  }
                  notifyChanged();
              }

            }
        });

        builder.setNegativeButton(" Cancel",null);


        //------- adaptor Part -------

        // creating array

        String[] arr = {
                TPStrings.FONT_MONOSPACE,
                TPStrings.FONT_SANS_SERIF,
                TPStrings.FONT_SERIF
        };

        List<String> first= Arrays.asList(arr);

        // set Single Choice on Adaptor
        FontAdaptor adaptor=new FontAdaptor(getContext(),android.R.layout.simple_list_item_single_choice,first);

        // insert Adaptor in the dialog Prompt
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
            if(s.equals(TPStrings.FONT_MONOSPACE))
            {
                tv.setTypeface(Typeface.MONOSPACE);
            }
            if(s.equals(TPStrings.FONT_SANS_SERIF))
            {
                tv.setTypeface(Typeface.SANS_SERIF);
            }
            if(s.equals(TPStrings.FONT_SERIF))
            {
                tv.setTypeface(Typeface.SERIF);
            }


            tv.setTextColor(Color.BLACK);
            tv.setPadding(10,3,3,3);

            return v;


        }
    }


}










