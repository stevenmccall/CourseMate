/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.logging.Level;
import java.util.logging.Logger;

class MyJavaScriptInterface {

    private Context ctx;
    public static String HTMLStream = "";

    MyJavaScriptInterface(Context ctx) {
        this.ctx = ctx;
    }

    @JavascriptInterface
    public void showHTML(String html) {
        HTMLStream = html;
    }
}

public class WebViewActivity2 extends Activity {

    private WebView webView;
    private final String USER_AGENT = "Mozilla/5.0";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        webView = (WebView) findViewById(R.id.webView1);
        WebSettings webSettings = webView.getSettings();
        
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");
        
        webView.setWebViewClient(new WebViewClient() {
            boolean done = false;
            
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!"https://ssb.txstate.edu/prod/twbkwbis.P_ValLogin".equals(url)
                        && !"https://ssb.txstate.edu/prod/bwskfshd.P_CrseSchd".equals(url)) 
                {
                    url = "https://ssb.txstate.edu/prod/bwskfshd.P_CrseSchd";
                    view.loadUrl(url);
                    done = true;
                } else if(!"https://ssb.txstate.edu/prod/bwskfshd.P_CrseSchd".equals(url)) {
                    view.loadUrl(url);
                }
                
                if(done) passStream();
                return true;
            }

            public void onPageFinished(WebView view, String url) {
        
            }
        });

        webView.loadUrl("https://ssb.txstate.edu/prod/twbkwbis.P_ValLogin");
    }
    
    public void passStream()
    {
        webView.loadUrl("javascript:window.HtmlViewer.showHTML"
                            + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                    try {
                        wait(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(WebViewActivity2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Intent intent = new Intent(WebViewActivity2.this, MainActivity.class);
                    intent.putExtra("HTMLStream", MyJavaScriptInterface.HTMLStream);  //used to pass data
                    startActivity(intent);
    }
}