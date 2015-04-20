/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.txstatecs4398.coursemate.collections.Person;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 *
 * @author Steven
 */
public class GroupSelectionActivity extends Activity {
    
    final Context context = this;
    private ListView list;
    private final ArrayList<String> groupList = new ArrayList<>();
    private final ArrayList<String> groupDate = new ArrayList<>();
    private final ArrayList<String> groupNames = new ArrayList<>();
    private GroupCustomListAdapter listAdapter = null;
    private String nfcNetID;
    private String nfcSched;
    private AlertDialog alertDialog;
    private ImageView group;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.group);
        Bundle extras = getIntent().getExtras();
        
        list = (ListView) findViewById(R.id.list1);
        group = (ImageView) findViewById(R.id.image1);

        // end of view setup code
        if (extras != null) {
            nfcNetID = extras.getString("nfcNetID");
            nfcSched = extras.getString("nfcSched");
            Person tempPerson = new Person(nfcNetID);
            tempPerson.nfcParse(nfcSched);
            personCreate(tempPerson);
        }
        //----------Start of Group Processing--------------
        if (groupRetriever()) {
            listAdapter = new GroupCustomListAdapter(this, groupList, groupDate, groupNames);
            list.setAdapter(listAdapter);
            
            list.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    Intent intent = new Intent(GroupSelectionActivity.this, ShareGroupActivity.class);
                    intent.putExtra("groupName", groupList.get(position));
                    startActivity(intent);
                }
            });
            
            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() 
            {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    deleteFile("CMG"+groupList.get(position));
                    groupList.remove(position);
                    listAdapter.notifyDataSetChanged();
                    
                    if(groupList.isEmpty())
                        group.setVisibility(View.VISIBLE);
                    return true;
                }
            });
        }
        
        group.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                dialogCreate();
                alertDialog.show();
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_person_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_add_group:
                dialogCreate();
                alertDialog.show();
                return true;
            case R.id.action_share:
                intent = new Intent(getApplicationContext(), SharePersonActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                deleteFile("user");
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void dialogCreate() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog, null);
        
        ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.drawable.dialog);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        
        final EditText userInput = (EditText) promptsView.findViewById(R.id.userGroup);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (!userInput.getText().toString().isEmpty()) {
                                    if (groupCreate(userInput.getText().toString())) {
                                        groupRetriever();
                                        if(listAdapter != null)
                                            listAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        
        alertDialog = alertDialogBuilder.create();
    }
    
    public boolean groupCreate(String filename) {
        try {
            FileOutputStream fs = openFileOutput("CMG" + filename, Context.MODE_PRIVATE);
            OutputStreamWriter ow = new OutputStreamWriter(fs);
            BufferedWriter writer = new BufferedWriter(ow);
            
            Calendar now = Calendar.getInstance();
            int date = now.get(Calendar.MONTH) + 1;
            String year = Integer.toString(now.get(Calendar.YEAR));
            
            if (date >= 1 && date < 6) {
                writer.write("Spring " + year);
            } else if (date >= 6 && date < 8) {
                writer.write("Summer " + year);
            } else {
                writer.write("Fall " + year);
            }
            
            writer.flush();
            writer.close();
            
            finish();
            startActivity(getIntent());
        } catch (IOException e) {
            return false;
        }
        
        return true;
    }
    
    public boolean personCreate(Person person) {
        try {
            FileOutputStream fs = openFileOutput("CMP" + person.getNetID(), Context.MODE_PRIVATE);
            OutputStreamWriter ow = new OutputStreamWriter(fs);
            BufferedWriter writer = new BufferedWriter(ow);
            writer.write(person.showSched());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            return false;
        }
        
        return true;
    }
    
    public boolean groupRetriever() {
        File root = getFilesDir();
        
        FilenameFilter beginswithm = new FilenameFilter() {
            @Override
            public boolean accept(File directory, String filename) {
                return filename.startsWith("CMG");
            }
        };
        
        File[] files = root.listFiles(beginswithm);
        
        if (files.length == 0) {
            return false;
        }
        
        groupList.clear();
        for (File file : files) {
            try {
                FileInputStream stream = openFileInput(file.getName());
                Scanner in = new Scanner(stream);
                groupList.add(file.getName().substring(3));
                groupDate.add(in.nextLine());
                String name = "";
                while (in.hasNextLine()) {
                    name += in.nextLine() + " ";
                    in.nextLine();
                }
                groupNames.add(name);
            } catch (FileNotFoundException ex) {
            }
        }
        if (!groupList.isEmpty()) {
            group.setVisibility(View.GONE);
        }
        
        return true;
    }
}
