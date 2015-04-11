package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Scanner;

public class LoginActivity extends Activity {

    private EditText user1;
    private EditText pass1;
    private String schedule = "";
    private Button button;
    private TextView mTextView;

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    
    private String nfcNetID = "";
    private String nfcSched = "";
    private boolean exitApp = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.login);

        user1 = (EditText) findViewById(R.id.username1);
        user1.setTextColor(Color.parseColor("#000000"));
        pass1 = (EditText) findViewById(R.id.password1);
        pass1.setTextColor(Color.parseColor("#000000"));

        //-----NFC Code-----
        mTextView = (TextView) findViewById(R.id.textview1);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter.equals(null)) 
            mTextView.setText("This phone is not NFC enabled.");
        
        if(login())
        {
            onNewIntent(getIntent());
            
            Intent intent = new Intent(LoginActivity.this, GroupSelectionActivity.class);///*
            if(!nfcNetID.isEmpty() && !nfcSched.isEmpty())
            {
                intent.putExtra("nfcNetID", nfcNetID);
                intent.putExtra("nfcSched", nfcSched);
            }//*/
            startActivity(intent);
            finish();
        }

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[]{ndefIntent};
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }

        mNFCTechLists = new String[][]{new String[]{NfcF.class.getName()}};

        //---End of NFC Code--
        button = (Button) findViewById(R.id.buttonUrl);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!user1.getText().toString().isEmpty() && !pass1.getText().toString().isEmpty()) {
                    Intent intent = new Intent(LoginActivity.this, HtmlLoaderActivity.class);//GroupSelectionActivity.class);//HtmlLoaderActivity.class);
                    intent.putExtra("username", user1.getText().toString());  //used to pass data
                    intent.putExtra("password", pass1.getText().toString());  //used to pass data
                    if (!schedule.isEmpty()) {
                        intent.putExtra("schedule", schedule);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter a username and password to continue", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        if (exitApp) {
            System.runFinalizersOnExit(true);
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            exitApp = true;
            Toast toast = Toast.makeText(getApplicationContext(), "Press back one more time to EXIT", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public boolean login() 
    {
        String FILENAME = "user";
        Scanner in;

        try (FileInputStream file = openFileInput(FILENAME)) {
            in = new Scanner(file);
            this.mTextView.append(in.next()+"\n");
            in.nextLine();//removes newLine character from sourcefile
            this.mTextView.append(in.nextLine());
            file.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
        Parcelable[] data;
        data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage) data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN
                                && Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            if (j == 0) {//setup linked list or storage for these added people
                                nfcNetID = (new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1,
                                        textEncoding));
                            } else {
                                nfcSched =  (new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1,
                                        textEncoding));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
        }
    }

    @Override
    public void onPause() {
        //this.finish();//broke nfc when enabled.
        super.onPause();

        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }
}
