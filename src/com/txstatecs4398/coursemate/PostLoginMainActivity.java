package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.TextView;
import com.txstatecs4398.coursemate.meetingshared.IndividualSchedule;
import com.txstatecs4398.coursemate.meetingshared.ParseBuff;

import java.nio.charset.Charset;
import java.util.Locale;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.widget.Toast;

public class PostLoginMainActivity extends Activity {

    private TextView text1;
    private IndividualSchedule person;
    private CalendarView calendar1;

    private NfcAdapter mNfcAdapter;
    private NdefMessage mNdefMessage;
    
    private boolean exitApp = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        String HTMLStream, netID, classmateSchedule;
        Bundle extras = getIntent().getExtras();

        calendar1 = (CalendarView) findViewById(R.id.calendar1);
        calendar1.setShowWeekNumber(false);

        text1 = (TextView) findViewById(R.id.text2);
        text1.setMovementMethod(new ScrollingMovementMethod());
        text1.setText("HTMLStream:");

        if (extras != null) {
            HTMLStream = extras.getString("HTMLStream");
            netID = extras.getString("netID");
            classmateSchedule = extras.getString("schedule");

            ParseBuff test = new ParseBuff(netID);
            person = test.parse(HTMLStream);
            text1.setText(person.showSched());

            if (extras.containsKey("schedule"))//if nfc brought in coursemate schedule
            {
                classmateSchedule = extras.getString("schedule");
                text1.append(classmateSchedule);
            }

            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

            if (mNfcAdapter != null) {
                text1.append("Tap to beam to another NFC device");
            } else {
                text1.append("This phone is not NFC enabled.");
            }

            // create an NDEF message with two records of plain text type
            mNdefMessage = new NdefMessage(
                    new NdefRecord[]{
                        createNewTextRecord(person.getNetID(), Locale.ENGLISH, true),
                        createNewTextRecord(person.showSched(), Locale.ENGLISH, true)});

        }

        calendar1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //check the day of month against a custom class storing schedule availability.
                //show a dialog with a list view to time frames.
            }
        });
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
        if(exitApp){
            this.finish();
        }
        else{
            exitApp = true;
            Toast toast = Toast.makeText(getApplicationContext(), "Press back one more time to EXIT", Toast.LENGTH_SHORT);
            toast.show();  
        }      
    }
}
