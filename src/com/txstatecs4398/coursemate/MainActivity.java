package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.txstatecs4398.coursemate.meetingshared.MeetingShared;

public class MainActivity extends Activity
{
    private TextView text1;
    private EditText user1;
    private Button button;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {        
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        String HTMLStream="";
        Bundle extras = getIntent().getExtras();
        
        if (extras != null) 
        {
            HTMLStream = extras.getString("HTMLStream");
            MeetingShared test = new MeetingShared();
            
        }
        
        user1 = (EditText) findViewById(R.id.username1);
        text1 = (TextView) findViewById(R.id.text2); 
        text1.setMovementMethod(new ScrollingMovementMethod());
        text1.setText("HTMLStream:");
        text1.append(HTMLStream);
 
	button = (Button) findViewById(R.id.buttonUrl);
	button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) 
                { 
                    if(!user1.getText().toString().isEmpty())
                    {
                        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                        intent.putExtra("username", user1.getText().toString());  //used to pass data
                        startActivity(intent);
                    }
                }
        });
    }
}
