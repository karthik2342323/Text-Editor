package com.maxistar.textpad;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditorActivity extends Activity
{
    // signal for what to load
    // signal --> 0 for new file
    // signal --> 1 for either android is requesting or opening previous file

    public static int signal=0;

    // path

    // holding current path
    public static String path_original="";

    //Now let say if its override the we gonna change the path in  order to save but
    // if user say no to override then we need to go to the previous path holding
    public static String temp_path="";

    //path


    // storing Strings for comparison
    public static String store="";

    /*
    if on back pressed then we need to load the modified strings from
    widget
     */
    private static String restore_1="";

    // which operation to perform
    public static int do_next;
    /*
    * do_next --> 0 for save
    * do_next --> 1 for saveas
    * do_next --> 3 for open
    * do_next --> 4 for exit
    * do_next --> 5 for settingActivity
    * d0_next --> 123 for nothing
     */
    public static int save=0;
    public static int saveas=1;
    public static int newfile=2;
    public static int open=3;
    public static int exit=4;
    public static int do_next_settingActivity=5;
    public static int nothing=123;




    public static int request=123; // for nothing means no task
    /*
    request --> 0 for opening dir
    request --> 1 for saving dir with file name
    request --> 2 for SettingActivity
     */
    public static int request_open=0;
    public static int request_save=1;
    public static int request_settingActivity=3;



    SettingService settingService;

    // for loading text into that UI
     private  EditText text;



    // onCreate is just for loading Widget and first time what to do

    @Override
    protected void onCreate( Bundle savedInstanceState)
    {

        int num=0;
        // num --> 0 for back press
        // num --> 1 for not back press

        // for debugging
       // Message_1(" onCreate()  value "+savedInstanceState);


        // for debugging

        super.onCreate(savedInstanceState);
        settingService=SettingService.Instance(getApplicationContext());

        // load the UI for using IDs to load the text
        setContentView(R.layout.main);

        // get the id for loading Strings
        text=findViewById(R.id.editText1);



        //store=text.getText().toString();




        // for first launching we need to do this after changing state At will
        // remain same otherwise what gonna happen is it will reload again and again
        // so we dont need to loose text in widget by reloading it again from path specified

        /* verry first Launching of this activity this thing is get loaded
         * so only 1 time it gets executed
         */
        if(savedInstanceState==null) {
            // for debugging
           // Message_1(" Oncreate  Executed inside the arena");
            // for debugging

            // lets verify the permissions
            permission_1(this);
            Intent i = this.getIntent();

            // If file manager which can put uri to open the file if user choose the our app for
            // an execution
            if (TPStrings.ACTION_VIEW .equals(i.getAction()))
            {

                num=1;

                /*
                 * directly open the file since path is specified by android and using
                 * uri as an System Specified path.
                 */

                android.net.Uri Pre_path = i.getData();

                // open the file from path
                open_file(Pre_path.getPath());
            }
            // As normal Opening of App
            else {

                // first load of this activity
                // if file is empty then load from previous file provided that preference box
                // should get true (Ticked)
                if (path_original.equals(TPStrings.EMPTY)) {
                    if (settingService.Last_File())
                    {
                        num=1;

                        OpenLastFileName();
                    }
                }
            }


            /*
            * neither Android is requesting nor previous file is requested for
            * opening .so default is new file
             */
            if(path_original.equals(TPStrings.EMPTY))
            {
                /*
                // set the title
                setTitle(TPStrings.NEW_FILE_TXT);

                //apply the preference
                apply_preference();

                 */
                num=1;
                new_file_2();
            }
            /* let say if back pressed is pressed then activity will
            relaunched with current path
             */

            if(num==0)
            {
                text.setText(restore_1);
                apply_preference();
                updateTitle();
            }
        }
        else
        {
            // since it wont hold any preference and title so reload the preference and title

            apply_preference();
            updateTitle();
        }

        // request focus


        text.requestFocus();



        // apply language Since we gonna use system default language i.e english
        // so not setting language


    }

    // last file name

    private void OpenLastFileName()
    {
        // Must ensure That Last file name should not be empty
        if(!settingService.Last_Name().equals(TPStrings.EMPTY))
        {
            this.open_file(settingService.Last_Name());
        }
    }



    /*
    * Check the Permission we have or not
     */
    public void permission_1(Activity activity)
    {
        int p= ActivityCompat.checkSelfPermission(activity,Manifest.permission.READ_EXTERNAL_STORAGE);

        String arr[]={Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if(p!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,arr,1);
        }
    }


    /*
    * If Last time user has opened the file so it gets registered on openLastFile()
    * now if user has deleted that path but due to our app prototype it gonna open
    * previous file if user has clicked previous in preference
    * At that point of time we need an Exception to show user
    *  1) FileNotFoundException
    *  2) IOException
     */

    private void open_file(String path)
    {
        settingService=SettingService.Instance(this.getApplicationContext());
        try
        {
            File f=new File(path);


            // for streaming the file
            FileInputStream fin=new FileInputStream(f);

            // for streaming data
            DataInputStream din=new DataInputStream(fin);

            byte b[]=new byte[(int) f.length()];

            // lets fill the byte
            din.read(b);

            // convert into String and apply the encoding

            // for debugging
            String s=new String(b,0,(int) f.length(),settingService.Encoding());


            s=delimeter(s);


            //String s=new String(b);


            // for debugging


            // FIll  thw widget with text
            text.setText(s);


            // Fill the string to manipulate the changes in file
            store=s;

            // set the path for set the title
            path_original=path;

            // for prompt
            Message_1(" File Opened : "+path_original);

            // set the title
            updateTitle();

            // store the previous file path for loading next time
            settingService.EditLastFileName(path,this.getApplicationContext());

            /*
            * Apply the preference works in 2 ways
            * 1) By opening the file
            * 2) By URI Android Request direct calling
            *
            * At this point of time no need to call separately
             */
            apply_preference();


        }
        /* if user had deleted previous file and app is launched
        if its not found that file then load newfile
         */

        catch (FileNotFoundException e)
        {
            Message_1("File Not Found");
            new_file_2();
        }
        catch(IOException e)
        {
            Message_1(" IO Error");
            new_file_2();
        }
    }

    private void apply_preference()
    {
        settingService=SettingService.Instance(this.getApplicationContext());

        // This are the variation
        text.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS |
                InputType.TYPE_TEXT_VARIATION_NORMAL |
                //InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD |
                InputType.TYPE_CLASS_TEXT);

        // set font
        String font=settingService.Font();
        if(font.equals(TPStrings.FONT_MONOSPACE))
        {
            text.setTypeface(Typeface.MONOSPACE);
        }
        if(font.equals(TPStrings.FONT_SANS_SERIF))
        {
            text.setTypeface(Typeface.SANS_SERIF);
        }
        if(font.equals(TPStrings.FONT_SERIF))
        {
            text.setTypeface(Typeface.SERIF);
        }

        // set size
        String size=settingService.FontSize();
        if(size.equals(SettingService.SETTING_EXTRA_SMALL))
        {
            text.setTextSize(12.0f);
        }
        if(size.equals(SettingService.SETTING_SMALL))
        {
            text.setTextSize(16.0f);
        }
        if(size.equals(SettingService.SETTING_MEDIUM))
        {
            text.setTextSize(20.0f);
        }
        if(size.equals(SettingService.SETTING_LARGE))
        {
            text.setTextSize(24.0f);
        }
        if(size.equals(SettingService.SETTING_HUGE))
        {
            text.setTextSize(28.0f);
        }

        // set color bg
        int bgcolor=settingService.BgColor();
        text.setBackgroundColor(bgcolor);

        //set Text Color
        int text_color=settingService.FontColor();
        text.setTextColor(text_color);

    }

    /*
    * Now UpdateTitle() has been called by more than one function so
     */
    private void updateTitle()
    {

        if(path_original.equals(TPStrings.EMPTY))
        {
            setTitle(TPStrings.NEW_FILE_TXT);
        }
        else
        {
            setTitle(path_original);
        }
    }

    // Menu launching part

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        // launch the menu
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    // lets perform according to menu item selected

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       // return super.onOptionsItemSelected(item);

        /*
         * function_1() is for prompt way to call like menu way to call
         * function_2() is directly calling without taken any constrains
         */

        switch (item.getItemId())
        {
            case R.id.menu_document_new:
                new_file_1();
                break;
            case R.id.menu_document_open:
                open_1();
                break;
            case R.id.menu_exit:
                exit_1();
                break;

                /*
                 -----  save option --------
                Now save menu option will save the file from
                path now if path is empty the it gonna open filedialog
                Activity to open the file

                -----   save as ---------------------------
                Now always ask path to save Now that
                doesnt matter whether path is exist or not
                 */
            case R.id.menu_document_save:
                menu_save();
                break;
            case R.id.menu_document_save_as:
                menu_saveas();
                break;
            case R.id.menu_document_settings:
                menu_setting();
                break;

        }

        return true;
    }


    private void prompt_dialog()
    {


            new AlertDialog.Builder(this)
                    //title
                    .setTitle(" Save Prompt")
                    // body
                    .setMessage(" File has been Modified So Save current file")
                    // buttons
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // prompt to  save()
                            save();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // let see which request

                            if (do_next == newfile) {
                                new_file_2();
                            }
                            if(do_next==open) {
                                open_2();
                            }
                            if(do_next==exit)
                            {
                                exit_2(EditorActivity.this);
                            }

                        }
                    })
            .create()
            .show();



    }

    // work with prompt
    public void new_file_1()
    {
        // set credential in case for requesting
        request=request_save;
        do_next=newfile;

        if(!store.equals(text.getText().toString()))
        {
            prompt_dialog();
        }
        else
        {
            new_file_2();
        }
    }
    // work directly
    public void new_file_2()
    {
        /*
        1) set title
        2) set widget empty string
        3) set store to empty string
        4) set path empty
        5) preference
         */

        // preference
        apply_preference();

        setTitle(TPStrings.NEW_FILE_TXT);
        text.setText(TPStrings.EMPTY);
        store=TPStrings.EMPTY;
        path_original=TPStrings.EMPTY;


    }

    public void open_1()
    {

        /*
        For this function credintial is not save
        because if we say save then save but if text is not changed
        then also save dialog will popuped

        Now if text has Changed then also for saving opening dialog will
        will popup.

        since One credential cannot be same for both condition
        so set the credential accordingly
         */

        if(!store.equals(text.getText().toString()))
        {
            //set credential
            request=request_save;
            do_next=open;

            prompt_dialog();
        }
        else
        {
            // set credential
            request=request_open;
            do_next=open;

            open_2();
        }
    }
    public void open_2()
    {
        /*
        if file is not changed then credintial will
        be request_save so it will be in loop
        since function_2() is an direct action
        credintial will be similar to default action

         */
        request=request_open;
        do_next=open;

        request_filedialog();
        // now we are requesting the FileDialog to open
    }

    public void exit_1()
    {
        // set credential
        request=request_save;
        do_next=exit;

        if(!store.equals(text.getText().toString()))
        {
            prompt_dialog();
        }
        else
        {
            exit_2(EditorActivity.this);
        }
    }
    public void exit_2(Activity activity)
    {
        activity.finish();
        java.lang.System.exit(0);
    }

    private  void menu_save()
    {
        // set the credential
        request=request_save;
        do_next=save;

        // call the respective function
        save();
    }
    private void menu_saveas()
    {
        // set the credential
        request=request_save;
        do_next=saveas;

        // call the respective function
        request_filedialog();
    }

    private void menu_setting()
    {
        // set the credential
        request=request_settingActivity;

        // this one is for formality it do nothing
        do_next=do_next_settingActivity;
        // set the credential


        // start the respective activity
        Intent intent=new Intent(this.getBaseContext(),SettingActivity.class);
        this.startActivityForResult(intent,do_next);
    }


    // Menu launching part


    private void Message_1(String m)
    {
        Context context=getApplicationContext();
        int duration= Toast.LENGTH_LONG;
        Toast t=Toast.makeText(context,m,duration);
        t.show();
    }

    // for formality

    @Override
    protected void onResume() {
        //Message_1(" Resume");
        super.onResume();
    }

    @Override
    protected void onPause() {
       // Message_1(" Pause");
        super.onPause();
    }


    @Override
    protected void onStop() {
       // Message_1(" stop");
        super.onStop();
    }



    // we have no usage since we had used Static var so no need to store into key for fetching
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        //Message_1("SaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    // for formality

    /*
    On Back pressed for save file or not prompt dialog

     */

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // stored strings on file opening and current strings is not same
        // so prompt for save or not


        /*
            Now if user gonna press back then onresume will call onCreate
            so for that purpose we gonna store the modified
        */
        restore_1=text.getText().toString();


        if(!store.equals(text.getText().toString()))
        {
            // Now we know that alert fialog is holding the popup message
            // so rest of code in this function will execute except popup message for
            // pressing click ok or cancell





            new AlertDialog.Builder(this)
                    // Title
                    .setTitle(" U had made some change ")
                    // body
                    .setMessage(" You had made some change Are u sure want to press back")
                    // buttons
                    .setPositiveButton(" Yes ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           EditorActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(" No ",null)
                    // what happen when on pressed back after showing dialog
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //EditorActivity.super.onBackPressed();
                        }
                    })
                    .create()
                    .show();

        }
        else
        {
            super.onBackPressed();
        }
    }

    // save file process
    private void save()
    {
        // file is new or another way to think  path="" is empty
        if(path_original.equals(TPStrings.EMPTY))
        {
            /* then we need to request FIleDialog to get the path
             * then onActivityResult() will re-route the data
             */
            request_filedialog();
            // which is below this function
        }
        else
        {
            // save the file from given path
            file_save();
        }
    }



    // requesting file dialog for getting work done
    private void request_filedialog()
    {
        // now request depends on which function is calling

        // get intend
        Intent intent=new Intent(this.getBaseContext(),FileDialog.class);
        // set the data for request
        intent.putExtra(TPStrings.SELECTION_MODE,request);
        // lets start the activity with specific request code according to it we gonna
        // fetch the result in OnActivityResult()


        this.startActivityForResult(intent,do_next);



        // lets fetch data from OnActivityResult() which is below this function
    }

    // this function is for fetching the result on FileDialog requested by this activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        // save and saveas will work almost same if we are requesting
        // file dialog for saving


        /* now it can be 2 thing user may cancel the request or
        user proceed further . so fetch from FileDialog accordingly

         */

        // this one is for FileDialog

        // if we get signal ok then proceed further
        if(resultCode==RESULT_OK)
        {
            // if request is for opening file then
            if(request==request_open)
            {
                // fetch the data which is giving by filedialog activity
                path_original=data.getStringExtra(TPStrings.RESULT_PATH);


                // for debugging

                //Message_1(" File Opened : "+path_original);

                // for debugging

                // attach the function
                open_file(path_original);

            }
            if(request==request_save)
            {
                // now we need to know whether user has choose the file or select exist file
                // so for overrite we can prompt

                // first let fetch the path
                temp_path=path_original;
                path_original=data.getStringExtra(TPStrings.RESULT_PATH);

                /*
                if Its override and user gonna cancel then current path is holding result
                path not previous path so to fetch the previous path we gonna use temp_path
                 */

                // for debugging

               // Message_1(" Path : "+path_original);

                // for debugging

                check_overwrite();
                // then override will call file_save()
                // then it route to dedicated do_next which is embedded in this
                // called function i.e file_save()




            }
        }
        // this one is for SettingActivity
        else if(request==request_settingActivity)
        {
            // reload the preference in case changes in SettingService
            apply_preference();

            // if delimeter gets changed then we need to reload
            // now we had implemented in save and open
            // but we need implement on setting change

            // delimeter
            delimeter();
        }

        else if(resultCode==RESULT_CANCELED)
        {
            Message_1(" Operation Cancelled");
            do_next=nothing;
        }


        // do nothing


    }

    // this one is to check one more step  i.e file exist when prompting filedialog
    private void check_overwrite() {
        File f = new File(path_original);

        // set see if file exist
        if (f.exists())
        {
            // for debugging

            Message_1(" File : "+path_original);

            // for debugging

            new AlertDialog.Builder(this)
                    .setTitle(" File Already Exist")
                    .setMessage(" File Exist Do U want to Override ")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // directly save file on request
                            file_save();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            // lets get the previous path if operation is cancel
                            // since path_original is holding save path
                            path_original=temp_path;
                        }
                    })
                    .create()
                    .show();
        }

        // else save
        else
        {
            file_save();
        }

    }


    private void file_save()
    {

        settingService=SettingService.Instance(getApplicationContext());
        try {
            File f=new File(path_original);

            // if file is not exist then create that file from that dir
            if (!f.exists())
            {
                f.createNewFile();
            }

            FileOutputStream fout=new FileOutputStream(f);

            // save process begin
            String s=text.getText().toString();
            // save with specific encoding

            //for debugging
            s=delimeter(s);

            fout.write(s.getBytes(settingService.Encoding()));

            //fout.write(s.getBytes());

            // for debugging


            // for debugging

            Message_1(" File saved : "+path_original);

            // for debugging


            // close the stream
            fout.close();

            // since we had save the file so update the store(String)  var for comparison
            // if user gonna change after saving for next time

            store=s;

            // update the title
            updateTitle();

            // reroute attach function is remaining

            do_next();

        }
        catch(FileNotFoundException e)
        {
            Message_1(""+e);
        }
        catch(IOException e)
        {
           Message_1(""+e);
        }

    }


    private void do_next()
    {
        // now save and save as are requesting with this function so no application
        // over here

        if(do_next==newfile)
        {
            new_file_2();
        }
        if(do_next==open)
        {
            open_2();
        }
        if(do_next==exit)
        {
            exit_2(EditorActivity.this);
        }

        // setting is remaining


    }



    // file saving process

    // delimeter

    /*

     usage

     1) opening file
     2) Saving file

     */

    // this one is for save and open
    private String delimeter(String s)
    {
        // fetch the previous delimeter
        settingService=SettingService.Instance(getApplicationContext());
        s=delimeter.getInstance().applyEndings(s,settingService.Delimeter());
        return s;
    }

    // this one is for traversing from preference to this activity
    // attach to OnActivityResult
    private void delimeter()
    {
        settingService=SettingService.Instance(getApplicationContext());
        // apply delimeter
        String s=delimeter.getInstance().applyEndings(text.getText().toString(),settingService.Delimeter());
        text.setText(s);

        /*
         Now we need to change the store(comparison)  since
         it holding previous value so we need to update to current
         value according to delimeter otherwise what gonna happen is it will prompt
         that file is changed in case user haven't changed the file
         */
        store=s;
    }

    // delimeter
}
