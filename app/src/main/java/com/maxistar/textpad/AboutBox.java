package com.maxistar.textpad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.AttributeSet;

public class AboutBox extends DialogPreference
{

    public AboutBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        //super.onPrepareDialogBuilder(builder);

        // set Title
        builder.setTitle(ConvertString(R.string.About));

        // set Button
        builder.setPositiveButton(ConvertString(R.string.Continue),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }
        );
        builder.setNegativeButton(null,null);

        // add body

        final SpannableString s=new SpannableString(ConvertString(R.string.about_message));
        Linkify.addLinks(s,Linkify.WEB_URLS);

        builder.setMessage(s);



    }
    private String ConvertString(int res)
    {
        return getContext().getResources().getString(res);
    }
}
