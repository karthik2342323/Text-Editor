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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Encoding extends DialogPreference
{
    SettingService settingService;
    String CurrentEncoding,PreviousEncoding;

    int PreviousSelect,CurrentSelection;

    // this is an constructor called by inflator

    public Encoding(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        settingService=SettingService.Instance(context);

        PreviousEncoding=settingService.Encoding();

        // Now If user has not selected but clicked ok Then CurrentEncoding will
        // hold nothing So to tackle it We Gonna make both Same

        CurrentEncoding=PreviousEncoding;

        // previous selection
        PreviousSelect=settingService.Selection();

        // In case if user has not selected but clicked ok button at that point of time
        // PreviousSelection will be holding no value . so for solving this we gonna make
        // both equal

        CurrentSelection=PreviousSelect;


    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        //super.onPrepareDialogBuilder(builder);


        // set title
        builder.setTitle(" Select Encoding");

        // set button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // we don't need to do extra work
                if(!CurrentEncoding.equals(PreviousEncoding))
                {
                    settingService.EditEncoding(CurrentEncoding,getContext());

                    // now if Previous and current encoding is not same so their index is also
                    // not same

                    // set index
                    settingService.EditSelection(CurrentSelection,getContext());
                }

            }
        });

        builder.setNegativeButton("Cancel",null);

        // get The Encoding
        Map<String, Charset> first=Charset.availableCharsets();

        final List<String> value=new ArrayList<String>();

        // fill the array List with Encoding

        // since we are fetching key so get value from map now map is an Charset to
        // convert to string
        for(String x:first.keySet())
        {
            value.add(first.get(x).name());
        }

        FontAdaptor adaptor=new FontAdaptor(getContext(),android.R.layout.simple_list_item_single_choice,value);

        // attach that adaptor to the Dialog Builder

        builder.setSingleChoiceItems(adaptor, PreviousSelect, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // get the encoding from Selected Cell
                CurrentEncoding=value.get(which);

                // get the selection
                CurrentSelection=which;

            }
        });

    }

    //--------  Adaptor Part --------------


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



            tv.setTextColor(Color.BLACK);
            tv.setPadding(10,3,3,3);

            return v;


        }
    }






}
