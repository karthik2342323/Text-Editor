package com.maxistar.textpad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

public class SettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{

    SettingService settingService;
    Preference preference;

    PackageInfo pinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loading Preference

        addPreferencesFromResource(R.xml.preferences);

        settingService=SettingService.Instance(getApplicationContext());



        // setting version of Preference
        preference=this.findPreference(TPStrings.VERSION_NAME);

        try
        {
            // 0 is an creation
            // get package details
            pinfo=getPackageManager().getPackageInfo(getPackageName(),0);

            // set version on the xml
            preference.setSummary(pinfo.versionName);

        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

    }



    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }




    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // reload the this activity for notice the change

        Intent intent=new Intent(this,SettingActivity.class);

        // since everytime it gonna load so we dont need to traverse all
        // load activity so clear the activity

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        this.startActivity(intent);
    }
}
