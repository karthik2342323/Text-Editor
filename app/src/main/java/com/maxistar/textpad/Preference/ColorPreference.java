/*
 *  This  one is common for selecting Color for both i.e bg Color and Text Color
 */


package com.maxistar.textpad.Preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.maxistar.textpad.R;
import com.maxistar.textpad.SettingService;

public class ColorPreference extends DialogPreference
{

    //0 --> for bg color
    //1 --> for Font Color

    int PreviousColor,CurrentColor;
    int selected;
    String Title,Holder_key;
    SettingService settingService;

    public ColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Loading View for BindView Execution
        setWidgetLayoutResource(R.layout.colorpref);

        settingService=SettingService.Instance(context);

        // Index 1 is for fetching keys
        Holder_key=attrs.getAttributeValue(1);

        // Which key is requesting
        if(Holder_key.equals(SettingService.SETTING_BG_COLOR))
        {
            selected=0;
        }
        if(Holder_key.equals(SettingService.SETTING_FONT_COLOR))
        {
            selected=1;
        }

        // get Previous  Bg color

        if(selected==0)
        {
            Title=" Edit BG Color";
            PreviousColor=settingService.BgColor();
        }
        if(selected==1)
        {
            Title=" Edit Font Color";
            PreviousColor=settingService.FontColor();
        }

        // Now if User is Clicking ok without Selecting Color So At this Point we
        // CurrentColor is not holding anything so to solve this Make both Same

        CurrentColor=PreviousColor;

    }

    // This one is for Overview of Color Which we see in Preference
    // When Constructor is called by Layout Inflater or Preference
    // We need to Set an Xml of View then We can use the Ids
    // So we had Launched the colorpref.xml in Constructor
    // Since onBindView is an inbuild function so it gonna execute after constructor


    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        View V=view.findViewById(R.id.currentcolor);
        if(V!=null)
        {
            V.setBackgroundColor(PreviousColor);
        }

    }

    // On tap Its gonna Called
    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
       // super.onPrepareDialogBuilder(builder);

        // set Title
        builder.setTitle(Title);

        // set Button
        builder.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(CurrentColor!=PreviousColor)
                {
                    // Set which Color
                    if(selected==0)
                    {
                        settingService.EditBgColor(CurrentColor,getContext());
                    }
                    if(selected==1)
                    {
                        settingService.EditFontColor(CurrentColor,getContext());
                    }
                }

                notifyChanged();
            }
        });

        builder.setNegativeButton("Cancel",null);

        // color manipulation start according to color selected from img map

        // load the ui

        final View L= LayoutInflater.from(getContext()).inflate(R.layout.colorpicker,null);

        // get the img
        final ImageView  Img=L.findViewById(R.id.colormap);

        // set the previous color for first run
        L.setBackgroundColor(CurrentColor);

        Img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Bitmap bitmap=((BitmapDrawable) Img.getDrawable()).getBitmap();

                // x --> width
                // y --> height

                int x,y;

                // Scale th x,y axis for color pixel
                x=(int) (event.getX()-15)*bitmap.getWidth()/(Img.getWidth()-30);

                y=(int) (event.getY()-15)*bitmap.getHeight()/(Img.getHeight()-30);

                // set bounds of x,y axis

                if(x>=bitmap.getWidth())
                {
                    x=bitmap.getWidth()-1;
                }
                if(x<0)
                {
                    x=0;
                }

                if(y>=bitmap.getHeight())
                {
                    y=bitmap.getHeight()-1;
                }
                if(y<0)
                {
                    y=0;
                }

                // get color according to pixel
                CurrentColor= bitmap.getPixel(x,y);

                // set bg color according to scaled color from Img

                L.setBackgroundColor(CurrentColor);

                v.performClick();


                return true;
            }
        });

        // set the layout to the Dialog Builder

        builder.setView(L);

    }
}











