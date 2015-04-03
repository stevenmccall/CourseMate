package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.TextView;
import com.txstatecs4398.coursemate.meetingshared.IndividualSchedule;
import com.txstatecs4398.coursemate.meetingshared.ParseBuff;

public class PostLoginMainActivity extends Activity
{
    private TextView text1;
    private IndividualSchedule person;
    private CalendarView calendar1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {        
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        String HTMLStream, netID;
        Bundle extras = getIntent().getExtras();
        
        calendar1 = (CalendarView) findViewById(R.id.calendar1);
        calendar1.setShowWeekNumber(false);

        text1 = (TextView) findViewById(R.id.text2); 
        text1.setMovementMethod(new ScrollingMovementMethod());
        text1.setText("HTMLStream:");
        
        if (extras != null) 
        {
            HTMLStream = extras.getString("HTMLStream");
            netID = extras.getString("netID");
            ParseBuff test = new ParseBuff(netID); 
            person = test.parse(HTMLStream);
            text1.append(person.showSched());
        }
        
        calendar1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() 
        {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) 
            {
                //check the day of month against a custom class storing schedule availability.
                //show a dialog with a list view to time frames.
            }
        });
    }
}
