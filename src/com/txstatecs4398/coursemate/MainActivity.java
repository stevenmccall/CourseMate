package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity
{
    private TextView text1;
    private EditText user1;
    private Button button;
    
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
        
        user1 = (EditText) findViewById(R.id.username1);
        text1 = (TextView) findViewById(R.id.text1); 
        text1.setMovementMethod(new ScrollingMovementMethod());
        text1.setText("Press the button below:");
        text1.append(HTMLStream);
 
	button = (Button) findViewById(R.id.buttonUrl);
	button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) 
                { 
                    Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                    intent.putExtra("username", user1.getText().toString());  //used to pass data
                    startActivity(intent);
                }
        });	
    }
}
