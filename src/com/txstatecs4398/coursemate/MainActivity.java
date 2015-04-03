package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity
{
    private ImageView image;
    private EditText user1;
    private EditText pass1;
    private Button button;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {        
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login);//sourceXML4
        
        image = (ImageView) findViewById(R.id.image1);
        user1 = (EditText) findViewById(R.id.username1);
        user1.setTextColor(Color.parseColor("#000000"));
        pass1 = (EditText) findViewById(R.id.password1);
        pass1.setTextColor(Color.parseColor("#000000"));
 
	button = (Button) findViewById(R.id.buttonUrl);
	button.setOnClickListener(new OnClickListener() 
        {
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
