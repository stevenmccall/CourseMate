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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
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

    private TextView text1;
    private TextView view;
    private NfcAdapter mNfcAdapter;
    private NdefMessage mNdefMessage;
    private final ArrayList<String> userAdded = new ArrayList();
    private final ArrayList<String> schedAdded = new ArrayList();
    private final ArrayList<Integer> mSelectedItems = new ArrayList();
    private final ArrayList<String> user = new ArrayList();
    private final ArrayList<String> sched = new ArrayList();
    private final ArrayList<NdefRecord> NFCRecords = new ArrayList();
    private String groupName = "";
    private String groupDate = "";
    final Context context = this;
    private AlertDialog alertDialog;
    private Button addButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share_group);
        Bundle extras = getIntent().getExtras();

        text1 = (TextView) findViewById(R.id.text2);
        view = (TextView) findViewById(R.id.text3);

        if (login() && (extras != null)) {
            groupName = extras.getString("groupName");
            
            groupRetriever();
            peopleRetriever();
            
            for(String indUser : userAdded)
                view.append(indUser+"\n");

            //----------start of buttons-----
            dialogCreate();
            addButton = (Button) findViewById(R.id.addPerson);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    alertDialog.show();
                }
            });

            text1.setText(user.get(0) + " welcome \n\tto " + groupName + "!");

            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

            if (mNfcAdapter == null) {
                text1.append("This phone is not NFC enabled.");
            }

            NFCRecords.add(createNewTextRecord(user.get(0), Locale.ENGLISH, true));
            NFCRecords.add(createNewTextRecord(sched.get(0), Locale.ENGLISH, true));
            NdefRecord[] records = NFCRecords.toArray(new NdefRecord[NFCRecords.size()]);
            mNdefMessage = new NdefMessage(records);
        } else {
            finish();
        }
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
                        
                        view.setText("Group Members\n");
                        for(String indUser : userAdded)
                            view.append(indUser+"\n");
                        groupCreate();
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

    public void groupCreate() 
    {
        try {
            FileOutputStream fs = openFileOutput("CMG" + groupName, Context.MODE_PRIVATE);
            OutputStreamWriter ow = new OutputStreamWriter(fs);
            BufferedWriter writer = new BufferedWriter(ow);

            writer.write(groupDate);
            writer.newLine();
            for (int i = 0; i < userAdded.size(); i++) {   //user
                writer.write(userAdded.get(i));
                writer.newLine();
                writer.write(schedAdded.get(i));
                writer.newLine();
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {}
    }

    public boolean groupRetriever() 
    {
        view.setText("Group Members\n");

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

    public boolean peopleRetriever() {//this will go in new Activity for group member selection
        view.setText("Group Members\n");
        File root = getFilesDir();

        FilenameFilter beginswithm = new FilenameFilter() {
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
