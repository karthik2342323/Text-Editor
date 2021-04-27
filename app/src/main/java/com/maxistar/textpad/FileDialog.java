package com.maxistar.textpad;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
//import android.view.View.OnClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FileDialog extends ListActivity
{

    // variables

    // for storing all paths of File And Folder
    List<String> AllPath;

    // Storing Name of File and Folder with Differ Img
    List<HashMap<String,Object>> ImgAndDirName;

    EditText GetFileName_FromUser;
    TextView ShowCurrent_path;

    String ParentPath,CurrentPath,RootPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // default Result send when Activity is called on this class
        setResult(RESULT_CANCELED,getIntent());

        //load UI
        setContentView(R.layout.file_dialog_main);

        // get File Name from user using dialog of EditText
        GetFileName_FromUser=(EditText) findViewById(R.id.fdEditTextFile);

        GetFileName_FromUser.setText("newfile.txt");

        // display Path on top of screen
        ShowCurrent_path=(TextView) findViewById(R.id.path);

        // 0  --> for opening dir
        // 1  --> for save as with dialog of file name
        int mode=getIntent().getIntExtra(TPStrings.SELECTION_MODE,0);



        // file saving dialog
        LinearLayout dialog=findViewById(R.id.fdLinearLayoutCreate);

        // opening dir
        if(mode==0)
        {
            // dialog of saving is an layout so we need to disable it for opening dir
            dialog.setVisibility(View.GONE);
        }
        // save dir prompt(Dialog)
        else
        {
            dialog.setVisibility(View.VISIBLE);
        }

        // taking buttons
        final Button cancel=findViewById(R.id.fdButtonCancel);
        final Button ok=findViewById(R.id.fdButtonCreate);

       //  on cancellation
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // since default we had set cancel result so direct finish the activity

                finish();
            }
        });

        // on ok

        // if its on Then MainActivity is requesting for saving file to dir so
        // so put data with file name
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // using key TPStrings.RESULT_PATH for routing data from FileDialog to MainActivity


                if(GetFileName_FromUser.length()>0)
                {
                    // current  dir/filename.txt
                    getIntent().putExtra(TPStrings.RESULT_PATH,CurrentPath+TPStrings.SLASH+GetFileName_FromUser.getText().toString());

                    // set result

                    setResult(RESULT_OK,getIntent());
                    // finish the activity

                    finish();
                }

            }
        });

        // creating shared preference for relaunching the Previous  path
        SharedPreferences preferences=getSharedPreferences(TPStrings.FILE_DIALOG,0);
        // get root path i.e storage/emulated/0
        RootPath= Environment.getExternalStorageDirectory().getPath();

        CurrentPath=preferences.getString(TPStrings.START_PATH,RootPath);


        read(CurrentPath);


    }

    void read(String path)
    {
        File f=new File(path);
        File arr[]=f.listFiles();

        // see if file is exist or not  after that user has deleted that file in that case
        // we cannot show that path so load root dir

        // try to get Parent if Dir is exist
        if(f.exists())
        {

            try
            {
                File parent=new File(f.getParent());
                ParentPath=parent.getPath();
                 if(!parent.canRead())
                 {
                     // get the parent of its parent if parent cannot read
                     ParentPath=parent.getParent();
                 }
            }
            catch(Exception e)
            {
                //
            }
        }
        else
        {
            // use root dir
            f=new File(RootPath);
            arr=f.listFiles();
            ParentPath=f.getParent();
        }

        // set current path on top of FileDialog fragment
        ShowCurrent_path.setText(path);

        TreeMap<String,String> dir_name,dir_path,file_name,file_path;

        dir_name=new TreeMap<String,String>();
        dir_path=new TreeMap<String,String>();

        file_name=new TreeMap<String,String>();
        file_path=new TreeMap<String,String>();

        // Insert Parent on top of Block for Traversing
        if(ParentPath!=null)
        {
            dir_name.put(TPStrings.FOLDER_UP,TPStrings.FOLDER_UP);
            dir_path.put(TPStrings.FOLDER_UP,ParentPath);
        }

        for(File x:arr)
        {
            if(x.isDirectory())
            {
                dir_name.put(x.getName(),x.getName());
                dir_path.put(x.getName(),x.getPath());
            }
            else
            {
                file_name.put(x.getName(),x.getName());
                file_path.put(x.getName(),x.getPath());
            }
        }

        // backend for manipulaton
        AllPath=new ArrayList<String>();
        // first add dir then file
        AllPath.addAll(dir_path.values());
        AllPath.addAll(file_path.values());

        // this one is for front end for showing dir names
        ImgAndDirName=new ArrayList<HashMap<String, Object>>();

        SimpleAdapter adapter=new SimpleAdapter(this,ImgAndDirName,R.layout.file_dialog_row
                ,new String[]{TPStrings.ITEM_IMAGE,TPStrings.ITEM_KEY},new int[]{R.id.fdrowimage,R.id.fdrowtext});

        // add dir
        for(String x:dir_name.values())
        {
            compute(R.drawable.folder,x);
        }

        // add path

        for(String x:file_name.values())
        {
            compute(R.drawable.file,x);
        }

        adapter.notifyDataSetChanged();

        // set the adaptor
        setListAdapter(adapter);
    }


    // for saving path for opening next time
    void Savepath(String PATH)
    {
        SharedPreferences preferences=getSharedPreferences(TPStrings.FILE_DIALOG,0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(TPStrings.START_PATH,PATH);
        editor.apply();
    }

    void compute(int img,String name)
    {
        // now it has two thing Img And File Name and we are Inserting to ImgAndDirName
        HashMap<String,Object> first=new HashMap<String,Object>();

        // lets put img
        first.put(TPStrings.ITEM_IMAGE,img);

        // lets put Name
        first.put(TPStrings.ITEM_KEY,name);

        // lets put all this into one list

        ImgAndDirName.add(first);

    }

    // some Inbuilt function for manipulation


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);

        // whatever users had click the block so get dir from that block

        File f=new File(AllPath.get(position));

        // now if user has clicked on dir so load from that path
        if(f.isDirectory())
        {
            // first Store the path for next time launching from this path
            Savepath(f.getPath());


            // alter the current path in case result fetch from prompt dialog
            CurrentPath=f.getPath();

            // reload from clicked dir
            read(f.getPath());
        }
        // this one is an File not dir so route this dir from FileDialog to MainActivity
        else
        {
            // set the selected one As True
            v.setSelected(true);

            // Since Current path is holding previous path so Save that path for next time loading
            Savepath(CurrentPath);

            // Now this one is for opening file Request so Route that  Dir with help of Keys to MainActivity

            //set the data
            getIntent().putExtra(TPStrings.RESULT_PATH,f.getPath());

            //Send the data to the MainActivity which is calling for result
            setResult(RESULT_OK,getIntent());
            finish();

        }
    }

    // on back pressed Siminar is remanining

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       // return super.onKeyDown(keyCode, event);

        // on back pressed
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(ParentPath!=null && CurrentPath!=RootPath)
            {
                // alter the current path in case user press back and save the dir
                CurrentPath=ParentPath;

                // save the current path
                Savepath(CurrentPath);

                read(ParentPath);
            }
            else
            {
                return super.onKeyDown(keyCode, event);
            }
            return true;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }
    }

}
