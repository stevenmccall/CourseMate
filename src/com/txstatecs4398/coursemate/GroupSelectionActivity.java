/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.txstatecs4398.coursemate.meetingshared.GroupSchedule;
import com.txstatecs4398.coursemate.meetingshared.IndividualSchedule;
import com.txstatecs4398.coursemate.meetingshared.Time;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Steven
 */
public class GroupSelectionActivity extends Activity {

    final Context context = this;
    private TextView view;
    private Button addButton;
    private Button logoutButton;
    private Button shareButton;
    private String nfcNetID;
    private String nfcSched;
    private AlertDialog alertDialog;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.group);
        Bundle extras = getIntent().getExtras();

        view = (TextView) findViewById(R.id.text1);
        // end of view setup code

        if (extras != null) 
        {
            nfcNetID = extras.getString("nfcNetID");
            nfcSched = extras.getString("nfcSched");
            //start of group setup
            GroupSchedule temp = new GroupSchedule();
            //start of adding person
            IndividualSchedule tempPerson = new IndividualSchedule(nfcNetID);
            tempPerson.nfcParse(nfcSched);
            temp.AddPerson(tempPerson);
            //start of displaying storage
            for(IndividualSchedule person : temp.returnStorage())
            {
                view.append(person.getNetID()+"\n"+person.showSched()+"\n\n");
            }
        }
        //----------Start of Group Processing--------------
        /*
        if(groupRetriever())
        {
        }*/
        
        //----------start of buttons-----
        shareButton = (Button) findViewById(R.id.passSched);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(GroupSelectionActivity.this, ShareActivity.class);
                startActivity(intent);
            }
        });

        logoutButton = (Button) findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                deleteFile("user");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        // add button listener
        addButton = (Button) findViewById(R.id.addGroup);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialogCreate();
                alertDialog.show();
            }
        });
    }
    
    public void dialogCreate()
    {
        // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.dialog, null);

                ContextThemeWrapper ctw = new ContextThemeWrapper( this, R.drawable.dialog );
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                
                final EditText userInput = (EditText) promptsView.findViewById(R.id.userGroup);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if(!userInput.getText().toString().isEmpty())
                                            if(groupCreate(userInput.getText().toString()))
                                                groupRetriever();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                alertDialog = alertDialogBuilder.create();
    }
    
    public boolean groupCreate(String filename) 
    {
        try {            
            FileOutputStream fs = openFileOutput("CMG"+filename, Context.MODE_PRIVATE);
            OutputStreamWriter ow = new OutputStreamWriter(fs);
            BufferedWriter writer = new BufferedWriter(ow);
            writer.flush();
            writer.close();
        } 
        catch (Exception e){return false;}
        
        return true;
    }

    public boolean groupRetriever() {
        view.setText("");
        File root = getFilesDir();

        FilenameFilter beginswithm = new FilenameFilter() {
            public boolean accept(File directory, String filename) {
                return filename.startsWith("CMG");
            }
        };

        File[] files = root.listFiles(beginswithm);

        if (files.length == 0) {
            return false;
        }

        for (File file : files) {
            try {
                view.append(file.getName().substring(3)+"\n");
                Scanner in = new Scanner(file);
                
                while (in.hasNext()) {
                    this.view.append(in.next());//personID
                    in.next();
                    in.next();//schedule
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }
}
