package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.widget.TextView;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import com.txstatecs4398.coursemate.collections.Group;
import com.txstatecs4398.coursemate.collections.Person;
import com.txstatecs4398.coursemate.collections.calendar.CalendarFragment;
import java.nio.charset.Charset;
import java.util.Locale;
import java.io.FileInputStream;
import java.util.Scanner;

public class SharePersonActivity extends Activity {

    private TextView text1;
    private NfcAdapter mNfcAdapter;
    private NdefMessage mNdefMessage;
    private String user;
    private String sched;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share_person);

        text1 = (TextView) findViewById(R.id.text2);
        text1.setMovementMethod(new ScrollingMovementMethod());
        text1.setText("");
        
        if(login())
        {
            text1.setText("Welcome "+user+"!");

            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

            if (mNfcAdapter == null) {
                text1.append("This phone is not NFC enabled.");
            }

            // create an NDEF message with two records of plain text type
            mNdefMessage = new NdefMessage(
                    new NdefRecord[]{
                        createNewTextRecord(user, Locale.ENGLISH, true),
                        createNewTextRecord(sched, Locale.ENGLISH, true)});

        }

        Person tempPerson = new Person(user);
        tempPerson.nfcParse(sched);
        new CalendarFragment().temp = new Group();
        new CalendarFragment().temp.AddPerson(tempPerson);
        CalendarFragment fragment = new CalendarFragment();
        getFragmentManager().beginTransaction().add(R.id.person_fragment, fragment).commit();
    }
    
    public boolean login() 
    {
        String FILENAME = "user";
        Scanner in;

        try (FileInputStream file = openFileInput(FILENAME)) {
            in = new Scanner(file);
            user = in.next();
            in.nextLine();//removes newLine character from sourcefile
            sched = in.nextLine();
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
        Intent intent = new Intent(SharePersonActivity.this, LoginActivity.class);
                startActivity(intent);
    }
}
