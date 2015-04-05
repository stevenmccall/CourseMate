package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HtmlLoaderActivity extends Activity {

    private WebView webView;
    public boolean done = false;
    String username = "username";
    String password = "password";
    String schedule = "";
    public boolean doneTrail = false;
    public String tempStream = "";
    private Thread httpThreadGet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.html_loader);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            password = extras.getString("password");
            
            if (extras.containsKey("schedule"))//if nfc brought in coursemate schedule
            {
                schedule = extras.getString("schedule");
            }
            
            password = password.replace("\\", "\\\\"); // corrects \ escape character in passwords
        }

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onConsoleMessage(ConsoleMessage cmsg) {
                if (cmsg.message().startsWith("MAGIC") && done) {
                    passStream(cmsg.message().substring(5));
                }
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100 && done) {
                    view.loadUrl("javascript:console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);");
                } else if (newProgress == 100 && !doneTrail) {
                    doneTrail = true;

                    view.loadUrl("javascript: {"
                            + "document.getElementById('UserID').value = '" + username + "';"
                            + "document.getElementById('PIN').value = '" + password + "';"
                            + "var frms = document.getElementsByName('loginform');"
                            + "frms[0].submit(); };");
                } else if (newProgress == 100 && !done) {
                    Intent intent = new Intent(HtmlLoaderActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("https://ssb.txstate.edu/prod/twbkwbis.P_GenMenu?name=bmenu.P_MainMnu&msg=WELCOME")) {
                    done = true;
                    url = "https://ssb.txstate.edu/prod/bwskfshd.P_CrseSchd";
                    view.loadUrl(url);
                } else if (url.contains("https://ssb.txstate.edu/prod/twbkwbis.P_ValLogin") && !doneTrail) {
                    view.loadUrl(url);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        //Does not need to be multithreaded, but what the hell.
        httpThreadGet = new Thread() {
            @Override
            public void run() {
                try {
                    tempStream =    "<FORM ACTION=\"/prod/twbkwbis.P_ValLogin\" METHOD=\"POST\" NAME=\"loginform\" AUTOCOMPLETE=\"ON\">\n" +
                                    "   <INPUT TYPE=\"text\" NAME=\"sid\" SIZE=\"34\" MAXLENGTH=\"32\" ID=\"UserID\" >\n" +
                                    "   <INPUT TYPE=\"password\" NAME=\"PIN\" SIZE=\"64\" MAXLENGTH=\"63\" ID=\"PIN\">\n" +
                                    "   <INPUT TYPE=\"submit\" VALUE=\"Login\">\n" +
                                    "</FORM>";

                    webView.loadDataWithBaseURL("https://ssb.txstate.edu/prod/", tempStream, "text/html", "utf-8", "https://ssb.txstate.edu/prod/twbkwbis.P_WWWLogin");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        httpThreadGet.start();
    }

    public void passStream(String HTMLStream) {
        webView.loadUrl("javascript:window.HtmlViewer.showHTML"
                + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

        Logger.getLogger(HtmlLoaderActivity.class.getName()).log(Level.SEVERE, null);

        Intent intent = new Intent(HtmlLoaderActivity.this, PostLoginMainActivity.class);
        intent.putExtra("HTMLStream", HTMLStream);  //used to pass data
        intent.putExtra("netID", username);  //used to pass data
        if(!schedule.isEmpty())
            intent.putExtra("schedule", schedule);  //used to pass data
        startActivity(intent);
    }
}
