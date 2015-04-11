/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Steven
 */
public class GroupSelectionActivity extends Activity {

    private TextView view;
    private Button addButton;
    private String nfcNetID;
    private String nfcSched;

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
        addButton = (Button) findViewById(R.id.addGroup);
        // end of view setup code

        if (extras != null) {
            nfcNetID = extras.getString("nfcNetID");
            nfcSched = extras.getString("nfcSched");
            view.append(nfcNetID + "\n");
            view.append(nfcSched);
        }

        addButton = (Button) findViewById(R.id.passSched);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(GroupSelectionActivity.this, ShareActivity.class);
                startActivity(intent);
            }
        });
        
        addButton = (Button) findViewById(R.id.logout);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                deleteFile("user");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

    public boolean groupRetriever() {
        File root = getFilesDir();

        FilenameFilter beginswithm = new FilenameFilter() {
            public boolean accept(File directory, String filename) {
                return filename.startsWith("CM");
            }
        };

        File[] files = root.listFiles(beginswithm);

        if (files.length == 0) {
            return false;
        }

        for (File file : files) {
            try {
                Scanner in = new Scanner(file);
                this.view.append(in.next());//groupName
                while (in.hasNext()) {
                    this.view.append(in.next());//personID
                    in.nextLine();//schedule
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }
}
