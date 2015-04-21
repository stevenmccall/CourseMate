package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.txstatecs4398.coursemate.collections.Group;
import com.txstatecs4398.coursemate.collections.Person;
import com.txstatecs4398.coursemate.collections.list_adapters.PersonCustomListAdapter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShareGroupActivity extends Activity {
    private final Context context = this;    
    private NfcAdapter mNfcAdapter;
    private NdefMessage mNdefMessage;
    private final ArrayList<NdefRecord> NFCRecords = new ArrayList();    
    private ArrayList<String> userAdded = new ArrayList();
    private ArrayList<String> schedAdded = new ArrayList();    
    private final ArrayList<String> user = new ArrayList();
    private final ArrayList<String> sched = new ArrayList();        
    private String groupName = "";
    private String groupDate = "";    
    private AlertDialog alertDialog;    
    private ListView list;
    private PersonCustomListAdapter listAdapter;
    private final ArrayList<Integer> mSelectedItems = new ArrayList();
    private boolean first = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_group);
        Bundle extras = getIntent().getExtras();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
            if (mNfcAdapter == null)finish();
        
        if (login() && (extras != null)) {
            list = (ListView) findViewById(R.id.list1);
            
            if(extras.size() == 1)
            {
                groupName = extras.getString("groupName");
                groupRetriever();//gets all the people in the current group
            }
            else
            {
                userAdded = extras.getStringArrayList("nfcNetID");
                schedAdded = extras.getStringArrayList("nfcSched");
                groupName = userAdded.get(0);
                groupDate = schedAdded.get(0);
                userAdded.remove(0);
                schedAdded.remove(0);
            }
            peopleRetriever();//gets all the people that could be added to the group
            
            listAdapter = new PersonCustomListAdapter(this, userAdded);
            list.setAdapter(listAdapter);
            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() 
            {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if(userAdded.get(position).equals(user.get(0)))return true;
                    
                    userAdded.remove(position);
                    schedAdded.remove(position);
                    groupCreate();
                    listAdapter.notifyDataSetChanged();
                    return true;
                }
            });
            groupCreate();
            
        } else {
            finish();
        }
    }
    
    public Group groupCollectionMake()
    {
        Group userGroup = new Group();
        
        for(int i = 0; i < userAdded.size(); i++)
        {
            Person temp = new Person(userAdded.get(i));
            temp.nfcParse(schedAdded.get(i));
            userGroup.AddPerson(temp);
        }
        return userGroup;
    }

    public void dialogCreate() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setTitle("Select Members")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(user.toArray(new String[user.size()]), null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                    boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it 
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        for(int i : mSelectedItems)
                        {
                            if(!userAdded.contains(user.get(i)))
                            {
                                userAdded.add(user.get(i));
                                schedAdded.add(sched.get(i));
                            }
                        }
                        groupCreate();
                        listAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //...
                    }
                });

        alertDialog = alertDialogBuilder.create();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_group_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        //Intent intent;
        switch (item.getItemId()) {
            case R.id.action_add_person:
                dialogCreate();
                alertDialog.show();
                return true;
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void groupCreate() 
    {
        try {
            FileOutputStream fs = openFileOutput("CMG" + groupName, Context.MODE_PRIVATE);
            OutputStreamWriter ow = new OutputStreamWriter(fs);
            BufferedWriter writer = new BufferedWriter(ow);

            writer.write(groupDate);
            writer.newLine();
            NFCRecords.clear();
            NFCRecords.add(createNewTextRecord(groupName, Locale.ENGLISH, true));
            NFCRecords.add(createNewTextRecord(groupDate, Locale.ENGLISH, true));
            for (int i = 0; i < userAdded.size(); i++) {   //user
                writer.write(userAdded.get(i));
                writer.newLine();
                writer.write(schedAdded.get(i));
                writer.newLine();
                NFCRecords.add(createNewTextRecord(userAdded.get(i), Locale.ENGLISH, true));
                NFCRecords.add(createNewTextRecord(schedAdded.get(i), Locale.ENGLISH, true));
            }

            writer.flush();
            writer.close();
            
            NdefRecord[] records = NFCRecords.toArray(new NdefRecord[NFCRecords.size()]);
            mNdefMessage = new NdefMessage(records);
            if(!first)
                mNfcAdapter.enableForegroundNdefPush(this, mNdefMessage);
            first = false;
        } catch (IOException e) {}
    }

    public boolean groupRetriever() 
    {
        try (FileInputStream file = openFileInput("CMG" + groupName)) 
        {
            Scanner in = new Scanner(file);
            groupDate = in.nextLine();

            while (in.hasNextLine()) 
            {
                userAdded.add(in.nextLine());
                schedAdded.add(in.nextLine());
            }
        } catch (Exception e) {return false;}
        
        return true;
    }

    public boolean peopleRetriever() {
        File root = getFilesDir();

        FilenameFilter beginswithm = new FilenameFilter() {
            @Override
            public boolean accept(File directory, String filename) {
                return filename.startsWith("CMP");
            }
        };

        File[] files = root.listFiles(beginswithm);

        if (files.length == 0) {
            return false;
        }

        for (File file : files) {
            try {
                Scanner in = new Scanner(file);
                user.add(file.getName().substring(3));
                while (in.hasNextLine()) {
                    sched.add(in.nextLine());
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return true;
    }

    public boolean login() {
        String FILENAME = "user";
        Scanner in;

        try (FileInputStream file = openFileInput(FILENAME)) {
            in = new Scanner(file);
            user.add(in.next());
            in.nextLine();//removes newLine character from sourcefile
            sched.add(in.nextLine());
            file.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static NdefRecord createNewTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundNdefPush(this, mNdefMessage);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundNdefPush(this);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShareGroupActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
