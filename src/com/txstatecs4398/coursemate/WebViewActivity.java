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

public class WebViewActivity extends Activity {

    private WebView webView;
    public boolean done = false;
    String username = "sm1712";
    String password = "password";
    public boolean doneTrail = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9100 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");

        // intercept calls to console.log
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onConsoleMessage(ConsoleMessage cmsg) {
                if (cmsg.message().startsWith("MAGIC") && done) {
                    passStream(cmsg.message().substring(5));
                    //return true;
                }
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100 && done) {
                    view.loadUrl("javascript:console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);");
                } else if (newProgress == 100 && !doneTrail) {
                    doneTrail = true;
                    view.loadUrl("javascript:document.getElementById('UserID').value = '" + username + "';");
                            /* + "var frms = document.getElementById('PIN').value = '" + password + "';" *///+ "};");
                            //+ "var frms = document.getElementsByName('loginform');"
                            //+ "frms[0].submit(); };");
                }

            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //if(done)doneTrail = true;
                if (url.contains("https://ssb.txstate.edu/prod/twbkwbis.P_GenMenu?name=bmenu.P_MainMnu&msg=WELCOME")) {
                    done = true;
                    url = "https://ssb.txstate.edu/prod/bwskfshd.P_CrseSchd";
                    view.loadUrl(url);
                } 
                else if(url.contains("https://ssb.txstate.edu/prod/twbkwbis.P_ValLogin") && !doneTrail){
                    view.loadUrl(url);
                }
                else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        webView.loadUrl("https://ssb.txstate.edu/prod/twbkwbis.P_ValLogin");
    }

    public void passStream(String HTMLStream) {
        webView.loadUrl("javascript:window.HtmlViewer.showHTML"
                + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

        Logger.getLogger(WebViewActivity2.class.getName()).log(Level.SEVERE, null);

        Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
        intent.putExtra("HTMLStream", HTMLStream);  //used to pass data
        startActivity(intent);
    }
}
