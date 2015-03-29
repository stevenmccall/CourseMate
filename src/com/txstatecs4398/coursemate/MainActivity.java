package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity
{
    private TextView text1;
    private Button button;
    //public String StreamTemp;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String HTMLStream="";
        Bundle extras = getIntent().getExtras();
        
        if (extras != null) {
            HTMLStream = extras.getString("HTMLStream");
        }
            
        //---text view
        text1 = (TextView) findViewById(R.id.text1); 
        text1.setText("Press the button below:");
        
        text1.append(HTMLStream);
        
        //----------switching activity----------------
       
 
	button = (Button) findViewById(R.id.buttonUrl);
 
	button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) 
                { 
                    Intent intent = new Intent(MainActivity.this, WebViewActivity2.class);
                    //intent.putExtra("HTMLStream", StreamTemp);  used to pass data
                    startActivity(intent);
                }
        });	
        
    }
    
}
